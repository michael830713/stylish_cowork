package app.waynechen.stylish.payment.checkout;

import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class CheckOutSuccessPresenter implements CheckOutSuccessContract.Presenter {

    private final CheckOutSuccessContract.View mCheckOutSuccessView;

    public CheckOutSuccessPresenter(@NonNull CheckOutSuccessContract.View checkOutSuccessView) {
        mCheckOutSuccessView = checkNotNull(checkOutSuccessView, "checkOutSuccessView cannot be null!");
        mCheckOutSuccessView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {}

    @Override
    public void hideToolbarAndBottomNavigation() {}

    @Override
    public void showToolbarAndBottomNavigation() {}

    @Override
    public void onCheckOutSuccess() {}

    @Override
    public void start() {}

}
