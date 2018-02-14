package model;

/**
 * Created by Prabhu on 08-12-2017.
 */

public class AddressCommonClass {
    public AddressCommonClass(){

    }

    public String getLatlan() {
        return latlan;
    }

    public void setLatlan(String latlan) {
        this.latlan = latlan;
    }

    private String latlan;
    public AddressCommonClass(String addpid, String latlan,String addnickname, String addpersoname, String addhouseno, String addstreetname, String addarea, String addapartmentno, String addlandmark, String addcity, String addpincode,String addmobile) {
        this.addpid = addpid;
        this.latlan = latlan;
        this.addnickname = addnickname;
        this.addpersoname = addpersoname;
        this.addhouseno = addhouseno;
        this.addstreetname = addstreetname;
        this.addarea = addarea;
        this.addapartmentno = addapartmentno;
        this.addlandmark = addlandmark;
        this.addcity = addcity;
        this.addpincode = addpincode;
        this.addmobile = addmobile;
    }

    public String getAddpid() {
        return addpid;
    }

    public void setAddpid(String addpid) {
        this.addpid = addpid;
    }

    public String getAddnickname() {
        return addnickname;
    }

    public void setAddnickname(String addnickname) {
        this.addnickname = addnickname;
    }

    public String getAddpersoname() {
        return addpersoname;
    }

    public void setAddpersoname(String addpersoname) {
        this.addpersoname = addpersoname;
    }

    public String getAddhouseno() {
        return addhouseno;
    }

    public void setAddhouseno(String addhouseno) {
        this.addhouseno = addhouseno;
    }

    public String getAddstreetname() {
        return addstreetname;
    }

    public void setAddstreetname(String addstreetname) {
        this.addstreetname = addstreetname;
    }

    public String getAddarea() {
        return addarea;
    }

    public void setAddarea(String addarea) {
        this.addarea = addarea;
    }

    public String getAddapartmentno() {
        return addapartmentno;
    }

    public void setAddapartmentno(String addapartmentno) {
        this.addapartmentno = addapartmentno;
    }

    public String getAddlandmark() {
        return addlandmark;
    }

    public void setAddlandmark(String addlandmark) {
        this.addlandmark = addlandmark;
    }

    public String getAddcity() {
        return addcity;
    }

    public void setAddcity(String addcity) {
        this.addcity = addcity;
    }

    public String getAddpincode() {
        return addpincode;
    }

    public void setAddpincode(String addpincode) {
        this.addpincode = addpincode;
    }

    private String addpid;
    private String addnickname;
    private String addpersoname;
    private String addhouseno;
    private String addstreetname;
    private String addarea;
    private String addapartmentno;
    private String addlandmark;
    private String addcity;
    private String addpincode;

    public String getAddmobile() {
        return addmobile;
    }

    public void setAddmobile(String addmobile) {
        this.addmobile = addmobile;
    }

    private String addmobile;

}
