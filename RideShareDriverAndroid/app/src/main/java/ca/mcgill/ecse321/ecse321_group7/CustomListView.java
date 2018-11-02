package ca.mcgill.ecse321.ecse321_group7;


import android.widget.TextView;

public class CustomListView {
    private int mtrip_id = -1;
    private String mname = null;
    private String mseats = null;
    private String mtime = null;
    private String mduration = null;
    private String mjson = null;

    public CustomListView() {};

    /* Constructor
     * @param name: Driver's name
     * @param seats: available seats
     * @param time: departure time
     * @param duration: trip duration
     */
    public CustomListView(int trip_id, String name, String seats, String time, String duration, String json) {
        mtrip_id = trip_id;
        mname = name;
        mseats = seats;
        mtime = time;
        mduration = duration;
        mjson = json;
    }

    public void setTrip_id (int trip_id) {
        mtrip_id = trip_id;
    }

    public void setDriverName (String name) {
        mname = name;
    }

    public void setSeats (String seats) {
        mseats = seats;
    }

    public void setDepTime (String time) {
        mtime = time;
    }

    public void setTripDur (String dur) {
        mduration = dur;
    }

    public void setJSON (String json) {
        mjson = json;
    }


    public int getTripId () {
        return mtrip_id;
    }

    public String getDriverName () {
        return mname;
    }

    public String getSeats () {
        return mseats;
    }

    public String getDepTime () {
        return mtime;
    }

    public String getTripDur () {
        return mduration;
    }

    public String getJSON() { return mjson; }
}
