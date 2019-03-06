package app.waynechen.stylish.data;

import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class Product {

    private long mId;
    private String mTitle;
    private String mDescription;
    private int mPrice;
    private String mTexture;
    private String mWash;
    private String mPlace;
    private String mNote;
    private String mStory;
    private ArrayList<Color> mColors;
    private ArrayList<String> mSizes;
    private ArrayList<Variant> mVariants;
    private String mMainImage;
    private ArrayList<String> mImages;

    public Product() {
        mId = -1;
        mTitle = "";
        mDescription = "";
        mPrice = -1;
        mTexture = "";
        mWash = "";
        mPlace = "";
        mNote = "";
        mStory = "";
        mColors = new ArrayList<>();
        mSizes = new ArrayList<>();
        mVariants = new ArrayList<>();
        mMainImage = "";
        mImages = new ArrayList<>();
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public String getTexture() {
        return mTexture;
    }

    public void setTexture(String texture) {
        mTexture = texture;
    }

    public String getWash() {
        return mWash;
    }

    public void setWash(String wash) {
        mWash = wash;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String place) {
        mPlace = place;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }

    public String getStory() {
        return mStory;
    }

    public void setStory(String story) {
        mStory = story;
    }

    public ArrayList<Color> getColors() {
        return mColors;
    }

    public void setColors(ArrayList<Color> colors) {
        mColors = colors;
    }

    public ArrayList<String> getSizes() {
        return mSizes;
    }

    public void setSizes(ArrayList<String> sizes) {
        mSizes = sizes;
    }

    public ArrayList<Variant> getVariants() {
        return mVariants;
    }

    public void setVariants(ArrayList<Variant> variants) {
        mVariants = variants;
    }

    public String getMainImage() {
        return mMainImage;
    }

    public void setMainImage(String mainImage) {
        mMainImage = mainImage;
    }

    public ArrayList<String> getImages() {
        return mImages;
    }

    public void setImages(ArrayList<String> images) {
        mImages = images;
    }
}
