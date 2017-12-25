package adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.firebase.client.Firebase;
import com.indianservers.onlinegrocery.CheckOutActivity;
import com.indianservers.onlinegrocery.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.OrdersCommonClass;
import model.ProductCommonClass;


/**
 * Created by Hari Prahlad on 05-06-2016.
 */
public class CheckoutListviewAdapter extends RecyclerView.Adapter<CheckoutListviewAdapter.ViewHolder> implements
        ItemTouchHelperAdapter {
    public Activity activity;
    private Firebase firebase;
    private SharedPreferences sskey;
    private ProductCommonClass productCommonClass;
    private LayoutInflater mInflater;
    public List<OrdersCommonClass> allCommonClasses = new ArrayList<>();
    SharedPreferences sharedPrefs;
    private OnItemClickListener clickListener;
    int count;
    String uid;

    public CheckoutListviewAdapter(Activity activity, List<OrdersCommonClass> allCommonClasses) {
        this.allCommonClasses = allCommonClasses;
        this.activity = activity;
        mInflater = LayoutInflater.from(activity);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.items_checkout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public int getItemCount() {
        return allCommonClasses == null ? 0 : allCommonClasses.size();
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.pId.setText(allCommonClasses.get(position).getPruid());
        holder.pName.setText(allCommonClasses.get(position).getPrName());
        holder.pPrice.setText("RS."+allCommonClasses.get(position).getPrPrice());
        holder.pQuantity.setText(allCommonClasses.get(position).getPrQunatity());
        holder.pmeasure.setText(allCommonClasses.get(position).getPrMeasure());
        Glide.with(activity)
                .load(allCommonClasses.get(position).getPimage())
                .into(holder.pimage);

        holder.deleteitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sskey = PreferenceManager.getDefaultSharedPreferences(activity);
                String SSkey = sskey.getString("uid","0");
                firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"CartItems"+"/"+SSkey+"/"+SSkey);
                firebase.child(allCommonClasses.get(position).getPruid()).removeValue();
                onItemDismiss(position);
                updateResults(allCommonClasses);
                Intent intent = new Intent(activity, CheckOutActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }
    public void updateResults(List<OrdersCommonClass> results) {
        this.allCommonClasses = results;
        //Triggers the list update
        notifyDataSetChanged();
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

    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        public TextView pId,pName, pPrice, pQuantity,pmeasure;
        ImageView deleteitem,pimage;

        public ViewHolder(View itemView) {
            super(itemView);
            pId = (TextView) itemView.findViewById(R.id.checkitemid);
            pName = (TextView) itemView.findViewById(R.id.checkitemname);
            pPrice = (TextView) itemView.findViewById(R.id.checkitemcost);
            pQuantity = (TextView) itemView.findViewById(R.id.checkitemqu);
            pimage = (ImageView) itemView.findViewById(R.id.checkoutimage);
            pmeasure = (TextView)itemView.findViewById(R.id.checkitemmeasure);
            deleteitem = (ImageView) itemView.findViewById(R.id.checkoutitem_delete);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }

}
