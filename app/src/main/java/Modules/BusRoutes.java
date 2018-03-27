package Modules;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhijeet on 23/3/18.
 */

public class BusRoutes {

    public String bus_number;
    public List<BusStop> stations;
    public int total_stations;

    public void BusRoutes()
    {
        stations =  new ArrayList<>();
        total_stations=1;

    }


}
