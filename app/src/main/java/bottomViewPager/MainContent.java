package bottomViewPager;

import android.app.Activity;
import android.view.View;

import sun.ch.base.Base_Activity;

/**
 * Created by sunch on 2017/2/8.
 */
public class MainContent extends Base_Activity {

    public MainContent(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        title.setText("首页");
        img_btn.setVisibility(View.GONE);
    }
}
