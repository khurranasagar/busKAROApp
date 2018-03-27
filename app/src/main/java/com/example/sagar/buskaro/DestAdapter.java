package com.example.sagar.buskaro;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harshit Verma on 23-03-2018.
 */

public class DestAdapter extends RecyclerView.Adapter<DestAdapter.DestViewHolder> {
    private Context ctx;
    private List<Object> destlist;

    public DestAdapter(Context ctx, List<Object> destlist) {
        this.ctx = ctx;
        this.destlist = destlist;
    }


    @Override
    public DestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        DestViewHolder vh;
        switch (viewType){
            case 0:
                LayoutInflater inflater = LayoutInflater.from(ctx);
                view = inflater.inflate(R.layout.dest_card1, null);
                vh=new DestViewHolder(view,viewType);
                return vh;
            case 1:
                LayoutInflater inflater2 = LayoutInflater.from(ctx);
                view = inflater2.inflate(R.layout.dest_card2, null);
                vh=new DestViewHolder(view,viewType);
                return vh;
            default:
                LayoutInflater inflater3 = LayoutInflater.from(ctx);
                view = inflater3.inflate(R.layout.dest_card1, null);
                vh=new DestViewHolder(view,viewType);
                return vh;
        }

    }

    @Override
    public void onBindViewHolder(DestViewHolder holder, final int position) {
        switch (holder.getItemViewType()){
            case 0:

                final Dest dest=(Dest)destlist.get(position);
                holder.destname.setText(dest.getDestname());

                holder.destimg.setImageDrawable(ctx.getResources().getDrawable(dest.getImage()));
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ctx,Bus_Routes_Search_Result.class);
                        intent.putExtra("dest",dest.getDestname());

                        intent.putExtra("favrecimg",dest.getImage());
                        ctx.startActivity(intent);

                    }
                });
                break;

            case 1:
                Dest2 dest2=(Dest2) destlist.get(position);
                holder.destname.setText(dest2.getDest());
                holder.busno.setText(dest2.getBusno());
                holder.startname.setText(dest2.getStart());

                holder.destimg.setImageDrawable(ctx.getResources().getDrawable(dest2.getImage()));
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ctx,Route_Description.class);
                        intent.putExtra("dest",((Dest2) destlist.get(position)).getDest());
                        intent.putExtra("busno",((Dest2) destlist.get(position)).getBusno());
                        intent.putExtra("favrecimg",((Dest2) destlist.get(position)).getImage());
                        intent.putExtra("start",((Dest2) destlist.get(position)).getStart());

                        ctx.startActivity(intent);

                    }
                });
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(destlist.get(position) instanceof Dest)
            return 0;
        else if(destlist.get(position) instanceof Dest2)
            return 1;
        else
            return 0;
    }

    @Override
    public int getItemCount() {
        return destlist.size();
    }

    class DestViewHolder extends RecyclerView.ViewHolder{
        ImageView destimg;
        TextView destname,startname,busno;
        CardView cardView;



        public DestViewHolder(View itemView, int viewType) {
            super(itemView);
            if(viewType==0)
            {
                destimg=itemView.findViewById(R.id.desticon);
                destname=itemView.findViewById(R.id.destname);
                cardView = itemView.findViewById(R.id.dest_card);
            }
            else if(viewType==1)
            {
                destimg=itemView.findViewById(R.id.desticon2);
                busno=itemView.findViewById(R.id.destname2);
                cardView = itemView.findViewById(R.id.dest_card2);
                startname = itemView.findViewById(R.id.start);
                destname = itemView.findViewById(R.id.end);
            }


        }
    }
    public void setfilter(List<Object> filteredDests){

        destlist  = new ArrayList<Object>();
        destlist.addAll(filteredDests);
        notifyDataSetChanged();

    }

}
