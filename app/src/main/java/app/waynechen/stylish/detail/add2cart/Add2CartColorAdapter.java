package app.waynechen.stylish.detail.add2cart;

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
import app.waynechen.stylish.data.Color;

import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class Add2CartColorAdapter extends RecyclerView.Adapter {

    private Add2CartContract.Presenter mPresenter;
    private ArrayList<Color> mColors;

    private int mSelectedPosition = -1;

    public Add2CartColorAdapter(Add2CartContract.Presenter presenter, ArrayList<Color> colors) {
        mPresenter = presenter;
        mColors = colors;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ColorViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add2cart_color, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ColorViewHolder) {

            ((ColorViewHolder) holder).getImageColor().setBackground(new ShapeDrawable(new Shape() {
                @Override
                public void draw(Canvas canvas, Paint paint) {

                    paint.setColor(android.graphics.Color
                            .parseColor("#" + mColors.get(position).getCode()));
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);

                    paint.setColor(android.graphics.Color.BLACK);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(Stylish.getAppContext().getResources()
                            .getDimensionPixelSize(R.dimen.edge_add2cart_color));
                    canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);

                    if (position == mSelectedPosition) {

                        if (mColors.get(position).getCode().equals("FFFFFF")) {

                            paint.setColor(android.graphics.Color.BLACK);
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(Stylish.getAppContext().getResources()
                                    .getDimensionPixelSize(R.dimen.edge_add2cart_color_white));
                            canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
                        } else {

                            paint.setColor(android.graphics.Color.WHITE);
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(Stylish.getAppContext().getResources()
                                    .getDimensionPixelSize(R.dimen.edge_add2cart_color_white));
                            canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);

                            paint.setColor(android.graphics.Color.BLACK);
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(Stylish.getAppContext().getResources()
                                    .getDimensionPixelSize(R.dimen.edge_add2cart_color_black));
                            canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
                        }
                    }
                }
            }));
        }
    }

    @Override
    public int getItemCount() {
        return mColors.size();
    }

    private class ColorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageColor;

        ColorViewHolder(View itemView) {
            super(itemView);

            mImageColor = itemView.findViewById(R.id.image_add2cart_color);
            mImageColor.setOnClickListener(this);
        }

        public ImageView getImageColor() {
            return mImageColor;
        }

        @Override
        public void onClick(View v) {

            if (getAdapterPosition() != mSelectedPosition) {

                int lastPosition = mSelectedPosition;
                mSelectedPosition = getAdapterPosition();
                notifyItemChanged(lastPosition);
                notifyItemChanged(mSelectedPosition);

                mPresenter.selectAdd2CartColor(mColors.get(getAdapterPosition()));
            }
        }
    }
}
