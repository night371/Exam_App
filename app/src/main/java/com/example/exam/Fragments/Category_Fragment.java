package com.example.exam.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.exam.Adapters.CategoryAdapter;
import com.example.exam.DbQuery;
import com.example.exam.R;


public class Category_Fragment extends Fragment {

    public Category_Fragment() {

    }
    GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_catogery, container, false);


        gridView = view.findViewById(R.id.text_grid_view);

        CategoryAdapter adapter = new CategoryAdapter(DbQuery.g_catlist,getContext());
        gridView.setAdapter(adapter);
        return view;
    }


}