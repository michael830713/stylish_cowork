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
public class SaveFavoriteTask extends AsyncTask<Void, Void, String> {

    private String mErrorMessage;
    private String mToken;
    private long mItemId;
    private StylishDataSource.AvatarChangeCallback mCallback;

    public SaveFavoriteTask(String token, long itemId, StylishDataSource.AvatarChangeCallback callback) {
        mErrorMessage = "";
        mToken = token;
        mItemId = itemId;
        mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void[] voids) {

        String bean = null;

        try {
            bean = StylishApiHelper.saveFavoriteItem(mToken, mItemId);
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
    protected void onPostExecute(String bean) {
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
