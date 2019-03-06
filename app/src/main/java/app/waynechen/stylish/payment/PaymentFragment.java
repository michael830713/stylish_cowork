package app.waynechen.stylish.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import app.waynechen.stylish.R;
import app.waynechen.stylish.Stylish;
import app.waynechen.stylish.data.CartProduct;
import app.waynechen.stylish.dialog.LoginDialog;
import app.waynechen.stylish.util.Constants;

import java.util.ArrayList;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class PaymentFragment extends Fragment implements PaymentContract.View {

    private PaymentContract.Presenter mPresenter;
    private PaymentAdapter mPaymentAdapter;
    private RecyclerView mRecyclerPayment;

    public PaymentFragment() {
        // Requires empty public constructor
    }

    public static PaymentFragment newInstance() {
        return new PaymentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPaymentAdapter = new PaymentAdapter(mPresenter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(PaymentContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_payment, container, false);

        mRecyclerPayment = root.findViewById(R.id.recycler_payment);
        mRecyclerPayment.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerPayment.setAdapter(mPaymentAdapter);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.hideBottomNavigation();
        mPresenter.loadPaymentProducts();
    }

    @Override
    public void showPaymentUi(ArrayList<CartProduct> cartProducts) {
        if (mPaymentAdapter != null) {
            mPaymentAdapter.updateData(cartProducts);
            mRecyclerPayment.smoothScrollToPosition(0);
        }
    }

    @Override
    public boolean isActive() {
        return !isHidden();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.showBottomNavigation();
        mPresenter.updateToolbar(getResources().getString(R.string.cart));
    }

    @Override
    public boolean isShippingTimeNotChecked() {
        return !(((PaymentAdapter.RecipientViewHolder) mRecyclerPayment.findViewHolderForAdapterPosition(getShippingPosition()))
                .getRadioMorning().isChecked()
                    || ((PaymentAdapter.RecipientViewHolder) mRecyclerPayment.findViewHolderForAdapterPosition(getShippingPosition()))
                        .getRadioAfternoon().isChecked()
                            || ((PaymentAdapter.RecipientViewHolder) mRecyclerPayment.findViewHolderForAdapterPosition(getShippingPosition()))
                                .getRadioAnyTime().isChecked());
    }

    @Override
    public void showErrorNameUi() {

        mRecyclerPayment.smoothScrollToPosition(getRecipientPosition());
        showEditEmpty(((PaymentAdapter.RecipientViewHolder) mRecyclerPayment
                .findViewHolderForAdapterPosition(getRecipientPosition())).getEditName());

    }

    @Override
    public void showErrorEmailUi() {

        mRecyclerPayment.smoothScrollToPosition(getRecipientPosition());
        showEditEmpty(((PaymentAdapter.RecipientViewHolder) mRecyclerPayment
                .findViewHolderForAdapterPosition(getRecipientPosition())).getEditEmail());

    }

    @Override
    public void showErrorPhoneUi() {

        mRecyclerPayment.smoothScrollToPosition(getRecipientPosition());
        showEditEmpty(((PaymentAdapter.RecipientViewHolder) mRecyclerPayment
                .findViewHolderForAdapterPosition(getRecipientPosition())).getEditPhone());

    }

    @Override
    public void showErrorAddressUi() {

        mRecyclerPayment.smoothScrollToPosition(getRecipientPosition());
        showEditEmpty(((PaymentAdapter.RecipientViewHolder) mRecyclerPayment
                .findViewHolderForAdapterPosition(getRecipientPosition())).getEditAddress());

    }

    @Override
    public void showErrorShippingTimeUi() {

        mRecyclerPayment.smoothScrollBy(0, -500);
        ((PaymentAdapter.RecipientViewHolder) mRecyclerPayment.findViewHolderForAdapterPosition(getShippingPosition()))
                .getTextShippingTime().setTextColor(Stylish.getAppContext().getColor(R.color.red_d0021b));
        ((PaymentAdapter.RecipientViewHolder) mRecyclerPayment.findViewHolderForAdapterPosition(getShippingPosition()))
                .getRadioMorning().setTextColor(Stylish.getAppContext().getColor(R.color.red_d0021b));
        ((PaymentAdapter.RecipientViewHolder) mRecyclerPayment.findViewHolderForAdapterPosition(getShippingPosition()))
                .getRadioAfternoon().setTextColor(Stylish.getAppContext().getColor(R.color.red_d0021b));
        ((PaymentAdapter.RecipientViewHolder) mRecyclerPayment.findViewHolderForAdapterPosition(getShippingPosition()))
                .getRadioAnyTime().setTextColor(Stylish.getAppContext().getColor(R.color.red_d0021b));

    }

    private void showEditEmpty(EditText editText) {
        editText.setHintTextColor(getContext().getColor(R.color.red_d0021b));
        editText.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.red_d0021b)));
    }

    @Override
    public void getPrime() {
        ((PaymentAdapter.MethodViewHolder) mRecyclerPayment
                .findViewHolderForAdapterPosition(getMethodPosition())).tpdCard.getPrime();
    }

    @Override
    public void showLoadingUi(boolean isLoading) {

        if (isLoading) {
            ((PaymentAdapter.MethodViewHolder) mRecyclerPayment
                    .findViewHolderForAdapterPosition(getMethodPosition())).getProgressCheckOut().setVisibility(View.VISIBLE);
            ((PaymentAdapter.MethodViewHolder) mRecyclerPayment
                    .findViewHolderForAdapterPosition(getMethodPosition())).getButtonCheckOut().setText("");
        } else {
            ((PaymentAdapter.MethodViewHolder) mRecyclerPayment
                    .findViewHolderForAdapterPosition(getMethodPosition())).getProgressCheckOut().setVisibility(View.GONE);
            ((PaymentAdapter.MethodViewHolder) mRecyclerPayment
                    .findViewHolderForAdapterPosition(getMethodPosition())).getButtonCheckOut().setText(getContext().getString(R.string.goto_checkout));
        }
    }

    @Override
    public void showCheckOutSuccessUi() {
        mPresenter.updateCartBadge();
        mPresenter.showCheckOutSuccessDialog();
        mPresenter.finishPayment();
    }

    @Override
    public void showLoginUi() {
        mPresenter.showLoginDialog(LoginDialog.FROM_PAYMENT);
    }

    @Override
    public void showToastUi(String message) {
        mPresenter.showToast(message);
    }

    private int getRecipientPosition() {
        return mPaymentAdapter.getItemCount() - 2;
    }

    private int getShippingPosition() {
        return mPaymentAdapter.getItemCount() - 2;
    }

    private int getMethodPosition() {
        return mPaymentAdapter.getItemCount() - 1;
    }
}
