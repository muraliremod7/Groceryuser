package adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.firebase.client.Firebase;
import com.indianservers.onlinegrocery.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.CenterRepository;
import model.OrdersCommonClass;
import model.ProductCommonClass;


/**
 * Created by Hari Prahlad on 05-06-2016.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> implements
        ItemTouchHelperAdapter {
    public Activity activity;
    private Firebase firebase;
    private SharedPreferences sskey;
    private ProductCommonClass productCommonClass;
    private List<ProductCommonClass> productList = new ArrayList<ProductCommonClass>();
    List<ProductCommonClass> commonClasses = new ArrayList<ProductCommonClass>();
    private LayoutInflater mInflater;
    public List<ProductCommonClass> allCommonClasses;
    SharedPreferences sharedPrefs;
    private OnItemClickListener clickListener;
    int count;
    String uid;

    public GridAdapter(Activity activity, List<ProductCommonClass> allCommonClasses) {
        this.allCommonClasses = allCommonClasses;
        mInflater = LayoutInflater.from(activity);
        productList = CenterRepository.getCenterRepository().getListOfProductsInShoppingList();
        this.activity = activity;
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
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.pId.setText(productList.get(position).getPpid());
        holder.puid.setText(productList.get(position).getPuid());
        holder.pName.setText(productList.get(position).getProductName());
        holder.pPrice.setText(productList.get(position).getProductPrice());
        holder.pdprice.setText(productList.get(position).getProductdPrice());
        if(productList.get(position).getProductdPrice().equals("")){
            holder.rs.setVisibility(View.GONE);
        }else {
            holder.pPrice.setPaintFlags(holder.pPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.pdesc.setText(productList.get(position).getProductDesc());
        holder.pQuantity.setText(productList.get(position).getProductQuantity());
        holder.pMeasure.setText(productList.get(position).getProductMeasureType());
        try{
            holder.quantity.setText(CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList().get(position).getPrqu());
        }catch (IndexOutOfBoundsException e){

        }
        holder.pimageUrl.setText(productList.get(position).getProductImage());
        Picasso.with(activity)
                .load(productList.get(position).getProductImage())
                .into(holder.pimage);
        holder.plus.findViewById(R.id.plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductCommonClass tempObj = productList.get(position);
                if (CenterRepository.getCenterRepository()
                        .getListOfProductsInShoppingList().contains(tempObj)) {

                    //get position of current item in shopping list
                    int indexOfTempInShopingList = CenterRepository
                            .getCenterRepository().getListOfProductsInShoppingList()
                            .indexOf(tempObj);

                    // update quanity in shopping list
                    CenterRepository
                            .getCenterRepository()
                            .getListOfProductsInShoppingList()
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

                    CenterRepository.getCenterRepository()
                            .getListOfProductsInShoppingList().add(tempObj);

                }
            }
        });
        holder.minus.findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductCommonClass tempObj = (productList).get(position);

                if (CenterRepository.getCenterRepository().getListOfProductsInShoppingList()
                        .contains(tempObj)) {

                    int indexOfTempInShopingList = CenterRepository
                            .getCenterRepository().getListOfProductsInShoppingList()
                            .indexOf(tempObj);

                    if (Integer.valueOf(tempObj.getPrqu()) != 0) {

                        CenterRepository
                                .getCenterRepository()
                                .getListOfProductsInShoppingList()
                                .get(indexOfTempInShopingList)
                                .setPrqu(
                                        String.valueOf(Integer.valueOf(tempObj
                                                .getPrqu()) - 1));

                        holder.quantity.setText(CenterRepository
                                .getCenterRepository().getListOfProductsInShoppingList()
                                .get(indexOfTempInShopingList).getPrqu());

                        if (Integer.valueOf(CenterRepository
                                .getCenterRepository().getListOfProductsInShoppingList()
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
                ProductCommonClass aClasss = productList.get(position);
                commonClass.setPrpid(productList.get(position).getPpid());
                commonClass.setPruid(productList.get(position).getPuid());
                commonClass.setPrName(productList.get(position).getProductName());
                commonClass.setPimage(productList.get(position).getProductImage());
                int finalprice = Integer.parseInt(productList.get(position).getPrqu())*Integer.parseInt(productList.get(position).getProductdPrice());
                commonClass.setPrPrice(String.valueOf(finalprice));
                commonClass.setPrQunatity(productList.get(position).getPrqu());
                commonClass.setPrMeasure(productList.get(position).getProductMeasureType());
                if(productList.get(position).getPrqu().equals("0")){
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
                Collections.swap(productList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(productList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
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
        public TextView pId, puid, pName, pPrice, pdprice, pQuantity, pMeasure, pdesc, pimageUrl;
        private TextView quantity;
        TextView plus;
        TextView minus;
        Button add;
        TextView rs;
        ImageView pimage;
        public ViewHolder(View itemView) {
            super(itemView);
            pId = (TextView) itemView.findViewById(R.id.pId);
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
