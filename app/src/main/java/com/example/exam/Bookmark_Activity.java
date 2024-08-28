package com.example.exam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.exam.Adapters.BookMarkAdapter;
import com.example.exam.Interface.MyCompleteListener;

import java.util.Objects;

public class Bookmark_Activity extends AppCompat {
    Toolbar toolbar;
    RecyclerView recview;
    BookMarkAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        toolbar = findViewById(R.id.bm_toolbar4);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Saved Questions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recview = findViewById(R.id.bm_recyclerView);

        LinearLayoutManager layout = new LinearLayoutManager(this);
        recview.setLayoutManager(layout);

        DbQuery.loadBookMarks(new MyCompleteListener() {
            @Override
            public void OnSuccess() {
                adapter= new BookMarkAdapter(DbQuery.g_bookmarks_list);
                recview.setAdapter(adapter);
            }

            @Override
            public void OnFailure() {

            }
        });






    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}