package app.waynechen.stylish.data.source.task;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import app.waynechen.stylish.api.StylishApiHelper;
import app.waynechen.stylish.api.exception.StylishException;
import app.waynechen.stylish.api.exception.StylishInvalidTokenException;
import app.waynechen.stylish.data.ProductForGson;
import app.waynechen.stylish.data.VideoLink;
import app.waynechen.stylish.data.source.StylishDataSource;
import app.waynechen.stylish.util.Constants;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class GetVideoLinkTask extends AsyncTask<Void, Void, String> {

    private static final String TAG = "GetVideoLinkTask";

    private String mErrorMessage;
    private String mId;
    private StylishDataSource.AvatarChangeCallback mCallback;

    public GetVideoLinkTask(String id, StylishDataSource.AvatarChangeCallback callback) {
        mErrorMessage = "";
        mId = id;
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
            bean = StylishApiHelper.getVideoLink(mId);
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
            Gson gson = new Gson();
            Log.d(TAG, "onPostExecute: "+bean);
            VideoLink videoLink = gson.fromJson(bean, VideoLink.class);
            String[] video = videoLink.getData();
//            Log.d(TAG, "Note: "+note);
            mCallback.onCompleted(video[0]);
        } else if (!"".equals(mErrorMessage)) {

            mCallback.onError(mErrorMessage);
        } else {

            Log.d(Constants.TAG, "UserSignUpTask fail");
        }
    }
}
