package app.waynechen.stylish.catalog;

import static com.google.common.base.Preconditions.checkNotNull;

import app.waynechen.stylish.catalog.item.CatalogItemFragment;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class CatalogPresenter implements CatalogContract.Presenter {

    private final CatalogContract.View mCatalogView;

    public CatalogPresenter(CatalogContract.View catalogView) {
        mCatalogView = checkNotNull(catalogView, "mCatalogView cannot be null!");
        mCatalogView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {}

    @Override
    public CatalogItemFragment findWomen() {
        return null;
    }

    @Override
    public CatalogItemFragment findMen() {
        return null;
    }

    @Override
    public CatalogItemFragment findAccessories() {
        return null;
    }

    @Override
    public void start() {}
}
