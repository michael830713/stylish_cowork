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

import java.util.ArrayList;

import app.waynechen.stylish.data.Product;
import app.waynechen.stylish.data.ProductForGson;
import app.waynechen.stylish.data.source.bean.GetProductList;

/**
 * Created by Wayne Chen on Feb. 2019.
 *
 * This specifies the contract between the view and the presenter.
 */
public interface FavoriteItemContract {

    interface View extends BaseView<Presenter> {

        void showProductsUi(String[] bean);

        boolean hasNextPaging();

        int getPaging();

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadProductsData();

        void setProductsData(String[] bean);

        boolean isCatalogItemHasNextPaging(@MainMvpController.CatalogItem String itemType);

        void onCatalogItemScrollToBottom(@MainMvpController.CatalogItem String itemType);

        void openDetail(ProductForGson product);
    }
}
