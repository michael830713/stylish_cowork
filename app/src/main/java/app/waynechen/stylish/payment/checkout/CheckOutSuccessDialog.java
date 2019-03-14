package app.waynechen.stylish.payment.checkout;

import static com.google.common.base.Preconditions.checkNotNull;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.waynechen.stylish.R;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class CheckOutSuccessDialog extends AppCompatDialogFragment implements CheckOutSuccessContract.View {

    private CheckOutSuccessContract.Presenter mPresenter;

    public CheckOutSuccessDialog() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.CheckOutSuccessDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_checkout_success, container, false);
        view.findViewById(R.id.button_checkout_success_shopping).setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.hideToolbarAndBottomNavigation();
    }

    @Override
    public void setPresenter(CheckOutSuccessContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.showToolbarAndBottomNavigation();
        mPresenter.onCheckOutSuccess();
    }
}
