package sun.ch.leftMenu;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import sun.ch.base.Left_Menu_Base_Activity;

/**
 * Created by sunch on 2017/2/9.
 */
public class Left_Interact extends Left_Menu_Base_Activity {

    public Left_Interact(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mActivity);
        textView.setText("互动");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        return  textView;
    }

    @Override
    public void initData() {

    }
}
