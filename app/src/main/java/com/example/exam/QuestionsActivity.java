package com.example.exam;

import static com.example.exam.DbQuery.ANSWERED;
import static com.example.exam.DbQuery.NOT_ANSWERED;
import static com.example.exam.DbQuery.NOT_VISITED;
import static com.example.exam.DbQuery.REVIEW;
import static com.example.exam.DbQuery.g_catlist;
import static com.example.exam.DbQuery.g_selected_cat_index;
import static com.example.exam.DbQuery.g_selected_test_index;
import static com.example.exam.DbQuery.g_testList;
import static com.example.exam.DbQuery.q_quesList;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.exam.Adapters.QuestionGridAdapter;
import com.example.exam.Adapters.QuestionsAdapter;

import java.util.concurrent.TimeUnit;

public class QuestionsActivity extends AppCompat {
    protected RecyclerView questionView;
    protected TextView tv_Id, tv_timer, tv_Catname;
    protected Button btn_submit, btn_clear, btn_review;
    protected ImageButton back, forward, close;
    protected ImageView bookmark, apps, markbtnImage;
    protected int quesID;
    private QuestionsAdapter adapter;
    private DrawerLayout layout;
    private ConstraintLayout constraintLayout;
    private GridView gridView;
    private QuestionGridAdapter gridAdapter;
    private CountDownTimer timer;
    private long timeLeft;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_list_layout);
        Init();


        adapter = new QuestionsAdapter(q_quesList);
        gridAdapter = new QuestionGridAdapter(q_quesList.size());
        gridView.setAdapter(gridAdapter);


        questionView.setAdapter(adapter);
        questionView.setLayoutManager(new LinearLayoutManager(this));

        snapHelper();
        setOnClickListeners();
        setTimer();

    }


    @SuppressLint("SetTextI18n")
    private void Init() {
        questionView = findViewById(R.id.questions_view);
        tv_Id = findViewById(R.id.tv_question_Id);
        tv_timer = findViewById(R.id.tv_question_time);
        tv_Catname = findViewById(R.id.tv_cat_name);
        btn_submit = findViewById(R.id.btn_submit);
        btn_clear = findViewById(R.id.btn_clean_selection);
        btn_review = findViewById(R.id.btn_review);
        back = findViewById(R.id.img_btn_back);
        forward = findViewById(R.id.img_btn_forward);
        bookmark = findViewById(R.id.img_view_bookmark);
        apps = findViewById(R.id.img_view_apps);
        quesID = 0;
        layout = findViewById(R.id.drawer_layout);
        gridView = findViewById(R.id.ques_list_gridView);
        constraintLayout = findViewById(R.id.cons_layout);
        markbtnImage = findViewById(R.id.marked_image);
        tv_Id.setText("1/"+String.valueOf(q_quesList.size()));
        tv_Catname.setText(g_catlist.get(g_selected_cat_index).getName());
        q_quesList.get(0).setStatus(NOT_ANSWERED);


        if (q_quesList.get(0).isBookMarked()){
            bookmark.setImageResource(R.drawable.ic_bookmark_fill);
        }else {
            bookmark.setImageResource(R.drawable.ic_bookmark);

        }



    }

    private void snapHelper() {
        SnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(questionView);

        questionView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View view = helper.findSnapView(questionView.getLayoutManager());
                quesID = recyclerView.getLayoutManager().getPosition(view);
                if (q_quesList.get(quesID).getStatus() == NOT_VISITED)
                    q_quesList.get(quesID).setStatus(NOT_ANSWERED);

                if (q_quesList.get(quesID).getStatus() == REVIEW) {
                    markbtnImage.setVisibility(View.VISIBLE);
                } else {
                    markbtnImage.setVisibility(View.GONE);
                }

                tv_Id.setText(String.valueOf(quesID+1)+"/"+String.valueOf(q_quesList.size()));

                if (q_quesList.get(quesID).isBookMarked()){
                    bookmark.setImageResource(R.drawable.ic_bookmark_fill);
                }else {
                    bookmark.setImageResource(R.drawable.ic_bookmark);

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void setOnClickListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quesID > 0) {
                    questionView.smoothScrollToPosition(quesID - 1);
                }
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quesID < q_quesList.size() - 1) {
                    questionView.smoothScrollToPosition(quesID + 1);
                }
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                q_quesList.get(quesID).setSelectedAnswer(-1);
                q_quesList.get(quesID).setStatus(NOT_ANSWERED);
                markbtnImage.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });

        apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!layout.isDrawerOpen(GravityCompat.END)) {
                    gridAdapter.notifyDataSetChanged();
                    layout.openDrawer(GravityCompat.END);
                }
            }
        });



        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (markbtnImage.getVisibility() != View.VISIBLE) {
                    markbtnImage.setVisibility(View.VISIBLE);
                    q_quesList.get(quesID).setStatus(REVIEW);
                } else {
                    markbtnImage.setVisibility(View.GONE);
                    if (q_quesList.get(quesID).getSelectedAnswer() != -1) {
                        q_quesList.get(quesID).setStatus(ANSWERED);

                    } else {
                        q_quesList.get(quesID).setStatus(NOT_ANSWERED);
                    }


                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionsActivity.this);
                builder.setTitle(R.string.exit_test)
                        .setCancelable(false)
                        .setMessage(R.string.do_you_want_exit_test)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                timer.cancel();
                                Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class);
                                long totalTime = g_testList.get(g_selected_test_index).getTime() * 60 * 1000;
                                intent.putExtra("TIME_TAKEN", totalTime - timeLeft);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

            }
        });
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToBookMark();
            }
        });
    }

    private void addToBookMark() {
        if (q_quesList.get(quesID).isBookMarked()) {
            q_quesList.get(quesID).setBookMarked(false);
            bookmark.setImageResource(R.drawable.ic_bookmark);

        } else {
            q_quesList.get(quesID).setBookMarked(true);
            bookmark.setImageResource(R.drawable.ic_bookmark_fill);
        }


    }

    private void submitTest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionsActivity.this);
        builder.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_layout, null);
        Button cancel = view.findViewById(R.id.btn_cancel);
        Button confirm = view.findViewById(R.id.btn_confirm);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                alertDialog.dismiss();
                startActivity(new Intent(QuestionsActivity.this, ScoreActivity.class));
                finish();
            }
        });
        alertDialog.show();


    }

    private void setTimer() {
        long totalTime = g_testList.get(g_selected_test_index).getTime() * 60 * 1000;
        timer = new CountDownTimer(totalTime + 1000, 1000) {

            @Override
            public void onTick(long remainingTime) {
                timeLeft = remainingTime;
                @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d min",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime)));

                tv_timer.setText(time);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class);
                long totalTime = g_testList.get(g_selected_test_index).getTime() * 60 * 1000;
                intent.putExtra("TIME_TAKEN", totalTime - timeLeft);
                startActivity(intent);
                finish();
            }

        };
        timer.start();


    }


}