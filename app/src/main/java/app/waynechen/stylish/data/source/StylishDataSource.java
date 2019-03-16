package app.waynechen.stylish.data.source;

import android.support.annotation.NonNull;

import app.waynechen.stylish.MainMvpController;
import app.waynechen.stylish.data.CheckOutInfo;
import app.waynechen.stylish.data.User;
import app.waynechen.stylish.data.source.bean.GetMarketingHots;
import app.waynechen.stylish.data.source.bean.GetProductList;
import app.waynechen.stylish.data.source.bean.UserSignIn;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public interface StylishDataSource {

    interface GetHotsListCallback {

        void onCompleted(GetMarketingHots bean);

        void onError(String errorMessage);
    }

    interface GetProductListCallback {

        void onCompleted(GetProductList bean);

        void onError(String errorMessage);
    }

    interface UserSignInCallback {

        void onCompleted(UserSignIn bean);

        void onError(String errorMessage);
    }

    interface GetUserProfileCallback {

        void onCompleted(User user);

        void onError(String errorMessage);

        void onInvalidToken(String errorMessage);
    }

    interface OrderCheckOutCallback {

        void onCompleted(String number);

        void onError(String errorMessage);

        void onInvalidToken(String errorMessage);
    }

    void getHotsList(@NonNull GetHotsListCallback callback);

    void getProductList(@NonNull @MainMvpController.CatalogItem String itemType,
                        int paging, @NonNull GetProductListCallback callback);

    void postUserSignIn(@NonNull String token, @NonNull UserSignInCallback callback);

    void postUserSignUp(@NonNull String name, String email, String password, UserSignInCallback callback);

    void getUserProfile(@NonNull String token, @NonNull GetUserProfileCallback callback);

    void postOrderCheckOutCallback(@NonNull String token, @NonNull CheckOutInfo checkOutInfo,
                                   @NonNull OrderCheckOutCallback callback);

    String getUserInformation(@NonNull String key);
}
