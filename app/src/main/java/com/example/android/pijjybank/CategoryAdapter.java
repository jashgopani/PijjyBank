package com.example.android.pijjybank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<Category> {
    public CategoryAdapter(@NonNull Context context, ArrayList<Category> categoryList) {
        super(context, 0, categoryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_category_layout, parent, false);
        }

        ImageView imageViewCategory = convertView.findViewById(R.id.imageViewCategory);
        TextView textViewCategory = convertView.findViewById(R.id.textViewCategory);

        Category currentItem = getItem(position);
        if (currentItem != null) {
            imageViewCategory.setImageResource(currentItem.getCategoryIcon());
            textViewCategory.setText(currentItem.getCategoryName());
        }
        return convertView;
    }
}
