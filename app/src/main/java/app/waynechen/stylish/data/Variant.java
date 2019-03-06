package app.waynechen.stylish.data;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class Variant {

    private String mColorCode;
    private String mSize;
    private int mStock;

    public Variant() {
        mColorCode = "";
        mSize = "";
        mStock = -1;
    }

    public String getColorCode() {
        return mColorCode;
    }

    public void setColorCode(String colorCode) {
        mColorCode = colorCode;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String size) {
        mSize = size;
    }

    public int getStock() {
        return mStock;
    }

    public void setStock(int stock) {
        mStock = stock;
    }
}
