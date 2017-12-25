package model;

/**
 * Created by Prabhu on 08-12-2017.
 */

public class OrdersCommonClass {
    public OrdersCommonClass(){

    }

    public String getPrMeasure() {
        return prMeasure;
    }

    public void setPrMeasure(String prMeasure) {
        this.prMeasure = prMeasure;
    }

    public OrdersCommonClass(String prpid, String pruid, String pimage, String prName, String prQunatity, String prMeasure, String prPrice) {
        this.prpid = prpid;
        this.pruid = pruid;
        this.prName = prName;
        this.pimage = pimage;
        this.prQunatity = prQunatity;
        this.prMeasure = prMeasure;
        this.prPrice = prPrice;
    }

    public String getPrpid() {
        return prpid;
    }

    public void setPrpid(String prpid) {
        this.prpid = prpid;
    }

    public String getPruid() {
        return pruid;
    }

    public void setPruid(String pruid) {
        this.pruid = pruid;
    }

    public String getPrName() {
        return prName;
    }

    public void setPrName(String prName) {
        this.prName = prName;
    }

    public String getPrQunatity() {
        return prQunatity;
    }

    public void setPrQunatity(String prQunatity) {
        this.prQunatity = prQunatity;
    }

    public String getPrPrice() {
        return prPrice;
    }

    public void setPrPrice(String prPrice) {
        this.prPrice = prPrice;
    }

    private String prpid;
    private String pruid;

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    private String pimage;
    private String prName;
    private String prQunatity;
    private String prPrice;
    private String prMeasure;

}
