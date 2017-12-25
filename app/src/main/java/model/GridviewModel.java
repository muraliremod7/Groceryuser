package model;

/**
 * Created by Prabhu on 14-12-2017.
 */

public class GridviewModel {
    public GridviewModel(){

    }

    public GridviewModel(String imagename, String image, String ImageUrl) {
        this.imagename = imagename;
        this.image = image;
        this.ImageUrl = ImageUrl;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String imagename;
    private String image;

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    private String ImageUrl;

}
