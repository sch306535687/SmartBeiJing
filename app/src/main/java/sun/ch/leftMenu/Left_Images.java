package sun.ch.leftMenu;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
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

import java.util.ArrayList;

import sun.ch.base.Left_Menu_Base_Activity;
import sun.ch.domain.PhotoBean;
import sun.ch.global.GlobalData;
import sun.ch.smartbeijing.R;

/**
 * Created by sunch on 2017/2/9.
 */
public class Left_Images extends Left_Menu_Base_Activity {

    private ArrayList<PhotoBean.PhotoNewsData> news;
    private ListView listview;
    private GridView gridview;
    private ImageButton list_grid_btn;
    private boolean isList = true;

    public Left_Images(Activity activity, ImageButton list_grid_btn) {
        super(activity);
        this.list_grid_btn = list_grid_btn;
    }

    @Override
    public View initView() {
        View photoView = View.inflate(mActivity, R.layout.pager_menu_detail_photo, null);
        listview = (ListView) photoView.findViewById(R.id.lv_list);
        gridview = (GridView) photoView.findViewById(R.id.gv_list);
        return photoView;
    }

    @Override
    public void initData() {
        getDataFromServer();
    }

    private void getDataFromServer() {
        String photosUrl = GlobalData.photosUrl;//图片数据请求地址
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, photosUrl, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Gson gson = new Gson();
                PhotoBean photoBean = gson.fromJson(result, PhotoBean.class);
                news = photoBean.data.news;

                listview.setAdapter(new MyAdapter());
                gridview.setAdapter(new MyAdapter());

                list_grid_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isList) {
                            isList = false;
                            listview.setVisibility(View.INVISIBLE);
                            gridview.setVisibility(View.VISIBLE);
                            list_grid_btn.setBackgroundResource(R.mipmap.icon_pic_list_type);
                        } else {
                            isList = true;
                            listview.setVisibility(View.VISIBLE);
                            gridview.setVisibility(View.INVISIBLE);
                            list_grid_btn.setBackgroundResource(R.mipmap.icon_pic_grid_type);
                        }
                    }
                });
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    public class MyAdapter extends BaseAdapter {

        public BitmapUtils bitmapUtils;

        public MyAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadingImage(R.mipmap.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public PhotoBean.PhotoNewsData getItem(int i) {
            return news.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = View.inflate(mActivity, R.layout.list_item_photo, null);
                holder.image = (ImageView) view.findViewById(R.id.iv_icon);
                holder.title = (TextView) view.findViewById(R.id.tv_title);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            PhotoBean.PhotoNewsData item = getItem(i);
            bitmapUtils.display(holder.image, item.listimage);
            holder.title.setText(item.title);
            return view;
        }
    }

    public class ViewHolder {
        public TextView title;
        public ImageView image;
    }

}
