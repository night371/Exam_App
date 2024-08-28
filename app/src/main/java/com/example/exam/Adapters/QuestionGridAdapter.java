package com.example.exam.Adapters;

import static com.example.exam.DbQuery.ANSWERED;
import static com.example.exam.DbQuery.NOT_ANSWERED;
import static com.example.exam.DbQuery.NOT_VISITED;
import static com.example.exam.DbQuery.REVIEW;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.exam.DbQuery;
import com.example.exam.R;

public class QuestionGridAdapter extends BaseAdapter {

    private final int noOfQues;

    public QuestionGridAdapter(int noOfQues) {
        this.noOfQues = noOfQues;
    }

    @Override
    public int getCount() {
        return noOfQues;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView Grid_no;


        View myView;
        if (convertView == null) {
            myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_grid, parent, false);
        } else {
            myView = convertView;
        }
        Grid_no = myView.findViewById(R.id.txt_grid_num);
        Grid_no.setText(String.valueOf(position+1));

        switch (DbQuery.q_quesList.get(position).getStatus()) {
            case NOT_VISITED:
                Grid_no.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(), R.color.grey)));
                break;
            case NOT_ANSWERED:
                Grid_no.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(), R.color.red)));
                break;
            case ANSWERED:
                Grid_no.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(), R.color.timeColor)));
                break;
            case REVIEW:
                Grid_no.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(), R.color.blue)));
                break;
            default:
                break;
        }
        return myView;


    }
}
