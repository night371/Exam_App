package com.example.exam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.exam.Adapters.AnswersAdapter;

import java.util.Objects;

public class AnswerActivity extends AppCompat {

    Toolbar toolbar;
    RecyclerView testView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);


        toolbar = findViewById(R.id.aa_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.ViewAnswer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        testView = findViewById(R.id.aa_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        testView.setLayoutManager(layoutManager);
        AnswersAdapter adapter = new AnswersAdapter(DbQuery.q_quesList);
        testView.setAdapter(adapter);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            AnswerActivity.this.finish();

        return super.onOptionsItemSelected(item);
    }
}