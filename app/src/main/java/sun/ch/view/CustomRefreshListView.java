package sun.ch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import sun.ch.smartbeijing.R;

/**
 * Created by Administrator on 2017/2/14.
 */

public class CustomRefreshListView extends ListView implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private static final int REFRESH_DOWN = 1;
    private static final int REFRESH_UP = 2;
    private static final int REFRESHING = 3;
    private ImageView arrow;
    private ProgressBar pb;
    private TextView title;
    private TextView time;
    private View headerView;
    private int headerHeight;

    private int refreshState;
    private View footerView;
    private int footerHeight;

    public CustomRefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public CustomRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public CustomRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    public void initHeaderView() {
        headerView = View.inflate(getContext(), R.layout.list_news_refresh_header, null);
        arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        pb = (ProgressBar) headerView.findViewById(R.id.pb_loading);
        title = (TextView) headerView.findViewById(R.id.tv_title);
        time = (TextView) headerView.findViewById(R.id.tv_time);

        //添加头布局
        this.addHeaderView(headerView);
        //获取测量后的布局高度
        headerView.measure(0, 0);
        headerHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -headerHeight, 0, 0);


    }

    public void initFooterView() {
        footerView = View.inflate(getContext(), R.layout.list_news_refresh_footer, null);
        //添加头布局
        this.addFooterView(footerView);
        //获取测量后的布局高度
        footerView.measure(0, 0);
        footerHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -this.footerHeight, 0, 0);

        this.setOnScrollListener(this);//把滑动监听设置给当前控件
    }

    int startY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                if (refreshState == REFRESHING) {
                    break;
                }

                if (startY == -1) {//手指有可能点在viewpager上而获取不到startY值,所以需要重新赋值
                    startY = (int) ev.getY();
                }
                int endY = (int) ev.getY();
                int dy = endY - startY;

                if (getFirstVisiblePosition() == 0) {//必须显示位置在第一个条目才可以滑动
                    if (dy > 0 && dy < headerHeight) {//向下滑动
                        refreshState = REFRESH_DOWN;//记录向下活动状态
                        initState();
                    } else if (dy >= headerHeight) {
                        refreshState = REFRESH_UP;//记录向上活动状态
                        initState();
                    }

                    headerView.setPadding(0, dy - headerHeight, 0, 0);//重新设置布局padding值
                }

                break;
            case MotionEvent.ACTION_UP:
                if (refreshState == REFRESH_DOWN) {
                    headerView.setPadding(0, -headerHeight, 0, 0);//重新设置布局padding值
                    refreshState = 0;
                } else if (refreshState == REFRESH_UP) {
                    headerView.setPadding(0, 0, 0, 0);//重新设置布局padding值
                    refreshState = REFRESHING;
                    initState();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    //根据三个状态初始化数据
    public void initState() {
        switch (refreshState) {
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
                //SystemClock.sleep(5000);
                mOnRefreshData.refreshData();//调用刷新数据
                break;
        }
    }



    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            int lastVisiblePosition = getLastVisiblePosition();//获取显示的最后一个条目索引
            if (lastVisiblePosition >= getCount() - 1) {
                footerView.setPadding(0, 0, 0, 0);//显示底部刷新布局
                setSelection(getCount()-1);//直接定位到刷新布局位置
                //调用接口的加载更多方法
                mOnRefreshData.refreshMoreData();
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    private OnItemClickListener mOnItemClickListener;
    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
        super.setOnItemClickListener(this);//把父级的此事件交给当前控件监听
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(adapterView,view,i-2,l);
        }
    }

    private OnRefreshData mOnRefreshData;

    public void setOnRefreshData(OnRefreshData onRefreshData) {
        mOnRefreshData = onRefreshData;
    }


    //定义外部刷新接口
    public interface OnRefreshData {
        //刷新数据方法
        public void refreshData();
        //加载更多方法
        public void refreshMoreData();
    }

    //定义一个结束刷新方法供外部使用
    public void refreshComplete() {
        headerView.setPadding(0, -headerHeight, 0, 0);//重新设置布局padding值
        title.setText("向下刷新");
        arrow.setVisibility(VISIBLE);
        pb.setVisibility(INVISIBLE);
        refreshState = 0;
        footerView.setPadding(0, -this.footerHeight, 0, 0);
    }
}
