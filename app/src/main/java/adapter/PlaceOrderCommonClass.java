package adapter;

/**
 * Created by Prabhu on 12-12-2017.
 */

public class PlaceOrderCommonClass {
    public PlaceOrderCommonClass(){

    }

    public PlaceOrderCommonClass(String uid, String orderid, String orderitems, String deliveryaddress,String orderdate, String ordertime, String orderpayableamount,String status) {
        this.uid = uid;
        this.orderid = orderid;
        this.orderitems = orderitems;
        this.orderdate = orderdate;
        this.ordertime = ordertime;
        this.orderpayableamount = orderpayableamount;
        this.status = status;
        this.deliveryaddress = deliveryaddress;
    }

    private String uid;

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    private String kid;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;
    private String orderid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrderitems() {
        return orderitems;
    }

    public void setOrderitems(String orderitems) {
        this.orderitems = orderitems;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public String getOrderpayableamount() {
        return orderpayableamount;
    }

    public void setOrderpayableamount(String orderpayableamount) {
        this.orderpayableamount = orderpayableamount;
    }

    private String orderitems;
    private String orderdate;
    private String ordertime;
    private String orderpayableamount;

    public String getDeliveryaddress() {
        return deliveryaddress;
    }

    public void setDeliveryaddress(String deliveryaddress) {
        this.deliveryaddress = deliveryaddress;
    }

    private String deliveryaddress;
}
