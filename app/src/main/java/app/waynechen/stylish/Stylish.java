package app.waynechen.stylish;

import android.app.Application;
import android.content.Context;

import app.waynechen.stylish.database.StylishSQLiteHelper;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class Stylish extends Application {

    private static Context mContext;
    private static StylishSQLiteHelper mStylishSQLiteHelper;

    public Stylish() {}

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mStylishSQLiteHelper = null;
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static StylishSQLiteHelper getSQLiteHelper() {
        if (mStylishSQLiteHelper == null) mStylishSQLiteHelper = new StylishSQLiteHelper(getAppContext());
        return mStylishSQLiteHelper;
    }
}

