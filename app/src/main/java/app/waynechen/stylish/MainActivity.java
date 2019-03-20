package app.waynechen.stylish;

import static app.waynechen.stylish.profile.ProfileFragment.PICK_IMAGE;
import static com.google.common.base.Preconditions.checkNotNull;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import app.waynechen.stylish.catalog.item.CatalogItemFragment;
import app.waynechen.stylish.component.ProfileAvatarOutlineProvider;
import app.waynechen.stylish.data.Product;
import app.waynechen.stylish.dialog.LoginDialog;
import app.waynechen.stylish.dialog.MessageDialog;
import app.waynechen.stylish.util.Constants;
import app.waynechen.stylish.util.ImageManager;
import app.waynechen.stylish.util.RealPathUtil;
import app.waynechen.stylish.util.UserManager;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class MainActivity extends BaseActivivty implements MainContract.View,
        NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 300;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private BottomNavigationView mBottomNavigation;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private ImageView mToolbarLogo;
    private LoginDialog mLoginDialog;
    private MessageDialog mMessageDialog;
    private View mBadge;
    private ImageView mDrawerUserImage;
    private TextView mDrawerUserName;
    private TextView mDrawerUserInfo;
    private MainMvpController mMainMvpController;

    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, LogoActivity.class));

        init();

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

            return;
        }

    }

    private void init() {
        setContentView(R.layout.activity_main);

        mMainMvpController = MainMvpController.create(this);
        mPresenter.openHots();

        setToolbar();
        setBottomNavigation();
        setDrawerLayout();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();

            String realpath = getRealPathFromURI(getApplicationContext(), imageUri);

//            Log.d("MainActivitys", "getRealPathFromURI: "+fff);
//            String realpath = RealPathUtil.getRealPath(getApplicationContext(), imageUri);
//            Log.d("MainActivitys", "onActivityResult: "+realpath);
            mPresenter.onGalleryImagePicked(imageUri, realpath);

        } else {
            UserManager.getInstance().getFbCallbackManager().onActivityResult(requestCode, resultCode, data);

        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null
                , MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    /**
     * Let toolbar to extend to status bar.
     *
     * @notice this method have to be used after setContentView.
     */
    private void setToolbar() {
        // Retrieve the AppCompact Toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        // Set the padding to match the Status Bar height
        mToolbar.setPadding(0, getStatusBarHeight(), 0, 0);

        mToolbarTitle = mToolbar.findViewById(R.id.text_toolbar_title);
        mToolbarLogo = mToolbar.findViewById(R.id.image_toolbar_logo);
    }

    /**
     * Set the title of toolbar.
     *
     * @param title:
     */
    @Override
    public void setToolbarTitleUi(String title) {

        if ("".equals(title)) {

            mToolbarTitle.setVisibility(View.GONE);
            mToolbarLogo.setVisibility(View.VISIBLE);

        } else {

            if (title.equals(getString(R.string.payment))) {
                mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                mToolbar.setNavigationIcon(R.drawable.toolbar_back);
                mActionBarDrawerToggle.setToolbarNavigationClickListener(v -> onBackPressed());
            } else {
                mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                mToolbar.setNavigationIcon(R.drawable.toolbar_menu);
                mActionBarDrawerToggle.setToolbarNavigationClickListener(null);
            }

            mToolbarLogo.setVisibility(View.GONE);
            mToolbarTitle.setVisibility(View.VISIBLE);
            mToolbarTitle.setText(title);
        }
    }

    @Override
    public void closeDrawerUi() {
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void showDrawerUserUi() {

        ImageManager.getInstance().setImageByUrl(mDrawerUserImage,
                UserManager.getInstance().getUser().getPicture());

        mDrawerUserName.setText(UserManager.getInstance().getUser().getName());
        mDrawerUserInfo.setText(UserManager.getInstance().getUserInfo());
    }

    /**
     * plugin: BottomNavigationViewEx.
     */
    private void setBottomNavigation() {

        mBottomNavigation = findViewById(R.id.bottom_navigation_main);
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationMenuView menuView =
                (BottomNavigationMenuView) mBottomNavigation.getChildAt(0);

        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            layoutParams.height = (int) getResources().getDimension(R.dimen.size_bottom_nav_icon);
            layoutParams.width = (int) getResources().getDimension(R.dimen.size_bottom_nav_icon);
            iconView.setLayoutParams(layoutParams);
        }

        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(2);
        mBadge = LayoutInflater.from(this)
                .inflate(R.layout.badge_main_bottom, itemView, true);

        mPresenter.updateCartBadge();
    }

    /**
     * Set Drawer
     */
    private void setDrawerLayout() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.layout_main);
        mDrawerLayout.setFitsSystemWindows(true);
        mDrawerLayout.setClipToPadding(false);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                mPresenter.onDrawerOpened();
            }
        };
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        // nav view header
        mDrawerUserImage = navigationView.getHeaderView(0).findViewById(R.id.image_drawer_avatar);
        mDrawerUserImage.setOutlineProvider(new ProfileAvatarOutlineProvider());
        mDrawerUserImage.setOnClickListener(v -> mPresenter.onClickDrawerAvatar());

        mDrawerUserName = navigationView.getHeaderView(0).findViewById(R.id.image_drawer_name);

        mDrawerUserInfo = navigationView.getHeaderView(0).findViewById(R.id.image_drawer_info);
    }

    /**
     * @return height of status bar
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * It's the item selected listener of bottom navigation.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {

        switch (item.getItemId()) {
            case R.id.navigation_home:

                mPresenter.updateToolbar("");
                mPresenter.openHots();
                return true;

            case R.id.navigation_catalog:

                mPresenter.updateToolbar(getResources().getString(R.string.catalog));
                mPresenter.openCatalog();
                return true;

            case R.id.navigation_cart:

                mPresenter.updateToolbar(getResources().getString(R.string.cart));
                mPresenter.openCart();
                return true;

            case R.id.navigation_profile:

                if (mPresenter.openProfile()) {
                    mPresenter.updateToolbar(getResources().getString(R.string.profile));
                    return true;
                }
                return false;
            default:
                return false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        String string = "";

        switch (item.getItemId()) {

            case R.id.nav_awaiting_payment:
                string = getString(R.string.awaiting_payment);
                break;
            case R.id.nav_awaiting_shipment:
                string = getString(R.string.awaiting_shipment);
                break;
            case R.id.nav_shipped:
                string = getString(R.string.shipped);
                break;
            case R.id.nav_awaiting_review:
                string = getString(R.string.awaiting_review);
                break;
            case R.id.nav_exchange:
                string = getString(R.string.exchange);
                break;
            default:
        }

        Toast.makeText(this, getString(R.string._coming_soon, string), Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void openHotsUi() {
        mMainMvpController.findOrCreateHotsView();
    }

    @Override
    public void openCatalogUi() {
        mMainMvpController.findOrCreateCatalogView();
    }

    @Override
    public void openProfileUi() {
        mMainMvpController.findOrCreateProfileView();
    }

    @Override
    public void openCartUi() {
        mMainMvpController.findOrCreateCartView();
    }

    @Override
    public void openPaymentUi() {

        mPresenter.updateToolbar(getResources().getString(R.string.payment));
        mMainMvpController.findOrCreatePaymentView();
    }

    @Override
    public void openAdd2CartUi(Product product) {
        mMainMvpController.findOrCreateAdd2CartView(product);
    }

    @Override
    public void openCheckOutSuccessUi() {
        mMainMvpController.findOrCreateCheckOutSuccessView();
    }

    @Override
    public void openDetailUi(Product product) {
        mMainMvpController.findOrCreateDetailView(product);
    }

    @Override
    public void finishDetailUi() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void finishPaymentUi() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public CatalogItemFragment findWomenView() {
        return mMainMvpController.findOrCreateWomenView();
    }

    @Override
    public CatalogItemFragment findMenView() {
        return mMainMvpController.findOrCreateMenView();
    }

    @Override
    public CatalogItemFragment findAccessoriesView() {
        return mMainMvpController.findOrCreateAccessoriesView();
    }

    @Override
    public FavoriteFragment findFavoriteView() {
        return mMainMvpController.findOrCreateFavoriteView();
    }

    @Override
    public void switchProfileUiInitiative() {
        mBottomNavigation.setSelectedItemId(R.id.navigation_profile);
    }

    @Override
    public void switchHotsUiInitiative() {
        mBottomNavigation.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public void openLoginUi(int loginFrom) {

        if (mLoginDialog == null) {

            mLoginDialog = new LoginDialog();
            mLoginDialog.setMainPresenter(mPresenter);
            mLoginDialog.setLoginFrom(loginFrom);
            mLoginDialog.show(getSupportFragmentManager(), Constants.LOGIN);

        } else if (!mLoginDialog.isAdded()) {

            mLoginDialog.setLoginFrom(loginFrom);
            mLoginDialog.show(getSupportFragmentManager(), Constants.LOGIN);
        }
    }

    @Override
    public void showMessageDialogUi(@MessageDialog.MessageType int type) {

        if (mMessageDialog == null) {

            mMessageDialog = new MessageDialog();
            mMessageDialog.setMessage(type);
            mMessageDialog.show(getSupportFragmentManager(), "");

        } else if (!mMessageDialog.isAdded()) {

            mMessageDialog.setMessage(type);
            mMessageDialog.show(getSupportFragmentManager(), "");
        }
    }

    @Override
    public void showToastUi(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void hideToolbarUi() {
        mToolbar.setVisibility(View.GONE);
    }

    @Override
    public void showToolbarUi() {
        mToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomNavigationUi() {
        mBottomNavigation.setVisibility(View.GONE);
    }

    @Override
    public void showBottomNavigationUi() {
        mBottomNavigation.setVisibility(View.VISIBLE);
    }


    @Override
    public void updateCartBadgeUi(int amount) {

        if (amount > 0) {
            mBadge.findViewById(R.id.text_badge_main).setVisibility(View.VISIBLE);
            ((TextView) mBadge.findViewById(R.id.text_badge_main)).setText(String.valueOf(amount));
        } else {
            mBadge.findViewById(R.id.text_badge_main).setVisibility(View.GONE);
        }
    }
}
