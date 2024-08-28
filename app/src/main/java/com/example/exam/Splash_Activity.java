package com.example.exam;

import static com.example.exam.DbQuery.db;
import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;

import com.example.exam.Interface.MyCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Splash_Activity extends AppCompat {
    TextView appName;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appName = findViewById(R.id.app_name);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        appName.setTypeface(typeface);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.myanim);
        appName.setAnimation(animation);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (mAuth.getCurrentUser() != null) {
                    DocumentReference df = FirebaseFirestore.getInstance().collection("USERS").document(mAuth.getCurrentUser().getUid());
                    df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.getString("isAdmin") != null) {
                                startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                                finish();
                            }
                            if (documentSnapshot.getString("isNormal") != null) {
                                DbQuery.loadData(new MyCompleteListener() {
                                    @Override
                                    public void OnSuccess() {
                                        startActivity(new Intent(Splash_Activity.this, MainActivity.class));
                                        finish();
                                    }

                                    @Override
                                    public void OnFailure() {
                                        //Toast.makeText(Splash_Activity.this, "There is no internet connection", Toast.LENGTH_SHORT).show();
                                        Dialog();
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(), Login_Activity.class));
                            finish();
                        }
                    });

                } else {
                    startActivity(new Intent(Splash_Activity.this, Login_Activity.class));
                    finish();
                }
            }
        }).start();
    }

    private void Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Splash_Activity.this);
        builder.setCancelable(false)
                .setMessage("No internet connection please try again.")
                .setTitle("Error")
                .setIcon(R.drawable.ic_error)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Splash_Activity.this.finish();
                    }
                });
        // .create();
        // .show();
    }
}