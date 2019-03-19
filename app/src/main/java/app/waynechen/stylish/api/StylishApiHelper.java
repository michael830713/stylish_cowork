package app.waynechen.stylish.api;

import static app.waynechen.stylish.MainMvpController.ACCESSORIES;
import static app.waynechen.stylish.MainMvpController.MEN;
import static app.waynechen.stylish.MainMvpController.WOMEN;
import static com.facebook.share.internal.DeviceShareDialogFragment.TAG;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.widget.Toast;

import app.waynechen.stylish.MainActivity;
import app.waynechen.stylish.MainMvpController;
import app.waynechen.stylish.api.exception.StylishException;
import app.waynechen.stylish.api.exception.StylishInvalidTokenException;
import app.waynechen.stylish.data.CheckOutInfo;
import app.waynechen.stylish.data.User;
import app.waynechen.stylish.data.source.bean.GetMarketingHots;
import app.waynechen.stylish.data.source.bean.GetProductList;
import app.waynechen.stylish.data.source.bean.UserSignIn;
import app.waynechen.stylish.util.Constants;
import app.waynechen.stylish.util.UserManager;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class StylishApiHelper {

    private static final String TAG = "StylishApiHelper";

    private static final String HOST = "https://api.appworks-school.tw";

    private static final String API_VERSION = "1.0";
    private static final String API_VERSION_PATH = "/" + API_VERSION;
    private static final String API_PATH = "/api";
    private static final String NIMABACHI_HOST = "https://www.nimabachi.com";
    private static final String NIMABACHI_API_VERSION = "1.0";
    private static final String NIMABACHI_API_VERSION_PATH = "/" + NIMABACHI_API_VERSION;
    private static final String NIMABACHI_AVATAR_PATH = "/admin/avatar";
    private static final String DAVIDADM_HOST = "https://davidadm.com";
    private static final String MARKETING_PATH = "/marketing";
    private static final String HOTS_PATH = "/hots";
    private static final String MARKETING_HOTS_PATH = MARKETING_PATH + HOTS_PATH;
    private static final String PRODUCTS_PATH = "/products";
    private static final String WOMEN_PATH = "/women";
    private static final String MEN_PATH = "/men";
    private static final String ACCESSORIES_PATH = "/accessories";
    private static final String PRODUCTS_WOMEN_PATH = PRODUCTS_PATH + WOMEN_PATH;
    private static final String PRODUCTS_MEN_PATH = PRODUCTS_PATH + MEN_PATH;
    private static final String PRODUCTS_ACCESSORIES_PATH = PRODUCTS_PATH + ACCESSORIES_PATH;
    private static final String PAGING_PATH = "?paging=";
    private static final String ID_PATH = "?id=";
    private static final String USER_PATH = "/user";
    private static final String SIGNIN_PATH = "/signin";
    private static final String SIGNUP_PATH = "/signup";
    private static final String FAVORITE_SAVE_PATH = "/favorite-save";
    private static final String FAVORITE_REMOVE_PATH = "/favorite-delete";
    private static final String FAVORITE_GET_PATH = "/favorite-get";
    private static final String USER_SIGNIN_PATH = USER_PATH + SIGNIN_PATH;
    private static final String USER_SIGNUP_PATH = USER_PATH + SIGNUP_PATH;
    private static final String USER_FAVORITE_SAVE_PATH = USER_PATH + FAVORITE_SAVE_PATH;
    private static final String USER_FAVORITE_REMOVE_PATH = USER_PATH + FAVORITE_REMOVE_PATH;
    private static final String USER_FAVORITE_GET_PATH = USER_PATH + FAVORITE_GET_PATH;
    private static final String PROFILE_PATH = "/profile";
    private static final String USER_PROFILE_PATH = USER_PATH + PROFILE_PATH;
    private static final String ORDER_PATH = "/order";
    private static final String CHECKOUT_PATH = "/checkout";
    private static final String ORDER_CHECKOUT_PATH = ORDER_PATH + CHECKOUT_PATH;

    private static final String GET_MARKETING_HOTS_URL = HOST + API_PATH + API_VERSION_PATH + MARKETING_HOTS_PATH;
    private static final String GET_PRODUCTS_WOMEN_URL = HOST + API_PATH + API_VERSION_PATH + PRODUCTS_WOMEN_PATH;
    private static final String GET_PRODUCTS_MEN_URL = HOST + API_PATH + API_VERSION_PATH + PRODUCTS_MEN_PATH;
    private static final String GET_PRODUCTS_ACCESSORIES_URL = HOST + API_PATH + API_VERSION_PATH + PRODUCTS_ACCESSORIES_PATH;
    private static final String POST_USER_SIGNIN_URL = HOST + API_PATH + API_VERSION_PATH + USER_SIGNIN_PATH;
    private static final String POST_USER_SIGNUP_URL = HOST + API_PATH + API_VERSION_PATH + USER_SIGNUP_PATH;
    private static final String GET_USER_PROFILE_URL = DAVIDADM_HOST + API_PATH + API_VERSION_PATH + USER_PROFILE_PATH;
    private static final String POST_ORDER_CHECKOUT_URL = HOST + API_PATH + API_VERSION_PATH + ORDER_CHECKOUT_PATH;
    private static final String POST_AVATAR_URL = DAVIDADM_HOST + API_PATH + API_VERSION_PATH + NIMABACHI_AVATAR_PATH;
    private static final String POST_USER_SIGNIN_DAVIDADM_URL = DAVIDADM_HOST + API_PATH + API_VERSION_PATH + USER_SIGNIN_PATH;
    private static final String POST_USER_SIGNUP_DAVIDADM_URL = DAVIDADM_HOST + API_PATH + API_VERSION_PATH + USER_SIGNUP_PATH;
    private static final String GET_USER_FAVORITE_SAVE_DAVIDADM_URL = DAVIDADM_HOST + API_PATH + API_VERSION_PATH + USER_FAVORITE_SAVE_PATH;
    private static final String GET_USER_FAVORITE_REMOVE_DAVIDADM_URL = DAVIDADM_HOST + API_PATH + API_VERSION_PATH + USER_FAVORITE_REMOVE_PATH;
    private static final String GET_USER_FAVORITE_GET_DAVIDADM_URL = DAVIDADM_HOST + API_PATH + API_VERSION_PATH + USER_FAVORITE_GET_PATH;

    // Headers
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String PROVIDER = "provider";
    private static final String FACEBOOK = "facebook";
    private static final String NATIVE = "native";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_ = "Bearer ";

    /**
     * GET Marketing Hots API.
     *
     * @param
     * @return
     * @throws IOException
     * @throws StylishException
     */
    public static GetMarketingHots getMarketingHots() throws IOException, StylishException {

        HashMap headers = new HashMap();

        try {
            return StylishParser.parseGetMarketingHots(new StylishClient()
                    .get(GET_MARKETING_HOTS_URL, headers));
        } catch (StylishInvalidTokenException e) {
            throw new StylishInvalidTokenException(e.getMessage());
        } catch (StylishException e) {
            throw new StylishException(e.getMessage());
        }
    }

    /**
     * GET Product List API.
     *
     * @param
     * @return
     * @throws IOException
     * @throws StylishException
     */
    public static GetProductList getProductList(@MainMvpController.CatalogItem String itemType, int paging)
            throws IOException, StylishException {

        String url;

        switch (itemType) {
            case WOMEN:
                url = GET_PRODUCTS_WOMEN_URL;
                break;
            case MEN:
                url = GET_PRODUCTS_MEN_URL;
                break;
            case ACCESSORIES:
                url = GET_PRODUCTS_ACCESSORIES_URL;
                break;
            default:
                url = "";
                break;
        }

        HashMap headers = new HashMap();

        try {
            return StylishParser.parseGetProductList(new StylishClient()
                    .get((paging == GetProductList.FIRST_PAGING) ? url : url + PAGING_PATH + String.valueOf(paging), headers));
        } catch (StylishInvalidTokenException e) {
            throw new StylishInvalidTokenException(e.getMessage());
        } catch (StylishException e) {
            throw new StylishException(e.getMessage());
        }
    }

    /**
     * POST User Sign In API.
     *
     * @param
     * @return
     * @throws IOException
     * @throws StylishException
     */
    public static UserSignIn postUserSignInWithToken(@NonNull String token) throws IOException, StylishException {

        HashMap headers = new HashMap();
        headers.put(CONTENT_TYPE, APPLICATION_JSON);

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put(PROVIDER, FACEBOOK);
        bodyMap.put(ACCESS_TOKEN, token);

        String body = new JSONObject(bodyMap).toString();

        try {
            return StylishParser.parseUserSignIn(new StylishClient()
                    .post(POST_USER_SIGNIN_DAVIDADM_URL, body, headers));
        } catch (StylishInvalidTokenException e) {
            throw new StylishInvalidTokenException(e.getMessage());
        } catch (StylishException e) {
            throw new StylishException(e.getMessage());
        }
    }

    public static UserSignIn postUserSignUp(@NonNull String name, String email, String password) throws IOException, StylishException {

        HashMap headers = new HashMap();
        headers.put(CONTENT_TYPE, APPLICATION_JSON);

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put(NAME, name);
        bodyMap.put(EMAIL, email);
        bodyMap.put(PASSWORD, password);

        String body = new JSONObject(bodyMap).toString();

        try {
            return StylishParser.parseUserSignIn(new StylishClient()
                    .post(POST_USER_SIGNUP_DAVIDADM_URL, body, headers));
        } catch (StylishInvalidTokenException e) {
            throw new StylishInvalidTokenException(e.getMessage());
        } catch (StylishException e) {
            e.printStackTrace();
            throw new StylishException(e.getMessage());
        }
    }

    public static UserSignIn postUserSignInNative(@NonNull String email, @NonNull String password) throws IOException, StylishException {

        HashMap headers = new HashMap();
        headers.put(CONTENT_TYPE, APPLICATION_JSON);

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put(PROVIDER, NATIVE);
        bodyMap.put(EMAIL, email);
        bodyMap.put(PASSWORD, password);

        String body = new JSONObject(bodyMap).toString();

        try {
            return StylishParser.parseUserSignIn(new StylishClient()
                    .post(POST_USER_SIGNIN_DAVIDADM_URL, body, headers));
        } catch (StylishInvalidTokenException e) {
            throw new StylishInvalidTokenException(e.getMessage());
        } catch (StylishException e) {
            e.printStackTrace();
            throw new StylishException(e.getMessage());
        }
    }

    public static String saveFavoriteItem(@NonNull String token, @NonNull long itemId) throws IOException, StylishException {

        HashMap headers = new HashMap();
        headers.put(AUTHORIZATION, BEARER_ + token);
        String url = GET_USER_FAVORITE_SAVE_DAVIDADM_URL + ID_PATH + itemId;
        try {
            String results = new StylishClient()
                    .get(url, headers);
            return results;
        } catch (StylishInvalidTokenException e) {
            throw new StylishInvalidTokenException(e.getMessage());
        } catch (StylishException e) {
            e.printStackTrace();
            throw new StylishException(e.getMessage());
        }
    }

    public static String removeFavoriteItem(@NonNull String token, @NonNull long itemId) throws IOException, StylishException {

        HashMap headers = new HashMap();
        headers.put(AUTHORIZATION, BEARER_ + token);
        String url = GET_USER_FAVORITE_REMOVE_DAVIDADM_URL + ID_PATH + itemId;
        try {
            String results = new StylishClient()
                    .get(url, headers);
            return results;
        } catch (StylishInvalidTokenException e) {
            throw new StylishInvalidTokenException(e.getMessage());
        } catch (StylishException e) {
            e.printStackTrace();
            throw new StylishException(e.getMessage());
        }
    }

    public static String getFavoriteItemList(@NonNull String token) throws IOException, StylishException {

        HashMap headers = new HashMap();
        headers.put(AUTHORIZATION, BEARER_ + token);
        String url = GET_USER_FAVORITE_GET_DAVIDADM_URL ;
        try {
            String results = new StylishClient()
                    .get(url, headers);
            return results;
        } catch (StylishInvalidTokenException e) {
            throw new StylishInvalidTokenException(e.getMessage());
        } catch (StylishException e) {
            e.printStackTrace();
            throw new StylishException(e.getMessage());
        }
    }

    public static String postAvatarImage(Uri imageUri, String realPath) {

        try {

//            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            File sourceFile = new File(realPath);

            Log.d(TAG, "File...::::" + sourceFile + " : " + sourceFile.exists());

            final MediaType MEDIA_TYPE = MediaType.parse("image/jpeg");
            Log.d(TAG, "MEDIA_TYPE: " + MEDIA_TYPE);
            System.out.println(new File(realPath).getAbsoluteFile());
//                    MediaType.parse("image/png") : MediaType.parse("image/jpeg");

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("avatar", "avatar.jpg", RequestBody.create(MEDIA_TYPE, sourceFile))
//                    .addFormDataPart("name", "Mike")
                    .build();

            Request request = new Request.Builder()
                    .url(POST_AVATAR_URL)
                    .addHeader(AUTHORIZATION, BEARER_ + UserManager.getInstance().getUserToken())
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            Log.d(TAG, "response: " + response);
            String result = response.body().string();
            Log.d("Resulty", "result: " + result);
            return result;

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;

    }

    /**
     * GET User Sign In API.
     *
     * @param
     * @return
     * @throws IOException
     * @throws StylishException
     */
    public static User getUserProfile(@NonNull String token) throws IOException, StylishException {

        HashMap headers = new HashMap();
        headers.put(AUTHORIZATION, BEARER_ + token);

        try {
            return StylishParser.parseGetUserProfile(new StylishClient()
                    .get(GET_USER_PROFILE_URL, headers));
        } catch (StylishInvalidTokenException e) {
            throw new StylishInvalidTokenException(e.getMessage());
        } catch (StylishException e) {
            throw new StylishException(e.getMessage());
        }
    }

    /**
     * POST Order Check Out API.
     *
     * @param
     * @return
     * @throws IOException
     * @throws StylishException
     */
    public static String postOrderCheckOut(@NonNull String token, @NonNull CheckOutInfo checkOutInfo) throws IOException, StylishException {

        HashMap headers = new HashMap();
        headers.put(CONTENT_TYPE, APPLICATION_JSON);
        headers.put(AUTHORIZATION, BEARER_ + token);

        String body = null;
        try {
            body = checkOutInfo.toJsonString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(Constants.TAG, "body: " + body);

        try {
            return StylishParser.parseOrderCheckOut(new StylishClient()
                    .post(POST_ORDER_CHECKOUT_URL, body, headers));
        } catch (StylishInvalidTokenException e) {
            throw new StylishInvalidTokenException(e.getMessage());
        } catch (StylishException e) {
            throw new StylishException(e.getMessage());
        }
    }
}
