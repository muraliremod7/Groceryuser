package model;

/**
 * Created by Prabhu on 02-12-2017.
 */

public class ProductCommonClass {
    public ProductCommonClass(){

    }

    public String getPrqu() {
        return prqu;
    }

    public void setPrqu(String prqu) {
        this.prqu = prqu;
    }

    public ProductCommonClass(String ppid, String prqu, String puid, String productName, String productMeasureType, String productQuantity, String productPrice, String productImage, String productdPrice, String productDesc, String productId) {
        this.ppid = ppid;
        this.puid = puid;
        this.prqu = prqu;
        this.productName = productName;
        this.productMeasureType = productMeasureType;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productdPrice = productdPrice;
        this.productDesc = productDesc;
    }

    public String getPpid() {
        return ppid;
    }

    public void setPpid(String ppid) {
        this.ppid = ppid;
    }

    public String getProductdPrice() {
        return productdPrice;
    }

    public void setProductdPrice(String productdPrice) {
        this.productdPrice = productdPrice;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    private String ppid,prqu;

    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }

    private String puid;
    private String productName;
    private String productMeasureType;
    private String productQuantity;
    private String productPrice;
    private String productImage;
    private String productdPrice;
    private String productDesc;

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductMeasureType() {
        return productMeasureType;
    }

    public void setProductMeasureType(String productMeasureType) {
        this.productMeasureType = productMeasureType;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

}
