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

package app.waynechen.stylish.payment;

import app.waynechen.stylish.BasePresenter;
import app.waynechen.stylish.BaseView;
import app.waynechen.stylish.data.CartProduct;

import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 *
 * This specifies the contract between the view and the presenter.
 */
public interface PaymentContract {

    interface View extends BaseView<Presenter> {

        void showPaymentUi(ArrayList<CartProduct> cartProducts);

        boolean isActive();

        boolean isShippingTimeNotChecked();

        void showErrorNameUi();

        void showErrorEmailUi();

        void showErrorPhoneUi();

        void showErrorAddressUi();

        void showErrorShippingTimeUi();

        void getPrime();

        void showLoadingUi(boolean isLoading);

        void showCheckOutSuccessUi();

        void showLoginUi();

        void showToastUi(String message);
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadPaymentProducts();

        void calculatePaymentProductsFee();

        void clickPaymentCheckOut();

        void onPaymentCanGetPrimeChanged(boolean isCanGetPrime);

        void onPaymentPrimeSuccess(String prime);

        void onPaymentPrimeFail(String errorMessage);

        void onPaymentRecipientNameChanged(String name);

        void onPaymentRecipientEmailChanged(String email);

        void onPaymentRecipientPhoneChanged(String phone);

        void onPaymentRecipientAddressChanged(String address);

        void onPaymentShippingTimeChanged(String time);

        void onPaymentSpinnerMethodsChanged(int position);

        void onPaymentTpdErrorMessageChanged(String errorMessage);

        int getPaymentSubTotal();

        int getPaymentFreight();

        int getPaymentTotal();

        void updateToolbar(String title);

        void hideBottomNavigation();

        void showBottomNavigation();

        void showCheckOutSuccessDialog();

        void finishPayment();

        void showLoginDialog(int loginFrom);

        boolean isPaymentViewActive();

        void onPaymentLoginSuccess();

        void updateCartBadge();

        void showToast(String message);
    }
}
