package app.waynechen.stylish.api;

import app.waynechen.stylish.api.exception.StylishException;
import app.waynechen.stylish.api.exception.StylishInvalidTokenException;
import app.waynechen.stylish.data.Color;
import app.waynechen.stylish.data.Hots;
import app.waynechen.stylish.data.Product;
import app.waynechen.stylish.data.User;
import app.waynechen.stylish.data.Variant;
import app.waynechen.stylish.data.source.bean.GetMarketingHots;
import app.waynechen.stylish.data.source.bean.GetProductList;
import app.waynechen.stylish.data.source.bean.UserSignIn;
import app.waynechen.stylish.util.Constants;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class StylishParser {

    /**
     * Check error and then parse Stylish Error Message.
     * @param errorCode
     * @param jsonString
     * @return
     */
    public static JSONObject parseError(int errorCode, String jsonString) throws StylishException {

        JSONObject obj = null;

        try {
            obj = new JSONObject(jsonString);

            if (obj.has("error")) {

                if (errorCode == 411 || errorCode == 412) {
                    throw new StylishInvalidTokenException(obj.getString("error"));
                } else if (obj.getString("error").contains("Invalid Access Token")) {
                    throw new StylishInvalidTokenException(obj.getString("error"));
                } else {
                    throw new StylishException(obj.getString("error"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            throw new StylishException(Constants.GENERAL_ERROR);
        }

        return obj;
    }

    /**
     * Check error and then parse Stylish Error Message.
     * @param jsonString
     * @return
     * @throws StylishException
     */
    public static JSONObject parseError(String jsonString) throws StylishException {
        return parseError(0, jsonString);
    }

    /**
     * Parse Marketing Hots API.
     * @include: hots'
     * @param jsonString
     * @return GetMarketingHots
     * @throws StylishException
     */
    public static GetMarketingHots parseGetMarketingHots(String jsonString) throws StylishException {

        GetMarketingHots bean = new GetMarketingHots();

        try {
            JSONObject obj = parseError(jsonString);

            JSONArray data = obj.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                bean.getHotsList().add(parseHots(data.getJSONObject(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bean;
    }

    /**
     * Parse Product List API.
     * @include: products, paging
     * @param jsonString
     * @return GetProductList
     * @throws StylishException
     */
    public static GetProductList parseGetProductList(String jsonString) throws StylishException {

        GetProductList bean = new GetProductList();

        try {
            JSONObject obj = parseError(jsonString);

            JSONArray data = obj.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                bean.getProducts().add(parseProduct(data.getJSONObject(i)));
            }

            try {
                if ((obj.has("paging"))) {
                    bean.setPaging(obj.getInt("paging"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bean;
    }

    /**
     * Parse User Sign In API.
     * @include: access_token, access_expired, user
     * @param jsonString
     * @return UserSignIn
     * @throws StylishException
     */
    public static UserSignIn parseUserSignIn(String jsonString) throws StylishException {

        UserSignIn bean = new UserSignIn();

        try {
            JSONObject obj = parseError(jsonString);

            JSONObject data = obj.getJSONObject("data");

            try {
                bean.setAccessToken(data.getString("access_token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                bean.setAccessExpired(data.getString("access_expired"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                bean.setUser(parseUser(data.getJSONObject("user")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bean;
    }

    /**
     * Parse User Profile API.
     * @include: id, provider, name, email, picture
     * @param jsonString
     * @return User
     * @throws StylishException
     */
    public static User parseGetUserProfile(String jsonString) throws StylishException {

        User bean = new User();

        try {
            JSONObject obj = parseError(jsonString);

            bean = parseUser(obj.getJSONObject("data"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bean;
    }

    /**
     * Parse Order Check Out API.
     * @include:
     * @param jsonString
     * @return User
     * @throws StylishException
     */
    public static String parseOrderCheckOut(String jsonString) throws StylishException {

        String number = "";

        try {
            JSONObject obj = parseError(jsonString);

            number = obj.getJSONObject("data").getString("number");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return number;
    }

    /**
     * Parse Hots.
     * @include: title, products
     * @param jsonObject
     * @return Hots
     */
    public static Hots parseHots(JSONObject jsonObject) {

        Hots objHots = new Hots();

        try {
            objHots.setTitle(jsonObject.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray arrProducts = jsonObject.getJSONArray("products");

            for (int i = 0; i < arrProducts.length(); i++) {
                objHots.getProducts().add(parseProduct(arrProducts.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return objHots;
    }

    /**
     * Parse Product.
     * @include:
     * @param jsonObject
     * @return Product
     */
    public static Product parseProduct(JSONObject jsonObject) {

        Product objProduct = new Product();

        try {
            objProduct.setId(jsonObject.getLong("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objProduct.setTitle(jsonObject.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objProduct.setDescription(jsonObject.getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objProduct.setPrice(jsonObject.getInt("price"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objProduct.setTexture(jsonObject.getString("texture"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objProduct.setWash(jsonObject.getString("wash"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objProduct.setPlace(jsonObject.getString("place"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objProduct.setNote(jsonObject.getString("note"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objProduct.setStory(jsonObject.getString("story"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray arrColors = jsonObject.getJSONArray("colors");

            for (int i = 0; i < arrColors.length(); i++) {
                objProduct.getColors().add(parseColor(arrColors.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objProduct.setSizes(parseJsonArrayToStringArrayList(jsonObject.getJSONArray("sizes")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray arrVariants = jsonObject.getJSONArray("variants");

            for (int i = 0; i < arrVariants.length(); i++) {
                objProduct.getVariants().add(parseVariant(arrVariants.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objProduct.setMainImage(jsonObject.getString("main_image"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objProduct.setImages(parseJsonArrayToStringArrayList(jsonObject.getJSONArray("images")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return objProduct;
    }

    /**
     * Parse Color.
     * @include: name, code
     * @param jsonObject
     * @return Color
     */
    public static Color parseColor(JSONObject jsonObject) {

        Color objColor = new Color();

        try {
            objColor.setName(jsonObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objColor.setCode(jsonObject.getString("code"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return objColor;
    }

    /**
     * Parse Variant.
     * @include: color_code, size, stock
     * @param jsonObject
     * @return Variant
     */
    public static Variant parseVariant(JSONObject jsonObject) {

        Variant objVariant = new Variant();

        try {
            objVariant.setColorCode(jsonObject.getString("color_code"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objVariant.setSize(jsonObject.getString("size"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objVariant.setStock(jsonObject.getInt("stock"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return objVariant;
    }

    /**
     * Parse User.
     * @include: id, provider, name, email, picture
     * @param jsonObject
     * @return User
     */
    public static User parseUser(JSONObject jsonObject) {

        User objUser = new User();

        try {
            objUser.setId(jsonObject.getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objUser.setProvider(jsonObject.getString("provider"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objUser.setName(jsonObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objUser.setEmail(jsonObject.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            objUser.setPicture(jsonObject.getString("picture"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return objUser;
    }

    /**
     * Parse JsonArray to StringArrayList.
     * @param jsonArray
     * @return ArrayList<String>
     */
    public static ArrayList<String> parseJsonArrayToStringArrayList(JSONArray jsonArray) {

        ArrayList<String> list = new ArrayList<String>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}
