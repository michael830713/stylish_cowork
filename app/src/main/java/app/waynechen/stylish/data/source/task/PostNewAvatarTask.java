package app.waynechen.stylish.data.source.task;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

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
public class PostNewAvatarTask extends AsyncTask<Void, Void, String> {

    private String mErrorMessage;
    private Uri mImageUri;
    private String mRealPath;
    private StylishDataSource.AvatarChangeCallback mCallback;

    public PostNewAvatarTask(Uri imageUri, String realPath, StylishDataSource.AvatarChangeCallback callback) {
        mErrorMessage = "";
        mImageUri = imageUri;
        mRealPath = realPath;

        mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void[] voids) {

        String bean = null;

        bean = StylishApiHelper.postAvatarImage(mImageUri, mRealPath);

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
