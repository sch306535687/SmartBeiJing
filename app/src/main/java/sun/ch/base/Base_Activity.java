package sun.ch.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import sun.ch.smartbeijing.R;

/**
 * 基类
 * Created by sunch on 2017/2/8.
 */
public abstract class Base_Activity {

    public Activity mActivity;
    public View mRootView;
    public TextView title;
    public FrameLayout fl_frameLayout;
    public ImageButton img_btn;
    public ImageButton list_grid_btn;

    public Base_Activity(Activity activity){
        mActivity = activity;
        initView();
    }

    /**
     * 初始化界面
     * @return 返回View
     */
    public void initView(){
        mRootView = View.inflate(mActivity, R.layout.base_activity, null);
        title = (TextView) mRootView.findViewById(R.id.title);
        fl_frameLayout = (FrameLayout) mRootView.findViewById(R.id.fl_frameLayout);
        img_btn = (ImageButton) mRootView.findViewById(R.id.img_btn);
        list_grid_btn = (ImageButton) mRootView.findViewById(R.id.list_grid_btn);
    }

    /**
     * 初始化数据
     */
    public abstract void initData();
}
