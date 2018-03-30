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


public class FavBusnoAdapter extends RecyclerView.Adapter<FavBusnoAdapter.FavBusNoViewHolder> {


    private Context mCtx;
    private List<FavBusNo> buslist;

    public FavBusnoAdapter(Context mCtx, List<FavBusNo> buslist) {
        this.mCtx = mCtx;
        this.buslist = buslist;
    }

    @Override
    public FavBusNoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.fav_busnocard, null);
        return new FavBusNoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavBusNoViewHolder holder, int position) {
        //getting the product of the specified position
        FavBusNo product = buslist.get(position);

        //binding the data with the viewholder views
        holder.fav_busno.setText(product.getFav_busno());
        holder.fav_dest.setText(product.getFav_dest());

    }


    @Override
    public int getItemCount() {
        return buslist.size();
    }


    class FavBusNoViewHolder extends RecyclerView.ViewHolder {

        TextView fav_busno,fav_dest;

        public FavBusNoViewHolder(View itemView) {
            super(itemView);

            fav_busno = itemView.findViewById(R.id.Busno_fav1);
            fav_dest = itemView.findViewById(R.id.EndDestination_fav1);

        }
    }
}