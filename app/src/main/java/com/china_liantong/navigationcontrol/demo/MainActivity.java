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
        // ****** 构造 Fragment 数据
        NavigationFragment.DataHolder fragment1 = getFragmentDemo1();
        NavigationFragment.DataHolder fragment2 = getFragmentDemo2();

        // 添加 NavigationFragment.DataHolder 到 List
        ArrayList<NavigationFragment.DataHolder> nfHolderList = new ArrayList<>();
        nfHolderList.add(fragment1);
        nfHolderList.add(fragment2);

        /* ************ Set Config and Show() ********** */
        mNavigationControl.navigationControlHolder(ncHolder)
                .navigationBarHolder(nbHolder)
                .navigationFragmentHolder(nfHolderList).show();
    }

    private NavigationFragment.DataHolder getFragmentDemo1() {
        // submenu
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
                .itemStyle(SubMenu.DataHolder.SUBMENU_ITEM_STYLE_ICON_LEFT)
                .fullDisplayNumber(5)
                .fadingWidth(DensityUtils.dp2px(this, 25))
                .textColor(Color.WHITE)
                .textSize(20)
                .rowSpacing(DensityUtils.dp2px(this, 8));

        // grid view
        Drawable[][] picList = new Drawable[][]{
                {getResources().getDrawable(R.drawable.content_1),
                getResources().getDrawable(R.drawable.content_2),
                getResources().getDrawable(R.drawable.content_3),
                getResources().getDrawable(R.drawable.content_4),
                getResources().getDrawable(R.drawable.content_5)}};
        String[][] titleList = new String[][]{ {"愤怒的小鸟", "漫威精选", "坦克大战进化", "", "超级飞机侠"} };
        NavigationFragment.GridViewInfo info = new NavigationFragment.GridViewInfo();
        info.pictures = picList;
        info.titles = titleList;
        info.titleSize = 20;
        info.titleColor = Color.WHITE;
        info.pageCount = 1;
        info.perPageItemCount = new int[]{5};
        info.rows = 2;
        info.columns = 3;
        info.rowSpacing = 10;
        info.columnSpacing = 10;
        info.itemStartIndex = new int[][]{ {0, 1, 2, 3, 4} };
        info.itemRowSize = new int[][]{ {1, 1, 1, 1, 2} };
        info.itemColumnSize = new int[][]{ {1, 1, 1, 1, 1} };
        List<NavigationFragment.GridViewInfo> gridViewInfos = new ArrayList<>();
        gridViewInfos.add(info);
        gridViewInfos.add(info);

        return new NavigationFragment.DataHolder()
                .infoList(gridViewInfos)
                .subHolder(subMenuHolder)
                .subMenuWidth(DensityUtils.dp2px(this, 175))
                .subMenuMarginRight(10);
    }

    private NavigationFragment.DataHolder getFragmentDemo2() {
        String[] subList = new String[]{"消费记录", "已购买", "已订阅"};
        String[] tagList = new String[]{"", "12", "967"};
        List<Pair<String, Drawable>> pairList = new ArrayList<>();
        pairList.add(new Pair<>(subList[0], getResources().getDrawable(R.drawable.sub_9)));
        pairList.add(new Pair<>(subList[1], getResources().getDrawable(R.drawable.sub_10)));
        pairList.add(new Pair<>(subList[2], getResources().getDrawable(R.drawable.sub_11)));
        SubMenu.DataHolder subMenuHolder2 = new SubMenu.DataHolder()
                .subPairs(pairList)
                .itemStyle(SubMenu.DataHolder.SUBMENU_ITEM_STYLE_ICON_UP)
                .fullDisplayNumber(3)
                .textColor(Color.WHITE)
                .textSize(20)
                .tagList(Arrays.asList(tagList))
                .rowSpacing(DensityUtils.dp2px(this, 8));

        return new NavigationFragment.DataHolder()
                .subHolder(subMenuHolder2)
                .subMenuWidth(DensityUtils.dp2px(this, 175))
                .subMenuMarginRight(20);
    }
}
