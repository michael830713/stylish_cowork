package app.waynechen.stylish.detail.add2cart;

import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;

import app.waynechen.stylish.Stylish;
import app.waynechen.stylish.data.CartProduct;
import app.waynechen.stylish.data.Color;
import app.waynechen.stylish.data.Product;
import app.waynechen.stylish.data.Variant;

import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class Add2CartPresenter implements Add2CartContract.Presenter {

    private final Add2CartContract.View mAdd2CartView;

    private Product mProduct;
    private Variant mSelectedVariant;

    public Add2CartPresenter(Product product, @NonNull Add2CartContract.View add2CartView) {
        mAdd2CartView = checkNotNull(add2CartView, "add2CartView cannot be null!");
        mAdd2CartView.setPresenter(this);
        mProduct = product;
    }

    @Override
    public void result(int requestCode, int resultCode) {}

    @Override
    public void loadAdd2CartProductData() {
        mAdd2CartView.showProductUi(mProduct);
        selectAdd2CartVariant(null);
    }

    @Override
    public void setAdd2CartProductData(Product product) {
        mProduct = product;
    }

    @Override
    public void selectAdd2CartColor(Color color) {

        ArrayList<Variant> variants = new ArrayList<>();

        for (Variant variant: mProduct.getVariants()) {
            if (variant.getColorCode().equals(color.getCode())) {
                variants.add(variant);
            }
        }
        mAdd2CartView.showSizesUi(variants);
        selectAdd2CartVariant(null);
    }

    @Override
    public void selectAdd2CartVariant(Variant variant) {
        mSelectedVariant = variant;

        if (variant != null) {

            mAdd2CartView.showCurrentStockUi(mSelectedVariant.getStock());
            initialAdd2CartEditor();
            mAdd2CartView.setAdd2CartButtonEnable(true);
        } else {
            mAdd2CartView.showCurrentStockUi(-1);
            mAdd2CartView.disableEditorUi();
            mAdd2CartView.setAdd2CartButtonEnable(false);
        }
    }

    @Override
    public void clickAdd2CartIncrease() {

        if (mAdd2CartView.getEditorAmount() < mSelectedVariant.getStock()) {

            mAdd2CartView.increaseEditorAmount(mAdd2CartView.getEditorAmount() + 1);
            mAdd2CartView.setInRangeOfAmountUi();

            if (mAdd2CartView.getEditorAmount() == mSelectedVariant.getStock()) {
                mAdd2CartView.setMaximumAmountUi();
            }
        }
    }

    @Override
    public void clickAdd2CartDecrease() {

        mAdd2CartView.decreaseEditorAmount(mAdd2CartView.getEditorAmount() - 1);
        mAdd2CartView.setInRangeOfAmountUi();

        if (mAdd2CartView.getEditorAmount() == 1) {
            mAdd2CartView.setMinimumAmountUi();
        }
    }

    @Override
    public void initialAdd2CartEditor() {
        mAdd2CartView.showCurrentStockUi(mSelectedVariant.getStock());

        if (mSelectedVariant.getStock() == 1) {
            mAdd2CartView.disableEditorUi();
        } else {
            mAdd2CartView.initEditorUi();
        }
    }

    @Override
    public void onAdd2CartEditorTextChange(CharSequence currentNum) {

        if (mSelectedVariant != null) {

            if (!"".equals(currentNum.toString())) {

                if (Long.valueOf(currentNum.toString()) > mSelectedVariant.getStock()) {
                    mAdd2CartView.showStockNotEnoughUi(mSelectedVariant.getStock());
                }

            } else {
                initialAdd2CartEditor();
            }
        }
    }

    @Override
    public void addProductToCart() {
        Stylish.getSQLiteHelper().updateAmount(
                new CartProduct(mProduct, mSelectedVariant, mAdd2CartView.getEditorAmount()));

        mAdd2CartView.finishAdd2CartUi();
    }

    @Override
    public void showAddToSuccessDialog() {}

    @Override
    public void start() {}

}
