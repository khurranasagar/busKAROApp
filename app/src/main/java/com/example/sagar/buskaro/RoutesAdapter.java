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

import Modules.BusRoutes;
import Modules.BusStop;

/**
 * Created by Sagar on 3/27/2018.
 */

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.RouteViewHolder> {

    private Context ctx;
    private List<BusRoutes> Routes;
    private String origin;
    private String CurrenLoc;
    private BusStop DestBusStop;

    public RoutesAdapter(Context ctx, List<BusRoutes> Routes,String Origin,String CurentLocLatLng,BusStop DestinationBusStop ) {
        this.ctx = ctx;
        this.Routes = Routes;
        this.origin = Origin;
        this.CurrenLoc = CurentLocLatLng;
        this.DestBusStop = DestinationBusStop;
    }

    @Override
    public RoutesAdapter.RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.bus_routes_search_layout, null);
        return new RoutesAdapter.RouteViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RoutesAdapter.RouteViewHolder holder, final int position) {
        holder.BusName.setText(Routes.get(position).getBus_number().toString());
        holder.End_Destination.setText(Routes.get(position).getDestination());
        holder.cdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, Route_Description.class);
                intent.putExtra("BusRoutes", Routes.get(position));
                intent.putExtra("Origin", origin);
                intent.putExtra("LatLngCurrentLocation", CurrenLoc);
                intent.putExtra("Destination",DestBusStop);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Routes.size();
    }

    class RouteViewHolder extends RecyclerView.ViewHolder{
        TextView ETA;
        TextView BusName;
        TextView End_Destination;
        CardView cdv;
        public RouteViewHolder(View itemView) {
            super(itemView);
            ETA = (TextView)itemView.findViewById(R.id.EtaTime);
            BusName = (TextView) itemView.findViewById(R.id.Busno);
            End_Destination = (TextView) itemView.findViewById(R.id.EndDestination);
            cdv = (CardView) itemView.findViewById(R.id.cdv);
        }
    }

    public void setfilter(List<BusRoutes> filteredDests){

        Routes  = new ArrayList<BusRoutes>();
        Routes.addAll(filteredDests);
        notifyDataSetChanged();

    }




}
