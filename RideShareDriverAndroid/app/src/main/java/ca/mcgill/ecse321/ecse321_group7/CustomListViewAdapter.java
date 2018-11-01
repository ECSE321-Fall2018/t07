package ca.mcgill.ecse321.ecse321_group7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CustomListViewAdapter  extends ArrayAdapter<CustomListView> {
    private int mResource;
    private List<CustomListView> mItems;
    private LayoutInflater mInflater;

    public CustomListViewAdapter(Context context, int resource, List<CustomListView> items) {
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
        CustomListView item = mItems.get(position);

        // Setting up values
        TextView driver_name = (TextView)view.findViewById(R.id._drivname);
        driver_name.setText(item.getDriverName());

        TextView seats = (TextView)view.findViewById(R.id._seatsleft);
        seats.setText(item.getSeats() + " Seats Available");

        TextView dep_time = (TextView)view.findViewById(R.id._depTime);
        dep_time.setText(item.getDepTime());

        TextView duration = (TextView)view.findViewById(R.id._duration);
        duration.setText(item.getTripDur());

        return view;
    }

}
