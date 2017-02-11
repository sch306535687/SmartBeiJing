package bottomViewPager;

import android.app.Activity;
import android.view.View;

import sun.ch.base.Base_Activity;

/**
 * Created by sunch on 2017/2/8.
 */
public class SettingContent extends Base_Activity {

    public SettingContent(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        title.setText("设置");
        img_btn.setVisibility(View.GONE);
    }
}
