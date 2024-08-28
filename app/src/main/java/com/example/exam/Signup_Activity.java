package com.example.exam;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.exam.Interface.MyCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Map;

public class Signup_Activity extends AppCompat {
    EditText firstname, emailText, passText, confirmText;
    Button signup;
    ImageView btn_back;
    FirebaseAuth mAuth;
    String filenamesStr, EmailStr, PassStr, ConfirmStr;
    simple_progress simple_progress = new simple_progress(this);
    CheckBox chAdmin, chNormal;
    FirebaseFirestore db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setupUi();

        validatedChBoxAndchListener();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup_Activity.this, Login_Activity.class));
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    SignupNewUser();
                }
            }
        });


    }

    private void validatedChBoxAndchListener() {

        chNormal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    chAdmin.setChecked(false);
                }
            }
        });

        chAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    chNormal.setChecked(false);
                }
            }
        });
    }

    private void setupUi() {
        firstname = findViewById(R.id.fullName);
        emailText = findViewById(R.id.emailId);
        passText = findViewById(R.id.pass_ed);
        confirmText = findViewById(R.id.confirm_pass);
        signup = findViewById(R.id.sing_up_btn);
        btn_back = findViewById(R.id.back_btn);
        chAdmin = findViewById(R.id.chBoxAdmin);
        chNormal = findViewById(R.id.chBoxNormal);
    }


    private boolean validate() {
        filenamesStr = firstname.getText().toString().trim();
        EmailStr = emailText.getText().toString().trim();
        PassStr = passText.getText().toString().trim();
        ConfirmStr = confirmText.getText().toString().trim();

        if (filenamesStr.isEmpty()) {
            firstname.setError(getResources().getString(R.string.errName));
            firstname.requestFocus();
            return false;
        }
        if (EmailStr.isEmpty()) {
            emailText.setError(getResources().getString(R.string.errEmail));
            emailText.requestFocus();
            return false;
        }
        if (PassStr.isEmpty()) {
            passText.setError(getResources().getString(R.string.errPass));
            passText.requestFocus();
            return false;
        }
        if (ConfirmStr.isEmpty()) {
            confirmText.setError(getResources().getString(R.string.errPass));
            confirmText.requestFocus();
            return false;
        }
        if (PassStr.compareTo(ConfirmStr) != 0) {
            confirmText.setError(getResources().getString(R.string.mismatch));
            confirmText.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simple_progress.dissmissDialog();
    }

    private void SignupNewUser() {
        simple_progress.startLoadDialog(getResources().getString(R.string.register));
        mAuth.createUserWithEmailAndPassword(EmailStr, PassStr).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // Sign in success, update UI with the signed-in user's information
                Toast.makeText(Signup_Activity.this, R.string.succssRegister, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Signup_Activity.this, MainActivity.class));
                Signup_Activity.this.finish();
                createUserData(EmailStr, filenamesStr, new MyCompleteListener() {
                    @Override
                    public void OnSuccess() {
                        DbQuery.loadData(new MyCompleteListener() {
                            @Override
                            public void OnSuccess() {
                                simple_progress.dissmissDialog();
                                startActivity(new Intent(Signup_Activity.this, MainActivity.class));
                                Signup_Activity.this.finish();
                            }

                            @Override
                            public void OnFailure() {
                                Toast.makeText(Signup_Activity.this, R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                simple_progress.dissmissDialog();
                            }
                        });
                    }

                    @Override
                    public void OnFailure() {
                        Toast.makeText(Signup_Activity.this, R.string.errorMsg, Toast.LENGTH_SHORT).show();
                        simple_progress.dissmissDialog();
                    }
                });


            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Signup_Activity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                simple_progress.dissmissDialog();
            }
        });
    }


    public void createUserData(String email, String name, MyCompleteListener completeListener) {
        Map<String, Object> userdata = new ArrayMap<>();
        userdata.put("EMAIL_ID", email);
        userdata.put("NAME", name);
        userdata.put("TOTAL_SCORE", 0);
        userdata.put("BOOKMARK", 0);

        if (chAdmin.isChecked()) {
            userdata.put("isAdmin", "1");
        } else {
            userdata.put("isNormal", "1");
        }


        DocumentReference reference = db.collection("USERS")
                .document(mAuth.getCurrentUser().getUid());
        WriteBatch batch = db.batch();
        batch.set(reference, userdata);

        DocumentReference reference1 = db.collection("USERS").document("TOTAL_USERS");
        batch.update(reference1, "COUNT", FieldValue.increment(1));
        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                completeListener.OnSuccess();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                completeListener.OnFailure();
            }
        });
        if (chAdmin.isChecked()) {
            startActivity(new Intent(Signup_Activity.this, AdminActivity.class));
            finish();
        } else {
            startActivity(new Intent(Signup_Activity.this, MainActivity.class));
            finish();
        }


    }

}