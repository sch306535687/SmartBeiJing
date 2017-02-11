package sun.ch.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import sun.ch.domain.NewsMenuData;
import sun.ch.smartbeijing.MainActivity;
import sun.ch.smartbeijing.R;

/**
 * Created by sunch on 2017/2/8.
 */
public class Left_menu_fragment extends android.support.v4.app.Fragment {

    private Activity mActivity;
    private ListView left_menu_listview;
    private ArrayList<NewsMenuData.NewsData> Datas;

    private int mPositin;//当前item索引
    private MyLeftMenuAdapter myLeftMenuAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        Datas = new ArrayList<NewsMenuData.NewsData>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_menu_fragment,null);
        left_menu_listview = (ListView) view.findViewById(R.id.left_menu_listview);

        return view;
    }

    public void setNewsData(ArrayList<NewsMenuData.NewsData> newsDatas) {
        mPositin = 0;//每次点击新闻时就初始化此值
        Datas = newsDatas;
        myLeftMenuAdapter = new MyLeftMenuAdapter();
        left_menu_listview.setAdapter(myLeftMenuAdapter);
        //侧边栏item点击
        left_menu_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPositin = i;
                myLeftMenuAdapter.notifyDataSetChanged();
                //点击切换新闻页面内容
                MainActivity mainUI = (MainActivity) mActivity;
               mainUI.content_fragment.newsContent.setItemContent(i);
                //当slidingmenu打开时就关闭，反之亦然
                SlidingMenu slidingMenu = ((MainActivity) mActivity).getSlidingMenu();
                slidingMenu.toggle();
            }
        });

    }

    public class MyLeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Datas.size();
        }

        @Override
        public NewsMenuData.NewsData getItem(int i) {
            return Datas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = View.inflate(mActivity, R.layout.left_menu_item, null);
            TextView tv_left_menu = (TextView) view1.findViewById(R.id.tv_left_menu);
            tv_left_menu.setText(Datas.get(i).title);
            if(mPositin == i){
                tv_left_menu.setEnabled(true);
            }else {
                tv_left_menu.setEnabled(false);
            }
            return view1;
        }
    }
}
