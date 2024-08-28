package com.example.exam.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.Models.RankModel;
import com.example.exam.R;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

    public RankAdapter(List<RankModel> userList) {
        this.userList = userList;
    }

    List<RankModel> userList;


    @NonNull
    @Override
    public RankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item_layout, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankAdapter.ViewHolder holder, int position) {
        String name = userList.get(position).getName();
        int score = userList.get(position).getScore();
        int rank = userList.get(position).getRank();
        holder.setData(name, score, rank);

    }

    @Override
    public int getItemCount() {
        if (userList.size() > 10) {
            return 10;
        } else {
            return userList.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_score, tv_rank, tv_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.txt_name_of_rec);
            tv_score = itemView.findViewById(R.id.txt_score_of_rec);
            tv_rank = itemView.findViewById(R.id.rank2);
            tv_img = itemView.findViewById(R.id.img_text2);
        }

        @SuppressLint("SetTextI18n")
        private void setData(String name, int score, int rank) {
            tv_name.setText(name);
            tv_score.setText(itemView.getResources().getString(R.string.score_1234) + score);
            tv_rank.setText(itemView.getResources().getString(R.string.rank_is) + rank);
            tv_img.setText(name.toUpperCase().substring(0, 1));
        }
    }
}
