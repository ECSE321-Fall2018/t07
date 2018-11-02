package ca.mcgill.ecse321.ecse321_group7;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

public class DestinationListViewAdapter  extends ArrayAdapter<DestinationListView> {
    private int mResource;
    private List<DestinationListView> mItems;
    private LayoutInflater mInflater;

    public DestinationListViewAdapter(Context context, int resource, List<DestinationListView> items) {
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

        final DestinationListView data = getItem(position);
        EditText dest = (EditText)view.findViewById(R.id.newDestinationField);
        EditText dur = (EditText)view.findViewById(R.id.newDurationField);
        EditText pr = (EditText)view.findViewById(R.id.newPriceField);

        dest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final EditText Caption = (EditText) v;
                    String obtained = Caption.getText().toString();
                    data.setDest(obtained);
                }
            }
        });


        dur.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final EditText Caption = (EditText) v;
                    String obtained = Caption.getText().toString();
                    data.setDur(obtained);
                }
            }
        });


        pr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final EditText Caption = (EditText) v;
                    String obtained = Caption.getText().toString();
                    data.setPrice(obtained);
                }
            }
        });

        dest.setText(mItems.get(position).getDest());
        dur.setText(mItems.get(position).getDur());
        pr.setText(mItems.get(position).getPrice());

        return view;
    }

    public String getDest(View view) {
        EditText destET = (EditText)view.findViewById(R.id.newDestinationField);
        return destET.getText().toString();
    }

    public String getDur(View view) {
        EditText durET = (EditText)view.findViewById(R.id.newDurationField);
        return durET.getText().toString();
    }

    public String getPrice(View view) {
        EditText priceET = (EditText)view.findViewById(R.id.newPriceField);
        return priceET.getText().toString();
    }

}
