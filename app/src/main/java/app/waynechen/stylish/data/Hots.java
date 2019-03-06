package app.waynechen.stylish.data;

import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class Hots {

    private String mTitle;
    private ArrayList<Product> mProducts;

    public Hots() {
        mTitle = "";
        mProducts = new ArrayList<>();
    }

    public ArrayList<Object> toObjList() {
        ArrayList<Object> list = new ArrayList<>();
        list.add(mTitle);
        list.addAll(mProducts);
        return list;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public ArrayList<Product> getProducts() {
        return mProducts;
    }

    public void setProducts(ArrayList<Product> products) {
        mProducts = products;
    }
}
