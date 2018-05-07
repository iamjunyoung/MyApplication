package com.bbeaggoo.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbeaggoo.myapplication.R;
import com.bbeaggoo.myapplication.datas.ItemObjects;
import com.bbeaggoo.myapplication.listener.OnItemClickListener;
import com.bumptech.glide.Glide;


//public class SolventViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
public class MyViewHolders extends ViewHolder implements View.OnClickListener{
    public CardView cardView;
    public TextView countryName;
    public ImageView countryPhoto;
    private OnItemClickListener onItemClickListener;

    public MyViewHolders(View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);

        this.onItemClickListener = onItemClickListener;

        ///
        cardView = (CardView)itemView.findViewById(R.id.card_view);
        ///
        countryName = (TextView) itemView.findViewById(R.id.country_name);
        countryPhoto = (ImageView) itemView.findViewById(R.id.country_photo);
    }

    @Override
    void bind(Context context, ItemObjects item, int position) {
        setNormalItem(context, item, position);
    }

    private void setNormalItem(final Context context, final ItemObjects item, final int position) {
        countryName.setText(item.getName());

            Glide.with(context)
                    .load(item.getPhoto())
                    .into(countryPhoto);

        Log.i("SolventViewHolders", "onBindViewHolder " + item.getName() + "   " + item.getPhoto());

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Long clicked item = " + item.getName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                    /*
                    if (item == null) {
                        Toast.makeText(context, "Clicked item is null.. return", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    File file = new File(item.path);
                    if (file.isDirectory()) {
                        if (file.canRead()) {
                            CurPath = path;
                            setupAdapter();
                        } else {

                        }
                        Log.i("JYN", "[MyViewHolders][setOnClickListener] " + item.getName() + " is dir");
                    } else {
                        Log.i("JYN", "[MyViewHolders][setOnClickListener] " + item.getName() + " is file");
                    }
                    */
                    Toast.makeText(context, "Clicked item = " + item.getName(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }


}
