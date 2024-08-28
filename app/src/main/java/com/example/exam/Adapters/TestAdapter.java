package com.example.exam.Adapters;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.DbQuery;
import com.example.exam.Models.TestModel;
import com.example.exam.R;
import com.example.exam.StartTestActivity;

import java.util.List;
import java.util.Locale;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    List<TestModel> testModels;


    public TestAdapter(List<TestModel> testlist) {
        this.testModels = testlist;
    }

    @NonNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int progress = testModels.get(position).getTopScore();

        holder.setData(position, progress);

    }

    @Override
    public int getItemCount() {
        return testModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Score;
        TextView test_no;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Score = itemView.findViewById(R.id.score_test);
            test_no = itemView.findViewById(R.id.test_no);
            progressBar = itemView.findViewById(R.id.test_progress);

        }

        public void setData(int position, int progress) {
            int testNumber = position + 1; // Add 1 to position to start from 1 instead of zero

            String testNoText = itemView.getContext().getString(R.string.test_no, testNumber);
            test_no.setText(testNoText);
            Score.setText(String.format(Locale.US, "%d %%", progress));
            progressBar.setProgress(progress);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DbQuery.g_selected_test_index = position;
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), StartTestActivity.class));
                }
            });
        }
    }



}


