package app.waynechen.stylish.cart;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import app.waynechen.stylish.R;
import app.waynechen.stylish.Stylish;
import app.waynechen.stylish.data.CartProduct;
import app.waynechen.stylish.util.ImageManager;

import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class CartAdapter extends RecyclerView.Adapter {

    private static final int TYPE_LOADING   = 0;
    private static final int TYPE_PRODUCT_CART = 0x01;

    private CartContract.Presenter mPresenter;
    private ArrayList<CartProduct> mCartProducts = new ArrayList<>();

    public CartAdapter(CartContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new CartViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof CartViewHolder) {

            ImageManager.getInstance().setImageByUrl(
                    ((CartViewHolder) holder).getImageMain(), mCartProducts.get(position).getMainImage());

            // Set title
            ((CartViewHolder) holder).getTextTitle().setText(mCartProducts.get(position).getTitle());

            ((CartViewHolder) holder).getImageColor().setBackground(new ShapeDrawable(new Shape() {
                @Override
                public void draw(Canvas canvas, Paint paint) {

                    paint.setColor(android.graphics.Color
                            .parseColor("#" + mCartProducts.get(position).getSelectedColorCode()));
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);

                    paint.setColor(android.graphics.Color.BLACK);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(Stylish.getAppContext().getResources().getDimensionPixelSize(R.dimen.edge_cart_color));
                    canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);

                }
            }));

            ((CartViewHolder) holder).getTextSize().setText(mCartProducts.get(position).getSelectedSize());

            ((CartViewHolder) holder).getTextPrice().setText(Stylish.getAppContext().getString(R.string.nt_dollars_,
                    mCartProducts.get(position).getPrice()));

            ((CartViewHolder) holder).getButtonIncrease().setForeground(new ShapeDrawable(new Shape() {
                @Override
                public void draw(Canvas canvas, Paint paint) {

                    paint.setColor(android.graphics.Color.BLACK);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(Stylish.getAppContext().getResources()
                            .getDimensionPixelSize(R.dimen.edge_cart_select));
                    canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
                }
            }));

            ((CartViewHolder) holder).getButtonDecrease().setForeground(new ShapeDrawable(new Shape() {
                @Override
                public void draw(Canvas canvas, Paint paint) {

                    paint.setColor(android.graphics.Color.BLACK);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(Stylish.getAppContext().getResources()
                            .getDimensionPixelSize(R.dimen.edge_cart_select));
                    canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
                }
            }));

            ((CartViewHolder) holder).getTextAmount().setText(String.valueOf(mCartProducts.get(position).getSelectedAmount()));
            ((CartViewHolder) holder).getTextAmount().setBackground(new ShapeDrawable(new Shape() {
                @Override
                public void draw(Canvas canvas, Paint paint) {

                    paint.setColor(android.graphics.Color.BLACK);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(Stylish.getAppContext().getResources()
                            .getDimensionPixelSize(R.dimen.edge_cart_select));
                    canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
                }
            }));

            ((CartViewHolder) holder).initEditorStatus(mCartProducts.get(position).getSelectedStock());

        }
    }

    @Override
    public int getItemCount() {
        return mCartProducts.size();
    }


    @Override
    public int getItemViewType(int position) {
        return TYPE_PRODUCT_CART;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int mStock;
        private ImageView mImageMain;
        private TextView mTextTitle;
        private ImageView mImageColor;
        private TextView mTextSize;
        private TextView mTextPrice;
        private ImageButton mButtonIncrease;
        private ImageButton mButtonDecrease;
        private TextView mTextAmount;
        private TextView mTextRemove;


        public CartViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_cart_main);
            mTextTitle = itemView.findViewById(R.id.text_cart_title);
            mImageColor = itemView.findViewById(R.id.image_cart_color);
            mTextSize = itemView.findViewById(R.id.text_cart_size);
            mTextPrice = itemView.findViewById(R.id.text_cart_price);
            mButtonIncrease = itemView.findViewById(R.id.button_cart_increase);
            mButtonDecrease = itemView.findViewById(R.id.button_cart_decrease);
            mTextAmount = itemView.findViewById(R.id.text_cart_amount_editor);
            mTextRemove = itemView.findViewById(R.id.text_cart_remove);

            mButtonIncrease.setOnClickListener(this);
            mButtonDecrease.setOnClickListener(this);
            mTextRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_cart_increase) {
                mPresenter.clickCartItemIncrease(getAdapterPosition());
            } else if (v.getId() == R.id.button_cart_decrease) {
                mPresenter.clickCartItemDecrease(getAdapterPosition());
            } else if (v.getId() == R.id.text_cart_remove) {
                mPresenter.clickCartItemRemove(getAdapterPosition());
            }
        }

        void initEditorStatus(int stock) {

            setStock(stock);
            refreshUi();
        }

        void refreshUi() {
            if (getStock() == 1) {
                setOnly1StockUi();
            } else if (Integer.valueOf(mTextAmount.getText().toString()) == getStock()) {
                setMaximumAmountUi();
            } else if (Integer.valueOf(mTextAmount.getText().toString()) == 1) {
                setMinimumAmountUi();
            } else {
                setInRangeOfAmountUi();
            }
        }

        void setMaximumAmountUi() {
            setDecreaseEnable(true);
            setIncreaseEnable(false);
        }

        void setMinimumAmountUi() {
            setDecreaseEnable(false);
            setIncreaseEnable(true);
        }

        void setInRangeOfAmountUi() {
            setDecreaseEnable(true);
            setIncreaseEnable(true);
        }

        private void setOnly1StockUi() {
            setDecreaseEnable(false);
            setIncreaseEnable(false);
        }

        private void setIncreaseEnable(boolean enable) {
            mButtonIncrease.setClickable(enable);
            mButtonIncrease.setBackgroundTintList(
                    ColorStateList.valueOf(
                            Stylish.getAppContext().getColor(
                                    (enable) ? R.color.black_3f3a3a : R.color.gray_999999)));
            mButtonIncrease.setForegroundTintList(
                    ColorStateList.valueOf(
                            Stylish.getAppContext().getColor(
                                    (enable) ? R.color.black_3f3a3a : R.color.gray_999999)));
        }

        private void setDecreaseEnable(boolean enable) {
            mButtonDecrease.setClickable(enable);
            mButtonDecrease.setBackgroundTintList(
                    ColorStateList.valueOf(
                            Stylish.getAppContext().getColor(
                                    (enable) ? R.color.black_3f3a3a : R.color.gray_999999)));
            mButtonDecrease.setForegroundTintList(
                    ColorStateList.valueOf(
                            Stylish.getAppContext().getColor(
                                    (enable) ? R.color.black_3f3a3a : R.color.gray_999999)));
        }

        public ImageView getImageMain() {
            return mImageMain;
        }

        public TextView getTextTitle() {
            return mTextTitle;
        }

        public ImageView getImageColor() {
            return mImageColor;
        }

        public TextView getTextSize() {
            return mTextSize;
        }

        public TextView getTextPrice() {
            return mTextPrice;
        }

        public ImageButton getButtonIncrease() {
            return mButtonIncrease;
        }

        public ImageButton getButtonDecrease() {
            return mButtonDecrease;
        }

        public TextView getTextAmount() {
            return mTextAmount;
        }

        public int getStock() {
            return mStock;
        }

        public void setStock(int stock) {
            mStock = stock;
        }
    }

    public void updateData(ArrayList<CartProduct> cartProducts) {

        if (cartProducts != null) {
            mCartProducts = cartProducts;
            notifyDataSetChanged();
        }
    }

    public void updateData(int position, int amount) {
        mCartProducts.get(position).setSelectedAmount(amount);
    }

    public void deleteData(int position) {

        notifyItemRemoved(position);
        mCartProducts.remove(position);
    }
}
