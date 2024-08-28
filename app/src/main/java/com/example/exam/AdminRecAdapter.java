package com.example.exam;

import static android.app.ProgressDialog.show;
import static java.lang.ref.Cleaner.create;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.Models.Quiz;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminRecAdapter extends RecyclerView.Adapter<AdminRecAdapter.QuizViewHolder> {

    private final List<Quiz> quizList;
    private Context context;
    Activity activity;

    public AdminRecAdapter(Context context, ArrayList<Quiz> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cat_admin_rec_layout, parent, false); // Replace with your item layout
        return new QuizViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.nameTextView.setText("Name: " + quiz.getName());
        holder.noOfTestsTextView.setText("No. of Tests: " + quiz.getNo_of_tests());




    }


    private void Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false)
                .setMessage("Do you really wanna delete this record?")
                .setTitle("Deleting Record")
                .setIcon(R.drawable.ic_error)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRecordsFromDb();
                    }
                });
        builder.create();
        builder.show();

    }

    private void deleteRecordsFromDb() {
      FirebaseFirestore  db = FirebaseFirestore.getInstance();
      db.collection("");
    }


    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public  class QuizViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView noOfTestsTextView;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.cat_name_admin);
            noOfTestsTextView = itemView.findViewById(R.id.no_of_tests_admin);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                      itemView.getContext().startActivity(new Intent(itemView.getContext(), AddTestActivityAdmin.class));
                }
            });
        }
    }
}
