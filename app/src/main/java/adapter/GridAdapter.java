package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.firebase.client.Firebase;
import com.indianservers.onlinegrocery.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import model.CenterRepository;
import model.OrdersCommonClass;
import model.ProductCommonClass;


/**
 * Created by Hari Prahlad on 05-06-2016.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> implements
        ItemTouchHelperAdapter,Filterable {
    public Context activity;
    private Firebase firebase;
    private SharedPreferences sskey;
    private ProductCommonClass productCommonClass;
    private List<ProductCommonClass> productList;
    private List<ProductCommonClass> mFilteredList;
    private LayoutInflater mInflater;
    public List<ProductCommonClass> allCommonClasses;
    SharedPreferences sharedPrefs;
    int finalprice;
    ViewHolder holderr;
    private OnItemClickListener clickListener;
    int count;
    String uid;

    public GridAdapter(Context activity, List<ProductCommonClass> allCommonClasses) {
        this.activity = activity;
        this.allCommonClasses = allCommonClasses;
        this.productList = allCommonClasses;
        this.mFilteredList = allCommonClasses;
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        uid = sharedPrefs.getString("uid", "0");
    }

    public void updateResults(List<ProductCommonClass> results) {
        this.allCommonClasses = results;
        //Triggers the list update
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.grid_single, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public int getItemCount() {
        return mFilteredList.size() ;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final  ProductCommonClass commonClass = mFilteredList.get(position);
        holder.pId.setText(commonClass.getPuid());
        holder.ppid.setText(commonClass.getPpid());
        holder.pName.setText(commonClass.getProductName());
        holder.pPrice.setText(commonClass.getProductPrice());
        holder.pdprice.setText(commonClass.getProductdPrice());
        if(commonClass.getProductdPrice().equals("")){
            holder.rs.setVisibility(View.GONE);
            holder.pPrice.setPaintFlags(holder.pPrice.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }else {
            holder.rs.setVisibility(View.VISIBLE);
            holder.pPrice.setPaintFlags(holder.pPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.pdesc.setText(commonClass.getProductDesc());
        holder.pQuantity.setText(commonClass.getProductQuantity());
        holder.pMeasure.setText(commonClass.getProductMeasureType());
        try{
            holder.quantity.setText(CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList().get(position).getPrqu());
        }catch (IndexOutOfBoundsException e){

        }
        holder.pimageUrl.setText(commonClass.getProductImage());
        if(commonClass.getProductImage().equals("")){

        }else {
            Picasso.with(activity)
                    .load(commonClass.getProductImage())
                    .into(holder.pimage);
        }
        holder.plus.findViewById(R.id.plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductCommonClass tempObj = mFilteredList.get(position);
                if (allCommonClasses.contains(tempObj)) {

                    //get position of current item in shopping list
                    int indexOfTempInShopingList = allCommonClasses
                            .indexOf(tempObj);

                    // update quanity in shopping list
                    allCommonClasses
                            .get(indexOfTempInShopingList)
                            .setPrqu(
                                    String.valueOf(Integer
                                            .valueOf(tempObj
                                                    .getPrqu())+1));

                    // update current item quanitity
                    holder.quantity.setText(tempObj.getPrqu());

                } else {

                    tempObj.setPrqu(String.valueOf(1));

                    holder.quantity.setText(tempObj.getPrqu());

                    allCommonClasses.add(tempObj);

                }
            }
        });
        holder.minus.findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductCommonClass tempObj = (mFilteredList).get(position);

                if (allCommonClasses
                        .contains(tempObj)) {

                    int indexOfTempInShopingList = allCommonClasses
                            .indexOf(tempObj);

                    if (Integer.valueOf(tempObj.getPrqu()) != 0) {

                        allCommonClasses
                                .get(indexOfTempInShopingList)
                                .setPrqu(
                                        String.valueOf(Integer.valueOf(tempObj
                                                .getPrqu()) - 1));

                        holder.quantity.setText(allCommonClasses
                                .get(indexOfTempInShopingList).getPrqu());

                        if (Integer.valueOf(allCommonClasses
                                .get(indexOfTempInShopingList).getPrqu()) == 0) {

                            notifyDataSetChanged();
                        }

                    }

                } else {

                }

            }
        });

        holder.add.findViewById(R.id.additem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrdersCommonClass commonClass = new OrdersCommonClass();
                ProductCommonClass aClasss = mFilteredList.get(position);
                commonClass.setPrpid(aClasss.getPpid());
                commonClass.setPruid(aClasss.getPuid());
                commonClass.setPrName(aClasss.getProductName());
                commonClass.setPimage(aClasss.getProductImage());
                if(aClasss.getProductdPrice().equals("")){
                    finalprice = Integer.parseInt(aClasss.getPrqu())*Integer.parseInt(aClasss.getProductPrice());

                }else {
                    finalprice = Integer.parseInt(aClasss.getPrqu())*Integer.parseInt(aClasss.getProductdPrice());
                }
                commonClass.setPrPrice(String.valueOf(finalprice));
                commonClass.setPrQunatity(aClasss.getPrqu());
                commonClass.setPrMeasure(aClasss.getProductMeasureType());
                if(aClasss.getPrqu().equals("0")){
                    Toast.makeText(activity,"You didn't Select item",Toast.LENGTH_SHORT).show();
                }else{
                    firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"CartItems"+"/"+uid);
                    firebase.child(uid).push().setValue(commonClass);
                    Toast.makeText(activity,"Item Added To Cart",Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mFilteredList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mFilteredList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = productList;
                } else {

                    ArrayList<ProductCommonClass> filteredList = new ArrayList<>();

                    for (ProductCommonClass androidVersion : productList) {

                        if (androidVersion.getProductName().contains(charString) || androidVersion.getPpid().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }
                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<ProductCommonClass>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public void onItemDismiss(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        public TextView pId,ppid, puid, pName, pPrice, pdprice, pQuantity, pMeasure, pdesc, pimageUrl;
        private TextView quantity;
        TextView plus;
        TextView minus;
        Button add;
        TextView rs;
        ImageView pimage;
        public ViewHolder(View itemView) {
            super(itemView);
            pId = (TextView) itemView.findViewById(R.id.pId);
            ppid = (TextView) itemView.findViewById(R.id.ppid);
            pName = (TextView) itemView.findViewById(R.id.productname);
            puid = (TextView) itemView.findViewById(R.id.pruid);
            pPrice = (TextView) itemView.findViewById(R.id.vendoreprice);
            pdprice = (TextView) itemView.findViewById(R.id.pdiscprice);
            pQuantity = (TextView) itemView.findViewById(R.id.productquantity);
            pMeasure = (TextView) itemView.findViewById(R.id.cartmeasure);
            pdesc = (TextView) itemView.findViewById(R.id.productDesc);
            pimageUrl = (TextView) itemView.findViewById(R.id.primageUrl);
            pimage = (ImageView) itemView.findViewById(R.id.productimage);
            add = (Button) itemView.findViewById(R.id.additem);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            plus = (TextView) itemView.findViewById(R.id.plus);
            minus = (TextView) itemView.findViewById(R.id.minus);
            rs = (TextView)itemView.findViewById(R.id.rs);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }

}
