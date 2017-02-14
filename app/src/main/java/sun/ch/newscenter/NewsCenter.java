package sun.ch.newscenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import sun.ch.base.Left_Menu_Base_Activity;
import sun.ch.domain.NewsListData;
import sun.ch.domain.NewsMenuData;
import sun.ch.global.GlobalData;
import sun.ch.smartbeijing.R;

/**
 * Created by sunch on 2017/2/10.
 */
public class NewsCenter extends Left_Menu_Base_Activity {

    public View view;
    private ViewPager news_viewpager;
    private ListView news_listview;

    public NewsMenuData.NewsTabData mData;
    private String url;
    private ArrayList<NewsListData.TopNews> topnews;
    private CirclePageIndicator indicator;
    private TextView tv_title;
    private View header;
    private SharedPreferences sharedPreferences;

    public NewsCenter(Activity activity, NewsMenuData.NewsTabData data) {
        super(activity);
        mData = data;
    }

    @Override
    public View initView() {

        sharedPreferences = mActivity.getSharedPreferences("cache", Context.MODE_PRIVATE);

        view = View.inflate(mActivity, R.layout.activity_news_list, null);
        news_listview = (ListView) view.findViewById(R.id.news_listview);

        //添加布局头
        header = View.inflate(mActivity, R.layout.listview_header, null);
        news_listview.addHeaderView(header);

        news_viewpager = (ViewPager) header.findViewById(R.id.news_viewpager);
        indicator = (CirclePageIndicator) header.findViewById(R.id.indicator);
        tv_title = (TextView) header.findViewById(R.id.tv_title);

        return view;
    }

    @Override
    public void initData() {

        url = GlobalData.bseUrl + mData.url;

        String topnews_list_cache = sharedPreferences.getString("topnews_list_cache", null);
        if(topnews_list_cache!=null){
            System.out.println("从缓存获取数据");
            initNewsData(topnews_list_cache);
        }

        getDataFromServer(url);

    }

    //从服务器获取json数据,并封装成对象
    public void getDataFromServer(String url) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                //缓存数据
                sharedPreferences.edit().putString("topnews_list_cache", result).commit();

                //初始化数据
                initNewsData(result);

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    /**
     * 初始化数据
     */
    public void initNewsData(String result) {

        Gson gson = new Gson();
        NewsListData newsListData = gson.fromJson(result, NewsListData.class);
        topnews = newsListData.data.topnews;

        tv_title.setText(topnews.get(0).title);//初始化标题

        //viewpager数据设置
        MyNewsPagerAdapter myNewsPagerAdapter = new MyNewsPagerAdapter();

        if(topnews!=null){
            news_viewpager.setAdapter(myNewsPagerAdapter);
        }

        indicator.setViewPager(news_viewpager);
        indicator.setSnap(true);
        indicator.onPageSelected(0);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //设置标题
                tv_title.setText(topnews.get(position).title);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //listview数据设置
        MyNewsListViewAdapter myNewsListViewAdapter = new MyNewsListViewAdapter();
        if(topnews!=null){
            news_listview.setAdapter(myNewsListViewAdapter);
        }
    }

    public class MyNewsPagerAdapter extends PagerAdapter {

        public BitmapUtils bitmapUtils;

        public MyNewsPagerAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadingImage(R.mipmap.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            bitmapUtils.display(imageView, topnews.get(position).topimage);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public class MyNewsListViewAdapter extends BaseAdapter {

        public BitmapUtils bitmapUtils;

        public MyNewsListViewAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadingImage(R.mipmap.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public NewsListData.TopNews getItem(int i) {
            return topnews.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = View.inflate(mActivity, R.layout.news_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.img = (ImageView) view.findViewById(R.id.iv_icon);
                viewHolder.title = (TextView) view.findViewById(R.id.tv_title);
                viewHolder.date = (TextView) view.findViewById(R.id.tv_date);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            NewsListData.TopNews news = getItem(i);

            bitmapUtils.display(viewHolder.img, news.topimage);
            viewHolder.title.setText(news.title);
            viewHolder.date.setText(news.pubdate);

            return view;
        }
    }

    public class ViewHolder {
        public ImageView img;
        public TextView title;
        public TextView date;
    }
}
