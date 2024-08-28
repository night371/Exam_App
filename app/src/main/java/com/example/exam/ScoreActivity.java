package com.example.exam;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.exam.Interface.MyCompleteListener;
import com.example.exam.Models.QuestionsModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompat {
    TextView tv_score, tv_taken_time, tv_total_ques, tv_not_answered, tv_wrong_ques, tv_correct_ques;
    Button btn_rank, btn_view, btn_retry;
    long timeTaken;
    Dialog progress;
    int finalScore;
    simple_progress simple_progress;
    BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        simple_progress = new simple_progress(this);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // simple_progress.startLoadDialog("Loading...");



        Init();
        LoadData();
        setBookMarks();
        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, AnswerActivity.class);
                startActivity(intent);
            }
        });
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
               // reTry();
            }
        });

        saveResult();

    }

    private void setBookMarks() {

        for (int i = 0; i < DbQuery.q_quesList.size(); i++) {
            QuestionsModel model = DbQuery.q_quesList.get(i);
            if (model.isBookMarked()) {
                if (!DbQuery.g_BmIdList.contains(model.getQuesID())) {
                    DbQuery.g_BmIdList.add(model.getQuesID());
                    DbQuery.myProfile.setBookMarkCount(DbQuery.g_BmIdList.size());


                }
            } else {
                if (DbQuery.g_BmIdList.contains(model.getQuesID())) {
                    DbQuery.g_BmIdList.remove(model.getQuesID());
                    DbQuery.myProfile.setBookMarkCount(DbQuery.g_BmIdList.size());
                }

            }


        }

    }

    private void saveResult() {
        DbQuery.saveResult(finalScore, new MyCompleteListener() {
            @Override
            public void OnSuccess() {

            }

            @Override
            public void OnFailure() {
                Toast.makeText(ScoreActivity.this,getResources().getString(R.string.errorMsg), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void reTry() {
        for (int i = 0; i < DbQuery.q_quesList.size(); i++) {
            DbQuery.q_quesList.get(i).setSelectedAnswer(-1);
            DbQuery.q_quesList.get(i).setStatus(DbQuery.NOT_VISITED);

        }
        startActivity(new Intent(ScoreActivity.this, StartTestActivity.class));
        finish();


    }


    private void Init() {
        tv_score = findViewById(R.id.txt_score_activity);
        tv_taken_time = findViewById(R.id.txt_taken_time);
        tv_total_ques = findViewById(R.id.txt_total_ques);
        tv_not_answered = findViewById(R.id.txt_unanswered);
        tv_wrong_ques = findViewById(R.id.txt_wrong_ques);
        tv_correct_ques = findViewById(R.id.txt_correct_ques);
        btn_view = findViewById(R.id.view_answer);
        btn_retry = findViewById(R.id.re_try_button);



    }

    private void LoadData() {
        int answer = 0, unanswered = 0, wrongS = 0;

        for (int i = 0; i < DbQuery.q_quesList.size(); i++) {
            if (DbQuery.q_quesList.get(i).getSelectedAnswer() == -1) {
                unanswered++;
            } else {
                if (DbQuery.q_quesList.get(i).getSelectedAnswer() == DbQuery.q_quesList.get(i).getCorrectAnswer()) {
                    answer++;
                } else {
                    wrongS++;
                }
            }

        }
        tv_correct_ques.setText(String.valueOf(answer));
        tv_wrong_ques.setText(String.valueOf(wrongS));
        tv_not_answered.setText(String.valueOf(unanswered));
        tv_total_ques.setText(String.valueOf(DbQuery.q_quesList.size()));

        finalScore = (answer * 100) / DbQuery.q_quesList.size();
        tv_score.setText(String.valueOf(finalScore));

        timeTaken = getIntent().getLongExtra("TIME_TAKEN", 0);
        @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d min",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken)));
        tv_taken_time.setText(time);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}