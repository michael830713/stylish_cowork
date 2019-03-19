package app.waynechen.stylish;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.waynechen.stylish.catalog.item.CatalogItemContract;
import app.waynechen.stylish.data.Product;
import app.waynechen.stylish.util.ImageManager;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class FavoriteItemAdapter extends RecyclerView.Adapter {

    private static final int TYPE_LOADING   = 0;
    private static final int TYPE_GRID      = 0x01;

    private FavoriteItemContract.Presenter mPresenter;
    private ArrayList<Product> mProducts = new ArrayList<>();

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

            bindGridViewHolder((GridViewHolder) holder, mProducts.get(position));
        }
    }

    private void bindGridViewHolder(GridViewHolder holder, Product product) {
        // Set main image
        ImageManager.getInstance().setImageByUrl(holder.getImageMain(), product.getMainImage());

        // Set title
        holder.getTextTitle().setText(product.getTitle());

        // Set description
        holder.getTextPrice().setText(
                Stylish.getAppContext().getString(R.string.nt_dollars_, product.getPrice()));
    }


    @Override
    public int getItemCount() {
        return mProducts.size();
    }


    @Override
    public int getItemViewType(int position) {
        return (position < mProducts.size()) ? TYPE_GRID : TYPE_LOADING;
    }

    private class GridViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextPrice;

        public GridViewHolder(View itemView) {
            super(itemView);

            itemView.findViewById(R.id.layout_catalog_grid).setOnClickListener(view -> {
                mPresenter.openDetail(mProducts.get(getAdapterPosition()));
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

    public void updateData(ArrayList<Product> products) {
        mProducts.addAll(products);
        notifyDataSetChanged();
    }
}
