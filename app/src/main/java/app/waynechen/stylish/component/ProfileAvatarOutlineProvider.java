package app.waynechen.stylish.component;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

import app.waynechen.stylish.R;
import app.waynechen.stylish.Stylish;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class ProfileAvatarOutlineProvider extends ViewOutlineProvider {
    @Override
    public void getOutline(View view, Outline outline) {
        view.setClipToOutline(true);
        int radius = Stylish.getAppContext().getResources().getDimensionPixelSize(R.dimen.radius_profile_avatar);
        outline.setOval(0, 0, radius, radius);
    }
}
