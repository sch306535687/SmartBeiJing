package sun.ch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import sun.ch.smartbeijing.R;

/**
 * Created by Administrator on 2017/2/14.
 */

public class CustomRefreshListView extends ListView {

    private static final int REFRESH_DOWN = 1;
    private static final int REFRESH_UP = 2;
    private static final int REFRESHING = 3;
    private ImageView arrow;
    private ProgressBar pb;
    private TextView title;
    private TextView time;
    private View refreshView;
    private int measuredHeight;

    private int refreshState;

    public CustomRefreshListView(Context context) {
        super(context);
        initView();
    }

    public CustomRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        refreshView = View.inflate(getContext(), R.layout.list_news_refresh_header, null);
        arrow = (ImageView) refreshView.findViewById(R.id.iv_arrow);
        pb = (ProgressBar) refreshView.findViewById(R.id.pb_loading);
        title = (TextView) refreshView.findViewById(R.id.tv_title);
        time = (TextView) refreshView.findViewById(R.id.tv_time);

        //添加头布局
        this.addHeaderView(refreshView);
        //获取测量后的布局高度
        refreshView.measure(0, 0);
        measuredHeight = refreshView.getMeasuredHeight();
        refreshView.setPadding(0, -measuredHeight, 0, 0);
    }

    int startY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                if(refreshState == REFRESHING){
                    break;
                }

                if (startY == -1) {//手指有可能点在viewpager上而获取不到startY值,所以需要重新赋值
                    startY = (int) ev.getY();
                }
                int endY = (int) ev.getY();
                int dy = endY - startY;

                if (dy > 0 && dy<measuredHeight) {//向下滑动
                    refreshState = REFRESH_DOWN;//记录向下活动状态
                    initState();
                }else if(dy >= measuredHeight){
                    refreshState = REFRESH_UP;//记录向上活动状态
                    initState();
                }

                refreshView.setPadding(0, dy-measuredHeight, 0, 0);//重新设置布局padding值

                break;
            case MotionEvent.ACTION_UP:
                if(refreshState == REFRESH_DOWN){
                    refreshView.setPadding(0, -measuredHeight, 0, 0);//重新设置布局padding值
                    refreshState = 0;
                }else if(refreshState == REFRESH_UP){
                    refreshView.setPadding(0, 0, 0, 0);//重新设置布局padding值
                    refreshState = REFRESHING;
                    initState();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }
    //根据三个状态初始化数据
    public void initState(){
        switch (refreshState){
            case REFRESH_DOWN:
                title.setText("向下刷新");
                RotateAnimation rotateUpAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateUpAnimation.setDuration(500);
                rotateUpAnimation.setFillAfter(true);
                arrow.startAnimation(rotateUpAnimation);
                break;
            case REFRESH_UP:
                title.setText("向上刷新");
                RotateAnimation rotateDownAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateDownAnimation.setDuration(500);
                rotateDownAnimation.setFillAfter(true);
                arrow.startAnimation(rotateDownAnimation);
                break;
            case REFRESHING:
                title.setText("正在刷新");
                pb.setVisibility(VISIBLE);
                arrow.clearAnimation();
                arrow.setVisibility(INVISIBLE);
                mOnRefreshData.refreshData();//调用刷新数据
                break;
        }
    }

    private OnRefreshData mOnRefreshData;

    public void setOnRefreshData(OnRefreshData onRefreshData){
        mOnRefreshData = onRefreshData;
    }
    //定义外部刷新接口
    public interface OnRefreshData{
        public void refreshData();
    }
}
