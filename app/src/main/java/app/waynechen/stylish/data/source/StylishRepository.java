package app.waynechen.stylish.data.source;

import static com.google.common.base.Preconditions.checkNotNull;

import android.net.Uri;
import android.support.annotation.NonNull;

import org.json.JSONObject;

import app.waynechen.stylish.MainMvpController;
import app.waynechen.stylish.data.CheckOutInfo;
import app.waynechen.stylish.data.User;
import app.waynechen.stylish.data.source.bean.GetMarketingHots;
import app.waynechen.stylish.data.source.bean.GetProductList;
import app.waynechen.stylish.data.source.bean.UserSignIn;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class StylishRepository implements StylishDataSource {

    private static StylishRepository INSTANCE = null;

    private final StylishDataSource mStylishRemoteDataSource;

    private final StylishDataSource mStylishLocalDataSource;

    boolean isOffline = false;

    private StylishRepository(@NonNull StylishDataSource remoteDataSource,
                              @NonNull StylishDataSource localDataSource) {
        mStylishRemoteDataSource = checkNotNull(remoteDataSource);
        mStylishLocalDataSource = checkNotNull(localDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param remoteDataSource the backend data source
     * @return the {@link StylishRepository} instance
     */
    public static StylishRepository getInstance(StylishDataSource remoteDataSource,
                                                StylishDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new StylishRepository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getHotsList(@NonNull GetHotsListCallback callback) {
        mStylishRemoteDataSource.getHotsList(new GetHotsListCallback() {

            @Override
            public void onCompleted(GetMarketingHots bean) {
                callback.onCompleted(bean);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    @Override
    public void getProductList(@MainMvpController.CatalogItem String itemType,
                               int paging, @NonNull GetProductListCallback callback) {
        mStylishRemoteDataSource.getProductList(itemType, paging, new GetProductListCallback() {
            @Override
            public void onCompleted(GetProductList bean) {
                callback.onCompleted(bean);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void postUserFacebookSignIn(@NonNull String token, UserSignInCallback callback) {
        mStylishRemoteDataSource.postUserFacebookSignIn(token, new UserSignInCallback() {
            @Override
            public void onCompleted(UserSignIn bean) {

                // todo something
                callback.onCompleted(bean);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    @Override
    public void postUserNativeSignIn(@NonNull String email, String password, UserSignInCallback callback) {
        mStylishRemoteDataSource.postUserNativeSignIn(email, password, new UserSignInCallback() {
            @Override
            public void onCompleted(UserSignIn bean) {

                // todo something
                callback.onCompleted(bean);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    @Override
    public void postUserSignUp(@NonNull String name, String email, String password, UserSignInCallback callback) {
        mStylishRemoteDataSource.postUserSignUp(name, email, password, new UserSignInCallback() {
            @Override
            public void onCompleted(UserSignIn bean) {
                callback.onCompleted(bean);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    @Override
    public void postChangedAvatar(Uri imageUri, String realPath, AvatarChangeCallback callback) {
        mStylishRemoteDataSource.postChangedAvatar(imageUri, realPath, new AvatarChangeCallback() {
            @Override
            public void onCompleted(JSONObject bean) {
                callback.onCompleted(bean);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    @Override
    public void getUserProfile(@NonNull String token, @NonNull GetUserProfileCallback callback) {
        mStylishRemoteDataSource.getUserProfile(token, new GetUserProfileCallback() {
            @Override
            public void onCompleted(User bean) {
                callback.onCompleted(bean);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }

            @Override
            public void onInvalidToken(String errorMessage) {
                callback.onInvalidToken(errorMessage);
            }
        });
    }

    @Override
    public void postOrderCheckOutCallback(@NonNull String token, @NonNull CheckOutInfo checkOutInfo,
                                          @NonNull OrderCheckOutCallback callback) {
        mStylishRemoteDataSource.postOrderCheckOutCallback(token, checkOutInfo, new OrderCheckOutCallback() {
            @Override
            public void onCompleted(String number) {
                callback.onCompleted(number);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }

            @Override
            public void onInvalidToken(String errorMessage) {
                callback.onInvalidToken(errorMessage);
            }
        });
    }

    @Override
    public String getUserInformation(@NonNull String key) {
        return mStylishLocalDataSource.getUserInformation(key);
    }

}
