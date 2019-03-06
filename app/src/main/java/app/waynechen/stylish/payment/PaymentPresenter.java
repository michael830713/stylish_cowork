package app.waynechen.stylish.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;
import android.util.Log;

import app.waynechen.stylish.R;
import app.waynechen.stylish.Stylish;
import app.waynechen.stylish.data.CartProduct;
import app.waynechen.stylish.data.CheckOutInfo;
import app.waynechen.stylish.data.source.StylishDataSource;
import app.waynechen.stylish.data.source.StylishRepository;
import app.waynechen.stylish.util.Constants;
import app.waynechen.stylish.util.UserManager;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class PaymentPresenter implements PaymentContract.Presenter {

    private final StylishRepository mStylishRepository;
    private final PaymentContract.View mPaymentView;

    private CheckOutInfo mCheckOutInfo;
    private boolean inCanGetPrime = false;
    private String mTpdErrorMessage = "";

    public PaymentPresenter(
            @NonNull StylishRepository stylishRepository,
            @NonNull PaymentContract.View paymentView) {
        mStylishRepository = checkNotNull(stylishRepository, "stylishRepository cannot be null!");
        mPaymentView = checkNotNull(paymentView, "paymentView cannot be null!");
        mPaymentView.setPresenter(this);
        mCheckOutInfo = new CheckOutInfo();
    }

    @Override
    public void result(int requestCode, int resultCode) {}

    @Override
    public void loadPaymentProducts() {
        mCheckOutInfo.setCartProducts(Stylish.getSQLiteHelper().getCartProducts());
        calculatePaymentProductsFee();
        mPaymentView.showPaymentUi(mCheckOutInfo.getCartProducts());
    }

    @Override
    public void calculatePaymentProductsFee() {
        int totalPrice = 0;
        for (CartProduct cartProduct : mCheckOutInfo.getCartProducts()) {
            totalPrice += (cartProduct.getPrice() * cartProduct.getSelectedAmount());
        }
        mCheckOutInfo.setSubTotal(totalPrice);
        mCheckOutInfo.setFreight(0);
    }

    @Override
    public void clickPaymentCheckOut() {

        if ("".equals(mCheckOutInfo.getName())) {

            mPaymentView.showErrorNameUi();

        } else if ("".equals(mCheckOutInfo.getEmail())) {

            mPaymentView.showErrorEmailUi();

        } else if ("".equals(mCheckOutInfo.getPhone())) {

            mPaymentView.showErrorPhoneUi();

        } else if ("".equals(mCheckOutInfo.getAddress())) {

            mPaymentView.showErrorAddressUi();

        } else if ("".equals(mCheckOutInfo.getTime())) {

            mPaymentView.showErrorShippingTimeUi();

        } else if (!mCheckOutInfo.isPayByCreditCard()) {

            mPaymentView.showToastUi(Stylish.getAppContext().getString(R.string.no_pay_by_cash));

        } else if (!isInCanGetPrime()) {

            mPaymentView.showToastUi(mTpdErrorMessage);

        } else {

            if (UserManager.getInstance().isLoggedIn()) {

                mPaymentView.showLoadingUi(true);
                mPaymentView.getPrime();
            } else {

                mPaymentView.showLoginUi();
            }
        }
    }

    @Override
    public void onPaymentCanGetPrimeChanged(boolean isCanGetPrime) {
        setInCanGetPrime(isCanGetPrime);
    }

    @Override
    public void onPaymentPrimeSuccess(String prime) {
        mCheckOutInfo.setPrime(prime);

        mStylishRepository.postOrderCheckOutCallback(UserManager.getInstance().getUserToken(), mCheckOutInfo,
                new StylishDataSource.OrderCheckOutCallback() {
                    @Override
                    public void onCompleted(String number) {
                        mPaymentView.showLoadingUi(false);
                        Stylish.getSQLiteHelper().cleanProducts();
                        mPaymentView.showCheckOutSuccessUi();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        mPaymentView.showLoadingUi(false);
                    }

                    @Override
                    public void onInvalidToken(String errorMessage) {
                        UserManager.getInstance().clearUserLogin();
                        mPaymentView.showLoginUi();
                    }
                });
    }

    @Override
    public void onPaymentPrimeFail(String errorMessage) {
        mPaymentView.showLoadingUi(false);
        mPaymentView.showToastUi(mTpdErrorMessage);
    }

    @Override
    public void onPaymentRecipientNameChanged(String name) {
        mCheckOutInfo.setName(name);
    }

    @Override
    public void onPaymentRecipientEmailChanged(String email) {
        mCheckOutInfo.setEmail(email);
    }

    @Override
    public void onPaymentRecipientPhoneChanged(String phone) {
        mCheckOutInfo.setPhone(phone);
    }

    @Override
    public void onPaymentRecipientAddressChanged(String address) {
        mCheckOutInfo.setAddress(address);
    }

    @Override
    public void onPaymentShippingTimeChanged(String time) {
        mCheckOutInfo.setTime(time);
    }

    @Override
    public void onPaymentSpinnerMethodsChanged(int position) {
        mCheckOutInfo.setPayByCreditCard((position == 1));
    }

    @Override
    public void onPaymentTpdErrorMessageChanged(String errorMessage) {
        mTpdErrorMessage = errorMessage;
    }

    @Override
    public int getPaymentSubTotal() {
        return mCheckOutInfo.getSubTotal();
    }

    @Override
    public int getPaymentFreight() {
        return mCheckOutInfo.getFreight();
    }

    @Override
    public int getPaymentTotal() {
        return mCheckOutInfo.getTotal();
    }

    @Override
    public void updateToolbar(String title) {}

    @Override
    public void hideBottomNavigation() {}

    @Override
    public void showBottomNavigation() {}

    @Override
    public void showCheckOutSuccessDialog() {}

    @Override
    public void finishPayment() {}

    @Override
    public void showLoginDialog(int loginFrom) {}

    @Override
    public boolean isPaymentViewActive() {
        return mPaymentView.isActive();
    }

    @Override
    public void onPaymentLoginSuccess() {
        mPaymentView.showLoadingUi(true);
        mPaymentView.getPrime();
    }

    @Override
    public void updateCartBadge() {}

    @Override
    public void showToast(String message) {}

    public void setInCanGetPrime(boolean inCanGetPrime) {
        this.inCanGetPrime = inCanGetPrime;
    }

    public boolean isInCanGetPrime() {
        return inCanGetPrime;
    }

    @Override
    public void start() {}
}
