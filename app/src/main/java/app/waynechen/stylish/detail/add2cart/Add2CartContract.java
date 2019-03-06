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

package app.waynechen.stylish.detail.add2cart;

import app.waynechen.stylish.BasePresenter;
import app.waynechen.stylish.BaseView;
import app.waynechen.stylish.data.Color;
import app.waynechen.stylish.data.Product;
import app.waynechen.stylish.data.Variant;

import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 *
 * This specifies the contract between the view and the presenter.
 */
public interface Add2CartContract {

    interface View extends BaseView<Presenter> {

        void showProductUi(Product product);

        void showSizesUi(ArrayList<Variant> variants);

        void setAdd2CartButtonEnable(boolean clickable);

        void initEditorUi();

        int getEditorAmount();

        void increaseEditorAmount(int amount);

        void decreaseEditorAmount(int amount);

        void showCurrentStockUi(int stock);

        void showStockNotEnoughUi(int stockMaximum);

        void finishAdd2CartUi();

        void setMaximumAmountUi();

        void setMinimumAmountUi();

        void setInRangeOfAmountUi();

        void disableEditorUi();

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadAdd2CartProductData();

        void setAdd2CartProductData(Product product);

        void selectAdd2CartColor(Color color);

        void selectAdd2CartVariant(Variant variant);

        void clickAdd2CartIncrease();

        void clickAdd2CartDecrease();

        void initialAdd2CartEditor();

        void onAdd2CartEditorTextChange(CharSequence charSequence);

        void addProductToCart();

        void showAddToSuccessDialog();

    }
}
