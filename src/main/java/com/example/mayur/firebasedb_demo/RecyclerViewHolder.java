package com.example.mayur.firebasedb_demo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView txt_title, txt_content;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_title = (TextView) itemView.findViewById(R.id.txt_title);
        txt_content = (TextView) itemView.findViewById(R.id.txt_content);
    }
}
