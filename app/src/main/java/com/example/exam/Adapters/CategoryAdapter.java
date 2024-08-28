package com.example.exam.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.exam.DbQuery;
import com.example.exam.Models.CategoryModel;
import com.example.exam.R;
import com.example.exam.TestActivity;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    List<CategoryModel> categoryModels;
    private final Context context;

    public CategoryAdapter(List<CategoryModel> models, Context context) {
        this.categoryModels = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categoryModels.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n", "StringFormatInvalid"})
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_item_layout, parent, false);
        convertView.setOnClickListener(v -> {
            DbQuery.g_selected_cat_index = i;
            Intent intent = new Intent(v.getContext(), TestActivity.class);
            v.getContext().startActivity(intent);
        });

        TextView txt_name = convertView.findViewById(R.id.cat_name);
        TextView txt_no = convertView.findViewById(R.id.no_of_tests);

        // Get the subject name from the database
        String subjectName = categoryModels.get(i).getName();

        // Use the string resource to get the Persian translation
        int persianNameResId = context.getResources().getIdentifier(subjectName.toLowerCase(), "string", context.getPackageName());
        if (persianNameResId != 0) {
            txt_name.setText(context.getString(persianNameResId));
        } else {
            txt_name.setText(subjectName);
        }

        // Display the number of tests
        txt_no.setText(context.getString(R.string.num_of_tests, categoryModels.get(i).getNumOfTests()));

        return convertView;
    }


}