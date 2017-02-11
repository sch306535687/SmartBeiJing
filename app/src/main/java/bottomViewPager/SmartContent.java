package bottomViewPager;

import android.app.Activity;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import sun.ch.base.Base_Activity;
import sun.ch.smartbeijing.MainActivity;

/**
 * Created by sunch on 2017/2/8.
 */
public class SmartContent extends Base_Activity {

    public SmartContent(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        title.setText("智慧服务");

        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainUI = (MainActivity) mActivity;
                SlidingMenu slidingMenu = mainUI.getSlidingMenu();
                slidingMenu.toggle();
            }
        });
    }
}
