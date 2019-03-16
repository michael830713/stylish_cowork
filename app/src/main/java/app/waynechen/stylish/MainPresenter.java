/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.waynechen.stylish;

import static com.google.common.base.Preconditions.checkNotNull;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import app.waynechen.stylish.cart.CartContract;
import app.waynechen.stylish.cart.CartPresenter;
import app.waynechen.stylish.catalog.CatalogContract;
import app.waynechen.stylish.catalog.CatalogPresenter;
import app.waynechen.stylish.catalog.item.CatalogItemContract;
import app.waynechen.stylish.catalog.item.CatalogItemFragment;
import app.waynechen.stylish.catalog.item.CatalogItemPresenter;
import app.waynechen.stylish.data.Color;
import app.waynechen.stylish.data.Product;
import app.waynechen.stylish.data.Variant;
import app.waynechen.stylish.data.source.StylishRepository;
import app.waynechen.stylish.data.source.bean.GetMarketingHots;
import app.waynechen.stylish.data.source.bean.GetProductList;
import app.waynechen.stylish.detail.DetailContract;
import app.waynechen.stylish.detail.DetailPresenter;
import app.waynechen.stylish.detail.add2cart.Add2CartContract;
import app.waynechen.stylish.detail.add2cart.Add2CartPresenter;
import app.waynechen.stylish.dialog.LoginDialog;
import app.waynechen.stylish.dialog.MessageDialog;
import app.waynechen.stylish.hots.HotsContract;
import app.waynechen.stylish.hots.HotsPresenter;
import app.waynechen.stylish.payment.PaymentContract;
import app.waynechen.stylish.payment.PaymentPresenter;
import app.waynechen.stylish.payment.checkout.CheckOutSuccessContract;
import app.waynechen.stylish.payment.checkout.CheckOutSuccessPresenter;
import app.waynechen.stylish.profile.ProfileContract;
import app.waynechen.stylish.profile.ProfilePresenter;
import app.waynechen.stylish.util.UserManager;

/**
 * Created by Wayne Chen on Feb. 2019.
 *
 * Presenter for the tablet screen that can act as a Tasks Presenter and a Task Detail Presenter.
 */
public class MainPresenter implements MainContract.Presenter, HotsContract.Presenter,
        CatalogContract.Presenter, CatalogItemContract.Presenter,
            ProfileContract.Presenter, DetailContract.Presenter, Add2CartContract.Presenter,
        CartContract.Presenter, PaymentContract.Presenter, CheckOutSuccessContract.Presenter {

    private final StylishRepository mStylishRepository;
    private MainContract.View mMainView;

    private HotsPresenter mHotsPresenter;
    private CatalogPresenter mCatalogPresenter;

    private CatalogItemPresenter mCatalogWomenPresenter;
    private CatalogItemPresenter mCatalogMenPresenter;
    private CatalogItemPresenter mCatalogAccessoriesPresenter;

    private ProfilePresenter mProfilePresenter;
    private DetailPresenter mDetailPresenter;
    private Add2CartPresenter mAdd2CartPresenter;

    private CartPresenter mCartPresenter;
    private PaymentPresenter mPaymentPresenter;
    private CheckOutSuccessPresenter mCheckOutSuccessPresenter;

    public MainPresenter(
            @NonNull StylishRepository stylishRepository,
            @NonNull MainContract.View mainView) {
        mStylishRepository = checkNotNull(stylishRepository, "stylishRepository cannot be null!");
        mMainView = checkNotNull(mainView, "mainView cannot be null!");
        mMainView.setPresenter(this);
    }

    void setHotsPresenter(HotsPresenter hotsPresenter) {
        mHotsPresenter = checkNotNull(hotsPresenter);
    }

    void setCatalogPresenter(CatalogPresenter catalogPresenter) {
        mCatalogPresenter = checkNotNull(catalogPresenter);
    }

    void setCatalogWomenPresenter(CatalogItemPresenter catalogWomenPresenter) {
        mCatalogWomenPresenter = checkNotNull(catalogWomenPresenter);
    }

    void setCatalogMenPresenter(CatalogItemPresenter catalogMenPresenter) {
        mCatalogMenPresenter = checkNotNull(catalogMenPresenter);
    }

    void setCatalogAccessoriesPresenter(CatalogItemPresenter catalogAccessoriesPresenter) {
        mCatalogAccessoriesPresenter = checkNotNull(catalogAccessoriesPresenter);
    }

    void setProfilePresenter(ProfilePresenter profilePresenter) {
        mProfilePresenter = checkNotNull(profilePresenter);
    }

    void setDetailPresenter(DetailPresenter detailPresenter) {
        mDetailPresenter = checkNotNull(detailPresenter);
    }

    void setAdd2CartPresenter(Add2CartPresenter add2CartPresenter) {
        mAdd2CartPresenter = checkNotNull(add2CartPresenter);
    }

    void setCartPresenter(CartPresenter cartPresenter) {
        mCartPresenter = checkNotNull(cartPresenter);
    }

    void setPaymentPresenter(PaymentPresenter paymentPresenter) {
        mPaymentPresenter = checkNotNull(paymentPresenter);
    }

    void setCheckOutSuccessPresenter(CheckOutSuccessPresenter checkOutSuccessPresenter) {
        mCheckOutSuccessPresenter = checkOutSuccessPresenter;
    }

    @Override
    public void result(int requestCode, int resultCode) {}

    @Override
    public void result(int requestCode, int resultCode, Intent data) {}

    @Override
    public void start() {}
    /* ------------------------------------------------------------------------------------------ */
    /* Payment Only */

    @Override
    public void loadPaymentProducts() {
        mPaymentPresenter.loadPaymentProducts();
    }

    @Override
    public void calculatePaymentProductsFee() {
        mPaymentPresenter.calculatePaymentProductsFee();
    }

    @Override
    public void clickPaymentCheckOut() {
        mPaymentPresenter.clickPaymentCheckOut();
    }

    @Override
    public void onPaymentCanGetPrimeChanged(boolean isCanGetPrime) {
        mPaymentPresenter.onPaymentCanGetPrimeChanged(isCanGetPrime);
    }

    @Override
    public void onPaymentPrimeSuccess(String prime) {
        mPaymentPresenter.onPaymentPrimeSuccess(prime);
    }

    @Override
    public void onPaymentPrimeFail(String errorMessage) {
        mPaymentPresenter.onPaymentPrimeFail(errorMessage);
    }

    @Override
    public void onPaymentRecipientNameChanged(String name) {
        mPaymentPresenter.onPaymentRecipientNameChanged(name);
    }

    @Override
    public void onPaymentRecipientEmailChanged(String email) {
        mPaymentPresenter.onPaymentRecipientEmailChanged(email);
    }

    @Override
    public void onPaymentRecipientPhoneChanged(String phone) {
        mPaymentPresenter.onPaymentRecipientPhoneChanged(phone);
    }

    @Override
    public void onPaymentRecipientAddressChanged(String address) {
        mPaymentPresenter.onPaymentRecipientAddressChanged(address);
    }

    @Override
    public void onPaymentShippingTimeChanged(String time) {
        mPaymentPresenter.onPaymentShippingTimeChanged(time);
    }

    @Override
    public void onPaymentSpinnerMethodsChanged(int position) {
        mPaymentPresenter.onPaymentSpinnerMethodsChanged(position);
    }

    @Override
    public void onPaymentTpdErrorMessageChanged(String errorMessage) {
        mPaymentPresenter.onPaymentTpdErrorMessageChanged(errorMessage);
    }

    @Override
    public int getPaymentSubTotal() {
        return mPaymentPresenter.getPaymentSubTotal();
    }

    @Override
    public int getPaymentFreight() {
        return mPaymentPresenter.getPaymentFreight();
    }

    @Override
    public int getPaymentTotal() {
        return mPaymentPresenter.getPaymentTotal();
    }
    /* ------------------------------------------------------------------------------------------ */
    /* Cart Only */

    @Override
    public void loadCartProductsData() {
        mCartPresenter.loadCartProductsData();
    }

    @Override
    public void clickCartItemIncrease(int position) {
        mCartPresenter.clickCartItemIncrease(position);
    }

    @Override
    public void clickCartItemDecrease(int position) {
        mCartPresenter.clickCartItemDecrease(position);
    }

    @Override
    public void clickCartItemRemove(int position) {
        mCartPresenter.clickCartItemRemove(position);
    }
    /* ------------------------------------------------------------------------------------------ */
    /* Add2Cart Only */

    /**
     * Load Add2Cart Product
     */
    @Override
    public void loadAdd2CartProductData() {
        mAdd2CartPresenter.loadAdd2CartProductData();
    }

    /**
     * Set Add2Cart Product
     * @param product
     */
    @Override
    public void setAdd2CartProductData(Product product) {
        mAdd2CartPresenter.setAdd2CartProductData(product);
    }

    @Override
    public void selectAdd2CartColor(Color color) {
        mAdd2CartPresenter.selectAdd2CartColor(color);
    }

    @Override
    public void selectAdd2CartVariant(Variant variant) {
        mAdd2CartPresenter.selectAdd2CartVariant(variant);
    }

    @Override
    public void clickAdd2CartIncrease() {
        mAdd2CartPresenter.clickAdd2CartIncrease();
    }

    @Override
    public void clickAdd2CartDecrease() {
        mAdd2CartPresenter.clickAdd2CartDecrease();
    }

    @Override
    public void initialAdd2CartEditor() {
        mAdd2CartPresenter.initialAdd2CartEditor();
    }

    @Override
    public void onAdd2CartEditorTextChange(CharSequence charSequence) {
        mAdd2CartPresenter.onAdd2CartEditorTextChange(charSequence);
    }

    @Override
    public void addProductToCart() {
        mAdd2CartPresenter.addProductToCart();
    }
    /* ------------------------------------------------------------------------------------------ */
    /* Detail Only */

    /**
     * Load Detail Product
     */
    @Override
    public void loadDetailProductData() {
        mDetailPresenter.loadDetailProductData();
    }

    /**
     * Set Detail Product
     * @param product
     */
    @Override
    public void setDetailProductData(Product product) {
        mDetailPresenter.setDetailProductData(product);
    }
    /* ------------------------------------------------------------------------------------------ */
    /* View Paging Use Only */

    @Override
    public CatalogItemFragment findWomen() {
        return mMainView.findWomenView();
    }

    @Override
    public CatalogItemFragment findMen() {
        return mMainView.findMenView();
    }

    @Override
    public CatalogItemFragment findAccessories() {
        return mMainView.findAccessoriesView();
    }
    /* ------------------------------------------------------------------------------------------ */
    /* General */

    @Override
    public void hideToolbarAndBottomNavigation() {
        mMainView.hideToolbarUi();
        mMainView.hideBottomNavigationUi();
    }

    @Override
    public void showToolbarAndBottomNavigation() {
        mMainView.showToolbarUi();
        mMainView.showBottomNavigationUi();
    }

    @Override
    public void hideBottomNavigation() {
        mMainView.hideBottomNavigationUi();
    }

    @Override
    public void showBottomNavigation() {
        mMainView.showBottomNavigationUi();
    }

    @Override
    public void updateCartBadge() {
        mMainView.updateCartBadgeUi(Stylish.getSQLiteHelper().getCartProducts().size());
    }

    @Override
    public void updateToolbar(String title) {
        mMainView.setToolbarTitleUi(title);
    }

    @Override
    public void onLoginSuccess(int loginFrom) {
        if (loginFrom == LoginDialog.FROM_PROFILE) {

            switchToProfileByBottomNavigation();
        } else if (loginFrom == LoginDialog.FROM_PAYMENT) {

            mPaymentPresenter.onPaymentLoginSuccess();
        } else if (loginFrom == LoginDialog.FROM_DRAWER) {
            /* Do Nothing */
        } else if (loginFrom == LoginDialog.FROM_EVERYWHERE) {
            /* Do Nothing */
        }
    }

    @Override
    public void onCheckOutSuccess() {
        switchToHotsByBottomNavigation();
    }

    @Override
    public void finishDetail() {
        mMainView.finishDetailUi();
    }

    @Override
    public void finishPayment() {
        mMainView.finishPaymentUi();
    }

    @Override
    public boolean isPaymentViewActive() {
        return mPaymentPresenter.isPaymentViewActive();
    }

    @Override
    public void onPaymentLoginSuccess() {
        mPaymentPresenter.onPaymentLoginSuccess();
    }
    /* ------------------------------------------------------------------------------------------ */
    /* Hots Only */

    /**
     * Load Hots
     */
    @Override
    public void loadHotsData() {
        if (mHotsPresenter != null) {
            mHotsPresenter.loadHotsData();
        }
    }

    /**
     * Set Hots
     * @param bean
     */
    @Override
    public void setHotsData(GetMarketingHots bean) {
        mHotsPresenter.setHotsData(bean);
    }
    /* ------------------------------------------------------------------------------------------ */
    /* Catalog Item Only */

    /**
     * Load Women Products
     */
    @Override
    public void loadWomenProductsData() {

        if (mCatalogWomenPresenter != null) {
            mCatalogWomenPresenter.loadWomenProductsData();
        }
    }

    /**
     * Set women Products
     */
    @Override
    public void setWomenProductsData(GetProductList bean) {

        if (mCatalogWomenPresenter != null) {
            mCatalogWomenPresenter.setWomenProductsData(bean);
        }
    }

    /**
     * Load Men Products
     */
    @Override
    public void loadMenProductsData() {

        if (mCatalogMenPresenter != null) {
            mCatalogMenPresenter.loadMenProductsData();
        }
    }

    /**
     * Set Men Products
     */
    @Override
    public void setMenProductsData(GetProductList bean) {

        if (mCatalogMenPresenter != null) {
            mCatalogMenPresenter.setMenProductsData(bean);
        }
    }

    /**
     * Load Accessories Products
     */
    @Override
    public void loadAccessoriesProductsData() {

        if (mCatalogAccessoriesPresenter != null) {
            mCatalogAccessoriesPresenter.loadAccessoriesProductsData();
        }
    }

    /**
     * Set Accessories Products
     * @param bean
     */
    @Override
    public void setAccessoriesProductsData(GetProductList bean) {
        mCatalogAccessoriesPresenter.setAccessoriesProductsData(bean);
    }

    /**
     * Check that CatalogItem has next paging.
     * @param itemType
     * @return
     */
    @Override
    public boolean isCatalogItemHasNextPaging(@MainMvpController.CatalogItem String itemType) {
        switch (itemType) {
            case MainMvpController.WOMEN:
                return mCatalogWomenPresenter.isCatalogItemHasNextPaging(itemType);
            case MainMvpController.MEN:
                return mCatalogMenPresenter.isCatalogItemHasNextPaging(itemType);
            case MainMvpController.ACCESSORIES:
                return mCatalogAccessoriesPresenter.isCatalogItemHasNextPaging(itemType);
            default:
                return false;
        }
    }

    /**
     * When CatalogItem scroll to bottom.
     * @param itemType
     */
    @Override
    public void onCatalogItemScrollToBottom(String itemType) {
        switch (itemType) {
            case MainMvpController.WOMEN:
                mCatalogWomenPresenter.onCatalogItemScrollToBottom(itemType);
                break;
            case MainMvpController.MEN:
                mCatalogMenPresenter.onCatalogItemScrollToBottom(itemType);
                break;
            case MainMvpController.ACCESSORIES:
                mCatalogAccessoriesPresenter.onCatalogItemScrollToBottom(itemType);
                break;
            default:
        }
    }
    /* ------------------------------------------------------------------------------------------ */
    /* Profile Only */

    /**
     * Load User Data for Profile
     */
    @Override
    public void loadProfileUserData() {
        mProfilePresenter.loadProfileUserData();
    }

    /**
     * Check User Data exists
     */
    @Override
    public void checkProfileUserData() {
        mProfilePresenter.checkProfileUserData();
    }
    /* ------------------------------------------------------------------------------------------ */
    /* Switch Pages */

    /**
     * Open Detail
     * @param product
     */
    @Override
    public void openDetail(@NonNull Product product) {
        mMainView.openDetailUi(product);
    }

    /**
     * Open Hots
     */
    @Override
    public void openHots() {
        mMainView.openHotsUi();
    }

    /**
     * Open Catalog
     */
    @Override
    public void openCatalog() {
        mMainView.openCatalogUi();
    }

    /**
     * Open Profile
     * @return: it for BottomNavigation
     */
    @Override
    public boolean openProfile() {
        if (UserManager.getInstance().isLoggedIn()) {
            mMainView.openProfileUi();
            return true;
        } else {
            mMainView.openLoginUi(LoginDialog.FROM_PROFILE);
            return false;
        }
    }

    /**
     * Open Cart
     */
    @Override
    public void openCart() {
        mMainView.openCartUi();
        /* Query data from database whenever switch to Cart */
        loadCartProductsData();
    }

    /**
     * Open Payment
     */
    @Override
    public void openPayment() {
        mMainView.openPaymentUi();
    }

    /**
     * Switch to Profile by BottomNavigation.setSelectedItemId.
     */
    @Override
    public void switchToProfileByBottomNavigation() {
        mMainView.switchProfileUiInitiative();
    }

    @Override
    public void switchToHotsByBottomNavigation() {
        mMainView.switchHotsUiInitiative();
    }


    @Override
    public void showLoginDialog(int loginFrom) {
        mMainView.openLoginUi(loginFrom);
    }

    @Override
    public void setGalleryImagePicked(Uri imageUri) {

    }

    @Override
    public void showAdd2CartDialog(Product product) {
        mMainView.openAdd2CartUi(product);
    }

    @Override
    public void showCheckOutSuccessDialog() {
        mMainView.openCheckOutSuccessUi();
    }

    @Override
    public void showAddToSuccessDialog() {
        mMainView.showMessageDialogUi(MessageDialog.ADDTO_SUCCESS);
    }

    @Override
    public void showLoginSuccessDialog() {
        mMainView.showMessageDialogUi(MessageDialog.LOGIN_SUCCESS);
    }

    @Override
    public void showToast(String message) {
        mMainView.showToastUi(message);
    }
    /* ------------------------------------------------------------------------------------------ */
    /* Drawer */

    @Override
    public void onDrawerOpened() {
        if (!UserManager.getInstance().isLoggedIn()) {

            mMainView.closeDrawerUi();
            showLoginDialog(LoginDialog.FROM_DRAWER);

        } else if (!UserManager.getInstance().hasUserInfo()) {

            UserManager.getInstance().getUserProfile(new UserManager.LoadCallback() {
                @Override
                public void onSuccess() {

                    mMainView.showDrawerUserUi();
                }

                @Override
                public void onFail(String errorMessage) {}

                @Override
                public void onInvalidToken(String errorMessage) {
                    showLoginDialog(LoginDialog.FROM_DRAWER);
                }
            });
        } else {

            mMainView.showDrawerUserUi();
        }
    }

    @Override
    public void onClickDrawerAvatar() {
        UserManager.getInstance().challenge();
    }

    @Override
    public void onGalleryImagePicked(Uri imageUri) {
        mProfilePresenter.setGalleryImagePicked(imageUri);
    }
}
