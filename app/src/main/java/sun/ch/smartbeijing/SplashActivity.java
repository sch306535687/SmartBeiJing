package sun.ch.smartbeijing;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import cn.sharesdk.framework.ShareSDK;

public class SplashActivity extends Activity {

    private RelativeLayout splash;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash = (RelativeLayout) findViewById(R.id.splash);
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        ShareSDK.initSDK(this);
        //旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        //透明动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
        //组合动画
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        //开启动画
        splash.startAnimation(animationSet);

        //监听动画集合
      /*  animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/
        //跳到主界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean is_guide_show = sharedPreferences.getBoolean("is_guide_show", false);
                if (is_guide_show) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    finish();
                }

            }
        }, 3000);

    }
}
