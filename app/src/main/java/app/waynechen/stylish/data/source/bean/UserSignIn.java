package app.waynechen.stylish.data.source.bean;

import app.waynechen.stylish.data.User;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class UserSignIn {

    private String mAccessToken;
    private String mAccessExpired;
    private User mUser;

    public UserSignIn() {
        mAccessToken = "";
        mAccessExpired = "";
        mUser = new User();
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }

    public String getAccessExpired() {
        return mAccessExpired;
    }

    public void setAccessExpired(String accessExpired) {
        mAccessExpired = accessExpired;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}
