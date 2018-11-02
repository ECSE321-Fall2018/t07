package ca.mcgill.ecse321.ecse321_group7;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class myDialogFragment extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage("Sorry, no matches found. ");

        dialogBuilder.setPositiveButton("Back", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                TripSearchResult prevActivity = (TripSearchResult) getActivity();
                prevActivity.finish();
            }
        });

        return dialogBuilder.create();
    }
}
