package com.example.exam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.exam.Interface.MyCompleteListener;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login_Activity extends AppCompat {
    EditText eEmail, ePass;
    Button btnLogin;
    TextView txt_forget, txt_sign_up;
    FirebaseAuth mAuth;
    simple_progress simple_progress;


    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        eEmail = findViewById(R.id.emailone);
        ePass = findViewById(R.id.passwordone);
        btnLogin = findViewById(R.id.login_btn);
        txt_forget = findViewById(R.id.forget_pass);
        txt_sign_up = findViewById(R.id.Sign_upText);


        mAuth = FirebaseAuth.getInstance();
        simple_progress = new simple_progress(this);


        txt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_sign_up.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                startActivity(new Intent(Login_Activity.this, Signup_Activity.class));
                finish();
            }
        });
        txt_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_forget.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                startActivity(new Intent(Login_Activity.this, ForgotPasswordActivity.class));
                finish();
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidate()) {
                    Login();
                }
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void Login() {
        simple_progress.startLoadDialog(getResources().getString(R.string.logging));
        mAuth.signInWithEmailAndPassword(eEmail.getText().toString().trim(), ePass.getText().toString().trim())
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Sign in success, update UI with the signed-in user's information
                        checkifadmin(authResult.getUser().getUid());
                        Toast.makeText(Login_Activity.this, getResources().getString(R.string.LoginSucess), Toast.LENGTH_SHORT).show();
                        simple_progress.dissmissDialog();

                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(Login_Activity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        simple_progress.dissmissDialog();
                    }
                });

    }

    private void checkifadmin(String uid) {
        DocumentReference df = FirebaseFirestore.getInstance().collection("USERS").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("isAdmin") != null) {
                    startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                    finish();
                    simple_progress.dissmissDialog();
                }
                if (documentSnapshot.getString("isNormal") != null) {
                    DbQuery.loadData(new MyCompleteListener() {
                        @Override
                        public void OnSuccess() {
                            simple_progress.dissmissDialog();
                            startActivity(new Intent(Login_Activity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void OnFailure() {
                            Toast.makeText(Login_Activity.this, R.string.errorMsg,
                                    Toast.LENGTH_SHORT).show();
                            simple_progress.dissmissDialog();
                        }
                    });
//                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                    finish();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                simple_progress.dissmissDialog();
                Toast.makeText(Login_Activity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidate() {
        String email = eEmail.getText().toString().trim();
        String pass = ePass.getText().toString().trim();
        if (email.isEmpty() || !email.contains("@")
                || email.lastIndexOf("@") > email.lastIndexOf(".") ||
                email.split("@").length > 2) {
            eEmail.setError(getResources().getString(R.string.errEmail));
            eEmail.requestFocus();
            return false;
        }
        if (pass.isEmpty()) {
            ePass.setError(getResources().getString(R.string.errPass));
            ePass.requestFocus();
            return false;
        }
        if (pass.length() <= 8) {
            ePass.setError(getResources().getString(R.string.passLenght));
            ePass.requestFocus();
            return false;
        }
        return true;
    }


}
