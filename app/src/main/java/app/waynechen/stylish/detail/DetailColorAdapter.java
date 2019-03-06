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
import app.waynechen.stylish.data.Color;

import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class DetailColorAdapter extends RecyclerView.Adapter {

    private ArrayList<Color> mColors;

    public DetailColorAdapter(ArrayList<Color> colors) {
        mColors = colors;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ColorViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_color, parent, false));
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
                    paint.setStrokeWidth(Stylish.getAppContext().getResources().getDimensionPixelSize(R.dimen.edge_detail_color));
                    canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);

                }
            }));
        }
    }

    @Override
    public int getItemCount() {
        return mColors.size();
    }

    private class ColorViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageColor;

        ColorViewHolder(View itemView) {
            super(itemView);

            mImageColor = itemView.findViewById(R.id.image_detail_color);
        }

        public ImageView getImageColor() {
            return mImageColor;
        }
    }
}
