package com.example.sagar.buskaro;

/**
 * Created by Harshit Verma on 27-03-2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RouteDescAdapter extends RecyclerView.Adapter<RouteDescAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Routedesc_getters> mData ;


    public RouteDescAdapter(Context mContext, List<Routedesc_getters> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    // Recycler view Adapter for courses

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.routedesc_singleitem,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.stop.setText(mData.get(position).getStop());
        holder.location.setImageResource(mData.get(position).getLocation());

    }
    // Default functions
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView stop;
        ImageView location;

        public MyViewHolder(View itemView) {
            super(itemView);

            stop = itemView.findViewById(R.id.stop);
            location=itemView.findViewById(R.id.location);


        }
    }


}