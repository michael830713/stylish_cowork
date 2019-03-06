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
import android.widget.TextView;

import app.waynechen.stylish.R;
import app.waynechen.stylish.Stylish;
import app.waynechen.stylish.data.Variant;

import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class Add2CartSizeAdapter extends RecyclerView.Adapter {

    private Add2CartContract.Presenter mPresenter;
    private ArrayList<Variant> mVariants;

    private int mSelectedPosition = -1;

    public Add2CartSizeAdapter(Add2CartContract.Presenter presenter, ArrayList<Variant> variants) {
        mPresenter = presenter;
        mVariants = variants;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new SizeViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add2cart_size, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof SizeViewHolder) {

            ((SizeViewHolder) holder).getTextSize().setText(mVariants.get(position).getSize());

            if (mVariants.get(position).getStock() == 0) {

                ((SizeViewHolder) holder).getTextSize().setTextColor(Stylish.getAppContext().getColor(R.color.gray_999999));
                ((SizeViewHolder) holder).getTextSize().setClickable(false);
                ((SizeViewHolder) holder).getMaskNoSize().setVisibility(View.VISIBLE);
            } else {

                ((SizeViewHolder) holder).getTextSize().setTextColor(Stylish.getAppContext().getColor(R.color.black_3f3a3a));
                ((SizeViewHolder) holder).getTextSize().setClickable(true);
                ((SizeViewHolder) holder).getMaskNoSize().setVisibility(View.GONE);
            }

            ((SizeViewHolder) holder).getTextSize().setForeground(new ShapeDrawable(new Shape() {
                @Override
                public void draw(Canvas canvas, Paint paint) {

                    if (position == mSelectedPosition) {

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
            }));
        }
    }

    @Override
    public int getItemCount() {
        return mVariants.size();
    }

    private class SizeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextSize;
        private ImageView mMaskNoSize;

        SizeViewHolder(View itemView) {
            super(itemView);

            mMaskNoSize = itemView.findViewById(R.id.image_add2cart_no_size);
            mTextSize = itemView.findViewById(R.id.text_add2cart_size);
            mTextSize.setOnClickListener(this);
        }

        public TextView getTextSize() {
            return mTextSize;
        }

        public ImageView getMaskNoSize() {
            return mMaskNoSize;
        }

        @Override
        public void onClick(View v) {

            if (getAdapterPosition() != mSelectedPosition) {

                int lastPosition = mSelectedPosition;
                mSelectedPosition = getAdapterPosition();
                notifyItemChanged(lastPosition);
                notifyItemChanged(mSelectedPosition);

                mPresenter.selectAdd2CartVariant(mVariants.get(getAdapterPosition()));
            }
        }
    }

    public void updateData(ArrayList<Variant> variants) {
        mVariants = variants;
        mSelectedPosition = -1;
        notifyDataSetChanged();
    }
}
