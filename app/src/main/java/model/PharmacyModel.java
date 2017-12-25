package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prabhu on 14-12-2017.
 */

public class PharmacyModel {
    public PharmacyModel(){

    }

    public PharmacyModel(String orderid, String orderpid, String ordername,String date,List<String> imageUrl) {
        this.orderid = orderid;
        this.orderpid = orderpid;
        this.imageUrl = imageUrl;
        this.ordername = ordername;
        this.date = date;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrderpid() {
        return orderpid;
    }

    public void setOrderpid(String orderpid) {
        this.orderpid = orderpid;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String orderid;
    private String orderpid;

    public String getOrdername() {
        return ordername;
    }

    public void setOrdername(String ordername) {
        this.ordername = ordername;
    }

    private String ordername;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;
    private List<String> imageUrl;
}
