package com.example.exam;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.exam.Adapters.CategoryAdapter;


public class Test_List_Admin_Category extends Fragment {

    GridView gridView;

    public Test_List_Admin_Category() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.test_list_add_activity_layout, container, false);


        gridView = view.findViewById(R.id.text_grid_view_admin);

        CategoryAdapter adapter = new CategoryAdapter(DbQuery.g_catlist,getContext());
        gridView.setAdapter(adapter);
        return view;
    }
}