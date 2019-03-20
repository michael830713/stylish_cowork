package app.waynechen.stylish.detail;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import app.waynechen.stylish.R;
import app.waynechen.stylish.Stylish;
import app.waynechen.stylish.YoutubeConfig;
import app.waynechen.stylish.data.Product;
import app.waynechen.stylish.data.Variant;
import app.waynechen.stylish.data.source.StylishDataSource;
import app.waynechen.stylish.data.source.task.RemoveFavoriteTask;
import app.waynechen.stylish.data.source.task.SaveFavoriteTask;
import app.waynechen.stylish.util.Constants;
import app.waynechen.stylish.util.UserManager;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class DetailAdapter extends RecyclerView.Adapter {
    private static final String TAG = "DetailAdapter";

    private static final int TYPE_LOADING = 0;
    private static final int TYPE_DETAIL = 0x01;
    private boolean flag = false;

    private DetailContract.Presenter mPresenter;

    private Product mProduct;
    private int mSnapPosition = -1;

    public DetailAdapter(DetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new DetailViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof DetailViewHolder) {

            /* Set gallery */
            /* Step 1. Setup Circles */
            DetailCircleAdapter circleAdapter = new DetailCircleAdapter(mProduct.getImages().size());
            ((DetailViewHolder) holder).getRecyclerCircles()
                    .setLayoutManager(new LinearLayoutManager(
                            Stylish.getAppContext(), LinearLayoutManager.HORIZONTAL, false));
            ((DetailViewHolder) holder).getRecyclerCircles().addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);

                    // Add top margin only for the first item to avoid double space between items
                    if (parent.getChildLayoutPosition(view) == 0) {
                        outRect.left = 0;
                    } else {
                        outRect.left = Stylish.getAppContext().getResources().getDimensionPixelSize(R.dimen.space_detail_circle);
                    }
                }
            });
            ((DetailViewHolder) holder).getRecyclerCircles().setAdapter(circleAdapter);

            /* Step 2. Setup Gallery LayoutManager and LinearSnapHelper */
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    Stylish.getAppContext(), LinearLayoutManager.HORIZONTAL, false);
            LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
            linearSnapHelper.attachToRecyclerView(((DetailViewHolder) holder).getRecyclerGallery());

            ((DetailViewHolder) holder).getRecyclerGallery().setLayoutManager(layoutManager);
            /* Step 3. Setup OnScrollChangeListener
             * Find SnapView from LayoutManager
             * Get Position from SnapView */
            ((DetailViewHolder) holder).getRecyclerGallery().setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    int snapPosition = layoutManager.getPosition(linearSnapHelper.findSnapView(layoutManager));
                    if (mSnapPosition != snapPosition) {

                        mSnapPosition = snapPosition;
                        circleAdapter.setSelectedPosition(mSnapPosition % mProduct.getImages().size());
                    }
                }
            });

            ((DetailViewHolder) holder).getRecyclerGallery()
                    .setAdapter(new DetailGalleryAdapter(mProduct.getImages()));
            ((DetailViewHolder) holder).getRecyclerGallery()
                    .scrollToPosition(mProduct.getImages().size() * 100);

            // Set title
            ((DetailViewHolder) holder).getTextTitle().setText(mProduct.getTitle());

            // Set price
            ((DetailViewHolder) holder).getTextPrice().setText(
                    Stylish.getAppContext().getString(R.string.nt_dollars_, mProduct.getPrice()));

            // Set id
            ((DetailViewHolder) holder).getTextId().setText(String.valueOf(mProduct.getId()));

            // Set story
            ((DetailViewHolder) holder).getTextStory().setText(mProduct.getStory());

            // Set color
            ((DetailViewHolder) holder).getRecyclerColor()
                    .setLayoutManager(new LinearLayoutManager(
                            Stylish.getAppContext(), LinearLayoutManager.HORIZONTAL, false));
            ((DetailViewHolder) holder).getRecyclerColor().addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);

                    // Add top margin only for the first item to avoid double space between items
                    if (parent.getChildLayoutPosition(view) == 0) {
                        outRect.left = 0;
                    } else {
                        outRect.left = Stylish.getAppContext().getResources().getDimensionPixelSize(R.dimen.space_detail_color);
                    }
                }
            });
            ((DetailViewHolder) holder).getRecyclerColor()
                    .setAdapter(new DetailColorAdapter(mProduct.getColors()));

            // Set size
            ((DetailViewHolder) holder).getTextSize().setText(
                    (mProduct.getSizes().size() > 0)
                            ? (mProduct.getSizes().size() == 1)
                            ? mProduct.getSizes().get(0)
                            : Stylish.getAppContext().getString(R.string._dash_,
                            mProduct.getSizes().get(0),
                            mProduct.getSizes().get(mProduct.getSizes().size() - 1))
                            : "");

            // Set Stock
            int totalStocks = 0;
            for (Variant variant : mProduct.getVariants()) {
                totalStocks += variant.getStock();
            }
            ((DetailViewHolder) holder).getTextStock().setText(String.valueOf(totalStocks));

            // Set texture
            ((DetailViewHolder) holder).getTextTexture().setText(mProduct.getTexture());

            // Set wash
            ((DetailViewHolder) holder).getTextWash().setText(mProduct.getWash());

            // Set place
            ((DetailViewHolder) holder).getTextPlace().setText(mProduct.getPlace());

            // Set note
            ((DetailViewHolder) holder).getTextNote().setText(mProduct.getNote());

            ((DetailViewHolder) holder).getmYoutubePlayer().loadData("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/eWEF1Zrmdow\" frameborder=\"0\" allowfullscreen></iframe>","text/html","utf-8");
        }
    }

    @Override
    public int getItemCount() {
        return (mProduct != null) ? 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_DETAIL;
    }

    private class DetailViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView mRecyclerGallery;
        private RecyclerView mRecyclerCircles;
        private TextView mTextTitle;
        private TextView mTextPrice;
        private TextView mTextId;
        private TextView mTextStory;
        private RecyclerView mRecyclerColor;
        private TextView mTextSize;
        private TextView mTextStock;
        private TextView mTextTexture;
        private TextView mTextWash;
        private TextView mTextPlace;
        private TextView mTextNote;
        private ImageButton mButtonClose;
        private ImageView mButtonFavorite;
        private WebView mYoutubePlayer;

        public DetailViewHolder(View itemView) {
            super(itemView);

            mRecyclerGallery = itemView.findViewById(R.id.recycler_detail_gallery);
            mRecyclerCircles = itemView.findViewById(R.id.recycler_detail_circles);
            mTextTitle = itemView.findViewById(R.id.text_detail_title);
            mTextPrice = itemView.findViewById(R.id.text_detail_price);
            mTextId = itemView.findViewById(R.id.text_detail_id);
            mTextStory = itemView.findViewById(R.id.text_detail_story);
            mRecyclerColor = itemView.findViewById(R.id.recycler_detail_color);
            mTextSize = itemView.findViewById(R.id.text_detail_size);
            mTextStock = itemView.findViewById(R.id.text_detail_stock);
            mTextTexture = itemView.findViewById(R.id.text_detail_texture);
            mTextWash = itemView.findViewById(R.id.text_detail_wash);
            mTextPlace = itemView.findViewById(R.id.text_detail_place);
            mTextNote = itemView.findViewById(R.id.text_detail_note);
            mButtonClose = itemView.findViewById(R.id.button_detail_close);
            mButtonFavorite = itemView.findViewById(R.id.imageViewStarred);
            mYoutubePlayer = itemView.findViewById(R.id.viewYoutube);
            mYoutubePlayer.getSettings().setJavaScriptEnabled(true);
            mYoutubePlayer.setWebChromeClient(new WebChromeClient());
            mButtonFavorite.setOnClickListener(v -> {
                String token = UserManager.getInstance().getUserToken();
                long productId = mProduct.getId();
                //use flag to change image
                if (!flag) {

                    new SaveFavoriteTask(token, productId, new StylishDataSource.AvatarChangeCallback() {
                        @Override
                        public void onCompleted(String bean) {
                            Log.d(TAG, "Add to favorite complete: " + bean);
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.d(TAG, "Add to favorite Error: " + errorMessage);
                        }
                    }).execute();

                    mButtonFavorite.setImageResource(R.drawable.favorite_selected);
                    flag = true;
                } else {
                    new RemoveFavoriteTask(token, productId, new StylishDataSource.AvatarChangeCallback() {
                        @Override
                        public void onCompleted(String bean) {
                            Log.d(TAG, "Add to favorite complete: " + bean);

                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.d(TAG, "Add to favorite Error: " + errorMessage);

                        }
                    }).execute();

                    mButtonFavorite.setImageResource(R.drawable.favorite);
                    flag = false;
                }
            });

            mButtonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.finishDetail();
                }
            });

        }

        public RecyclerView getRecyclerGallery() {
            return mRecyclerGallery;
        }

        public RecyclerView getRecyclerCircles() {
            return mRecyclerCircles;
        }

        public TextView getTextTitle() {
            return mTextTitle;
        }

        public TextView getTextPrice() {
            return mTextPrice;
        }

        public TextView getTextId() {
            return mTextId;
        }

        public TextView getTextStory() {
            return mTextStory;
        }

        public RecyclerView getRecyclerColor() {
            return mRecyclerColor;
        }

        public TextView getTextSize() {
            return mTextSize;
        }

        public TextView getTextStock() {
            return mTextStock;
        }

        public TextView getTextTexture() {
            return mTextTexture;
        }

        public TextView getTextWash() {
            return mTextWash;
        }

        public TextView getTextPlace() {
            return mTextPlace;
        }

        public TextView getTextNote() {
            return mTextNote;
        }

        public WebView getmYoutubePlayer() {
            return mYoutubePlayer;
        }
    }

    public void updateData(Product product) {
        mProduct = product;
        notifyDataSetChanged();
    }
}
