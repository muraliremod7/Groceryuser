package model;

/**
 * Created by Prabhu on 06-12-2017.
 */

public class ProfileCommonClass {
    public ProfileCommonClass(){

    }

    public ProfileCommonClass(String profileName, String profileEmail, String profileMobile) {
        this.profileName = profileName;
        this.profileEmail = profileEmail;
        this.profileMobile = profileMobile;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileEmail() {
        return profileEmail;
    }

    public void setProfileEmail(String profileEmail) {
        this.profileEmail = profileEmail;
    }

    public String getProfileMobile() {
        return profileMobile;
    }

    public void setProfileMobile(String profileMobile) {
        this.profileMobile = profileMobile;
    }

    private String profileName,profileEmail,profileMobile;
}
