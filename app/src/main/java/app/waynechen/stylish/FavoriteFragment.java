package app.waynechen.stylish;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.waynechen.stylish.component.GridSpacingItemDecoration;
import app.waynechen.stylish.data.Product;
import app.waynechen.stylish.data.ProductForGson;
import app.waynechen.stylish.util.Constants;

import static app.waynechen.stylish.MainMvpController.ACCESSORIES;
import static app.waynechen.stylish.MainMvpController.MEN;
import static app.waynechen.stylish.MainMvpController.WOMEN;
import static app.waynechen.stylish.data.source.bean.GetProductList.FIRST_PAGING;
import static app.waynechen.stylish.data.source.bean.GetProductList.NO_MORE_PAGING;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class FavoriteFragment extends Fragment implements FavoriteItemContract.View {

    private FavoriteItemContract.Presenter mPresenter;
    private FavoriteItemAdapter mFavoriteItemAdapter;

    private String mItemType;
    private int mPaging;

    public FavoriteFragment() {
    }

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPaging(FIRST_PAGING);
//        mPresenter.findFavorite();
        mFavoriteItemAdapter = new FavoriteItemAdapter(mPresenter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(FavoriteItemContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_catalog_child, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_catalog_child);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (hasNextPaging() && position == mFavoriteItemAdapter.getItemCount() - 1) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,
                getContext().getResources().getDimensionPixelSize(R.dimen.space_catalog_grid), true));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(1)) {
                    Log.d(Constants.TAG, "Scroll to bottom");
                    mPresenter.onCatalogItemScrollToBottom(mItemType);
                }
            }
        });
        recyclerView.setAdapter(mFavoriteItemAdapter);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadProductsData();

    }

    @Override
    public void showProductsUi(ArrayList<ProductForGson> bean) {
//        setPaging(bean.getPaging());
        mFavoriteItemAdapter.updateData(bean);
    }

    @Override
    public boolean hasNextPaging() {
        return mPaging != NO_MORE_PAGING;
    }

    @Override
    public int getPaging() {
        return mPaging;
    }

    private void setPaging(int paging) {
        mPaging = paging;
    }

    public void setItemType(@MainMvpController.CatalogItem String itemType) {
        mItemType = itemType;
    }
}
