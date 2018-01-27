package model;

/**
 * Created by Prabhu on 21-12-2017.
 */

public class PharmacyModelOne {
    public PharmacyModelOne(){

    }

    public PharmacyModelOne(String uid, String pid,String date,String presName, String presDesc, String presImageUrl,String status) {
        this.uid = uid;
        this.presName = presName;
        this.presDesc = presDesc;
        this.presImageUrl = presImageUrl;
        this.date = date;
        this.pid = pid;
        this.status= status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPresName() {
        return presName;
    }

    public void setPresName(String presName) {
        this.presName = presName;
    }

    public String getPresDesc() {
        return presDesc;
    }

    public void setPresDesc(String presDesc) {
        this.presDesc = presDesc;
    }

    public String getPresImageUrl() {
        return presImageUrl;
    }

    public void setPresImageUrl(String presImageUrl) {
        this.presImageUrl = presImageUrl;
    }

    private String uid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    private String pid;
    private String presName;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;
    private String presDesc;
    private String presImageUrl;
}
