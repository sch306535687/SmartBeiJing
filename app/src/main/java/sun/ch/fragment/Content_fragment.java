package sun.ch.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;

import sun.ch.base.Base_Activity;
import sun.ch.smartbeijing.MainActivity;
import sun.ch.smartbeijing.R;
import bottomViewPager.GovContent;
import bottomViewPager.MainContent;
import bottomViewPager.NewsContent;
import bottomViewPager.SettingContent;
import bottomViewPager.SmartContent;

/**
 * Created by sunch on 2017/2/8.
 */
public class Content_fragment extends Fragment {
    private Activity mActivity;
    private ArrayList viewList;
    private ViewPager view_pager;
    private RadioGroup radioGroup;
    public static NewsContent newsContent;
    private MainActivity mainUI;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mainUI = (MainActivity) mActivity;

        //定义集合存放View对象
        viewList = new ArrayList();
        //获取View对象,放入集合
        MainContent mainContent = new MainContent(mActivity);
        viewList.add(mainContent);
        newsContent = new NewsContent(mActivity);
        viewList.add(newsContent);
        GovContent govContent = new GovContent(mActivity);
        viewList.add(govContent);
        SmartContent smartContent = new SmartContent(mActivity);
        viewList.add(smartContent);
        SettingContent settingContent = new SettingContent(mActivity);
        viewList.add(settingContent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_fragment, null);
        view_pager = (ViewPager) view.findViewById(R.id.view_pager);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        view_pager.setAdapter(new MyAdapter());

        mainUI.stopSlidingMenu();//初始化禁止滑动
        //viewpager页面改变事件
        view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println(position);
                if(position == 0 || position == 4){
                    mainUI.stopSlidingMenu();
                }else {
                    mainUI.startSlidingMenu();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //监听按钮状态改变事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.tab_main:
                        view_pager.setCurrentItem(0,false);//添加false表示禁止页面切换效果
                        MainContent mainContent = (MainContent) viewList.get(0);
                        mainContent.initData();
                        break;
                    case R.id.tab_news:
                        view_pager.setCurrentItem(1,false);
                        NewsContent newsContent = (NewsContent) viewList.get(1);
                        newsContent.initData();
                        break;
                    case R.id.tab_smart:
                        view_pager.setCurrentItem(2,false);
                        GovContent govContent = (GovContent) viewList.get(2);
                        govContent.initData();
                        break;
                    case R.id.tab_gov:
                        view_pager.setCurrentItem(3,false);
                        SmartContent smartContent = (SmartContent) viewList.get(3);
                        smartContent.initData();
                        break;
                    case R.id.tab_setting:
                        view_pager.setCurrentItem(4);
                        SettingContent settingContent = (SettingContent) viewList.get(4);
                        settingContent.initData();
                        break;
                }
            }
        });

        //初始化首页标题
        MainContent mainContent = (MainContent) viewList.get(0);
        mainContent.initData();

        return view;
    }


    public class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Base_Activity baseActivity = (Base_Activity) viewList.get(position);
            View view = baseActivity.mRootView;
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
