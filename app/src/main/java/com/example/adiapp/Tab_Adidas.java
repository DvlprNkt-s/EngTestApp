package com.example.adiapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adiapp.Adapter.CategoryAdapter;
import com.example.adiapp.Common.SpaceDecoration;
import com.example.adiapp.DBHelper.DBAdidas;

public class Tab_Adidas extends Fragment {
    private RecyclerView recycler_category;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_tab__adidas, container, false);
        recycler_category=(RecyclerView)view.findViewById(R.id.recycler_view);
        recycler_category.setHasFixedSize(true);
        recycler_category.setLayoutManager(new GridLayoutManager(getContext(),1));

        CategoryAdapter categoryAdapter =new CategoryAdapter(getContext(), DBAdidas.getInstance(getContext()).getAllCategories());
        int spacePixel = 4;
        recycler_category.addItemDecoration(new SpaceDecoration(spacePixel));
        recycler_category.setAdapter(categoryAdapter);
        return view;
    }


}
