package app.waynechen.stylish.cart;

import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;
import android.util.Log;

import app.waynechen.stylish.Stylish;
import app.waynechen.stylish.data.CartProduct;
import app.waynechen.stylish.data.source.StylishRepository;
import app.waynechen.stylish.util.Constants;

import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class CartPresenter implements CartContract.Presenter {

    private final StylishRepository mStylishRepository;
    private final CartContract.View mCartView;

    private ArrayList<CartProduct> mCartProducts;

    public CartPresenter(
            @NonNull StylishRepository stylishRepository,
            @NonNull CartContract.View cartView) {
        mStylishRepository = checkNotNull(stylishRepository, "stylishRepository cannot be null!");
        mCartView = checkNotNull(cartView, "cartView cannot be null!");
        mCartView.setPresenter(this);
    }


    @Override
    public void result(int requestCode, int resultCode) {}

    @Override
    public void loadCartProductsData() {
        mCartProducts = Stylish.getSQLiteHelper().getCartProducts();
        mCartView.showCartUi(mCartProducts);
    }

    @Override
    public void clickCartItemIncrease(int position) {
        if (mCartProducts.get(position).getSelectedAmount() < mCartProducts.get(position).getSelectedStock()) {

            mCartProducts.get(position).setSelectedAmount(mCartProducts.get(position).getSelectedAmount() + 1);

            Stylish.getSQLiteHelper().updateAmount(mCartProducts.get(position));

            mCartView.updateItemAmount(position, mCartProducts.get(position).getSelectedAmount());
        }

    }

    @Override
    public void clickCartItemDecrease(int position) {

        mCartProducts.get(position).setSelectedAmount(mCartProducts.get(position).getSelectedAmount() - 1);

        Stylish.getSQLiteHelper().updateAmount(mCartProducts.get(position));

        mCartView.updateItemAmount(position, mCartProducts.get(position).getSelectedAmount());

    }

    @Override
    public void clickCartItemRemove(int position) {

        Stylish.getSQLiteHelper().removeProduct(mCartProducts.get(position));

        mCartView.removeItem(position);
    }

    @Override
    public void updateCartBadge() {}

    @Override
    public void openPayment() {}

    @Override
    public void start() {}
}
