package app.waynechen.stylish;

import android.support.annotation.NonNull;

import app.waynechen.stylish.catalog.item.CatalogItemContract;
import app.waynechen.stylish.data.Product;
import app.waynechen.stylish.data.source.StylishDataSource;
import app.waynechen.stylish.data.source.StylishRepository;
import app.waynechen.stylish.data.source.bean.GetProductList;

import static app.waynechen.stylish.MainMvpController.ACCESSORIES;
import static app.waynechen.stylish.MainMvpController.MEN;
import static app.waynechen.stylish.MainMvpController.WOMEN;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class FavoriteItemPresenter implements FavoriteItemContract.Presenter {

    private final StylishRepository mStylishRepository;
    private final FavoriteItemContract.View mCatalogItemView;

    private boolean mLoadingData;

    public FavoriteItemPresenter(
            @NonNull StylishRepository stylishRepository,
            @NonNull FavoriteItemContract.View catalogItemView) {
        mStylishRepository = checkNotNull(stylishRepository, "stylishRepository cannot be null!");
        mCatalogItemView = checkNotNull(catalogItemView, "catalogItemView cannot be null!");

        mCatalogItemView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {}

    @Override
    public void start() {}

    @Override
    public void loadWomenProductsData() {

        if (!isLoadingData() && mCatalogItemView.hasNextPaging()) {
            setLoadingData(true);
            mStylishRepository.getProductList(WOMEN, mCatalogItemView.getPaging(),
                    new StylishDataSource.GetProductListCallback() {
                        @Override
                        public void onCompleted(GetProductList bean) {
                            setLoadingData(false);
                            setWomenProductsData(bean);
                        }

                        @Override
                        public void onError(String errorMessage) {
                            setLoadingData(false);
                        }
                    });
        }
    }

    @Override
    public void setWomenProductsData(GetProductList bean) {
        mCatalogItemView.showProductsUi(bean);
    }

    @Override
    public void loadMenProductsData() {

        if (!isLoadingData() && mCatalogItemView.hasNextPaging()) {
            setLoadingData(true);
            mStylishRepository.getProductList(MEN, mCatalogItemView.getPaging(),
                    new StylishDataSource.GetProductListCallback() {
                        @Override
                        public void onCompleted(GetProductList bean) {

                            setLoadingData(false);
                            setMenProductsData(bean);
                        }

                        @Override
                        public void onError(String errorMessage) {
                            setLoadingData(false);
                        }
                    });
        }
    }

    @Override
    public void setMenProductsData(GetProductList bean) {
        mCatalogItemView.showProductsUi(bean);
    }

    @Override
    public void loadAccessoriesProductsData() {

        if (!isLoadingData() && mCatalogItemView.hasNextPaging()) {
            setLoadingData(true);
            mStylishRepository.getProductList(ACCESSORIES, mCatalogItemView.getPaging(),
                    new StylishDataSource.GetProductListCallback() {
                        @Override
                        public void onCompleted(GetProductList bean) {

                            setLoadingData(false);
                            setAccessoriesProductsData(bean);
                        }

                        @Override
                        public void onError(String errorMessage) {
                            setLoadingData(false);
                        }
                    });
        }
    }

    @Override
    public void setAccessoriesProductsData(GetProductList bean) {
        mCatalogItemView.showProductsUi(bean);
    }

    @Override
    public boolean isCatalogItemHasNextPaging(String itemType) {
        return mCatalogItemView.hasNextPaging();
    }

    @Override
    public void onCatalogItemScrollToBottom(String itemType) {
        switch (itemType) {
            case WOMEN:
                loadWomenProductsData();
                break;
            case MEN:
                loadMenProductsData();
                break;
            case ACCESSORIES:
                loadAccessoriesProductsData();
                break;
            default:
        }
    }

    @Override
    public void openDetail(Product product) {

    }

    private boolean isLoadingData() {
        return mLoadingData;
    }

    private void setLoadingData(boolean loadingData) {
        mLoadingData = loadingData;
    }
}
