package app.waynechen.stylish.profile;

import static com.facebook.share.internal.DeviceShareDialogFragment.TAG;
import static com.google.common.base.Preconditions.checkNotNull;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.waynechen.stylish.R;
import app.waynechen.stylish.component.ProfileAvatarOutlineProvider;
import app.waynechen.stylish.dialog.LoginDialog;
import app.waynechen.stylish.util.ImageManager;
import app.waynechen.stylish.util.UserManager;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class ProfileFragment extends Fragment implements ProfileContract.View {

    public static final int PICK_IMAGE = 100;
    private ProfileContract.Presenter mPresenter;

    private ImageView mImageAvatar;
    private TextView mTextName;
    private TextView mTextInformation;

    public ProfileFragment() {
        // Requires empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mImageAvatar = root.findViewById(R.id.image_profile_avatar);
        mImageAvatar.setOutlineProvider(new ProfileAvatarOutlineProvider());
        mTextName = root.findViewById(R.id.text_profile_name);
        mTextInformation = root.findViewById(R.id.text_profile_info);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.checkProfileUserData();
    }

    @Override
    public void showUserUi() {

        ImageManager.getInstance().setImageByUrl(mImageAvatar,
                UserManager.getInstance().getUser().getPicture());
        mTextName.setText(UserManager.getInstance().getUser().getName());
        mTextInformation.setText(UserManager.getInstance().getUserInfo());
        mImageAvatar.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            getActivity().startActivityForResult(gallery, PICK_IMAGE);

        });
    }


    @Override
    public boolean isActive() {
        return !isHidden();
    }

    @Override
    public void showLoginDialogUi() {
        mPresenter.showLoginDialog(LoginDialog.FROM_PROFILE);
    }

    @Override
    public void showImagePicker(Uri imageUri) {
        Log.d(TAG, "showImagePicker: "+imageUri);
        mImageAvatar.setImageURI(imageUri);
    }
}
