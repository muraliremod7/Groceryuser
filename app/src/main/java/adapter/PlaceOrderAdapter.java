package adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.indianservers.onlinegrocery.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Prabhu on 12-12-2017.
 */

public class PlaceOrderAdapter extends RecyclerView.Adapter<PlaceOrderAdapter.ViewHolder> implements
        ItemTouchHelperAdapter {
    public Activity activity;
    private Firebase firebase;
    private LayoutInflater mInflater;
    public List<PlaceOrderCommonClass> allCommonClasses = new ArrayList<>();
    SharedPreferences sharedPrefs;
    private AddressAdapter.OnItemClickListener clickListener;

    public PlaceOrderAdapter(Activity activity, List<PlaceOrderCommonClass> allCommonClasses) {
        this.allCommonClasses = allCommonClasses;
        this.activity = activity;

    }

    @Override
    public PlaceOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.single_order, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.orderId.setText(allCommonClasses.get(position).getOrderid());
        holder.orderitems.setText(allCommonClasses.get(position).getOrderitems());
        holder.deliverydate.setText(allCommonClasses.get(position).getOrderdate());
        holder.deliverytime.setText(allCommonClasses.get(position).getOrdertime());
        holder.totalpayable.setText(allCommonClasses.get(position).getOrderpayableamount());
        holder.orderstatus.setText(allCommonClasses.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return allCommonClasses == null ? 0 : allCommonClasses.size();
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
        public TextView orderId, orderitems, deliverydate, deliverytime, totalpayable,orderstatus;

        public ViewHolder(View itemView) {
            super(itemView);
            orderId = (TextView) itemView.findViewById(R.id.orderId);
            orderitems = (TextView) itemView.findViewById(R.id.orderItems);
            deliverydate = (TextView) itemView.findViewById(R.id.orderDeliveryDate);
            deliverytime = (TextView) itemView.findViewById(R.id.orderDeliveryTime);
            totalpayable = (TextView) itemView.findViewById(R.id.orderDeliverypayable);
            orderstatus= (TextView)itemView.findViewById(R.id.orderDeliverystatus);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }
}
