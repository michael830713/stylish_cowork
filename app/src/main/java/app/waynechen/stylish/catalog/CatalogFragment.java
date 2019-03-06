package app.waynechen.stylish.catalog;

import static com.google.common.base.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.waynechen.stylish.R;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class CatalogFragment extends Fragment implements CatalogContract.View {

    private CatalogContract.Presenter mPresenter;
    private CatalogAdapter mCatalogAdapter;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public CatalogFragment() {}

    public static CatalogFragment newInstance() {
        return new CatalogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCatalogAdapter = new CatalogAdapter(getChildFragmentManager(), mPresenter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(CatalogContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_catalog, container, false);

        mTabLayout = root.findViewById(R.id.tabs_catalog);

        mViewPager = root.findViewById(R.id.viewpager_catalog);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mCatalogAdapter);
        mViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

    }

    @Override
    public boolean isActive() {
        return !isHidden();
    }
}
