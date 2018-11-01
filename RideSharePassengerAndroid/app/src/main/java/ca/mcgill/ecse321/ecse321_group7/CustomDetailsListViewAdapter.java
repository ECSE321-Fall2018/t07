package ca.mcgill.ecse321.ecse321_group7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CustomDetailsListViewAdapter  extends ArrayAdapter<CustomDetailsListView> {
    private int mResource;
    private List<CustomDetailsListView> mItems;
    private LayoutInflater mInflater;

    public CustomDetailsListViewAdapter(Context context, int resource, List<CustomDetailsListView> items) {
        super(context, resource, items);

        mResource = resource;
        mItems = items;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView != null) {
            view = convertView;
        }
        else {
            view = mInflater.inflate(mResource, null);
        }

        // obtain that particular item
        CustomDetailsListView item = mItems.get(position);

        // Setting up values
        TextView destination = (TextView)view.findViewById(R.id.destinationBox);
        destination.setText(item.getDestination());

        TextView duration = (TextView)view.findViewById(R.id.durationBox);
        duration.setText(formatDuration(item.getTripDur()));

        TextView price = (TextView)view.findViewById(R.id.priceBox);
        price.setText("$" + roundOffFloat(item.getPrice()));

        return view;
    }

    private String roundOffFloat(String input) {
        float floatVersion = Float.parseFloat(input);
        int cutOff = (int) floatVersion;
        return Integer.toString(cutOff);
    }

    private String formatDuration(String input) {
        float floatDur = Float.parseFloat(input);
        int hours = (int)floatDur;
        int floatHundred = (int)(floatDur*100);
        int fraction = floatHundred%100;
        int mins = (int)((float)fraction * 60)/100;
        String output = hours + "h " + mins + "m";
        return output;
    }

}
