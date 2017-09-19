package com.china_liantong.navigationcontrol.demo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;

import com.china_liantong.navigationcontrol.NavigationBar;
import com.china_liantong.navigationcontrol.NavigationControl;
import com.china_liantong.navigationcontrol.NavigationFragment;
import com.china_liantong.navigationcontrol.SubMenu;

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

        /* ************ 主界面 Config Data ********** */
        NavigationControl.DataHolder ncHolder = new NavigationControl.DataHolder()
                .activity(this)
                .navigationBarHeight(80)
                .fragmentMarginTop(10);

        /* ************ NavigationBar Config Data ********** */
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

        /* ************ NavigationFragment Config Data ********** */
        // 每个一级导航栏目对应一个NavigationFragment, 因此需要传入ArrayList
        ArrayList<NavigationFragment.DataHolder> nfHolderList = new ArrayList<>();

        String[] subList = new String[]{"全部", "动作", "射击", "休闲", "竞技", "养成", "三维", "益智"};
        List<Pair<String, Drawable>> pairList = new ArrayList<>();
        pairList.add(new Pair<>(subList[0], getResources().getDrawable(R.drawable.sub_1)));
        pairList.add(new Pair<>(subList[1], getResources().getDrawable(R.drawable.sub_2)));
        pairList.add(new Pair<>(subList[2], getResources().getDrawable(R.drawable.sub_3)));
        pairList.add(new Pair<>(subList[3], getResources().getDrawable(R.drawable.sub_4)));
        pairList.add(new Pair<>(subList[4], getResources().getDrawable(R.drawable.sub_5)));
        pairList.add(new Pair<>(subList[5], getResources().getDrawable(R.drawable.sub_6)));
        pairList.add(new Pair<>(subList[6], getResources().getDrawable(R.drawable.sub_7)));
        pairList.add(new Pair<>(subList[7], getResources().getDrawable(R.drawable.sub_8)));
        SubMenu.DataHolder subMenuHolder = new SubMenu.DataHolder()
                .subPairs(pairList)
                .fullDisplayNumber(5)
                .textColor(Color.WHITE)
                .textSize(22)
                .rowSpacing(8);

        // 此处只添加一个 NavigationFragment.DataHolder 测试
        NavigationFragment.DataHolder frgment1 = new NavigationFragment.DataHolder();
        nfHolderList.add(frgment1.subHolder(subMenuHolder)
                .subMenuWidth(175));

        /* ************ Set Config and Show() ********** */
        mNavigationControl.navigationControlHolder(ncHolder)
                .navigationBarHolder(nbHolder)
                .navigationFragmentHolder(nfHolderList).show();
    }
}
