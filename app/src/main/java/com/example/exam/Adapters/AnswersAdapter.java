package com.example.exam.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.Models.QuestionsModel;
import com.example.exam.R;

import java.util.List;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {
    List<QuestionsModel> questionsList;

    public AnswersAdapter(List<QuestionsModel> questionsList) {
        this.questionsList = questionsList;
    }

    @NonNull
    @Override
    public AnswersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answers_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersAdapter.ViewHolder holder, int position) {

        String question = questionsList.get(position).getQuestion();
        String opt_A = questionsList.get(position).getOptionA();
        String opt_B = questionsList.get(position).getOptionB();
        String opt_C = questionsList.get(position).getOptionC();
        String opt_D = questionsList.get(position).getOptionD();
        int correct = questionsList.get(position).getCorrectAnswer();
        int incorrect = questionsList.get(position).getSelectedAnswer();

        holder.setData(position, question, opt_A, opt_B, opt_C, opt_D, correct, incorrect);

    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView quesNo, Question, opA, opB, opC, opD, txt_wrong;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quesNo = itemView.findViewById(R.id.question_id);
            Question = itemView.findViewById(R.id.question);
            opA = itemView.findViewById(R.id.optiona);
            opB = itemView.findViewById(R.id.optionb);
            opC = itemView.findViewById(R.id.optionc);
            opD = itemView.findViewById(R.id.optiond);
            txt_wrong = itemView.findViewById(R.id.result);

        }

        @SuppressLint("SetTextI18n")
        void setData(int pos, String question, String a, String b, String c, String d, int ans, int selected) {
            quesNo.setText(itemView.getResources().getString(R.string.question_no_1) + String.valueOf(pos + 1));
            Question.setText(question);
            opA.setText(itemView.getResources().getString(R.string.opt) + a);
            opB.setText(itemView.getResources().getString(R.string.optb) + b);
            opC.setText(itemView.getResources().getString(R.string.optc) + c);
            opD.setText(itemView.getResources().getString(R.string.optd) + d);

            if (selected == -1) {
                txt_wrong.setText(itemView.getResources().getString(R.string.not_answered));
                txt_wrong.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                setOptionColor(selected,R.color.text_default);
            } else {
                if (selected == ans) {
                    txt_wrong.setText(itemView.getResources().getString(R.string.correct));
                    txt_wrong.setTextColor(itemView.getContext().getResources().getColor(R.color.timeColor));
                    setOptionColor(selected, R.color.timeColor);
                } else {
                    txt_wrong.setText(itemView.getResources().getString(R.string.wrong));
                    txt_wrong.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
                    setOptionColor(selected, R.color.red);


                }
            }


        }

        private void setOptionColor(int selected, int color) {

            if (selected == 1) {
                opA.setTextColor(itemView.getContext().getResources().getColor(color));
            } else {
                opA.setTextColor(itemView.getContext().getResources().getColor(R.color.text_default));

            }
            if (selected == 2) {
                opB.setTextColor(itemView.getContext().getResources().getColor(color));
            } else {
                opB.setTextColor(itemView.getContext().getResources().getColor(R.color.text_default));

            }
            if (selected == 3) {
                opC.setTextColor(itemView.getContext().getResources().getColor(color));
            } else {
                opC.setTextColor(itemView.getContext().getResources().getColor(R.color.text_default));

            }
            if (selected == 4) {
                opD.setTextColor(itemView.getContext().getResources().getColor(color));
            } else {
                opD.setTextColor(itemView.getContext().getResources().getColor(R.color.text_default));

            }


        }
    }
}
