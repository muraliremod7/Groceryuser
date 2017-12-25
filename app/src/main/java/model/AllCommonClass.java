package model;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hari Prahlad on 05-06-2016.
 */
public class AllCommonClass {

    private String pId;
    private String pName;
    private String pPrice;
    private String categoryName;
    private String pMeasure;
    private String quantityPeritem;

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    private String productImage;
    public AllCommonClass(){

    }

    public AllCommonClass(String pId, String pMeasure, String pName, String categoryName, String pPrice, String productImage, String quantityPeritem) {
        this.pId = pId;
        this.pMeasure = pMeasure;
        this.pName = pName;
        this.categoryName = categoryName;
        this.pPrice = pPrice;
        this.productImage = productImage;
        this.quantityPeritem = quantityPeritem;
    }
    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getpMeasure() {
        return pMeasure;
    }

    public void setpMeasure(String pMeasure) {
        this.pMeasure = pMeasure;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpPrice() {
        return pPrice;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }


    public String getQuantityPeritem() {
        return quantityPeritem;
    }

    public void setQuantityPeritem(String quantityPeritem) {
        this.quantityPeritem = quantityPeritem;
    }
}
