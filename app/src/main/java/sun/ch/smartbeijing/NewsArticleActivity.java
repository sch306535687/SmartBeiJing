package sun.ch.smartbeijing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;


/**
 * Created by asus on 2017/2/15.
 */

public class NewsArticleActivity extends Activity implements View.OnClickListener {

    private WebSettings settings;
    private int textSizePosition;
    private int defaultSizePosition = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_article);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        ImageView back = (ImageView) findViewById(R.id.back);
        ImageView share = (ImageView) findViewById(R.id.share);
        ImageView textsize = (ImageView) findViewById(R.id.textsize);
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        textsize.setOnClickListener(this);

        WebView webview = (WebView) findViewById(R.id.webview);
        settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);//打开js功能
        webview.loadUrl(url);//加载页面
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back://点击返回按钮
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.share://点击分享按钮
                showShare();
                break;
            case R.id.textsize://点击设置字体大小按钮
                setTextSize();
                break;
        }
    }

    //shareSDK
    private void showShare() {

        OnekeyShare oks = new OnekeyShare();
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

    //设置字体大小
    private void setTextSize() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择字体大小");
        String[] items = new String[]{"超大字体", "大字体", "正常字体", "小字体", "超小字体"};
        builder.setSingleChoiceItems(items, defaultSizePosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                textSizePosition = i;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (textSizePosition) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }
                defaultSizePosition = textSizePosition;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}
