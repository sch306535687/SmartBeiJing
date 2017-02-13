package bottomViewPager;

import android.app.Activity;
import android.view.View;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import sun.ch.base.Base_Activity;
import sun.ch.base.Left_Menu_Base_Activity;
import sun.ch.domain.NewsMenuData;
import sun.ch.global.GlobalData;
import sun.ch.leftMenu.Left_Images;
import sun.ch.leftMenu.Left_Interact;
import sun.ch.leftMenu.Left_News;
import sun.ch.leftMenu.Left_Type;
import sun.ch.smartbeijing.MainActivity;

/**
 * Created by sunch on 2017/2/8.
 */
public class NewsContent extends Base_Activity {

    private ArrayList<Left_Menu_Base_Activity> leftLists;//放置侧边栏四个对象
    public NewsMenuData newsMenuData;

    public NewsContent(Activity activity) {
        super(activity);
    }
    public static Left_News left_news;

    @Override
    public void initData() {

        leftLists = new ArrayList<Left_Menu_Base_Activity>();
        String categoriesUrl = GlobalData.categoriesUrl;
        getDataFromServer(categoriesUrl);//获取网络数据

        title.setText("新闻中心");

        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainUI = (MainActivity) mActivity;
                SlidingMenu slidingMenu = mainUI.getSlidingMenu();
                slidingMenu.toggle();
            }
        });

    }

    //从服务器获取json数据,并封装成对象
    public void  getDataFromServer(String url){
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Gson gson = new Gson();
                newsMenuData = gson.fromJson(result, NewsMenuData.class);

                left_news = new Left_News(mActivity,newsMenuData.data.get(0).children);
                leftLists.add(left_news);
                Left_Type left_type = new Left_Type(mActivity);
                leftLists.add(left_type);
                Left_Images left_images = new Left_Images(mActivity);
                leftLists.add(left_images);
                Left_Interact left_interact = new Left_Interact(mActivity);
                leftLists.add(left_interact);

                MainActivity mainUI = (MainActivity) mActivity;
                initNews();//初始化新闻
                mainUI.left_menu_fragment.setNewsData(newsMenuData.data);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    //侧边栏点击后执行此方法切换页面

    public void setItemContent(int position) {
        if(position==0){
            initNews();//初始化新闻
        }else {
            Left_Menu_Base_Activity left_menu_base_activity = leftLists.get(position);
            fl_frameLayout.removeAllViews();//添加界面之前先去掉子类控件
            fl_frameLayout.addView(left_menu_base_activity.mView);
        }

        title.setText(newsMenuData.data.get(position).title);//设置title
    }

    public void initNews(){
        //初始化新闻
        fl_frameLayout.removeAllViews();//添加界面之前先去掉子类控件
        fl_frameLayout.addView(left_news.initView());
        left_news.initData();
    }
}
