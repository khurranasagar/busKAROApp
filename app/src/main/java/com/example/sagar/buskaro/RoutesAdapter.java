package com.example.sagar.buskaro;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sagar on 3/27/2018.
 */

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.RouteViewHolder> {

    private Context ctx;
    private List<String> Routes;

    public RoutesAdapter(Context ctx, List<String> Routes) {
        this.ctx = ctx;
        this.Routes = Routes;
    }

    @Override
    public RoutesAdapter.RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.dest_card1, null);
        return new RoutesAdapter.RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoutesAdapter.RouteViewHolder holder, int position) {
        holder.BusName.setText(Routes.get(position));
    }

    @Override
    public int getItemCount() {
        return Routes.size();
    }

    class RouteViewHolder extends RecyclerView.ViewHolder{
        TextView ETA;
        TextView BusName;
        TextView End_Destination;
        public RouteViewHolder(View itemView) {
            super(itemView);


        }
    }



}
