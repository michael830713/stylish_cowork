package app.waynechen.stylish.detail;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import app.waynechen.stylish.R;
import app.waynechen.stylish.Stylish;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class DetailCircleAdapter extends RecyclerView.Adapter {

    private int mSelectedPosition = -1;
    private int mCount;

    public DetailCircleAdapter(int count) {
        mCount = count;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new CircleViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_circle, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof CircleViewHolder) {

            ((CircleViewHolder) holder).getImageCircle().setBackground(new ShapeDrawable(new Shape() {
                @Override
                public void draw(Canvas canvas, Paint paint) {

                    paint.setColor(Stylish.getAppContext().getColor(R.color.white));
                    paint.setAntiAlias(true);
                    if (position == mSelectedPosition) {
                        paint.setStyle(Paint.Style.FILL);

                    } else {
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(Stylish.getAppContext()
                                .getResources().getDimensionPixelSize(R.dimen.edge_detail_circle));
                    }

                    canvas.drawCircle(this.getWidth() / 2, this.getHeight() / 2,
                            Stylish.getAppContext().getResources()
                                    .getDimensionPixelSize(R.dimen.radius_detail_circle), paint);
                }
            }));
        }
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    private class CircleViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageCircle;

        CircleViewHolder(View itemView) {
            super(itemView);

            mImageCircle = itemView.findViewById(R.id.image_detail_circle);
        }

        public ImageView getImageCircle() {
            return mImageCircle;
        }
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
        notifyDataSetChanged();
    }
}
