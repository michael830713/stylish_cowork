package app.waynechen.stylish.data.source.local;

import android.net.Uri;
import android.support.annotation.NonNull;

import app.waynechen.stylish.R;
import app.waynechen.stylish.Stylish;
import app.waynechen.stylish.data.CheckOutInfo;
import app.waynechen.stylish.data.source.StylishDataSource;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class StylishLocalDataSource implements StylishDataSource {

    private static StylishLocalDataSource INSTANCE;

    private StylishLocalDataSource() {}

    public static StylishLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StylishLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getHotsList(@NonNull GetHotsListCallback callback) {}

    @Override
    public void getProductList(@NonNull String itemType, int paging,
                               @NonNull GetProductListCallback callback) {}

    @Override
    public void postUserFacebookSignIn(@NonNull String token, UserSignInCallback callback) {}

    @Override
    public void postUserSignUp(@NonNull String name, String email, String password, UserSignInCallback callback) {

    }

    @Override
    public void postUserNativeSignIn(@NonNull String email, @NonNull String password, @NonNull UserSignInCallback callback) {

    }

    @Override
    public void postChangedAvatar(Uri imageUri, String realPath, AvatarChangeCallback callback) {

    }

    @Override
    public void getUserProfile(@NonNull String token, @NonNull GetUserProfileCallback callback) {}

    @Override
    public void postOrderCheckOutCallback(@NonNull String token,
                                          @NonNull CheckOutInfo checkOutInfo,
                                          @NonNull OrderCheckOutCallback callback) {}

    @Override
    public String getUserInformation(@NonNull String key) {
        switch (key) {
            case "wayne.swchen@gmail.com":
                return "AKA 小安老師";
            default:
                return Stylish.getAppContext().getString(R.string.profile_default_information);
        }
    }
}
