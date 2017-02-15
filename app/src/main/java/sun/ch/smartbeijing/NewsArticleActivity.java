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

                break;
            case R.id.textsize://点击设置字体大小按钮
                setTextSize();
                break;
        }
    }

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
