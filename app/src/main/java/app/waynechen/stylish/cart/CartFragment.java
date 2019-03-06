package app.waynechen.stylish.cart;

import static com.google.common.base.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.waynechen.stylish.R;
import app.waynechen.stylish.data.CartProduct;
import app.waynechen.stylish.util.Constants;

import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class CartFragment extends Fragment implements CartContract.View {

    private CartContract.Presenter mPresenter;
    private CartAdapter mCartAdapter;
    private RecyclerView mRecyclerCart;
    private TextView mTextNoProducts;
    private ConstraintLayout mLayoutBottom;

    public CartFragment() {}

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCartAdapter = new CartAdapter(mPresenter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(CartContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        mRecyclerCart = root.findViewById(R.id.recycler_cart);
        mRecyclerCart.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerCart.setAdapter(mCartAdapter);

        root.findViewById(R.id.button_cart_checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPresenter.openPayment();
            }
        });

        mTextNoProducts = root.findViewById(R.id.text_cart_no_products);
        mLayoutBottom = root.findViewById(R.id.layout_cart_bottom);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadCartProductsData();
    }

    @Override
    public void showCartUi(ArrayList<CartProduct> cartProducts) {

        if (mCartAdapter != null) {
            mCartAdapter.updateData(cartProducts);
            mRecyclerCart.smoothScrollToPosition(0);

            refreshUi();
        }
    }

    @Override
    public boolean isActive() {
        return !isHidden();
    }

    @Override
    public void updateItemAmount(int position, int amount) {

        mCartAdapter.updateData(position, amount);

        ((CartAdapter.CartViewHolder) mRecyclerCart
                .findViewHolderForAdapterPosition(position))
                .getTextAmount().setText(String.valueOf(amount));
        ((CartAdapter.CartViewHolder) mRecyclerCart
                .findViewHolderForAdapterPosition(position)).refreshUi();
    }

    @Override
    public void removeItem(int position) {
        mCartAdapter.deleteData(position);
        mPresenter.updateCartBadge();
        refreshUi();
    }

    private void refreshUi() {

        if (mCartAdapter != null) {
            mTextNoProducts.setVisibility((mCartAdapter.getItemCount() == 0) ? View.VISIBLE : View.GONE);
            mLayoutBottom.setVisibility((mCartAdapter.getItemCount() == 0) ? View.GONE : View.VISIBLE);
        }
    }
}
