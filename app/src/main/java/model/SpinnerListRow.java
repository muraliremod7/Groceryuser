package model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.indianservers.onlinegrocery.R;

/**
 * Created by Hari Prahlad on 02-06-2016.
 */
public class SpinnerListRow extends ArrayAdapter<String> {
    TextView lid,lname;
    private Activity activity;
    private String[] locationid;
    private String[] locationname;

    public SpinnerListRow(Activity activitySpinner, String[] locationid, String[] locationname) {
        super(activitySpinner, R.layout.spinnertext,locationname);
        this.activity = activitySpinner;
        this.locationid = locationid;
        this.locationname = locationname;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater=activity.getLayoutInflater();
        View rowview=layoutInflater.inflate(R.layout.spinner_layout, null,true);
        lid = (TextView) rowview.findViewById(R.id.locationId);
        lname  = (TextView) rowview.findViewById(R.id.locationName);
        lid.setText(locationid[position]);
       lname.setText(locationname[position]);

        return rowview;
    }
}