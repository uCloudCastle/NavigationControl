package com.china_liantong.navigationcontrol.demo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;

import com.china_liantong.navigationcontrol.NavigationBar;
import com.china_liantong.navigationcontrol.NavigationControl;
import com.china_liantong.navigationcontrol.NavigationFragment;
import com.china_liantong.navigationcontrol.SubNavigationBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    NavigationControl mNavigationControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] list = new String[]{"已安装", "推荐", "影音", "游戏", "运动竞技", "教育", "公开课", "亲子栏目", "我的消费"};
        mNavigationControl = (NavigationControl) findViewById(R.id.mainactivity_navigationcontrol);

        NavigationControl.DataHolder ncHolder = new NavigationControl.DataHolder()
                .activity(this)
                .navigationBarHeight(80)
                .fragmentMarginTop(20);

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

        String[] sutList = new String[]{"全部", "动作", "射击", "休闲", "竞技", "养成", "三维", "益智"};
        List<Pair<String, Drawable>> pairList = new ArrayList<>();
        for (String s : sutList) {
            pairList.add(new Pair<String, Drawable>(s, null));
        }
        NavigationFragment.DataHolder temp = new NavigationFragment.DataHolder();
        SubNavigationBar.DataHolder s = new SubNavigationBar.DataHolder()
                .subPairs(pairList)
                .subWidth(200)
                .subItemHeight(80)
                .subIconWidth(40)
                .subIconHeight(40)
                .subTextColor(Color.WHITE)
                .subTextSize(25);
        ArrayList<NavigationFragment.DataHolder> nfHolder = new ArrayList<>();
        nfHolder.add(temp.subHolder(s));

        mNavigationControl.navigationBarHolder(nbHolder)
                .navigationControlHolder(ncHolder)
                .navigationFragmentHolder(nfHolder).show();
    }
}
