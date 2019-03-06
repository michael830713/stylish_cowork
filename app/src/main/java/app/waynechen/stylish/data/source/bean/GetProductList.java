package app.waynechen.stylish.data.source.bean;

import app.waynechen.stylish.data.Product;

import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class GetProductList {

    public static final int NO_MORE_PAGING = -1;
    public static final int FIRST_PAGING = -2;

    private ArrayList<Product> mProducts;
    private int mPaging;

    public GetProductList() {
        mProducts = new ArrayList<>();
        mPaging = NO_MORE_PAGING;
    }

    public ArrayList<Product> getProducts() {
        return mProducts;
    }

    public void setProducts(ArrayList<Product> products) {
        mProducts = products;
    }

    public int getPaging() {
        return mPaging;
    }

    public void setPaging(int paging) {
        mPaging = paging;
    }
}
