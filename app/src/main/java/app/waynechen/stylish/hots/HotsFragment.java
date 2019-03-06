package app.waynechen.stylish.hots;

import static com.google.common.base.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.waynechen.stylish.R;
import app.waynechen.stylish.data.source.bean.GetMarketingHots;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class  HotsFragment extends Fragment implements HotsContract.View {

    private HotsContract.Presenter mPresenter;
    private HotsAdapter mHotsAdapter;

    public HotsFragment() {}

    public static HotsFragment newInstance() {
        return new HotsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHotsAdapter = new HotsAdapter(mPresenter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(HotsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hots, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_hots);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(mHotsAdapter);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadHotsData();
    }

    @Override
    public void showHotsUi(GetMarketingHots bean) {
        mHotsAdapter.updateData(bean.combineHotsList());
    }

    @Override
    public boolean isActive() {
        return !isHidden();
    }
}
