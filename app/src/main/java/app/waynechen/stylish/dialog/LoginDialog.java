package app.waynechen.stylish.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import app.waynechen.stylish.MainActivity;
import app.waynechen.stylish.MainContract;
import app.waynechen.stylish.R;
import app.waynechen.stylish.util.Constants;
import app.waynechen.stylish.util.UserManager;
import app.waynechen.stylish.util.Util;

import static com.facebook.share.internal.DeviceShareDialogFragment.TAG;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class LoginDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private static final String TAG = "LoginDialog";
    public static final int FROM_EVERYWHERE = 0x40;
    public static final int FROM_PROFILE = 0x41;
    public static final int FROM_PAYMENT = 0x42;
    public static final int FROM_DRAWER = 0x43;

    private boolean mIsLoading = false;
    MainContract.Presenter mMainPresenter;
    private ConstraintLayout mLayout;
    private int mLoginFrom = FROM_EVERYWHERE;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;

    public LoginDialog() {
    }

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

        mEmail = view.findViewById(R.id.editTextEmail);
        mPassword = view.findViewById(R.id.editTextPassword);
        mName = view.findViewById(R.id.editTextName);

        view.findViewById(R.id.button_login_facebook).setOnClickListener(this);
        view.findViewById(R.id.buttonSignUp).setOnClickListener(this);
        view.findViewById(R.id.buttonSignIn).setOnClickListener(this);
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
        } else if (v.getId() == R.id.buttonSignUp) {

            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();
            String name = mName.getText().toString();

//            if (isEmail(mEmail) && isSixDigit(mPassword) && !name.isEmpty()) {
//                UserManager.getInstance()
//                        .signUpStylish(name, email, password, new UserManager.LoadCallback() {
//                            @Override
//                            public void onSuccess() {
//
//                                setLoading(false);
//
//                                dismiss();
//
//                                if (mMainPresenter != null) {
//                                    mMainPresenter.showLoginSuccessDialog();
//                                    mMainPresenter.onLoginSuccess(getLoginFrom());
//                                }
//                            }
//
//                            @Override
//                            public void onFail(String errorMessage) {
//                                Log.d(TAG, "onFail: " + errorMessage);
//                                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
//                                setLoading(false);
//                            }
//
//                            @Override
//                            public void onInvalidToken(String errorMessage) {
//                                Log.d(TAG, "onInvalidToken: " + errorMessage);
//                                setLoading(false);
//                            }
//                        });
//
//            }else
            if (name.isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter User Name!", Toast.LENGTH_SHORT).show();

            } else if (!isEmail(mEmail)) {
                Log.d(TAG, "this is not an email ");
                Toast.makeText(getActivity(), "this is not an email", Toast.LENGTH_SHORT).show();
            } else if (!isSixDigit(mPassword)) {
                Toast.makeText(getActivity(), "Password require more than 6 digits!", Toast.LENGTH_SHORT)
                        .show();
            } else {
                UserManager.getInstance()
                        .signUpStylish(name, email, password, new UserManager.LoadCallback() {
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
                                Log.d(TAG, "onFail: " + errorMessage);
                                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                                setLoading(false);
                            }

                            @Override
                            public void onInvalidToken(String errorMessage) {
                                Log.d(TAG, "onInvalidToken: " + errorMessage);
                                setLoading(false);
                            }
                        });
//                Toast.makeText(getActivity(), "Enter Email and password!", Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.buttonSignIn) {
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();
            if (!isEmail(mEmail)) {
                Toast.makeText(getActivity(), "this is not an email", Toast.LENGTH_SHORT).show();

            } else if (!isSixDigit(mPassword)) {
                Toast.makeText(getActivity(), "Password require more than 6 digits!", Toast.LENGTH_SHORT)
                        .show();
            } else {
                UserManager.getInstance().loginStylishNative(email, password, new UserManager.LoadCallback() {
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
                        Log.d(TAG, "onFail: " + errorMessage);
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                        setLoading(false);
                    }

                    @Override
                    public void onInvalidToken(String errorMessage) {
                        Log.d(TAG, "onInvalidToken: " + errorMessage);
                        setLoading(false);
                    }
                });
            }
        } else {

            dismiss();
        }
    }

    public boolean isSixDigit(EditText password) {
        return password.getText().toString().length() >= 5;

    }

    public boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
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
