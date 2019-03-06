package app.waynechen.stylish.data;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class User {

    private int mId;
    private String mProvider;
    private String mName;
    private String mEmail;
    private String mPicture;

    public User() {
        mId = -1;
        mProvider = "";
        mName = "";
        mEmail = "";
        mPicture = "";
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getProvider() {
        return mProvider;
    }

    public void setProvider(String provider) {
        mProvider = provider;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPicture() {
        return mPicture;
    }

    public void setPicture(String picture) {
        mPicture = picture;
    }
}
