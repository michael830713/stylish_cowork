package app.waynechen.stylish.detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

    public DetailGalleryAdapter(ArrayList<String> images) {
        mImages = images;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

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
