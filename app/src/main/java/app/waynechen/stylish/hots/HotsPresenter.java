package app.waynechen.stylish.hots;

import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;
import android.util.Log;

import app.waynechen.stylish.data.Product;
import app.waynechen.stylish.data.source.StylishDataSource;
import app.waynechen.stylish.data.source.StylishRepository;
import app.waynechen.stylish.data.source.bean.GetMarketingHots;
import app.waynechen.stylish.util.Constants;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class HotsPresenter implements HotsContract.Presenter {

    private final StylishRepository mStylishRepository;
    private final HotsContract.View mHotsView;

    public HotsPresenter(
            @NonNull StylishRepository stylishRepository,
            @NonNull HotsContract.View hotsView) {
        mStylishRepository = checkNotNull(stylishRepository, "stylishRepository cannot be null!");
        mHotsView = checkNotNull(hotsView, "hotsView cannot be null!");
        mHotsView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {}

    @Override
    public void start() {}

    @Override
    public void loadHotsData() {

        mStylishRepository.getHotsList(new StylishDataSource.GetHotsListCallback() {
            @Override
            public void onCompleted(GetMarketingHots bean) {

                setHotsData(bean);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void setHotsData(GetMarketingHots bean) {
        mHotsView.showHotsUi(bean);
    }

    @Override
    public void openDetail(Product product) {}
}
