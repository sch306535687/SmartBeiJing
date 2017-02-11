package sun.ch.base;

import android.app.Activity;
import android.view.View;

/**
 * 基类
 * Created by sunch on 2017/2/8.
 */
public abstract class Left_Menu_Base_Activity {

    public Activity mActivity;
    public View mView;

    public Left_Menu_Base_Activity(Activity activity){
        mActivity = activity;
        mView = initView();
    }

    /**
     * 初始化界面
     * @return 返回View
     */
    public abstract View initView();

    /**
     * 初始化数据
     */
    public abstract void initData();
}
