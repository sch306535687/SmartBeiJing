package sun.ch.smartbeijing;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import sun.ch.fragment.Left_menu_fragment;
import sun.ch.fragment.Content_fragment;

public class MainActivity extends SlidingFragmentActivity {

    public static Left_menu_fragment left_menu_fragment;
    public static Content_fragment content_fragment;
    private static SlidingMenu slidingMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        setBehindContentView(R.layout.left_menu);
        slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffset(200);

        left_menu_fragment = new Left_menu_fragment();
        content_fragment = new Content_fragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_left, left_menu_fragment);
        fragmentTransaction.replace(R.id.frame_container, content_fragment);
        fragmentTransaction.commit();
    }

    /**
     * 禁止侧边栏
     */
    public static void stopSlidingMenu(){
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }
    /**
     * 开启滑动侧边栏
     */
    public static void startSlidingMenu(){
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    }

}
