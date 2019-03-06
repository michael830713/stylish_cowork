package app.waynechen.stylish.data;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class Color {

    private String mName;
    private String mCode;

    public Color() {
        mName = "";
        mCode = "";
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }
}
