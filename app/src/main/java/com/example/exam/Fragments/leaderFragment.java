package com.example.exam.Fragments;

import static com.example.exam.DbQuery.g_userList;
import static com.example.exam.DbQuery.rankModel;
import static com.example.exam.DbQuery.user_count;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.Adapters.RankAdapter;
import com.example.exam.DbQuery;
import com.example.exam.Interface.MyCompleteListener;
import com.example.exam.MainActivity;
import com.example.exam.R;

public class leaderFragment extends Fragment {

    TextView tv_score, tv_rank, tv_img, tv_users;
    RecyclerView recyclerView;
    RankAdapter adapter;


    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar1);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(requireContext().getString(R.string.app_name));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leader, container, false);
        InitViews(view);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RankAdapter(DbQuery.g_userList);
        recyclerView.setAdapter(adapter);

        DbQuery.getTopScores(new MyCompleteListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void OnSuccess() {
                adapter.notifyDataSetChanged();
                if (rankModel.getScore() != 0) {
                    if (!DbQuery.amIonTop20) {
                        calculate();
                    }
                    tv_score.setText(requireContext().getString(R.string.your_score_is) + rankModel.getScore());
                    tv_rank.setText(requireContext().getString(R.string.rank_is) + rankModel.getRank());
                }
            }

            @Override
            public void OnFailure() {
                Toast.makeText(getContext(),requireContext().getString(R.string.errorMsg), Toast.LENGTH_SHORT).show();
            }
        });


        tv_users.setText(requireContext().getString(R.string.total_users_0)+ DbQuery.user_count);
        tv_img.setText(rankModel.getName().toUpperCase().substring(0, 1));
        return view;
    }

    private void calculate() {
        int lowTopScore = g_userList.get(g_userList.size() - 1).getScore();
        int remainingSlots = user_count - 20;
        int Myslot = (rankModel.getScore() * remainingSlots) / lowTopScore;
        int rank;
        if (lowTopScore != rankModel.getScore()) {
            rank = user_count - Myslot;
        } else {
            rank = 21;
        }
        rankModel.setRank(rank);

    }

    private void InitViews(View view) {
        tv_users = view.findViewById(R.id.total_users);
        tv_img = view.findViewById(R.id.img_text);
        tv_score = view.findViewById(R.id.total_Scores);
        tv_rank = view.findViewById(R.id.my_rank);
        recyclerView = view.findViewById(R.id.recyclerView2);
    }


}