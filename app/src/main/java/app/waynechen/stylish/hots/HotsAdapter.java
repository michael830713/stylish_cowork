package app.waynechen.stylish.hots;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.waynechen.stylish.R;
import app.waynechen.stylish.data.Product;
import app.waynechen.stylish.util.ImageManager;

import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class HotsAdapter extends RecyclerView.Adapter {

    private static final int TYPE_LOADING   = 0;
    private static final int TYPE_TITLE     = 0x01;
    private static final int TYPE_FULL      = 0x02;
    private static final int TYPE_COLLAGE   = 0x03;

    private HotsContract.Presenter mPresenter;

    private ArrayList<Object> mHotsDataList = new ArrayList<>();

    public HotsAdapter(HotsContract.Presenter presenter) {
        mPresenter = presenter;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TITLE) {

            return new TitleViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_hots_title, parent, false));
        } else if (viewType == TYPE_FULL) {

            return new FullViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_hots_full, parent, false));
        } else if (viewType == TYPE_COLLAGE) {

            return new CollageViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_hots_collage, parent, false));
        } else {

            return new LoadingViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_all_loading, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder) {

            bindTitleViewHolder((TitleViewHolder) holder, (String) mHotsDataList.get(position));

        } else if (holder instanceof FullViewHolder) {

            bindFullViewHolder((FullViewHolder) holder, (Product) mHotsDataList.get(position));

        } else if (holder instanceof CollageViewHolder) {

            bindCollageViewHolder((CollageViewHolder) holder, (Product) mHotsDataList.get(position));

        }
    }

    private void bindTitleViewHolder(TitleViewHolder holder, String title) {
        // Set title
        holder.getTextTitle().setText(title);
    }

    private void bindFullViewHolder(FullViewHolder holder, Product product) {
        // Set main image
        ImageManager.getInstance().setImageByUrl(holder.getImageMain(), product.getMainImage());

        // Set title
        holder.getTextTitle().setText(product.getTitle());

        // Set description
        holder.getTextDescription().setText(product.getDescription());
    }

    private void bindCollageViewHolder(CollageViewHolder holder, Product product) {
        // Set left image
        ImageManager.getInstance().setImageByUrl(holder.getImageLeft(),
                (product.getImages().size() > 0) ? product.getImages().get(0) : product.getMainImage());

        // Set top image
        ImageManager.getInstance().setImageByUrl(holder.getImageTop(),
                (product.getImages().size() > 1) ? product.getImages().get(1) : product.getMainImage());

        // Set bottom image
        ImageManager.getInstance().setImageByUrl(holder.getImageBottom(),
                (product.getImages().size() > 2) ? product.getImages().get(2) : product.getMainImage());

        // Set right image
        ImageManager.getInstance().setImageByUrl(holder.getImageRight(),
                (product.getImages().size() > 3) ? product.getImages().get(3) : product.getMainImage());

        // Set title
        holder.getTextTitle().setText(product.getTitle());

        // Set description
        holder.getTextDescription().setText(product.getDescription());
    }

    @Override
    public int getItemCount() {
        return mHotsDataList.size();
    }


    @Override
    public int getItemViewType(int position) {

        if (mHotsDataList.get(position) instanceof String) {
            return TYPE_TITLE;
        } else if ((position == 1) && (mHotsDataList.get(position) instanceof Product)) {
            return TYPE_FULL;
        } else if (mHotsDataList.get(position) instanceof Product) {
            return TYPE_COLLAGE;
        } else {
            return TYPE_LOADING;
        }
    }

    private class TitleViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextTitle;

        public TitleViewHolder(View itemView) {
            super(itemView);

            mTextTitle = itemView.findViewById(R.id.text_hots_title);
        }

        public TextView getTextTitle() {
            return mTextTitle;
        }
    }

    private class FullViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private TextView mTextDescription;

        public FullViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_hots_main);
            mTextTitle = itemView.findViewById(R.id.text_hots_title);
            mTextDescription = itemView.findViewById(R.id.text_hots_description);

            itemView.findViewById(R.id.layout_hots_full).setOnClickListener(view -> {
                mPresenter.openDetail((Product) mHotsDataList.get(getAdapterPosition()));
            });
        }

        public ImageView getImageMain() {
            return mImageMain;
        }

        public TextView getTextTitle() {
            return mTextTitle;
        }

        public TextView getTextDescription() {
            return mTextDescription;
        }
    }

    private class CollageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageLeft;
        private ImageView mImageTop;
        private ImageView mImageBottom;
        private ImageView mImageRight;
        private TextView mTextTitle;
        private TextView mTextDescription;

        public CollageViewHolder(View itemView) {
            super(itemView);

            mImageLeft = itemView.findViewById(R.id.image_hots_left);
            mImageTop = itemView.findViewById(R.id.image_hots_top);
            mImageBottom = itemView.findViewById(R.id.image_hots_bottom);
            mImageRight = itemView.findViewById(R.id.image_hots_right);
            mTextTitle = itemView.findViewById(R.id.text_hots_title);
            mTextDescription = itemView.findViewById(R.id.text_hots_description);

            itemView.findViewById(R.id.layout_hots_collage).setOnClickListener(view -> {
                mPresenter.openDetail((Product) mHotsDataList.get(getAdapterPosition()));
            });
        }

        public ImageView getImageLeft() {
            return mImageLeft;
        }

        public ImageView getImageTop() {
            return mImageTop;
        }

        public ImageView getImageBottom() {
            return mImageBottom;
        }

        public ImageView getImageRight() {
            return mImageRight;
        }

        public TextView getTextTitle() {
            return mTextTitle;
        }

        public TextView getTextDescription() {
            return mTextDescription;
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void updateData(ArrayList<Object> hotsDataList) {
        mHotsDataList = hotsDataList;
        notifyDataSetChanged();
    }
}
