package com.example.sagar.buskaro;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Modules.BusRoutes;
import Modules.BusStop;

/**
 * Created by Harshit Verma on 23-03-2018.
 */

public class DestAdapter extends RecyclerView.Adapter<DestAdapter.DestViewHolder> {
    private Context ctx;
    private List<Object> destlist;
    private EditText etorigin;
    private String origin;
    private List<BusRoutes> Allroutes;

    public DestAdapter(Context ctx, List<Object> destlist, EditText etorigin, String origin, List<BusRoutes> Allroutes) {
        this.ctx = ctx;
        this.destlist = destlist;
        this.etorigin = etorigin;
        this.origin = origin;
        this.Allroutes = Allroutes;
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
                holder.destname.setText(dest.getBusStop().getStopname());

                holder.destimg.setImageDrawable(ctx.getResources().getDrawable(dest.getImage()));
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etorigin.getText().toString().equals("")){
                            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(ctx);
                            builderSingle.setIcon(R.drawable.buskarologo);
                            builderSingle.setTitle("Origin Location not specified");
                            builderSingle.setMessage("Please Enter your Current/Nearest Bus Stop first");
                            builderSingle.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener(){
                                        @SuppressLint("LongLogTag")
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                            builderSingle.show();
                        }
                        else {
                            Intent intent = new Intent(ctx, Bus_Routes_Search_Result.class);
                            intent.putExtra("EndDestinationBusStop", dest.getBusStop());
                            intent.putExtra("Origin", etorigin.getText().toString());
                            intent.putExtra("LatLngCurrentLocation", origin);
//                          intent.putExtra("Routes",(Object) Allroutes);
                            ctx.startActivity(intent);
                        }

                    }
                });
                break;

            case 1:
                final Dest2 dest2=(Dest2) destlist.get(position);
                BusRoutes route = dest2.getBusRoute();
                holder.destname.setText(route.getDestination());
                holder.busno.setText(route.getBus_number());
                holder.startname.setText(route.getOrigin());

                holder.destimg.setImageDrawable(ctx.getResources().getDrawable(dest2.getImage()));
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(etorigin.getText().toString().equals("")){
                            Intent intent = new Intent(ctx, Route_Description.class);
                            intent.putExtra("BusRoutes", dest2.getBusRoute());
                            intent.putExtra("Origin", dest2.getBusRoute().getOrigin());
                            intent.putExtra("LatLngCurrentLocation", origin);
                            ctx.startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(ctx, Route_Description.class);
                            intent.putExtra("BusRoutes", dest2.getBusRoute());
                            intent.putExtra("Origin", etorigin.getText().toString());
                            intent.putExtra("LatLngCurrentLocation", origin);
                            Log.d("Origin Text Intent", "onClick: " + etorigin.getText().toString());
                            ctx.startActivity(intent);
                        }
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
