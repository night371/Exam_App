package com.example.exam.Fragments;

import static com.example.exam.DbQuery.g_userList;
import static com.example.exam.DbQuery.rankModel;
import static com.example.exam.DbQuery.user_count;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.exam.Bookmark_Activity;
import com.example.exam.DbQuery;
import com.example.exam.Interface.MyCompleteListener;
import com.example.exam.Login_Activity;
import com.example.exam.MainActivity;
import com.example.exam.MyProfileActivity;
import com.example.exam.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {
    LinearLayout logout_btn, l_board_btn, profile_btn, saveQues_btn;
    TextView tv_name, tv_rank, tv_score, tv_profile;
    ImageView iv_profile_img;
    BottomNavigationView bottomNavigationView;

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar1);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(requireContext().getString(R.string.app_name));
    }

    Activity activity;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        InitView(view);
        // getActivity().setTitle("Account Fragment");


        tv_profile.setText(DbQuery.myProfile.getName().toUpperCase().substring(0, 1));
        tv_name.setText(DbQuery.myProfile.getName());

        tv_score.setText(String.valueOf(DbQuery.rankModel.getScore()));

        if (DbQuery.g_userList.size() == 0) {

            DbQuery.getTopScores(new MyCompleteListener() {
                @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                @Override
                public void OnSuccess() {

                    if (rankModel.getScore() != 0) {
                        if (!DbQuery.amIonTop20) {
                            calculate();
                        }
                        tv_score.setText("Score is :" + rankModel.getScore());
                        if (rankModel.getScore() != 0)
                            tv_rank.setText("Rank : " + rankModel.getRank());
                    }
                }

                @Override
                public void OnFailure() {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            tv_score.setText(requireContext().getString(R.string.urScor) + rankModel.getScore());
            tv_rank.setText(requireContext().getString(R.string.urRank) + rankModel.getRank());
        }


        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MyProfileActivity.class));

            }
        });
        saveQues_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Bookmark_Activity.class));
                getActivity().finish();

            }
        });
        l_board_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.nav_bottom_leader);
            }
        });


        return view;
    }


    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setTitle("\t" + "Exit App")
                .setIcon(R.drawable.ic_logout)
                .setCancelable(false)
                .setMessage(R.string.msg)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getContext(), Login_Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }).setNegativeButton(R.string.cancel, null)
                .show();


    }

    private void InitView(View view) {
        logout_btn = view.findViewById(R.id.btn_logout);
        tv_profile = view.findViewById(R.id.txt_profile_img);
        tv_name = view.findViewById(R.id.txt_name);
        tv_rank = view.findViewById(R.id.txt_rank);
        tv_score = view.findViewById(R.id.txt_overAllscore);
        profile_btn = view.findViewById(R.id.btn_profile);
        l_board_btn = view.findViewById(R.id.btn_leader_board);
        saveQues_btn = view.findViewById(R.id.btn_bookMark);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navbar);
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


}