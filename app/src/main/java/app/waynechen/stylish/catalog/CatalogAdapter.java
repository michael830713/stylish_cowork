package app.waynechen.stylish.catalog;

import static app.waynechen.stylish.MainMvpController.ACCESSORIES;
import static app.waynechen.stylish.MainMvpController.MEN;
import static app.waynechen.stylish.MainMvpController.WOMEN;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class CatalogAdapter extends FragmentPagerAdapter {

    private FragmentManager mFragmentManager;
    private CatalogContract.Presenter mPresenter;
    private String[] mItemTypes = new String[] {WOMEN, MEN, ACCESSORIES};

    public CatalogAdapter(FragmentManager fragmentManager, CatalogContract.Presenter presenter) {
        super(fragmentManager);

        mFragmentManager = fragmentManager;
        mPresenter = presenter;
    }

    @Override
    public Fragment getItem(int i) {

        switch (mItemTypes[i]) {
            case WOMEN:
                return mPresenter.findWomen();
            case MEN:
                return mPresenter.findMen();
            case ACCESSORIES:
            default:
                return mPresenter.findAccessories();
        }
    }

    @Override
    public int getCount() {
        return mItemTypes.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mItemTypes[position];
    }

    public String getItemType(int position) {
        return mItemTypes[position];
    }
}
