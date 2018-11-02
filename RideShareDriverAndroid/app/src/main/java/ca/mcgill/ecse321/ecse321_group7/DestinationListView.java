package ca.mcgill.ecse321.ecse321_group7;


import android.widget.EditText;
import android.widget.TextView;

public class DestinationListView {

    private String destination;
    private String duration;
    private String price;

    public DestinationListView() {
        //destination = findViewById(R.id.newDestinationField);
        destination = new String();
        duration = new String();
        price = new String();
    };


    public void setDest (String dest) {
        destination = dest;
    }

    public void setDur (String dur) {
        duration = dur;
    }

    public void setPrice (String pr) {
        price = pr;
    }

    public String getDest () {
        return destination;
    }

    public String getDur () {
        return duration;
    }

    public String getPrice () {
        return price;
    }

}
