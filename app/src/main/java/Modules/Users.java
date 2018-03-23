package Modules;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by abhijeet on 17/3/18.
 */

public class Users {

    Map<String,Integer> bus_numbers_used_order ;
    List<String> starts;
    List<String> destinations;
    int buskaro_credits;
    int journey_number;


    public Users() {

        bus_numbers_used_order = new Hashtable<String, Integer>();
        starts = new ArrayList<String>();
        destinations = new ArrayList<String>();
        for (int i = 0; i < 1000; i++) {
            starts.add("nothing");
            destinations.add("nothing");
        }
        bus_numbers_used_order.put("Yo", 1);
        journey_number = 0;
        buskaro_credits = 5;

    }





}
