package app.waynechen.stylish.profile;

import static com.google.common.base.Preconditions.checkNotNull;

import android.net.Uri;
import android.support.annotation.NonNull;

import app.waynechen.stylish.data.source.StylishRepository;
import app.waynechen.stylish.util.UserManager;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class ProfilePresenter implements ProfileContract.Presenter {

    private final StylishRepository mStylishRepository;
    private final ProfileContract.View mProfileView;

    public ProfilePresenter(
            @NonNull StylishRepository stylishRepository,
            @NonNull ProfileContract.View profileView) {
        mStylishRepository = checkNotNull(stylishRepository, "stylishRepository cannot be null!");
        mProfileView = checkNotNull(profileView, "profileView cannot be null!");
        mProfileView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {
    }

    @Override
    public void start() {
    }

    @Override
    public void loadProfileUserData() {
        UserManager.getInstance().getUserProfile(new UserManager.LoadCallback() {
            @Override
            public void onSuccess() {
                mProfileView.showUserUi();
            }

            @Override
            public void onFail(String errorMessage) {
            }

            @Override
            public void onInvalidToken(String errorMessage) {
                mProfileView.showLoginDialogUi();
            }
        });
    }

    @Override
    public void checkProfileUserData() {

        if (UserManager.getInstance().hasUserInfo()) {

            mProfileView.showUserUi();
        } else {
            loadProfileUserData();
        }
    }

    @Override
    public void showLoginDialog(int loginFrom) {
    }

    @Override
    public void setGalleryImagePicked(Uri imageUri, String realPath) {
        mProfileView.showImagePickedFromGallery(imageUri, realPath);
    }
}
