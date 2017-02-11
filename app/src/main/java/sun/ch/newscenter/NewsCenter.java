package sun.ch.newscenter;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import sun.ch.base.Left_Menu_Base_Activity;

/**
 * Created by sunch on 2017/2/10.
 */
public class NewsCenter extends Left_Menu_Base_Activity {

    public TextView textView;

    public NewsCenter(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        textView = new TextView(mActivity);
        textView.setText("新闻中心");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {

    }
}
