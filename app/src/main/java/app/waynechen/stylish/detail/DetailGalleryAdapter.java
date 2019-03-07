package app.waynechen.stylish.detail;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import app.waynechen.stylish.R;
import app.waynechen.stylish.util.ImageManager;

import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class DetailGalleryAdapter extends RecyclerView.Adapter {

    private ArrayList<String> mImages;
    private Context mContext;

    public DetailGalleryAdapter(ArrayList<String> images) {
        mImages = images;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new GalleryViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof GalleryViewHolder) {

            if (mImages.size() > 0) {

                ImageManager.getInstance().setImageByUrl(
                        ((GalleryViewHolder) holder).getImageGallery(),
                        mImages.get(position % mImages.size())); // Real position: position % mImages.size()

                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                ((GalleryViewHolder) holder).getImageGallery().setLayoutParams(new ConstraintLayout
                        .LayoutParams(displayMetrics.widthPixels,
                        mContext.getResources().getDimensionPixelSize(R.dimen.height_detail_gallery)));
            }
        }
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    private class GalleryViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageGallery;

        GalleryViewHolder(View itemView) {
            super(itemView);

            mImageGallery = itemView.findViewById(R.id.image_detail_gallery);
        }

        ImageView getImageGallery() {
            return mImageGallery;
        }
    }
}
