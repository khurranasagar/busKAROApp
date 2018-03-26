package com.example.sagar.buskaro;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Harshit Verma on 23-03-2018.
 */

public class DestAdapter extends RecyclerView.Adapter<DestAdapter.ProductViewHolder> {
    private Context ctx;
    private List<Dest> destlist;

    public DestAdapter(Context ctx, List<Dest> destlist) {
        this.ctx = ctx;
        this.destlist = destlist;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.dest_card1, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Dest prod=destlist.get(position);
        holder.destname.setText(prod.getDestname());

        holder.destimg.setImageDrawable(ctx.getResources().getDrawable(prod.getImage()));
    }

    @Override
    public int getItemCount() {
        return destlist.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{
        ImageView destimg;
        TextView destname;
        public ProductViewHolder(View itemView) {
            super(itemView);
            destimg=itemView.findViewById(R.id.desticon);
            destname=itemView.findViewById(R.id.destname);

        }
    }
}
