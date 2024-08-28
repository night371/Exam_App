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

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.ViewHolder> {
    List<QuestionsModel> questionsList;

    public BookMarkAdapter(List<QuestionsModel> questionsList) {
        this.questionsList = questionsList;
    }

    @NonNull
    @Override
    public BookMarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answers_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String question = questionsList.get(position).getQuestion();
        String opt_A = questionsList.get(position).getOptionA();
        String opt_B = questionsList.get(position).getOptionB();
        String opt_C = questionsList.get(position).getOptionC();
        String opt_D = questionsList.get(position).getOptionD();
        int correct = questionsList.get(position).getCorrectAnswer();

        holder.setData(position, question, opt_A, opt_B, opt_C, opt_D, correct);
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
        private void setData(int pos, String question, String a, String b, String c, String d, int ans) {
            quesNo.setText("Question No." + String.valueOf(pos + 1));
            Question.setText(question);
            opA.setText("A.." + a);
            opB.setText("B.." + b);
            opC.setText("C.." + c);
            opD.setText("D.." + d);

            if (ans==1){
                txt_wrong.setText("ANSWER : "+a);
            }
            else if (ans==2) {
                txt_wrong.setText("ANSWER : "+b);


            }
            else if (ans==3) {
                txt_wrong.setText("ANSWER : "+c);


            }
            else if (ans==4) {
                txt_wrong.setText("ANSWER : "+d);


            }

        }

    }
}
