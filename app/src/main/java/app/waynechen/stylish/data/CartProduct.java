package app.waynechen.stylish.data;

import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class CartProduct extends Product {

    private String mSelectedColorCode;
    private String mSelectedSize;
    private int mSelectedStock;
    private int mSelectedAmount;

    public CartProduct() {
        mSelectedColorCode = "";
        mSelectedSize = "";
        mSelectedStock = -1;
        mSelectedAmount = -1;
    }

    public CartProduct(Product product, Variant selectedVariant, int amount) {

        setId(product.getId());
        setTitle(product.getTitle());
        setDescription(product.getDescription());
        setPrice(product.getPrice());
        setTexture(product.getTexture());
        setWash(product.getWash());
        setPlace(product.getPlace());
        setNote(product.getNote());
        setStory(product.getStory());
        setColors(product.getColors());
        setSizes(product.getSizes());
        setVariants(product.getVariants());
        setMainImage(product.getMainImage());
        setImages(product.getImages());
        setSelectedColorCode(selectedVariant.getColorCode());
        setSelectedSize(selectedVariant.getSize());
        setSelectedStock(selectedVariant.getStock());
        setSelectedAmount(amount);
    }

    public String getSelectedColorCode() {
        return mSelectedColorCode;
    }

    public void setSelectedColorCode(String selectedColorCode) {
        mSelectedColorCode = selectedColorCode;
    }

    public String getSelectedSize() {
        return mSelectedSize;
    }

    public void setSelectedSize(String selectedSize) {
        mSelectedSize = selectedSize;
    }

    public int getSelectedStock() {
        return mSelectedStock;
    }

    public void setSelectedStock(int selectedStock) {
        mSelectedStock = selectedStock;
    }

    public int getSelectedAmount() {
        return mSelectedAmount;
    }

    public void setSelectedAmount(int selectedAmount) {
        mSelectedAmount = selectedAmount;
    }
}
