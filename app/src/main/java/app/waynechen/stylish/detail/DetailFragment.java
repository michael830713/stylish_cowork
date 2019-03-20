package app.waynechen.stylish.detail;

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

import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import app.waynechen.stylish.R;
import app.waynechen.stylish.data.Product;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class DetailFragment extends Fragment implements DetailContract.View {

    private DetailContract.Presenter mPresenter;
    private DetailAdapter mDetailAdapter;
    private YouTubePlayerView mYoutubePlayerView;
    private Product mProduct;

    public DetailFragment() {
    }

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailAdapter = new DetailAdapter(mPresenter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_detail);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mDetailAdapter);

        root.findViewById(R.id.button_detail_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProduct != null) {
                    mPresenter.showAdd2CartDialog(mProduct);
                }
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.hideToolbarAndBottomNavigation();
        mPresenter.loadDetailProductData();
    }

    @Override
    public void showDetailUi(Product product) {
        mProduct = product;
        mDetailAdapter.updateData(product);
    }

    @Override
    public boolean isActive() {
        return !isHidden();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mPresenter.updateCartBadge();
        mPresenter.showToolbarAndBottomNavigation();
    }
}
