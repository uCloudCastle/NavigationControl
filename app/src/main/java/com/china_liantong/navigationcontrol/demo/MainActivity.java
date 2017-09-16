package com.china_liantong.navigationcontrol.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.china_liantong.navigationcontrol.NavigationBar;
import com.china_liantong.navigationcontrol.NavigationControl;
import java.util.Arrays;

public class MainActivity extends Activity {
    NavigationControl mNavigationControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] list = new String[]{"已安装", "推荐", "影音", "游戏", "运动竞技", "教育", "公开课", "亲子栏目", "我的消费"};
        mNavigationControl = (NavigationControl) findViewById(R.id.mainactivity_navigationcontrol);

        NavigationControl.DataHolder ncHolder = new NavigationControl.DataHolder()
                .navigationBarHeight(80);

        NavigationBar.DataHolder nbHolder = new NavigationBar.DataHolder()
                .titles(Arrays.asList(list))
                .fullDisplayNumber(6)
                .selectDrawable(getResources().getDrawable(R.drawable.nav_select))
                .focusDrawable(getResources().getDrawable(R.drawable.nav_focus))
                .drawableMargin(32)
                .textSize(22)
                .textFocusSize(25)
                .textColor(Color.parseColor("#bbbbbb"))
                .textFocusColor(Color.WHITE)
                .titleSpacing(50);

        mNavigationControl.navigationBarHolder(nbHolder)
                .navigationControlHolder(ncHolder).show();
    }
}
