package Modules;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by abhijeet on 23/3/18.
 */

public class BusStop {

    public String name_of_bus_stop;
    public LatLng coordinate;

    public BusStop(String stop_name){
        this.name_of_bus_stop = stop_name;
    }

    public String getStopname(){
        return name_of_bus_stop;
    }

}
