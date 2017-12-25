package model;

/**
 * Created by Prabhu on 21-12-2017.
 */

public class PharmacyModelOne {
    public PharmacyModelOne(){

    }

    public PharmacyModelOne(String uid, String presName, String presDesc, String presImageUrl) {
        this.uid = uid;
        this.presName = presName;
        this.presDesc = presDesc;
        this.presImageUrl = presImageUrl;
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

    private String uid,presName,presDesc,presImageUrl;
}
