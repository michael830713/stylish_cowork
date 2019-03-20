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

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import app.waynechen.stylish.cart.CartFragment;
import app.waynechen.stylish.cart.CartPresenter;
import app.waynechen.stylish.catalog.CatalogFragment;
import app.waynechen.stylish.catalog.CatalogPresenter;
import app.waynechen.stylish.catalog.item.CatalogItemFragment;
import app.waynechen.stylish.catalog.item.CatalogItemPresenter;
import app.waynechen.stylish.data.Product;
import app.waynechen.stylish.data.source.StylishRepository;
import app.waynechen.stylish.data.source.local.StylishLocalDataSource;
import app.waynechen.stylish.data.source.remote.StylishRemoteDataSource;
import app.waynechen.stylish.detail.DetailFragment;
import app.waynechen.stylish.detail.DetailPresenter;
import app.waynechen.stylish.detail.add2cart.Add2CartDialog;
import app.waynechen.stylish.detail.add2cart.Add2CartPresenter;
import app.waynechen.stylish.hots.HotsFragment;
import app.waynechen.stylish.hots.HotsPresenter;
import app.waynechen.stylish.payment.PaymentFragment;
import app.waynechen.stylish.payment.PaymentPresenter;
import app.waynechen.stylish.payment.checkout.CheckOutSuccessDialog;
import app.waynechen.stylish.payment.checkout.CheckOutSuccessPresenter;
import app.waynechen.stylish.profile.ProfileFragment;
import app.waynechen.stylish.profile.ProfilePresenter;
import app.waynechen.stylish.util.ActivityUtils;
import app.waynechen.stylish.util.Constants;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Wayne Chen on Feb. 2019.
 * <p>
 * Class that creates fragments (MVP views) and makes the necessary connections between them.
 */
public class MainMvpController {

    private final FragmentActivity mActivity;
    private MainPresenter mMainPresenter;

    private HotsPresenter mHotsPresenter;
    private CatalogPresenter mCatalogPresenter;
    private CartPresenter mCartPresenter;
    private ProfilePresenter mProfilePresenter;

    private CatalogItemPresenter mCatalogWomenPresenter;
    private CatalogItemPresenter mCatalogMenPresenter;
    private CatalogItemPresenter mCatalogAccessoriesPresenter;
    private FavoriteItemPresenter mFavoriteItemPresenter;

    private DetailPresenter mDetailPresenter;
    private Add2CartPresenter mAdd2CartPresenter;

    private PaymentPresenter mPaymentPresenter;
    private CheckOutSuccessPresenter mCheckOutSuccessPresenter;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            HOTS, CATALOG, CART, PROFILE, DETAIL, PAYMENT, FAVORITE
    })
    public @interface FragmentType {
    }

    static final String HOTS = "HOTS";
    static final String CATALOG = "CATALOG";
    static final String CART = "CART";
    static final String PROFILE = "PROFILE";
    static final String DETAIL = "DETAIL";
    static final String PAYMENT = "PAYMENT";
    static final String FAVORITE = "FAVORITE";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            WOMEN, MEN, ACCESSORIES
    })
    public @interface CatalogItem {
    }

    public static final String WOMEN = "WOMEN";
    public static final String MEN = "MEN";
    public static final String ACCESSORIES = "ACCESSORIES";

    private MainMvpController(@NonNull FragmentActivity activity) {
        mActivity = activity;
    }

    /**
     * Creates a controller.
     *
     * @param activity the context activity
     * @return a MainMvpController
     */
    static MainMvpController create(@NonNull FragmentActivity activity) {
        checkNotNull(activity);
        MainMvpController mainMvpController = new MainMvpController(activity);
        mainMvpController.createMainPresenter();
        return mainMvpController;
    }

    /**
     * Hots View
     */
    void findOrCreateHotsView() {

        HotsFragment hotsFragment = findOrCreateHotsFragment();

        if (mHotsPresenter == null) {
            mHotsPresenter = new HotsPresenter(StylishRepository.getInstance(
                    StylishRemoteDataSource.getInstance(),
                    StylishLocalDataSource.getInstance()), hotsFragment);
            mMainPresenter.setHotsPresenter(mHotsPresenter);
            hotsFragment.setPresenter(mMainPresenter);
        }
    }

    /**
     * Catalog View
     */
    void findOrCreateCatalogView() {

        CatalogFragment catalogFragment = findOrCreateCatalogFragment();

        if (mCatalogPresenter == null) {
            mCatalogPresenter = new CatalogPresenter(catalogFragment);
            mMainPresenter.setCatalogPresenter(mCatalogPresenter);
            catalogFragment.setPresenter(mMainPresenter);
        }
    }

    /**
     * Profile View
     */
    void findOrCreateProfileView() {

        ProfileFragment profileFragment = findOrCreateProfileFragment();

        if (mProfilePresenter == null) {
            mProfilePresenter = new ProfilePresenter(StylishRepository.getInstance(
                    StylishRemoteDataSource.getInstance(),
                    StylishLocalDataSource.getInstance()), profileFragment);
            mMainPresenter.setProfilePresenter(mProfilePresenter);
            profileFragment.setPresenter(mMainPresenter);
        }
    }

    /**
     * Cart View
     */
    void findOrCreateCartView() {

        CartFragment cartFragment = findOrCreateCartFragment();

        if (mCartPresenter == null) {
            mCartPresenter = new CartPresenter(StylishRepository.getInstance(
                    StylishRemoteDataSource.getInstance(),
                    StylishLocalDataSource.getInstance()), cartFragment);
            mMainPresenter.setCartPresenter(mCartPresenter);
            cartFragment.setPresenter(mMainPresenter);
        }
    }

    /**
     * Payment View
     */
    void findOrCreatePaymentView() {

        PaymentFragment paymentFragment = findOrCreatePaymentFragment();

        mPaymentPresenter = new PaymentPresenter(StylishRepository.getInstance(
                StylishRemoteDataSource.getInstance(),
                StylishLocalDataSource.getInstance()), paymentFragment);
        mMainPresenter.setPaymentPresenter(mPaymentPresenter);
        paymentFragment.setPresenter(mMainPresenter);
    }

    /**
     * Detail View
     */
    void findOrCreateDetailView(Product product) {

        DetailFragment detailFragment = createDetailFragment();

        mDetailPresenter = new DetailPresenter(StylishRepository.getInstance(
                StylishRemoteDataSource.getInstance(),
                StylishLocalDataSource.getInstance()), detailFragment);
        mDetailPresenter.setDetailProductData(product);
        mMainPresenter.setDetailPresenter(mDetailPresenter);
        detailFragment.setPresenter(mMainPresenter);
    }

    /**
     * Detail View
     */
    void findOrCreateFavoriteView() {

        FavoriteFragment favoriteFragment = findOrCreateFavoriteFragment();

        mFavoriteItemPresenter = new FavoriteItemPresenter(StylishRepository.getInstance(
                StylishRemoteDataSource.getInstance(),
                StylishLocalDataSource.getInstance()), favoriteFragment);
        mMainPresenter.setFavoritePresenter(mFavoriteItemPresenter);
        favoriteFragment.setPresenter(mMainPresenter);
    }

    /**
     * Add2Cart View
     */
    void findOrCreateAdd2CartView(Product product) {

        Add2CartDialog dialog =
                (Add2CartDialog) getFragmentManager().findFragmentByTag(Constants.ADD2CART);

        if (dialog == null) {

            dialog = new Add2CartDialog();
            mAdd2CartPresenter = new Add2CartPresenter(product, dialog);
            mMainPresenter.setAdd2CartPresenter(mAdd2CartPresenter);
            dialog.setPresenter(mMainPresenter);
            dialog.show(getFragmentManager(), Constants.ADD2CART);

        } else if (!dialog.isAdded()) {

            mAdd2CartPresenter.setAdd2CartProductData(product);
            dialog.show(getFragmentManager(), Constants.ADD2CART);
        }
    }

    /**
     * CheckOutSuccess View
     */
    void findOrCreateCheckOutSuccessView() {

        CheckOutSuccessDialog dialog =
                (CheckOutSuccessDialog) getFragmentManager().findFragmentByTag(Constants.CHECKOUT_SUCCESS);

        if (dialog == null) {

            dialog = new CheckOutSuccessDialog();
            mCheckOutSuccessPresenter = new CheckOutSuccessPresenter(dialog);
            mMainPresenter.setCheckOutSuccessPresenter(mCheckOutSuccessPresenter);
            dialog.setPresenter(mMainPresenter);
            dialog.show(getFragmentManager(), Constants.CHECKOUT_SUCCESS);

        } else if (!dialog.isAdded()) {

            dialog.show(getFragmentManager(), Constants.CHECKOUT_SUCCESS);
        }
    }

    FavoriteFragment findOrCreateFavoriteFragment() {

        FavoriteFragment favoriteFragment = FavoriteFragment.newInstance();

        ActivityUtils.addFragmentByTag(
                getFragmentManager(), favoriteFragment, FAVORITE);

        return favoriteFragment;
    }

    /**
     * Women View
     *
     * @return CatalogItemFragment: Women Fragment
     */
    CatalogItemFragment findOrCreateWomenView() {

        CatalogItemFragment fragment = findOrCreateCatalogItemFragment(WOMEN);

        mCatalogWomenPresenter = new CatalogItemPresenter(StylishRepository.getInstance(
                StylishRemoteDataSource.getInstance(),
                StylishLocalDataSource.getInstance()), fragment);
        fragment.setPresenter(mMainPresenter);
        fragment.setItemType(WOMEN);
        mMainPresenter.setCatalogWomenPresenter(mCatalogWomenPresenter);

        return fragment;
    }

    /**
     * Men View
     *
     * @return CatalogItemFragment: Men Fragment
     */
    CatalogItemFragment findOrCreateMenView() {

        CatalogItemFragment fragment = findOrCreateCatalogItemFragment(MEN);

        mCatalogMenPresenter = new CatalogItemPresenter(StylishRepository.getInstance(
                StylishRemoteDataSource.getInstance(),
                StylishLocalDataSource.getInstance()), fragment);
        fragment.setPresenter(mMainPresenter);
        fragment.setItemType(MEN);
        mMainPresenter.setCatalogMenPresenter(mCatalogMenPresenter);

        return fragment;
    }

    /**
     * Accessories View
     *
     * @return CatalogItemFragment: Accessories Fragment
     */
    CatalogItemFragment findOrCreateAccessoriesView() {

        CatalogItemFragment fragment = findOrCreateCatalogItemFragment(ACCESSORIES);

        mCatalogAccessoriesPresenter = new CatalogItemPresenter(StylishRepository.getInstance(
                StylishRemoteDataSource.getInstance(),
                StylishLocalDataSource.getInstance()), fragment);
        fragment.setPresenter(mMainPresenter);
        fragment.setItemType(ACCESSORIES);
        mMainPresenter.setCatalogAccessoriesPresenter(mCatalogAccessoriesPresenter);

        return fragment;
    }

    /**
     * Hots Fragment
     *
     * @return HotsFragment
     */
    @NonNull
    private HotsFragment findOrCreateHotsFragment() {

        HotsFragment hotsFragment =
                (HotsFragment) getFragmentManager().findFragmentByTag(HOTS);
        if (hotsFragment == null) {
            // Create the fragment
            hotsFragment = HotsFragment.newInstance();
        }

        ActivityUtils.showOrAddFragmentByTag(
                getFragmentManager(), hotsFragment, HOTS);

        return hotsFragment;
    }

    /**
     * Catalog Fragment
     *
     * @return CatalogFragment
     */
    @NonNull
    private CatalogFragment findOrCreateCatalogFragment() {

        CatalogFragment catalogFragment =
                (CatalogFragment) getFragmentManager().findFragmentByTag(CATALOG);
        if (catalogFragment == null) {
            // Create the fragment
            catalogFragment = CatalogFragment.newInstance();
        }

        ActivityUtils.showOrAddFragmentByTag(
                getFragmentManager(), catalogFragment, CATALOG);

        return catalogFragment;
    }

    /**
     * Catalog Item Fragment: Women, Men, Accessories
     *
     * @param itemType: @CatalogItem
     * @return CatalogItemFragment
     */
    @NonNull
    private CatalogItemFragment findOrCreateCatalogItemFragment(@CatalogItem String itemType) {

        CatalogItemFragment fragment =
                (CatalogItemFragment) (getFragmentManager().findFragmentByTag(CATALOG))
                        .getChildFragmentManager().findFragmentByTag(itemType);
        if (fragment == null) {
            // Create the fragment
            fragment = CatalogItemFragment.newInstance();
        }

        return fragment;
    }

    /**
     * Profile Fragment
     *
     * @return ProfileFragment
     */
    @NonNull
    private ProfileFragment findOrCreateProfileFragment() {

        ProfileFragment profileFragment =
                (ProfileFragment) getFragmentManager().findFragmentByTag(PROFILE);
        if (profileFragment == null) {
            // Create the fragment
            profileFragment = ProfileFragment.newInstance();
        }

        ActivityUtils.showOrAddFragmentByTag(
                getFragmentManager(), profileFragment, PROFILE);

        return profileFragment;
    }

    /**
     * Cart Fragment
     *
     * @return CartFragment
     */
    @NonNull
    private CartFragment findOrCreateCartFragment() {

        CartFragment cartFragment =
                (CartFragment) getFragmentManager().findFragmentByTag(CART);
        if (cartFragment == null) {
            // Create the fragment
            cartFragment = CartFragment.newInstance();
        }

        ActivityUtils.showOrAddFragmentByTag(
                getFragmentManager(), cartFragment, CART);

        return cartFragment;
    }

    /**
     * Payment Fragment
     *
     * @return PaymentFragment
     */
    @NonNull
    private PaymentFragment findOrCreatePaymentFragment() {

        PaymentFragment paymentFragment =
                (PaymentFragment) getFragmentManager().findFragmentByTag(PAYMENT);
        if (paymentFragment == null) {
            // Create the fragment
            paymentFragment = PaymentFragment.newInstance();
        }

        ActivityUtils.addFragmentByTag(
                getFragmentManager(), paymentFragment, PAYMENT);

        return paymentFragment;
    }

    /**
     * Detail Fragment
     *
     * @return DetailFragment
     */
    @NonNull
    private DetailFragment createDetailFragment() {

        DetailFragment detailFragment = DetailFragment.newInstance();

        ActivityUtils.addFragmentByTag(
                getFragmentManager(), detailFragment, DETAIL);

        return detailFragment;
    }

    /**
     * Create Main Presenter
     *
     * @return MainPresenter
     */
    private MainPresenter createMainPresenter() {
        mMainPresenter = new MainPresenter(StylishRepository.getInstance(
                StylishRemoteDataSource.getInstance(),
                StylishLocalDataSource.getInstance()), (MainActivity) mActivity);

        return mMainPresenter;
    }

    private FragmentManager getFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }
}
