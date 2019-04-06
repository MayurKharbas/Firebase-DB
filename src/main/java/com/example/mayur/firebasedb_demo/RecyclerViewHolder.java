package com.example.mayur.firebasedb_demo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView txt_title, txt_content;

    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_title = (TextView) itemView.findViewById(R.id.txt_title);
        txt_content = (TextView) itemView.findViewById(R.id.txt_content);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition());

    }
}
