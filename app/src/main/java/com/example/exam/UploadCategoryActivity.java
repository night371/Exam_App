package com.example.exam;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.exam.Models.FirestoreHelper;
import com.example.exam.databinding.ActivityUploadCategoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UploadCategoryActivity extends AppCompatActivity {

    ActivityUploadCategoryBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    Dialog dialog;
    FirestoreHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        helper = new FirestoreHelper();
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading);
        dialog.setCancelable(false);


        binding.btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();


            }
        });


    }

    private void insertData() {
        helper.insertQuizAndCategory(binding.edNameAdmin.getText().toString(), Integer.parseInt(binding.edTextAdmin.getText().toString()), new FirestoreHelper.CompleteListener() {
            @Override
            public void OnSuccess() {
                startActivity(new Intent(getApplicationContext(),AdminActivity.class));
               // finish();
                Toast.makeText(UploadCategoryActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnFailure() {
                Toast.makeText(UploadCategoryActivity.this, "Failed inserting record", Toast.LENGTH_SHORT).show();

            }
        });



    }

}