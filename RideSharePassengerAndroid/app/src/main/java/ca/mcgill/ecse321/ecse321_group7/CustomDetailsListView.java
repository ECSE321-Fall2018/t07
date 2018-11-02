package ca.mcgill.ecse321.ecse321_group7;


import android.widget.TextView;

public class CustomDetailsListView {

    private String mdestination = null;
    private String mduration = null;
    private String mprice = null;

    public CustomDetailsListView() {};

    public CustomDetailsListView(String destination, String duration, String price) {
        mdestination = destination;
        mduration = duration;
        mprice = price;
    }

    public void setDestination (String dest) {
        mdestination = dest;
    }
    public void setTripDur (String dur) {
        mduration = dur;
    }
    public void setPrice (String price) {
        mprice = price;
    }


    public String getDestination () {
        return mdestination;
    }
    public String getTripDur () {
        return mduration;
    }
    public String getPrice () {
        return mprice;
    }

}
