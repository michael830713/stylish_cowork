package app.waynechen.stylish.api;

import android.util.Log;

import app.waynechen.stylish.api.exception.StylishException;
import app.waynechen.stylish.api.exception.StylishInvalidTokenException;
import app.waynechen.stylish.util.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class StylishClient {

    public static final String APPLICATION_JSON = "application/json";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final MediaType JSON = MediaType.parse(APPLICATION_JSON);

    /**
     * POST
     * @param url
     * @param json
     * @param headers
     * @return
     * @throws IOException
     */
    String post(String url, String json, HashMap<String, String> headers) throws IOException, StylishException {

        Log.d(Constants.TAG, "StylishClient ");

        OkHttpClient client = new OkHttpClient();

        // Request build
        Request request = new Request.Builder()
                .url(url)
                .headers(doHeadersBuilder(headers).build())
                .post(RequestBody.create(JSON, json))
                .build();

        Log.d(Constants.TAG, "POST " + request.url());
        Log.d(Constants.TAG, request.headers().toString());
        Log.d(Constants.TAG, json);

        Response response = client.newCall(request).execute();

        try {
            return doResponse(response);

        } catch (StylishInvalidTokenException e) {
            throw new StylishInvalidTokenException(e.getMessage());
        } catch (StylishException e) {
            throw new StylishException(e.getMessage());
        }
    }

    /**
     * GET
     * @param url
     * @param headers
     * @return
     * @throws IOException
     */
    String get(String url, HashMap<String, String> headers) throws IOException, StylishException {

        Log.d(Constants.TAG, "StylishClient ");

        OkHttpClient client = new OkHttpClient();

        // Request build
        Request request = new Request.Builder()
                .url(url)
                .headers(doHeadersBuilder(headers).build())
                .build();

        Log.d(Constants.TAG, "GET " + request.url());
        Log.d(Constants.TAG, request.headers().toString());

        Response response = client.newCall(request).execute();

        try {
            return doResponse(response);

        } catch (StylishInvalidTokenException e) {
            throw new StylishInvalidTokenException(e.getMessage());
        } catch (StylishException e) {
            throw new StylishException(e.getMessage());
        }
    }

    /**
     * DELETE
     * @param url
     * @param headers
     * @return
     * @throws IOException
     */
    String delete(String url, HashMap<String, String> headers) throws IOException, StylishException {

        Log.d(Constants.TAG, "StylishClient ");

        OkHttpClient client = new OkHttpClient();

        // Request build
        Request request = new Request.Builder()
                .url(url)
                .headers(doHeadersBuilder(headers).build())
                .delete()
                .build();

        Log.d(Constants.TAG, "DELETE " + request.url());
        Log.d(Constants.TAG, request.headers().toString());

        Response response = client.newCall(request).execute();

        try {
            return doResponse(response);

        } catch (StylishInvalidTokenException e) {
            throw new StylishInvalidTokenException(e.getMessage());
        } catch (StylishException e) {
            throw new StylishException(e.getMessage());
        }
    }

    /**
     * PATCH
     * @param url
     * @param json
     * @param headers
     * @return
     * @throws IOException
     */
    String patch(String url, String json, HashMap<String, String> headers) throws IOException, StylishException {

        Log.d(Constants.TAG, "StylishClient ");

        OkHttpClient client = new OkHttpClient();

        // Request build
        Request request = new Request.Builder()
                .url(url)
                .headers(doHeadersBuilder(headers).build())
                .patch(RequestBody.create(JSON, json))
                .build();

        Log.d(Constants.TAG, "PATCH " + request.url());
        Log.d(Constants.TAG, request.headers().toString());
        Log.d(Constants.TAG, json);

        Response response = client.newCall(request).execute();

        try {
            return doResponse(response);

        } catch (StylishInvalidTokenException e) {
            throw new StylishInvalidTokenException(e.getMessage());
        } catch (StylishException e) {
            throw new StylishException(e.getMessage());
        }
    }

    private boolean isStylishServerError(int responseCode) {
        return (responseCode >= 400 && responseCode < 500);
    }

    private String doResponse(Response response) throws IOException, StylishException {
        Log.d(Constants.TAG, "Response Code: " + response.code());
        if (response.isSuccessful()) {

            String responseData = response.body().string();
            Log.d(Constants.TAG, "Response Data: " + responseData);
            return responseData;
        } else if (isStylishServerError(response.code())) {
            // Voyage server error.
            try {
                StylishParser.parseError(response.code(), response.body().string());

            } catch (StylishInvalidTokenException e) {
                throw new StylishInvalidTokenException(e.getMessage());
            } catch (StylishException e) {
                throw new StylishException(e.getMessage());
            }
            return "";
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    private Headers.Builder doHeadersBuilder(HashMap<String, String> headers) {
        // Add headers
        Headers.Builder headersBuilder = new Headers.Builder();
        headersBuilder.add(CONTENT_TYPE, APPLICATION_JSON);

        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                headersBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        return headersBuilder;
    }
}
