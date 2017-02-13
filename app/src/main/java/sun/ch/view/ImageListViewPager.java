package sun.ch.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by sunch on 2017/2/9.
 */
public class ImageListViewPager extends ViewPager {

    public ImageListViewPager(Context context) {
        super(context);
    }

    public ImageListViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int startX;
    public int startY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                //请求父控件不要拦截当前控件的事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();

                int x = endX - startX;
                int y = endY - startY;

                if (Math.abs(x) > Math.abs(y)) {//左右滑动

                    if (getCurrentItem() == 0) {//第一个页面

                        if (x > 0) {//向右滑动
                            //请求父控件不要拦截当前控件的事件
                            getParent().requestDisallowInterceptTouchEvent(false);
                        } else {//向左滑动
                            //请求父控件拦截当前控件的事件
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }

                    }

                    if (getCurrentItem() == getAdapter().getCount() - 1) {//最后一个页面

                        if (x > 0) {//向右滑动
                            //请求父控件拦截当前控件的事件
                            getParent().requestDisallowInterceptTouchEvent(true);
                        } else {//向左滑动
                            //请求父控件不要拦截当前控件的事件
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }

                    }

                } else {//上下滑动
                    //请求父控件不要拦截当前控件的事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
