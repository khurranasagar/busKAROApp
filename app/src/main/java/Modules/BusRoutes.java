package Modules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhijeet on 23/3/18.
 */

public class BusRoutes implements Serializable {

    public String bus_number;
    public List<BusStop> stations;
    public List<String> ETAs;

    public BusRoutes(String bus_number)
    {
        this.bus_number = bus_number;
        this.stations = new ArrayList<BusStop>();
        this.ETAs = new ArrayList<String>();
    }

    public String getBus_number(){
        return bus_number;
    }

    public void setStations(List<BusStop> stations){
        this.stations = stations;
    }

    public String getOrigin(){
        if(stations.size() > 1) {
            return stations.get(0).getStopname();
        }
        else{
            return "Default Station";
        }
    }

    public String getDestination(){
        if(stations.size() > 1) {
            return stations.get(stations.size() - 1).getStopname();
        }
        else{
            return "Default Station";
        }
    }

    public List<BusStop> getStations(){
        return stations;
    }

    public void setETAs(List<String> ET){
        this.ETAs = ET;
    }

    public List<String> getETAs(){
        return ETAs;
    }




}
