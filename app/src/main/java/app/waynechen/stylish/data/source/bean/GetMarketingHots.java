package app.waynechen.stylish.data.source.bean;

import app.waynechen.stylish.data.Hots;

import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class GetMarketingHots {

    private ArrayList<Hots> mHotsList;

    public GetMarketingHots() {
        mHotsList = new ArrayList<>();
    }

    public ArrayList<Hots> getHotsList() {
        return mHotsList;
    }

    public void setHotsList(ArrayList<Hots> hotsList) {
        mHotsList = hotsList;
    }

    public ArrayList<Object> combineHotsList() {

        ArrayList<Object> combineList = new ArrayList<>();

        for (Hots hots : mHotsList) {
            combineList.addAll(hots.toObjList());
        }
        return combineList;
    }
}
