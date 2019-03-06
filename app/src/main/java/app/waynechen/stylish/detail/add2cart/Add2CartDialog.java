package app.waynechen.stylish.detail.add2cart;

import static com.google.common.base.Preconditions.checkNotNull;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import app.waynechen.stylish.R;
import app.waynechen.stylish.Stylish;
import app.waynechen.stylish.data.Product;
import app.waynechen.stylish.data.Variant;
import app.waynechen.stylish.util.Constants;
import app.waynechen.stylish.util.Util;

import java.util.ArrayList;


/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class Add2CartDialog extends DialogFragment implements Add2CartContract.View, View.OnClickListener {

    private Add2CartContract.Presenter mPresenter;

    private ConstraintLayout mLayout;

    private TextView mTextTitle;
    private TextView mTextPrice;
    private RecyclerView mRecyclerColorSelector;
    private RecyclerView mRecyclerSizeSelector;
    private ImageButton mButtonIncrease;
    private ImageButton mButtonDecrease;
    private EditText mEditAmountEditor;
    private TextView mTextCurrentStock;
    private TextView mTextStockNotEnough;
    private TextView mTextSelectAmount;
    private Button mButtonAdd2Cart;
    private Add2CartSizeAdapter mAdd2CartSizeAdapter;

    public Add2CartDialog() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.Add2CartDialog);
    }

    @Override
    public void setPresenter(Add2CartContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_add2cart, container, false);
        view.setOnClickListener(this);
        mLayout = view.findViewById(R.id.layout_add2cart);

        mTextTitle = view.findViewById(R.id.text_add2cart_title);
        mTextPrice = view.findViewById(R.id.text_add2cart_price);
        mButtonAdd2Cart = view.findViewById(R.id.button_add2cart);
        mRecyclerColorSelector = view.findViewById(R.id.recycler_add2cart_color_selector);
        mRecyclerSizeSelector = view.findViewById(R.id.recycler_add2cart_size_selector);
        mButtonIncrease = view.findViewById(R.id.button_add2cart_increase);
        mButtonDecrease = view.findViewById(R.id.button_add2cart_decrease);
        mEditAmountEditor = view.findViewById(R.id.edit_add2cart_amount_editor);
        mTextCurrentStock = view.findViewById(R.id.text_add2cart_current_stock);
        mTextStockNotEnough = view.findViewById(R.id.text_add2cart_stock_not_enough);
        mTextSelectAmount = view.findViewById(R.id.text_add2cart_select_amount);

        mLayout.setOnClickListener(this);
        mButtonIncrease.setOnClickListener(this);
        mButtonDecrease.setOnClickListener(this);
        mButtonAdd2Cart.setOnClickListener(this);

        view.findViewById(R.id.layout_add2cart_bottom).setOnClickListener(this);
        view.findViewById(R.id.button_add2cart_close).setOnClickListener(this);
        Util.setTouchDelegate(view.findViewById(R.id.button_add2cart_close));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_up));

        mPresenter.loadAdd2CartProductData();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.layout_add2cart
                || v.getId() == R.id.layout_add2cart_bottom) {
            /* Avoid Dismiss */
        } else if (v.getId() == R.id.button_add2cart) {

            mPresenter.addProductToCart();

        } else if (v.getId() == R.id.button_add2cart_increase) {

            mPresenter.clickAdd2CartIncrease();

        } else if (v.getId() == R.id.button_add2cart_decrease) {

            mPresenter.clickAdd2CartDecrease();

        } else {

            dismiss();
        }
    }

    @Override
    public void dismiss() {

        mLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_down));

        new Handler().postDelayed(super::dismiss, 200);
    }

    @Override
    public void showProductUi(Product product) {
        mTextTitle.setText(product.getTitle());
        mTextPrice.setText(Stylish.getAppContext().getString(R.string.nt_dollars_, product.getPrice()));

        mRecyclerColorSelector.setLayoutManager(new LinearLayoutManager(
                Stylish.getAppContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerColorSelector.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                // Add top margin only for the first item to avoid double space between items
                if (parent.getChildLayoutPosition(view) == 0) {
                    outRect.left = 0;
                } else {
                    outRect.left = Stylish.getAppContext().getResources().getDimensionPixelSize(R.dimen.space_add2cart_select);
                }
            }
        });
        mRecyclerColorSelector.setAdapter(new Add2CartColorAdapter(mPresenter, product.getColors()));

        mAdd2CartSizeAdapter = new Add2CartSizeAdapter(mPresenter, new ArrayList<>());

        mRecyclerSizeSelector.setLayoutManager(new LinearLayoutManager(
                Stylish.getAppContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerSizeSelector.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                // Add top margin only for the first item to avoid double space between items
                if (parent.getChildLayoutPosition(view) == 0) {
                    outRect.left = 0;
                } else {
                    outRect.left = Stylish.getAppContext().getResources().getDimensionPixelSize(R.dimen.space_add2cart_select);
                }
            }
        });
        mRecyclerSizeSelector.setAdapter(mAdd2CartSizeAdapter);

        mButtonIncrease.setForeground(new ShapeDrawable(new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {

                paint.setColor(android.graphics.Color.BLACK);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(Stylish.getAppContext().getResources()
                        .getDimensionPixelSize(R.dimen.edge_add2cart_select));
                canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
            }
        }));

        mButtonDecrease.setForeground(new ShapeDrawable(new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {

                paint.setColor(android.graphics.Color.BLACK);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(Stylish.getAppContext().getResources()
                        .getDimensionPixelSize(R.dimen.edge_add2cart_select));
                canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
            }
        }));

        mEditAmountEditor.setBackground(new ShapeDrawable(new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {

                paint.setColor(android.graphics.Color.BLACK);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(Stylish.getAppContext().getResources()
                        .getDimensionPixelSize(R.dimen.edge_add2cart_select));
                canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
            }
        }));
        mEditAmountEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPresenter.onAdd2CartEditorTextChange(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void showSizesUi(ArrayList<Variant> variants) {

        mAdd2CartSizeAdapter.updateData(variants);
    }

    @Override
    public int getEditorAmount() {
        return Integer.valueOf(mEditAmountEditor.getText().toString());
    }

    @Override
    public void increaseEditorAmount(int amount) {
        mEditAmountEditor.setText(String.valueOf(amount));
    }

    @Override
    public void decreaseEditorAmount(int amount) {
        mEditAmountEditor.setText(String.valueOf(amount));

        if (mTextStockNotEnough.getVisibility() == View.VISIBLE) {
            mTextSelectAmount.setTextColor(getContext().getColor(R.color.gray_646464));
            mTextCurrentStock.setTextColor(getContext().getColor(R.color.gray_646464));
            mTextStockNotEnough.setVisibility(View.GONE);
        }
    }

    @Override
    public void showCurrentStockUi(int stock) {

        if (stock >= 0) {
            mTextCurrentStock.setVisibility(View.VISIBLE);
            mTextCurrentStock.setText(Stylish.getAppContext().getString(R.string.stock_, stock));
        } else {
            mTextCurrentStock.setVisibility(View.INVISIBLE);
            mTextCurrentStock.setText(Stylish.getAppContext().getString(R.string.stock_, stock));
        }
    }

    // Show not enough and setup maximum value.
    @Override
    public void showStockNotEnoughUi(int stockMaximum) {

        mEditAmountEditor.setText(String.valueOf(stockMaximum));
        setMaximumAmountUi();

        mTextSelectAmount.setTextColor(getContext().getColor(R.color.red_d0021b));
        mTextCurrentStock.setTextColor(getContext().getColor(R.color.red_d0021b));
        mTextStockNotEnough.setVisibility(View.VISIBLE);
    }

    @Override
    public void finishAdd2CartUi() {
        dismiss();
        mPresenter.showAddToSuccessDialog();
    }

    @Override
    public void setMaximumAmountUi() {
        setEditorEnable(true);
        setDecreaseEnable(true);
        setIncreaseEnable(false);
    }

    @Override
    public void setMinimumAmountUi() {
        setEditorEnable(true);
        setDecreaseEnable(false);
        setIncreaseEnable(true);
    }

    @Override
    public void setInRangeOfAmountUi() {
        setEditorEnable(true);
        setDecreaseEnable(true);
        setIncreaseEnable(true);
    }

    @Override
    public void disableEditorUi() {
        mEditAmountEditor.setText("1");

        setEditorEnable(false);
        setDecreaseEnable(false);
        setIncreaseEnable(false);
    }

    @Override
    public void initEditorUi() {
        mEditAmountEditor.setText("1");

        setMinimumAmountUi();

        mTextSelectAmount.setTextColor(getContext().getColor(R.color.gray_646464));
        mTextCurrentStock.setTextColor(getContext().getColor(R.color.gray_646464));
        mTextStockNotEnough.setVisibility(View.GONE);
    }

    @Override
    public void setAdd2CartButtonEnable(boolean enable) {
        mButtonAdd2Cart.setClickable(enable);
        mButtonAdd2Cart.setBackground(getContext().getDrawable(
                (enable)
                        ? R.drawable.button_add2cart_black_ripple
                        : R.drawable.button_add2cart_gray_ripple));
    }

    private void setEditorEnable(boolean enable) {
        mEditAmountEditor.setClickable(enable);
        mEditAmountEditor.setFocusable(enable);
        mEditAmountEditor.setFocusableInTouchMode(enable);
        mEditAmountEditor.setBackgroundTintList(
                ColorStateList.valueOf(
                        Stylish.getAppContext().getColor(
                                (enable) ? R.color.black_3f3a3a : R.color.gray_999999)));
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
}
