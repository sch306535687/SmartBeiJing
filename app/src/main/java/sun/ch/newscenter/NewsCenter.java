package sun.ch.newscenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import sun.ch.smartbeijing.NewsArticleActivity;
import sun.ch.smartbeijing.R;
import sun.ch.view.CustomRefreshListView;

/**
 * Created by sunch on 2017/2/10.
 */
public class NewsCenter extends Left_Menu_Base_Activity {

    public View view;
    private ViewPager news_viewpager;
    private CustomRefreshListView news_listview;

    public NewsMenuData.NewsTabData mData;
    private String url;
    private String moreUrl;
    private CirclePageIndicator indicator;
    private TextView tv_title;
    private View header;
    private SharedPreferences sharedPreferences;
    private ArrayList<NewsListData.News> news;
    private NewsListData newsListData;

    private boolean isUpdateMore;
    private MyNewsPagerAdapter myNewsPagerAdapter;
    private MyNewsListViewAdapter myNewsListViewAdapter;
    private ArrayList<NewsListData.TopNews> topnews;

    private Handler mHandler = null;

    public NewsCenter(Activity activity, NewsMenuData.NewsTabData data) {
        super(activity);
        mData = data;
    }

    @Override
    public View initView() {

        sharedPreferences = mActivity.getSharedPreferences("cache", Context.MODE_PRIVATE);

        view = View.inflate(mActivity, R.layout.activity_news_list, null);
        news_listview = (CustomRefreshListView) view.findViewById(R.id.news_listview);
        //添加布局头
        header = View.inflate(mActivity, R.layout.listview_header, null);
        news_listview.addHeaderView(header);

        news_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        news_listview.setOnRefreshData(new CustomRefreshListView.OnRefreshData() {
            @Override
            public void refreshData() {
                System.out.println("正在刷新");
                getDataFromServer(url);//从新获取网络数据
            }

            @Override
            public void refreshMoreData() {
                isUpdateMore = true;//切换为加载更多数据状态
                //加载更多数据
                getDataFromServer(moreUrl);
            }
        });

        news_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("当前item为"+i);
                TextView title = (TextView) view.findViewById(R.id.tv_title);
                title.setTextColor(Color.GRAY);
                String newsId = news.get(i).id;
                String news_id = sharedPreferences.getString("news_id", "");

                if(!news_id.contains(newsId)){

                    news_id = news_id + newsId + ",";
                    sharedPreferences.edit().putString("news_id",news_id).commit();
                }
                String url = news.get(i).url;
                Intent intent = new Intent(mActivity, NewsArticleActivity.class);
                intent.putExtra("url",url);
                mActivity.startActivity(intent);
            }
        });

        news_viewpager = (ViewPager) header.findViewById(R.id.news_viewpager);
        indicator = (CirclePageIndicator) header.findViewById(R.id.indicator);
        tv_title = (TextView) header.findViewById(R.id.tv_title);

        return view;
    }

    @Override
    public void initData() {

        url = GlobalData.bseUrl + mData.url;

        String topnews_list_cache = sharedPreferences.getString("topnews_list_cache", null);
        if (topnews_list_cache != null) {
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

                if (!isUpdateMore) {
                    //初始化数据
                    initNewsData(result);

                }else {
                    Gson gson = new Gson();
                    NewsListData newsListData = gson.fromJson(result, NewsListData.class);
                    ArrayList<NewsListData.News> moreNews = newsListData.data.news;
                    news.addAll(moreNews);
                    myNewsListViewAdapter.notifyDataSetChanged();
                }

                news_listview.refreshComplete();//收起下拉刷新控件

            }

            @Override
            public void onFailure(HttpException e, String s) {
                news_listview.refreshComplete();//收起下拉刷新控件
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化数据
     */
    public void initNewsData(String result) {

        Gson gson = new Gson();
        newsListData = gson.fromJson(result, NewsListData.class);
        news = newsListData.data.news;
        topnews = newsListData.data.topnews;
        String more = newsListData.data.more;
        moreUrl = GlobalData.bseUrl + more;//获取加载更多请求地址

        tv_title.setText(news.get(0).title);//初始化标题

        if (topnews != null) {
            //viewpager数据设置
            myNewsPagerAdapter = new MyNewsPagerAdapter();
            news_viewpager.setAdapter(myNewsPagerAdapter);
        }

        if(mHandler == null){
            mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    int currentItem = news_viewpager.getCurrentItem();
                    if(currentItem < news_viewpager.getChildCount()){
                        currentItem ++;
                    }else {
                        currentItem = 0;
                    }

                    news_viewpager.setCurrentItem(currentItem);
                    mHandler.sendEmptyMessageDelayed(0,3000);
                }
            };
        }
        mHandler.sendEmptyMessageDelayed(0,3000);

        news_viewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mHandler.removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        mHandler.sendEmptyMessageDelayed(0,3000);
                        break;
                }
                return false;
            }
        });

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
        myNewsListViewAdapter = new MyNewsListViewAdapter();
        if (news != null) {
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
            return news.size();
        }

        @Override
        public NewsListData.News getItem(int i) {
            return news.get(i);
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

            NewsListData.News news = getItem(i);

            bitmapUtils.display(viewHolder.img, news.listimage);
            viewHolder.title.setText(news.title);
            viewHolder.date.setText(news.pubdate);

            String newsId = news.id;
            String news_id = sharedPreferences.getString("news_id", "");
            if(news_id.contains(newsId)){
                viewHolder.title.setTextColor(Color.GRAY);
            }

            return view;
        }
    }

    public class ViewHolder {
        public ImageView img;
        public TextView title;
        public TextView date;
    }
}
