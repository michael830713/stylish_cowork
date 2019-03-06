package app.waynechen.stylish.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import app.waynechen.stylish.data.CartProduct;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class StylishSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "stylish_data.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CART_TABLE = "stylish_cart_table";

    private static final String PRODUCT_ID = "product_id";
    private static final String PRODUCT_TITLE = "product_title";
    private static final String PRODUCT_DESCRIPTION = "product_description";
    private static final String PRODUCT_PRICE = "product_price";
    private static final String PRODUCT_TEXTURE = "product_texture";
    private static final String PRODUCT_WASH = "product_wash";
    private static final String PRODUCT_PLACE = "product_place";
    private static final String PRODUCT_NOTE = "product_note";
    private static final String PRODUCT_STORY = "product_story";
    private static final String PRODUCT_COLORS = "product_colors";
    private static final String PRODUCT_SIZES = "product_sizes";
    private static final String PRODUCT_VARIANTS = "product_variants";
    private static final String PRODUCT_MAIN_IMAGE = "product_main_image";
    private static final String PRODUCT_IMAGES = "product_images";

    private static final String PRODUCT_SELECTED_COLOR_CODE = "product_selected_color_code";
    private static final String PRODUCT_SELECTED_SIZE = "product_selected_size";
    private static final String PRODUCT_SELECTED_STOCK = "product_selected_stock";
    private static final String PRODUCT_SELECTED_AMOUNT = "product_selected_amount";

    private Cursor mCursor;

    private static final String CREATE_CART_TABLE = "CREATE TABLE " + CART_TABLE + " ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PRODUCT_ID + " INTEGER NOT NULL, "
            + PRODUCT_TITLE + " TEXT NOT NULL, "
            + PRODUCT_DESCRIPTION + " TEXT NOT NULL, "
            + PRODUCT_PRICE + " INTEGER NOT NULL, "
            + PRODUCT_TEXTURE + " TEXT NOT NULL, "
            + PRODUCT_WASH + " TEXT NOT NULL, "
            + PRODUCT_PLACE + " TEXT NOT NULL, "
            + PRODUCT_NOTE + " TEXT NOT NULL, "
            + PRODUCT_STORY + " TEXT NOT NULL, "
//            + PRODUCT_COLORS + " BLOB NOT NULL, "
            + PRODUCT_SIZES + " BLOB NOT NULL, "
//            + PRODUCT_VARIANTS + " BLOB NOT NULL, "
            + PRODUCT_MAIN_IMAGE + " TEXT NOT NULL, "
            + PRODUCT_IMAGES + " BLOB NOT NULL, "
            + PRODUCT_SELECTED_COLOR_CODE + " TEXT NOT NULL, "
            + PRODUCT_SELECTED_SIZE + " TEXT NOT NULL, "
            + PRODUCT_SELECTED_STOCK + " INTEGER NOT NULL, "
            + PRODUCT_SELECTED_AMOUNT + " INTEGER NOT NULL) ";

    public StylishSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_CART_TABLE);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_CART_TABLE);
    }

    /**
     * Insert selected product to cart.
     * @param cartProduct
     */
    private void insertProduct(CartProduct cartProduct) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(PRODUCT_ID, cartProduct.getId());
        contentValues.put(PRODUCT_TITLE, cartProduct.getTitle());
        contentValues.put(PRODUCT_DESCRIPTION, cartProduct.getDescription());
        contentValues.put(PRODUCT_PRICE, cartProduct.getPrice());
        contentValues.put(PRODUCT_TEXTURE, cartProduct.getTexture());
        contentValues.put(PRODUCT_WASH, cartProduct.getWash());
        contentValues.put(PRODUCT_PLACE, cartProduct.getPlace());
        contentValues.put(PRODUCT_NOTE, cartProduct.getNote());
        contentValues.put(PRODUCT_STORY, cartProduct.getStory());
//        contentValues.put(PRODUCT_COLORS, getByteArray(product.getColors()));
        contentValues.put(PRODUCT_SIZES, getByteArray(cartProduct.getSizes()));
//        contentValues.put(PRODUCT_VARIANTS, getByteArray(product.getVariants()));
        contentValues.put(PRODUCT_MAIN_IMAGE, cartProduct.getMainImage());
        contentValues.put(PRODUCT_IMAGES, getByteArray(cartProduct.getImages()));

        contentValues.put(PRODUCT_SELECTED_COLOR_CODE, cartProduct.getSelectedColorCode());
        contentValues.put(PRODUCT_SELECTED_SIZE, cartProduct.getSelectedSize());
        contentValues.put(PRODUCT_SELECTED_STOCK, cartProduct.getSelectedStock());
        contentValues.put(PRODUCT_SELECTED_AMOUNT, cartProduct.getSelectedAmount());

        getWritableDatabase().insert(CART_TABLE, null, contentValues);

    }

    /**
     * Update amount.
     * @param cartProduct
     */
    public void updateAmount(CartProduct cartProduct) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(PRODUCT_SELECTED_AMOUNT, cartProduct.getSelectedAmount());

        if (isProductInTheCart(cartProduct)) {

            getWritableDatabase().update(CART_TABLE, contentValues,
                    PRODUCT_ID + "='" + cartProduct.getId() + "' AND "
                            + PRODUCT_SELECTED_COLOR_CODE + "='" + cartProduct.getSelectedColorCode() + "' AND "
                            + PRODUCT_SELECTED_SIZE + "='" + cartProduct.getSelectedSize() + "'", null);

        } else {

            insertProduct(cartProduct);
        }
    }

    /**
     * Remove product.
     * @param cartProduct
     */
    public void removeProduct(CartProduct cartProduct) {
        getWritableDatabase().delete(CART_TABLE,
                PRODUCT_ID + "='" + cartProduct.getId() + "' AND "
                        + PRODUCT_SELECTED_COLOR_CODE + "='" + cartProduct.getSelectedColorCode() + "' AND "
                        + PRODUCT_SELECTED_SIZE + "='" + cartProduct.getSelectedSize() + "'", null);
    }

    /**
     * Clean product.
     */
    public void cleanProducts() {
        getWritableDatabase().delete(CART_TABLE,
                null, null);
    }

    /**
     * Check product data whether exist.
     * @param cartProduct
     * @return
     */
    public boolean isProductInTheCart(CartProduct cartProduct) {

        mCursor = getWritableDatabase().query(CART_TABLE,
                new String[]{PRODUCT_ID},
                PRODUCT_ID + "='" + cartProduct.getId() + "' AND "
                        + PRODUCT_SELECTED_COLOR_CODE + "='" + cartProduct.getSelectedColorCode() + "' AND "
                        + PRODUCT_SELECTED_SIZE + "='" + cartProduct.getSelectedSize() + "'",
                null, null, null, null);
        return (mCursor.getCount() > 0);
    }

    public ArrayList<CartProduct> getCartProducts() {

        mCursor = getWritableDatabase().query(CART_TABLE,
                new String[]{PRODUCT_ID, PRODUCT_TITLE, PRODUCT_DESCRIPTION, PRODUCT_PRICE,
                        PRODUCT_TEXTURE, PRODUCT_WASH, PRODUCT_PLACE, PRODUCT_NOTE, PRODUCT_STORY,
                        PRODUCT_SIZES, PRODUCT_MAIN_IMAGE,
                        PRODUCT_IMAGES, PRODUCT_SELECTED_COLOR_CODE, PRODUCT_SELECTED_SIZE,
                        PRODUCT_SELECTED_STOCK, PRODUCT_SELECTED_AMOUNT}, null,
                null, null, null, null);

        ArrayList<CartProduct> cartProducts = new ArrayList<>();

        while (mCursor.moveToNext()) {
            CartProduct cartProduct = new CartProduct();

            cartProduct.setId(mCursor.getLong(mCursor.getColumnIndex(PRODUCT_ID)));
            cartProduct.setTitle(mCursor.getString(mCursor.getColumnIndex(PRODUCT_TITLE)));
            cartProduct.setDescription(mCursor.getString(mCursor.getColumnIndex(PRODUCT_DESCRIPTION)));
            cartProduct.setPrice(mCursor.getInt(mCursor.getColumnIndex(PRODUCT_PRICE)));
            cartProduct.setTexture(mCursor.getString(mCursor.getColumnIndex(PRODUCT_TEXTURE)));
            cartProduct.setWash(mCursor.getString(mCursor.getColumnIndex(PRODUCT_WASH)));
            cartProduct.setPlace(mCursor.getString(mCursor.getColumnIndex(PRODUCT_PLACE)));
            cartProduct.setNote(mCursor.getString(mCursor.getColumnIndex(PRODUCT_NOTE)));
            cartProduct.setStory(mCursor.getString(mCursor.getColumnIndex(PRODUCT_STORY)));
//            cartProduct.setColors((ArrayList) getBlob(mCursor.getBlob(mCursor.getColumnIndex(PRODUCT_COLORS))));
            cartProduct.setSizes((ArrayList) getBlob(mCursor.getBlob(mCursor.getColumnIndex(PRODUCT_SIZES))));
//            cartProduct.setVariants((ArrayList) getBlob(mCursor.getBlob(mCursor.getColumnIndex(PRODUCT_VARIANTS))));
            cartProduct.setMainImage(mCursor.getString(mCursor.getColumnIndex(PRODUCT_MAIN_IMAGE)));
            cartProduct.setImages((ArrayList) getBlob(mCursor.getBlob(mCursor.getColumnIndex(PRODUCT_IMAGES))));
            cartProduct.setSelectedColorCode(mCursor.getString(mCursor.getColumnIndex(PRODUCT_SELECTED_COLOR_CODE)));
            cartProduct.setSelectedSize(mCursor.getString(mCursor.getColumnIndex(PRODUCT_SELECTED_SIZE)));
            cartProduct.setSelectedStock(mCursor.getInt(mCursor.getColumnIndex(PRODUCT_SELECTED_STOCK)));
            cartProduct.setSelectedAmount(mCursor.getInt(mCursor.getColumnIndex(PRODUCT_SELECTED_AMOUNT)));

            cartProducts.add(cartProduct);
        }
        return cartProducts;
    }

    private byte[] getByteArray(Object object) {

        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object getBlob(byte[] byteArray) {
        ObjectInputStream objectInputStream = null;

        try {
            objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArray));
            return objectInputStream.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (null != objectInputStream) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
