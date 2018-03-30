package com.example.sagar.buskaro;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

/**
 * Created by Belal on 10/18/2017.
 */


public class FavDestAdapter extends RecyclerView.Adapter<FavDestAdapter.FavDestViewHolder> {


    private Context mCtx;
    private List<FavDest> buslist;

    public FavDestAdapter(Context mCtx, List<FavDest> buslist) {
        this.mCtx = mCtx;
        this.buslist = buslist;
    }

    @Override
    public FavDestAdapter.FavDestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.fav_destcard, null);
        return new FavDestAdapter.FavDestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavDestAdapter.FavDestViewHolder holder, int position) {
        //getting the product of the specified position
        FavDest product = buslist.get(position);
        holder.fav_dest.setText(product.getFav_dest());
        }


    @Override
    public int getItemCount() {
        return buslist.size();
    }


    class FavDestViewHolder extends RecyclerView.ViewHolder {

        TextView fav_dest;

        public FavDestViewHolder(View itemView) {
            super(itemView);
            fav_dest = itemView.findViewById(R.id.EndDestination_fav2);

        }
    }
}