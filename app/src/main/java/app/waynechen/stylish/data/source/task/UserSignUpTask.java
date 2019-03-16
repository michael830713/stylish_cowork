package app.waynechen.stylish.data.source.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import app.waynechen.stylish.api.StylishApiHelper;
import app.waynechen.stylish.api.exception.StylishException;
import app.waynechen.stylish.api.exception.StylishInvalidTokenException;
import app.waynechen.stylish.data.source.StylishDataSource;
import app.waynechen.stylish.data.source.bean.UserSignIn;
import app.waynechen.stylish.util.Constants;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class UserSignUpTask extends AsyncTask<Void, Void, UserSignIn> {

    private String mErrorMessage;
    private String mToken;
    private String mName;
    private String mEmail;
    private String mPassword;
    private StylishDataSource.UserSignInCallback mCallback;

    public UserSignUpTask(String name, String email, String password, StylishDataSource.UserSignInCallback callback) {
        mErrorMessage = "";
        mName = name;
        mEmail = email;
        mPassword = password;
        mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected UserSignIn doInBackground(Void[] voids) {

        UserSignIn bean = null;

        try {
            bean = StylishApiHelper.postUserSignUp(mName, mEmail, mPassword);
        } catch (IOException e) {
            mErrorMessage = e.getMessage();
            e.printStackTrace();
        } catch (StylishInvalidTokenException e) {
            mErrorMessage = e.getMessage();
            e.printStackTrace();
        } catch (StylishException e) {
            mErrorMessage = e.getMessage();
            e.printStackTrace();
        }
        return bean;
    }

    @Override
    protected void onPostExecute(UserSignIn bean) {
        super.onPostExecute(bean);

        if (bean != null) {

            mCallback.onCompleted(bean);
        } else if (!"".equals(mErrorMessage)) {

            mCallback.onError(mErrorMessage);
        } else {

            Log.d(Constants.TAG, "UserSignUpTask fail");
        }
    }
}
