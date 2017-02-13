package sun.ch.leftMenu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import sun.ch.base.Left_Menu_Base_Activity;
import sun.ch.domain.NewsMenuData;
import sun.ch.newscenter.NewsCenter;
import sun.ch.smartbeijing.MainActivity;
import sun.ch.smartbeijing.R;

/**
 * Created by sunch on 2017/2/9.
 */
public class Left_News extends Left_Menu_Base_Activity {

    public  ViewPager newscenter_viewpager;
    public ArrayList<NewsMenuData.NewsTabData> mData;
    private ArrayList<NewsCenter> viewList;
    private MyAdapter myAdapter;
    private TabPageIndicator mTabPageIndicator;


    public Left_News(Activity activity, ArrayList<NewsMenuData.NewsTabData> children) {
        super(activity);
        mData = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.activity_newscenter, null);
        newscenter_viewpager = (ViewPager) view.findViewById(R.id.newscenter_viewpager);
        mTabPageIndicator = (TabPageIndicator) view.findViewById(R.id.view_pager_indicator);
        return view;
    }

    @Override
    public void initData() {

        viewList = new ArrayList<NewsCenter>();
        for (NewsMenuData.NewsTabData n : mData) {
            NewsCenter newsCenter = new NewsCenter(mActivity);
            viewList.add(newsCenter);
        }
        myAdapter = new MyAdapter();
        newscenter_viewpager.setAdapter(myAdapter);
        mTabPageIndicator.setVisibility(View.VISIBLE);
        mTabPageIndicator.setViewPager(newscenter_viewpager);
        mTabPageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                MainActivity mainUI = (MainActivity) mActivity;
                SlidingMenu slidingMenu = mainUI.getSlidingMenu();
                if(position>0){
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }else {
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    ;

    public class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mData.get(position).title;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            NewsCenter newsCenter = viewList.get(position);
            newsCenter.textView.setText(mData.get(position).title);
            container.addView(newsCenter.mView);
            return newsCenter.mView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
