package app.waynechen.stylish.detail;

import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;

import app.waynechen.stylish.data.Product;
import app.waynechen.stylish.data.source.StylishRepository;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class DetailPresenter implements DetailContract.Presenter {

    private final StylishRepository mStylishRepository;
    private final DetailContract.View mDetailView;

    private Product mProduct;

    public DetailPresenter(
            @NonNull StylishRepository stylishRepository,
            @NonNull DetailContract.View detailView) {
        mStylishRepository = checkNotNull(stylishRepository, "stylishRepository cannot be null!");
        mDetailView = checkNotNull(detailView, "detailView cannot be null!");
        mDetailView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {}

    @Override
    public void loadDetailProductData() {
        mDetailView.showDetailUi(mProduct);
    }

    @Override
    public void setDetailProductData(Product product) {
        mProduct = product;
    }

    @Override
    public void hideToolbarAndBottomNavigation() {}

    @Override
    public void showToolbarAndBottomNavigation() {}

    @Override
    public void showAdd2CartDialog(Product product) {}

    @Override
    public void updateCartBadge() {}

    @Override
    public void finishDetail() {}

    @Override
    public void start() {}

}
