package com.example.exam;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.Adapters.TestAdapter;
import com.example.exam.Interface.MyCompleteListener;

public class TestActivity extends AppCompat {
    RecyclerView testView;
    Toolbar toolbar;
    TestAdapter adapter;


    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(toolbar);

        // Use the string resource for the Toolbar title
        getSupportActionBar().setTitle(getString(R.string.test_activity_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        testView = findViewById(R.id.recyclerView);

        testView.setLayoutManager(new LinearLayoutManager(this));

        DbQuery.loadModelTestData(new MyCompleteListener() {
            @Override
            public void OnSuccess() {
                DbQuery.loadMyscores(new MyCompleteListener() {
                    @Override
                    public void OnSuccess() {
                        adapter = new TestAdapter(DbQuery.g_testList);
                        testView.setAdapter(adapter);
                    }

                    @Override
                    public void OnFailure() {
                        // Use the string resource for the error message
                        Toast.makeText(TestActivity.this, R.string.errorMsg,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void OnFailure() {
                // Use the string resource for the error message
                Toast.makeText(TestActivity.this, R.string.errorMsg,
                        Toast.LENGTH_SHORT).show();
            }
        });



    }




    @Override
    protected void onResume() {
        super.onResume();
        testView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
