package app.waynechen.stylish.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import app.waynechen.stylish.MainContract;
import app.waynechen.stylish.R;
import app.waynechen.stylish.util.Constants;
import app.waynechen.stylish.util.UserManager;
import app.waynechen.stylish.util.Util;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class LoginDialog extends AppCompatDialogFragment implements View.OnClickListener {

    public static final int FROM_EVERYWHERE = 0x40;
    public static final int FROM_PROFILE    = 0x41;
    public static final int FROM_PAYMENT    = 0x42;
    public static final int FROM_DRAWER     = 0x43;

    private boolean mIsLoading = false;
    MainContract.Presenter mMainPresenter;
    private ConstraintLayout mLayout;
    private int mLoginFrom = FROM_EVERYWHERE;

    public LoginDialog() {}

    public void setMainPresenter(MainContract.Presenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.LoginDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_login, container, false);
        view.setOnClickListener(this);

        view.findViewById(R.id.button_login_facebook).setOnClickListener(this);
        view.findViewById(R.id.button_login_close).setOnClickListener(this);
        Util.setTouchDelegate(view.findViewById(R.id.button_login_close));

        mLayout = view.findViewById(R.id.layout_login);
        mLayout.setOnClickListener(this);
        mLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_up));

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.layout_login) {
            /* Avoid Dismiss */
        } else if (v.getId() == R.id.button_login_facebook) {

            if (!isLoading()) {
                setLoading(true);
                UserManager.getInstance().loginStylishByFacebook(getActivity(), new UserManager.LoadCallback() {
                    @Override
                    public void onSuccess() {

                        setLoading(false);

                        dismiss();

                        if (mMainPresenter != null) {
                            mMainPresenter.showLoginSuccessDialog();
                            mMainPresenter.onLoginSuccess(getLoginFrom());
                        }
                    }

                    @Override
                    public void onFail(String errorMessage) {

                        setLoading(false);
                    }

                    @Override
                    public void onInvalidToken(String errorMessage) {

                        setLoading(false);
                    }
                });
            }
        } else {

            dismiss();
        }
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void setLoading(boolean loading) {
        setCancelable(!loading);
        mIsLoading = loading;
    }

    public int getLoginFrom() {
        return mLoginFrom;
    }

    public void setLoginFrom(int loginFrom) {
        mLoginFrom = loginFrom;
    }

    @Override
    public void dismiss() {

        mLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_down));

        new Handler().postDelayed(super::dismiss, 200);
    }
}
