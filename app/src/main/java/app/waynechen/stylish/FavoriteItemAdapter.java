package app.waynechen.stylish;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.waynechen.stylish.data.ProductForGson;
import app.waynechen.stylish.data.source.StylishDataSource;
import app.waynechen.stylish.data.source.task.GetFavoritesItemTask;
import app.waynechen.stylish.util.ImageManager;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class FavoriteItemAdapter extends RecyclerView.Adapter {

    private static final String TAG = "FavoriteItemAdapter";

    private static final int TYPE_LOADING = 0;
    private static final int TYPE_GRID = 0x01;

    private FavoriteItemContract.Presenter mPresenter;
    private ProductForGson mProduct = new ProductForGson();
    private String[] mIds = new String[]{};

    public FavoriteItemAdapter(FavoriteItemContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_GRID) {

            return new GridViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_catalog_grid, parent, false));
        } else {

            return new LoadingViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_all_loading, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof GridViewHolder) {

            bindGridViewHolder((GridViewHolder) holder, mIds[position]);
        }
    }

    private void bindGridViewHolder(GridViewHolder holder, String id) {

        new GetFavoritesItemTask(id, new StylishDataSource.FavoriteItemCallback() {
            @Override
            public void onCompleted(ProductForGson bean) {
                mProduct = bean;
                ImageManager.getInstance().setImageByUrl(holder.getImageMain(), bean.getData().getMain_image());
                holder.getTextTitle().setText(bean.getData().getTitle());
                holder.getTextPrice().setText(
                        Stylish.getAppContext().getString(R.string.nt_dollars_, bean.getData().getPrice()));
            }

            @Override
            public void onError(String errorMessage) {
                Log.d(TAG, "onError: " + errorMessage);
            }
        }).execute();

    }

    @Override
    public int getItemCount() {
        return mIds.length;
    }

    @Override
    public int getItemViewType(int position) {
        return (position < mIds.length) ? TYPE_GRID : TYPE_LOADING;
    }

    private class GridViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextPrice;

        public GridViewHolder(View itemView) {
            super(itemView);

            itemView.findViewById(R.id.layout_catalog_grid).setOnClickListener(view -> {
                mPresenter.openDetail(mProduct);
            });

            mImageMain = itemView.findViewById(R.id.image_catalog_main);
            mTextTitle = itemView.findViewById(R.id.text_catalog_title);
            mTextPrice = itemView.findViewById(R.id.text_catalog_price);
        }

        public ImageView getImageMain() {
            return mImageMain;
        }

        public TextView getTextTitle() {
            return mTextTitle;
        }

        public TextView getTextPrice() {
            return mTextPrice;
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void updateData(String[] ids) {
        mIds = ids;
        notifyDataSetChanged();
    }
}
