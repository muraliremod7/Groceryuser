package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.firebase.client.Firebase;
import com.indianservers.onlinegrocery.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.AddressCommonClass;
import model.ProductCommonClass;


/**
 * Created by Hari Prahlad on 05-06-2016.
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> implements
        ItemTouchHelperAdapter {
    public Activity activity;
    private Firebase firebase;
    private SharedPreferences sskey;
    private ProductCommonClass productCommonClass;
    private LayoutInflater mInflater;
    public List<AddressCommonClass> allCommonClasses = new ArrayList<>();
    SharedPreferences sharedPrefs;
    private OnItemClickListener clickListener;
    int count;
    String uid;

    public AddressAdapter(Activity activity, List<AddressCommonClass> allCommonClasses) {
        this.allCommonClasses = allCommonClasses;
        this.activity = activity;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.deliveryaddresssingle, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public int getItemCount() {
        return allCommonClasses == null ? 0 : allCommonClasses.size();
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.aId.setText(allCommonClasses.get(position).getAddpid());
        holder.anName.setText(allCommonClasses.get(position).getAddnickname());
        holder.apname.setText(allCommonClasses.get(position).getAddpersoname());
        holder.ahouseno.setText(allCommonClasses.get(position).getAddhouseno());
        holder.astreetno.setText(allCommonClasses.get(position).getAddstreetname());
        holder.aArea.setText(allCommonClasses.get(position).getAddarea());
        holder.aApname.setText(allCommonClasses.get(position).getAddapartmentno());
        holder.alandmark.setText(allCommonClasses.get(position).getAddlandmark());
        holder.aCity.setText(allCommonClasses.get(position).getAddcity());
        holder.aPincode.setText(allCommonClasses.get(position).getAddpincode());
        holder.aMobile.setText(allCommonClasses.get(position).getAddmobile());

        holder.updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.update_address, null);
                builder.setView(dialogView);
                builder.setTitle("Update Address");
                final AlertDialog alertDialog = builder.create();
                final EditText nickname = (EditText)dialogView.findViewById(R.id.addressnicknameedital);
                final EditText personname = (EditText)dialogView.findViewById(R.id.addresspersonnameedital);
                final EditText hoseno = (EditText)dialogView.findViewById(R.id.addresshousenoedital);
                final EditText streetname = (EditText)dialogView.findViewById(R.id.addressstreetedital);
                final EditText areaname = (EditText)dialogView.findViewById(R.id.addressareaedital);
                final EditText apartmentname = (EditText)dialogView.findViewById(R.id.addressapartmentnameedital);
                final EditText landmark = (EditText)dialogView.findViewById(R.id.addresslandmarkedital);
                final EditText city = (EditText)dialogView.findViewById(R.id.addresscityedital);
                final EditText pincode = (EditText)dialogView.findViewById(R.id.addresspincodeedital);
                final EditText mobile = (EditText)dialogView.findViewById(R.id.addressmobileedital);
                nickname.setText(allCommonClasses.get(position).getAddnickname());
                personname.setText(allCommonClasses.get(position).getAddpersoname());
                hoseno.setText(allCommonClasses.get(position).getAddhouseno());
                streetname.setText(allCommonClasses.get(position).getAddstreetname());
                areaname.setText(allCommonClasses.get(position).getAddarea());
                apartmentname.setText(allCommonClasses.get(position).getAddapartmentno());
                landmark.setText(allCommonClasses.get(position).getAddlandmark());
                city.setText(allCommonClasses.get(position).getAddcity());
                pincode.setText(allCommonClasses.get(position).getAddpincode());
                mobile.setText(allCommonClasses.get(position).getAddmobile());

                Button update = (Button)dialogView.findViewById(R.id.saveAddressal);
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddressCommonClass aClass = new AddressCommonClass();
                        aClass.setAddnickname(nickname.getText().toString());
                        aClass.setAddpersoname(personname.getText().toString());
                        aClass.setAddhouseno(hoseno.getText().toString());
                        aClass.setAddstreetname(streetname.getText().toString());
                        aClass.setAddarea(areaname.getText().toString());
                        aClass.setAddapartmentno(apartmentname.getText().toString());
                        aClass.setAddlandmark(landmark.getText().toString());
                        aClass.setAddcity(city.getText().toString());
                        aClass.setAddpincode(pincode.getText().toString());
                        aClass.setAddmobile(mobile.getText().toString());

                        SharedPreferences sskey = PreferenceManager.getDefaultSharedPreferences(activity);
                        String SSkey = sskey.getString("uid","0");
                        firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"Address"+"/"+SSkey+"/"+SSkey);
                        firebase.child(allCommonClasses.get(position).getAddpid()).setValue(aClass);
                        alertDialog.dismiss();
                        Toast.makeText(activity,"AddressUpdate Successfully",Toast.LENGTH_SHORT).show();
                        onItemDismiss(position);
                    }
                });
                alertDialog.show();

            }
        });
        holder.deleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sskey = PreferenceManager.getDefaultSharedPreferences(activity);
                String SSkey = sskey.getString("uid","0");
                firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"Address"+"/"+SSkey+"/"+SSkey);
                firebase.child(allCommonClasses.get(position).getAddpid()).removeValue();
                onItemDismiss(position);
            }
        });
    }
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(allCommonClasses, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(allCommonClasses, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        allCommonClasses.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
    public void SetOnItemClickListener(final AddressAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        public TextView aId,anName, apname, ahouseno, astreetno,aArea,aApname,alandmark,aCity,aPincode,aMobile;
        ImageView deleteAddress,updateAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            aId = (TextView) itemView.findViewById(R.id.aid);
            anName = (TextView) itemView.findViewById(R.id.addressnickname);
            apname = (TextView) itemView.findViewById(R.id.addressName);
            ahouseno = (TextView) itemView.findViewById(R.id.addressHouseno);
            astreetno = (TextView) itemView.findViewById(R.id.addressStreet);
            aArea = (TextView) itemView.findViewById(R.id.addressArea);
            aApname = (TextView) itemView.findViewById(R.id.addressAppartmentName);
            alandmark = (TextView) itemView.findViewById(R.id.addressLandmark);
            aCity = (TextView) itemView.findViewById(R.id.addressCity);
            aPincode = (TextView) itemView.findViewById(R.id.addressPincode);
            aMobile = (TextView)itemView.findViewById(R.id.addressmobile);

            updateAddress = (ImageView) itemView.findViewById(R.id.updateAddress);
            deleteAddress = (ImageView) itemView.findViewById(R.id.deleteAddress);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }

}
