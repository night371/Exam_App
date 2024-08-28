package com.example.exam;

import static com.example.exam.DbQuery.g_catlist;
import static com.example.exam.DbQuery.g_testList;
import static com.example.exam.DbQuery.loadQuestions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exam.Interface.MyCompleteListener;

public class StartTestActivity extends AppCompat {

    TextView testName,testTitle ,totalQuestion,txt_time,txt_bestScore;
    ImageView arrowBack;
    Button btn_Start;
    simple_progress simple_progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);


        Init();
        simple_progress = new simple_progress(this);


        simple_progress.startLoadDialog(getString(R.string.loading));
        loadQuestions(new MyCompleteListener() {
            @Override
            public void OnSuccess() {
                setData();
                simple_progress.dissmissDialog();
            }

            @Override
            public void OnFailure() {
                Toast.makeText(StartTestActivity.this,R.string.errorMsg, Toast.LENGTH_SHORT).show();
                simple_progress.dissmissDialog();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        testName.setText(g_catlist.get(DbQuery.g_selected_cat_index).getName());
        testTitle.setText(getResources().getString(R.string.test_number)+String.valueOf(DbQuery.g_selected_test_index+1));
        totalQuestion.setText(String.valueOf(DbQuery.q_quesList.size()));
        txt_time.setText(String.valueOf(g_testList.get(DbQuery.g_selected_test_index).getTime()));
        txt_bestScore.setText(String.valueOf(g_testList.get(DbQuery.g_selected_test_index).getTopScore()));
    }

    private void Init() {
        testName = findViewById(R.id.st_Category_name);
        testTitle =findViewById(R.id.st_Test_No);
        totalQuestion= findViewById(R.id.st_total_ques);
        txt_time = findViewById(R.id.st_time);
        txt_bestScore = findViewById(R.id.st_best_score);
        btn_Start=findViewById(R.id.start_btn_of_test);
        arrowBack = findViewById(R.id.st_backArrow);

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartTestActivity.this,QuestionsActivity.class));
            }
        });

    }




}