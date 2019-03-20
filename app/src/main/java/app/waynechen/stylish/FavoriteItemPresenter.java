package app.waynechen.stylish;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import app.waynechen.stylish.data.Favorite;
import app.waynechen.stylish.data.Product;
import app.waynechen.stylish.data.ProductForGson;
import app.waynechen.stylish.data.source.StylishDataSource;
import app.waynechen.stylish.data.source.StylishRepository;
import app.waynechen.stylish.util.UserManager;

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
    public void result(int requestCode, int resultCode) {
    }

    @Override
    public void start() {
    }

    @Override
    public void loadProductsData() {

        if (!isLoadingData()) {
            setLoadingData(true);
            mStylishRepository.getFavoriteList(UserManager.getInstance().getUserToken(), new StylishDataSource.FavoriteListCallback() {
                @Override
                public void onCompleted(Favorite bean) {
                    ArrayList<ProductForGson> products = new ArrayList<ProductForGson>();
                    String[] ids = bean.getData().getIds();
                    for (int i = 0; i < ids.length; i++) {
                        String id = ids[i];
                        mStylishRepository.getFavoriteItem(id, new StylishDataSource.FavoriteItemCallback() {
                            @Override
                            public void onCompleted(ProductForGson bean) {
                                products.add(bean);
                            }

                            @Override
                            public void onError(String errorMessage) {

                            }
                        });
                    }

                    setLoadingData(false);
                    setProductsData(products);
                }

                @Override
                public void onError(String errorMessage) {

                }
            });

        }

//        if (!isLoadingData() && mCatalogItemView.hasNextPaging()) {

//            setLoadingData(true);
//            mStylishRepository.getProductList(WOMEN, mCatalogItemView.getPaging(),
//                    new StylishDataSource.GetProductListCallback() {
//                        @Override
//                        public void onCompleted(GetProductList bean) {
//                            setLoadingData(false);
//                            setWomenProductsData(bean);
//                        }
//
//                        @Override
//                        public void onError(String errorMessage) {
//                            setLoadingData(false);
//                        }
//                    });
//    }

    }

    @Override
    public void setProductsData(ArrayList<ProductForGson> bean) {
        mCatalogItemView.showProductsUi(bean);
    }

    @Override
    public FavoriteFragment findFavorite() {
        return null;
    }

    @Override
    public boolean isCatalogItemHasNextPaging(String itemType) {
        return mCatalogItemView.hasNextPaging();
    }

    @Override
    public void onCatalogItemScrollToBottom(String itemType) {
    }

    @Override
    public void openDetail(ProductForGson product) {

    }

    private boolean isLoadingData() {
        return mLoadingData;
    }

    private void setLoadingData(boolean loadingData) {
        mLoadingData = loadingData;
    }
}
