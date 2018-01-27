package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.indianservers.onlinegrocery.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.GridviewModel;
import model.PharmacyModelOne;

/**
 * Created by Prabhu on 13-12-2017.
 */

public class GridViewAdapter extends BaseAdapter {

    // Declare variables
    private Activity activity;
    Firebase firebase;
    private SharedPreferences sskey;
    public String profileid;
    private String[] strings;
    public List selectedPositions;
    private static LayoutInflater inflater = null;
    private ArrayList<PharmacyModelOne> gridviewModels;
    public GridViewAdapter(Activity a, ArrayList<PharmacyModelOne> gridviewModels) {
        activity = a;
        this.gridviewModels = gridviewModels;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sskey = PreferenceManager.getDefaultSharedPreferences(activity);
        profileid = sskey.getString("uid","0");
        selectedPositions = new ArrayList<>();

    }
    public int getCount() {
        return gridviewModels.size();

    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        GridItemView customView = (convertView == null) ? new GridItemView(activity) : (GridItemView) convertView;
        customView.display(gridviewModels.get(position).getPresName(), selectedPositions.contains(position));
        // Locate the TextView in gridview_item.xml
        TextView text = (TextView) customView.findViewById(R.id.pharmacysingletextview);
        TextView imageurl = (TextView) customView.findViewById(R.id.pharmacysingleimageurl);
        TextView uid = (TextView) customView.findViewById(R.id.pharmacysingleuid);
        TextView presdata = (TextView)customView.findViewById(R.id.pharmacysinglepresdata);
        // Locate the ImageView in gridview_item.xml
        ImageView image = (ImageView) customView.findViewById(R.id.pharmacysingleimage);
        ImageView delete = (ImageView)customView.findViewById(R.id.deletepharmacy);
        final PharmacyModelOne gridviewModel = gridviewModels.get(position);
        // Set file name to the TextView followed by the position
        text.setText(gridviewModel.getPresName());
        imageurl.setText(gridviewModel.getPresImageUrl());
        uid.setText(gridviewModel.getUid());
        presdata.setText(gridviewModel.getPresDesc());
        // Decode the filepath with BitmapFactory followed by the position
        Glide.with(activity).load(gridviewModel.getPresImageUrl()).into(image);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Firebase.setAndroidContext(activity);
                firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"Pharmacy"+"/"+profileid);
                firebase.child(gridviewModel.getUid()).removeValue();
                        gridviewModels.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(activity,"Prescription Deleted",Toast.LENGTH_LONG).show();

            }
        });
        // Set the decoded bitmap into ImageView
        return customView;
    }
}
