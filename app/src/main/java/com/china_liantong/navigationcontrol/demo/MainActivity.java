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
import com.china_liantong.navigationcontrol.utils.DensityUtils;

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
                .navigationBarHeight(DensityUtils.dp2px(this, 80))
                .fragmentMarginTop(DensityUtils.dp2px(this, 10));

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
                .titleSpacing(DensityUtils.dp2px(this, 50));

        /* ************ NavigationFragment Config Data ********** */
        // 每个一级导航栏目对应一个NavigationFragment, 因此需要传入ArrayList
        ArrayList<NavigationFragment.DataHolder> nfHolderList = new ArrayList<>();

        // ****** 第一个Fragment
        String[] subList1 = new String[]{"全部", "动作", "射击", "休闲", "竞技", "养成", "三维", "益智"};
        List<Pair<String, Drawable>> pairList1 = new ArrayList<>();
        pairList1.add(new Pair<>(subList1[0], getResources().getDrawable(R.drawable.sub_1)));
        pairList1.add(new Pair<>(subList1[1], getResources().getDrawable(R.drawable.sub_2)));
        pairList1.add(new Pair<>(subList1[2], getResources().getDrawable(R.drawable.sub_3)));
        pairList1.add(new Pair<>(subList1[3], getResources().getDrawable(R.drawable.sub_4)));
        pairList1.add(new Pair<>(subList1[4], getResources().getDrawable(R.drawable.sub_5)));
        pairList1.add(new Pair<>(subList1[5], getResources().getDrawable(R.drawable.sub_6)));
        pairList1.add(new Pair<>(subList1[6], getResources().getDrawable(R.drawable.sub_7)));
        pairList1.add(new Pair<>(subList1[7], getResources().getDrawable(R.drawable.sub_8)));
        SubMenu.DataHolder subMenuHolder1 = new SubMenu.DataHolder()
                .subPairs(pairList1)
                .iconPosition(SubMenu.DataHolder.SUBMENU_ICON_POSITION_LEFT)
                .fullDisplayNumber(5)
                .fadingWidth(DensityUtils.dp2px(this, 25))
                .textColor(Color.WHITE)
                .textSize(20)
                .rowSpacing(DensityUtils.dp2px(this, 8));
        NavigationFragment.DataHolder fragment1 = new NavigationFragment.DataHolder()
                .subHolder(subMenuHolder1)
                .subMenuWidth(DensityUtils.dp2px(this, 175))
                .subMenuMarginRight(10);

        // ****** 第二个Fragment
        String[] subList2 = new String[]{"消费记录", "已购买", "已订阅"};
        String[] tagList2 = new String[]{"", "12", "967"};
        List<Pair<String, Drawable>> pairList2 = new ArrayList<>();
        pairList2.add(new Pair<>(subList2[0], getResources().getDrawable(R.drawable.sub_9)));
        pairList2.add(new Pair<>(subList2[1], getResources().getDrawable(R.drawable.sub_10)));
        pairList2.add(new Pair<>(subList2[2], getResources().getDrawable(R.drawable.sub_11)));
        SubMenu.DataHolder subMenuHolder2 = new SubMenu.DataHolder()
                .subPairs(pairList2)
                .iconPosition(SubMenu.DataHolder.SUBMENU_ICON_POSITION_UP)
                .fullDisplayNumber(3)
                .textColor(Color.WHITE)
                .textSize(20)
                .tagList(Arrays.asList(tagList2))
                .rowSpacing(DensityUtils.dp2px(this, 8));
        NavigationFragment.DataHolder fragment2 = new NavigationFragment.DataHolder()
                .subHolder(subMenuHolder2)
                .subMenuWidth(DensityUtils.dp2px(this, 175))
                .subMenuMarginRight(20);

        // 添加 NavigationFragment.DataHolder 到 List
        nfHolderList.add(fragment1);
        nfHolderList.add(fragment2);
        nfHolderList.add(fragment1);

        /* ************ Set Config and Show() ********** */
        mNavigationControl.navigationControlHolder(ncHolder)
                .navigationBarHolder(nbHolder)
                .navigationFragmentHolder(nfHolderList).show();
    }
}
