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

import android.content.Intent;

import app.waynechen.stylish.catalog.item.CatalogItemFragment;
import app.waynechen.stylish.data.Product;
import app.waynechen.stylish.dialog.MessageDialog;

/**
 * Created by Wayne Chen on Feb. 2019.
 *
 * This specifies the contract between the view and the presenter.
 */
public interface MainContract {

    interface View extends BaseView<Presenter> {

        void openHotsUi();

        void openCatalogUi();

        void openProfileUi();

        void openCartUi();

        void openPaymentUi();

        void openLoginUi(int loginFrom);

        void openAdd2CartUi(Product product);

        void openCheckOutSuccessUi();

        void openDetailUi(Product product);

        void finishDetailUi();

        void finishPaymentUi();

        CatalogItemFragment findWomenView();

        CatalogItemFragment findMenView();

        CatalogItemFragment findAccessoriesView();

        void switchProfileUiInitiative();

        void switchHotsUiInitiative();

        void showMessageDialogUi(@MessageDialog.MessageType int type);

        void showToastUi(String message);

        void hideToolbarUi();

        void showToolbarUi();

        void hideBottomNavigationUi();

        void showBottomNavigationUi();

        void updateCartBadgeUi(int amount);

        void setToolbarTitleUi(String title);

        void closeDrawerUi();

        void showDrawerUserUi();

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode, Intent data);

        void openHots();

        void openCatalog();

        void openCart();

        boolean openProfile();

        void openPayment();

        void switchToProfileByBottomNavigation();

        void switchToHotsByBottomNavigation();

        CatalogItemFragment findWomen();

        CatalogItemFragment findMen();

        CatalogItemFragment findAccessories();

        void hideToolbarAndBottomNavigation();

        void showToolbarAndBottomNavigation();

        void hideBottomNavigation();

        void showBottomNavigation();

        void updateCartBadge();

        void updateToolbar(String title);

        void onLoginSuccess(int loginFrom);

        void showLoginDialog(int loginFrom);

        void showCheckOutSuccessDialog();

        void showAddToSuccessDialog();

        void showLoginSuccessDialog();

        void showToast(String message);

        void onDrawerOpened();

        void onClickDrawerAvatar();

    }
}
