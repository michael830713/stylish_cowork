package app.waynechen.stylish.data.source.task;

import android.os.AsyncTask;
import android.util.Log;

import app.waynechen.stylish.api.StylishApiHelper;
import app.waynechen.stylish.api.exception.StylishException;
import app.waynechen.stylish.api.exception.StylishInvalidTokenException;
import app.waynechen.stylish.data.User;
import app.waynechen.stylish.data.source.StylishDataSource;
import app.waynechen.stylish.data.source.remote.StylishRemoteDataSource;
import app.waynechen.stylish.util.Constants;

import java.io.IOException;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class GetUserProfileTask extends AsyncTask<Void, Void, User> {

    private int mStatus = StylishRemoteDataSource.DEFAULT;
    private String mErrorMessage;
    private String mToken;
    private StylishDataSource.GetUserProfileCallback mCallback;

    public GetUserProfileTask(String token,
                              StylishDataSource.GetUserProfileCallback callback) {
        mErrorMessage = "";
        mToken = token;
        mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected User doInBackground(Void[] voids) {

        User user = null;

        try {
            user = StylishApiHelper.getUserProfile(mToken);
            mStatus = StylishRemoteDataSource.SUCCESS;

        } catch (IOException e) {

            mErrorMessage = e.getMessage();
            mStatus = StylishRemoteDataSource.ERROR;
            e.printStackTrace();

        } catch (StylishInvalidTokenException e) {

            mErrorMessage = e.getMessage();
            mStatus = StylishRemoteDataSource.INVALID_TOKEN;
            e.printStackTrace();

        } catch (StylishException e) {

            mErrorMessage = e.getMessage();
            mStatus = StylishRemoteDataSource.ERROR;
            e.printStackTrace();
        }
        return user;
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);

        if (user != null && mStatus == StylishRemoteDataSource.SUCCESS) {

            mCallback.onCompleted(user);

        } else if (!"".equals(mErrorMessage)) {

            if (mStatus == StylishRemoteDataSource.INVALID_TOKEN) {

                mCallback.onInvalidToken(mErrorMessage);
            } else {

                mCallback.onError(mErrorMessage);
            }
        } else {

            Log.d(Constants.TAG, "GetUserProfileTask fail");
            mCallback.onError(Constants.GENERAL_ERROR);
        }
    }
}
