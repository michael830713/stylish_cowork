package app.waynechen.stylish.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class CheckOutInfo extends Product {

    private String mPrime;
    private int mSubTotal;
    private int mFreight;
    private String mName;
    private String mPhone;
    private String mEmail;
    private String mAddress;
    private String mTime;
    private ArrayList<CartProduct> mCartProducts;
    private boolean isPayByCreditCard;

    public CheckOutInfo() {
        mPrime = "";
        mSubTotal = -1;
        mFreight = -1;
        mName = "";
        mPhone = "";
        mEmail = "";
        mAddress = "";
        mTime = "";
        mCartProducts = new ArrayList<>();
        isPayByCreditCard = false;
    }

    public String toJsonString() throws JSONException {

        JSONObject body = new JSONObject();
        body.put("prime", getPrime());

        JSONObject order = new JSONObject();
        order.put("shipping", "delivery");
        order.put("payment", "credit_card");
        order.put("subtotal", getSubTotal());
        order.put("freight", getFreight());
        order.put("total", getTotal());

        JSONObject recipient = new JSONObject();
        recipient.put("name", getName());
        recipient.put("phone", getPhone());
        recipient.put("email", getEmail());
        recipient.put("address", getAddress());
        recipient.put("time", getTime());

        order.put("recipient", recipient);

        JSONArray list = new JSONArray();
        for (CartProduct cartProduct : getCartProducts()) {
            JSONObject product = new JSONObject();
            product.put("id", cartProduct.getId());
            product.put("name", cartProduct.getTitle());
            product.put("price", cartProduct.getPrice());

            JSONObject color = new JSONObject();
            color.put("name", cartProduct.getSelectedColorCode());
            color.put("code", cartProduct.getSelectedColorCode());

            product.put("color", color);
            product.put("size", cartProduct.getSelectedSize());
            product.put("qty", cartProduct.getSelectedAmount());

            list.put(product);
        }

        order.put("list", list);

        body.put("order", order);

        return body.toString();
    }

    public String getPrime() {
        return mPrime;
    }

    public void setPrime(String prime) {
        mPrime = prime;
    }

    public int getSubTotal() {
        return mSubTotal;
    }

    public void setSubTotal(int subTotal) {
        mSubTotal = subTotal;
    }

    public int getFreight() {
        return mFreight;
    }

    public void setFreight(int freight) {
        mFreight = freight;
    }

    public int getTotal() {
        return getSubTotal() + getFreight();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public ArrayList<CartProduct> getCartProducts() {
        return mCartProducts;
    }

    public void setCartProducts(ArrayList<CartProduct> cartProducts) {
        mCartProducts = cartProducts;
    }

    public boolean isPayByCreditCard() {
        return isPayByCreditCard;
    }

    public void setPayByCreditCard(boolean payByCreditCard) {
        isPayByCreditCard = payByCreditCard;
    }
}
