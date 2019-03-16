package app.waynechen.stylish.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import app.waynechen.stylish.R;
import app.waynechen.stylish.Stylish;
import app.waynechen.stylish.data.User;
import app.waynechen.stylish.data.source.StylishDataSource;
import app.waynechen.stylish.data.source.StylishRepository;
import app.waynechen.stylish.data.source.bean.UserSignIn;
import app.waynechen.stylish.data.source.local.StylishLocalDataSource;
import app.waynechen.stylish.data.source.remote.StylishRemoteDataSource;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;


/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class UserManager {

    private final StylishRepository mStylishRepository;
    private User mUser;
    private CallbackManager mFbCallbackManager;
    private long mLastChallengeTime;
    private int mChallengeCount;
    private static final int CHALLENGE_LIMIT = 3;

    private static class UserManagerHolder {
        private static final UserManager INSTANCE = new UserManager();
    }

    private UserManager() {
        mStylishRepository = StylishRepository.getInstance(
                StylishRemoteDataSource.getInstance(),
                StylishLocalDataSource.getInstance());
    }

    public static UserManager getInstance() {
        return UserManagerHolder.INSTANCE;
    }

    /**
     * Login Stylish by Facebook: Step 1. Register FB Login Callback
     *
     * @param context
     * @param loadCallback
     */
    public void loginStylishByFacebook(Context context, final LoadCallback loadCallback) {

        mFbCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mFbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d(Constants.TAG, "FB Login Success");
                Log.i(Constants.TAG, "loginResult.getAccessToken().getToken() = " + loginResult.getAccessToken().getToken());
                Log.i(Constants.TAG, "loginResult.getAccessToken().getUserId() = " + loginResult.getAccessToken().getUserId());
                Log.i(Constants.TAG, "loginResult.getAccessToken().getApplicationId() = " + loginResult.getAccessToken().getApplicationId());

                loginStylish(loginResult.getAccessToken().getToken(), loadCallback);
            }

            @Override
            public void onCancel() {

                Log.d(Constants.TAG, "FB Login Cancel");
                loadCallback.onFail("FB Login Cancel");
            }

            @Override
            public void onError(FacebookException exception) {

                Log.d(Constants.TAG, "FB Login Error");
                loadCallback.onFail("FB Login Error: " + exception.getMessage());
            }
        });

        loginFacebook(context);
    }

    /**
     * Login Stylish by Facebook: Step 2. Login Facebook
     */
    private void loginFacebook(Context context) {

        LoginManager.getInstance().logInWithReadPermissions(
                (Activity) context, Arrays.asList("email"));
    }




    /**
     * Login Stylish by Facebook: Step 3. Login Stylish
     *
     * @param token
     */

    private void loginStylish(String token, LoadCallback loadCallback) {
        mStylishRepository.postUserSignIn(token, new StylishDataSource.UserSignInCallback() {
            @Override
            public void onCompleted(UserSignIn bean) {

                setUser(bean.getUser());

                Stylish.getAppContext().getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE).edit()
                        .putString(Constants.USER_TOKEN, bean.getAccessToken())
                        .apply();
                loadCallback.onSuccess();
            }

            @Override
            public void onError(String errorMessage) {

                Log.d(Constants.TAG, errorMessage);
                loadCallback.onFail(errorMessage);
            }
        });
    }

    public void signUpStylish(String name, String email, String password, final LoadCallback loadCallback) {

        mStylishRepository.postUserSignUp(name, email, password, new StylishDataSource.UserSignInCallback() {
            @Override
            public void onCompleted(UserSignIn bean) {
                setUser(bean.getUser());

                Stylish.getAppContext().getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE).edit()
                        .putString(Constants.USER_TOKEN, bean.getAccessToken())
                        .apply();
                loadCallback.onSuccess();
            }

            @Override
            public void onError(String errorMessage) {
                Log.d(Constants.TAG, errorMessage);
                loadCallback.onFail(errorMessage);
            }
        });
    }

    public void getUserProfile(LoadCallback loadCallback) {
        mStylishRepository.getUserProfile(getUserToken(), new StylishDataSource.GetUserProfileCallback() {
            @Override
            public void onCompleted(User bean) {

                setUser(bean);

                loadCallback.onSuccess();
            }

            @Override
            public void onError(String errorMessage) {

                Log.d(Constants.TAG, errorMessage);
                loadCallback.onFail(errorMessage);
            }

            @Override
            public void onInvalidToken(String errorMessage) {

                clearUserLogin();
                loadCallback.onInvalidToken(errorMessage);
            }
        });
    }

    public void clearUserLogin() {
        setUser(null);
        Stylish.getAppContext().getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE).edit()
                .remove(Constants.USER_TOKEN)
                .apply();
    }

    public void challenge() {
        if (System.currentTimeMillis() - mLastChallengeTime > 5000) {
            mLastChallengeTime = System.currentTimeMillis();
            mChallengeCount = 0;
        } else {
            if (mChallengeCount == CHALLENGE_LIMIT) {
                clearUserLogin();
                Toast.makeText(Stylish.getAppContext(),
                        Stylish.getAppContext().getString(R.string.profile_default_information),
                        Toast.LENGTH_SHORT).show();
            } else {
                mChallengeCount++;
            }
        }
    }

    public String getUserInfo() {
        return mStylishRepository.getUserInformation(getUser().getEmail());
    }

    public String getUserToken() {

        return Stylish.getAppContext()
                .getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE)
                .getString(Constants.USER_TOKEN, null);
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public boolean isLoggedIn() {
        return getUserToken() != null;
    }

    public boolean hasUserInfo() {
        return getUser() != null;
    }

    public CallbackManager getFbCallbackManager() {
        return mFbCallbackManager;
    }

    public interface LoadCallback {

        void onSuccess();

        void onFail(String errorMessage);

        void onInvalidToken(String errorMessage);
    }
}

