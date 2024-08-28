package com.example.exam.Adapters;

import static com.example.exam.DbQuery.ANSWERED;
import static com.example.exam.DbQuery.NOT_ANSWERED;
import static com.example.exam.DbQuery.REVIEW;
import static com.example.exam.DbQuery.q_quesList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.DbQuery;
import com.example.exam.Models.QuestionsModel;
import com.example.exam.R;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {
    public List<QuestionsModel> questionsList;

    public QuestionsAdapter(List<QuestionsModel> questionsList) {
        this.questionsList = questionsList;
    }

    @NonNull
    @Override
    public QuestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionsAdapter.ViewHolder holder, int position) {

        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ques;
        Button optA, optB, optC, optD, PreviousBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ques = itemView.findViewById(R.id.tv_question_item_layout);
            optA = itemView.findViewById(R.id.optionA);
            optB = itemView.findViewById(R.id.optionB);
            optC = itemView.findViewById(R.id.optionC);
            optD = itemView.findViewById(R.id.optionD);
            PreviousBtn = null;
        }

        private void setData(final int pos) {
            ques.setText(questionsList.get(pos).getQuestion());
            optA.setText(questionsList.get(pos).getOptionA());
            optB.setText(questionsList.get(pos).getOptionB());
            optC.setText(questionsList.get(pos).getOptionC());
            optD.setText(questionsList.get(pos).getOptionD());

            SelectOption(optA, 1, pos);
            SelectOption(optB, 2, pos);
            SelectOption(optC, 3, pos);
            SelectOption(optD, 4, pos);

            optA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectedOptions(optA, 1, pos);
                }
            });
            optB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectedOptions(optB, 2, pos);
                }
            });
            optC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectedOptions(optC, 3, pos);

                }
            });
            optD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectedOptions(optD, 4, pos);

                }
            });

        }


        private void SelectedOptions(Button btnOption, int optionNum, int quesID) {
            if (PreviousBtn == null) {
                btnOption.setBackgroundResource(R.drawable.selected_btn);
                DbQuery.q_quesList.get(quesID).setSelectedAnswer(optionNum);
                changeStatus(quesID,ANSWERED);

                PreviousBtn = btnOption;
            } else {
                if (PreviousBtn.getId() == btnOption.getId()) {
                    btnOption.setBackgroundResource(R.drawable.unselected_btn);
                    DbQuery.q_quesList.get(quesID).setSelectedAnswer(-1);
                    changeStatus(quesID,NOT_ANSWERED);
                    PreviousBtn = null;
                } else {
                    PreviousBtn.setBackgroundResource(R.drawable.unselected_btn);
                    btnOption.setBackgroundResource(R.drawable.selected_btn);
                    DbQuery.q_quesList.get(quesID).setSelectedAnswer(optionNum);
                    changeStatus(quesID,ANSWERED);
                    PreviousBtn = btnOption;

                }
            }

        }

        private void changeStatus(int quesID, int answered) {
            if (q_quesList.get(quesID).getStatus()!=REVIEW){
                q_quesList.get(quesID).setStatus(answered);
            }

        }

        private void SelectOption(Button btn, int optionNum, int quesID) {
            if (DbQuery.q_quesList.get(quesID).getSelectedAnswer() == optionNum) {
                btn.setBackgroundResource(R.drawable.selected_btn);
            } else {
                btn.setBackgroundResource(R.drawable.unselected_btn);

            }


        }


    }


}


