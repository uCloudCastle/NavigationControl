package com.china_liantong.navigationcontrol.demo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.china_liantong.navigationcontrol.NavigationBar;
import com.china_liantong.navigationcontrol.NavigationControl;
import com.china_liantong.navigationcontrol.R;
import com.china_liantong.navigationcontrol.SubMenu;
import com.china_liantong.navigationcontrol.fragment.ContentViewProxy;
import com.china_liantong.navigationcontrol.fragment.NavigationFragment;
import com.china_liantong.navigationcontrol.utils.DensityUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.china_liantong.navigationcontrol.fragment.ContentViewProxy.BuiltInAdapter.CONTENT_ITEM_STYLE_BACKGROUND_COVERED;
import static com.china_liantong.navigationcontrol.fragment.ContentViewProxy.BuiltInAdapter.CONTENT_ITEM_STYLE_ICON_LEFT;
import static com.china_liantong.navigationcontrol.fragment.ContentViewProxy.BuiltInAdapter.CONTENT_ITEM_STYLE_ICON_TOP;
import static com.china_liantong.navigationcontrol.fragment.ContentViewProxy.BuiltInAdapter.CONTENT_ITEM_STYLE_ONLY_TEXT;

public class MainActivity extends Activity {
    NavigationControl mNavigationControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] list = new String[]{"已安装", "推荐", "游戏", "动态加载-我的消费", "动态加载-自定义View", "运动竞技", "教育", "公开课", "亲子栏目"};
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
        // 添加 NavigationFragment.DataHolder 到 List
        ArrayList<NavigationFragment.DataHolder> nfHolderList = new ArrayList<>();
        nfHolderList.add(getFragmentDemo1());
        nfHolderList.add(getFragmentDemo2());
        nfHolderList.add(getFragmentDemo3());
        nfHolderList.add(getFragmentDemo4());
        //nfHolderList.add(getFragmentDemo5());

        /* ************ Set Config and Show() ********** */
        mNavigationControl.navigationControlHolder(ncHolder)
                .navigationBarHolder(nbHolder)
                .navigationFragmentHolder(nfHolderList).init();
        mNavigationControl.setOnItemClickListener(new NavigationControl.OnItemClickListener() {
            @Override
            public void onBuiltInItemClick(View view, int page, int subpage, int position) {
                Toast.makeText(MainActivity.this, "OnItemClick : " + page
                        + " " + subpage + " " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageChanged(int page, int subpage) {
                Toast.makeText(MainActivity.this, "onPageChanged : "
                        + page + " " + subpage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private NavigationFragment.DataHolder getFragmentDemo1() {
        // no submenu
        // grid view
        Drawable[][] picList = new Drawable[][]{
                {getResources().getDrawable(R.drawable.content_1_1),
                        getResources().getDrawable(R.drawable.content_1_2),
                        getResources().getDrawable(R.drawable.content_1_3),
                        getResources().getDrawable(R.drawable.content_1_4),
                        getResources().getDrawable(R.drawable.content_1_5),
                        getResources().getDrawable(R.drawable.content_1_6),
                        getResources().getDrawable(R.drawable.content_1_7),
                        getResources().getDrawable(R.drawable.content_1_8),
                        getResources().getDrawable(R.drawable.content_1_9),
                        getResources().getDrawable(R.drawable.content_1_10)},
                {getResources().getDrawable(R.drawable.content_1_11),
                        getResources().getDrawable(R.drawable.content_1_12)}};
        String[][] titleList = new String[][]{{"欢乐斗地主", "部落冲突", "炉石传说", "城市猎人", "纪念碑",
                "走出迷宫-2", "飞行游戏-勇士的天宫_Online", "刀塔传奇", "豆豆", "海贼王 - 大航海家"},
                {"扑克游戏", "火柴人"}
        };
        ContentViewProxy.BuiltInAdapter adapter = new ContentViewProxy.BuiltInAdapter()
                .pageCount(2)
                .perPageStyle(new int[]{CONTENT_ITEM_STYLE_ICON_TOP, CONTENT_ITEM_STYLE_ICON_TOP})
                .perPageItemCount(new int[]{10, 2})
                .pictures(picList)
                .titles(titleList)
                .titleSize(20)
                .titleColor(Color.WHITE)
                .rows(2)
                .columns(5)
                .rowSpacing(10)
                .columnSpacing(10)
                .itemStartIndex(new int[][]{{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, {0, 1}})
                .itemRowSize(new int[][]{{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1}})
                .itemColumnSize(new int[][]{{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1}})
                .fadingWidth(DensityUtils.dp2px(this, 80));
        ContentViewProxy info = new ContentViewProxy(adapter);

        List<ContentViewProxy> contentViewProxies = new ArrayList<>();
        contentViewProxies.add(info);

        return new NavigationFragment.DataHolder()
                .infoList(contentViewProxies);
    }

    private NavigationFragment.DataHolder getFragmentDemo2() {
        // no submenu
        // grid view
        Drawable[][] picList = new Drawable[][]{
                {getResources().getDrawable(R.drawable.content_2_1),
                        getResources().getDrawable(R.drawable.content_2_2),
                        getResources().getDrawable(R.drawable.content_2_3),
                        getResources().getDrawable(R.drawable.content_2_4),
                        getResources().getDrawable(R.drawable.content_2_5),
                        getResources().getDrawable(R.drawable.content_2_6),
                        getResources().getDrawable(R.drawable.content_2_7)}};
        String[][] titleList = new String[][]{
                {"", "", "", "", "", "愤怒的小鸟", "猪猪特工队"}
        };
        ContentViewProxy.BuiltInAdapter adapter = new ContentViewProxy.BuiltInAdapter()
                .pageCount(1)
                .perPageStyle(new int[]{CONTENT_ITEM_STYLE_BACKGROUND_COVERED})
                .perPageItemCount(new int[]{7})
                .pictures(picList)
                .titles(titleList)
                .titleSize(20)
                .titleColor(Color.WHITE)
                .rows(20)
                .columns(4)
                .rowSpacing(15)
                .columnSpacing(15)
                .itemStartIndex(new int[][]{{0, 11, 20, 29, 40, 60, 71}})
                .itemRowSize(new int[][]{{11, 9, 9, 11, 20, 11, 9}})
                .itemColumnSize(new int[][]{{1, 1, 1, 1, 1, 1, 1}});
        ContentViewProxy info = new ContentViewProxy(adapter);

        List<ContentViewProxy> contentViewProxies = new ArrayList<>();
        contentViewProxies.add(info);

        return new NavigationFragment.DataHolder()
                .infoList(contentViewProxies);
    }

    private NavigationFragment.DataHolder getFragmentDemo3() {
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
                .rowSpacing(8);

        // grid view
        // info1
        Drawable[][] picList1 = new Drawable[][]{
                {getResources().getDrawable(R.drawable.content_3_1),
                        getResources().getDrawable(R.drawable.content_3_2),
                        getResources().getDrawable(R.drawable.content_3_3),
                        getResources().getDrawable(R.drawable.content_3_4),
                        getResources().getDrawable(R.drawable.content_3_5)},
                {getResources().getDrawable(R.drawable.content_1_11),
                        getResources().getDrawable(R.drawable.content_1_12)}};
        String[][] titleList1 = new String[][]{{"愤怒的小鸟", "漫威精选", "坦克大战进化", "", "超级飞机侠"},
                {"扑克游戏", "火柴人"}};
        String[][] subtitleList1 = new String[][]{{"", "", "", "", ""},
                {"621565+", "621565+"}};
        ContentViewProxy.BuiltInAdapter adapter1 = new ContentViewProxy.BuiltInAdapter()
                .pageCount(2)
                .perPageStyle(new int[]{CONTENT_ITEM_STYLE_BACKGROUND_COVERED, CONTENT_ITEM_STYLE_ICON_TOP})
                .perPageItemCount(new int[]{5, 2})
                .pictures(picList1)
                .titles(titleList1)
                .subtitles(subtitleList1)
                .titleSize(20)
                .titleColor(Color.WHITE)
                .subtitleSize(15)
                .subtitleColor(Color.LTGRAY)
                .rows(2)
                .columns(10)
                .rowSpacing(10)
                .columnSpacing(10)
                .itemStartIndex(new int[][]{{0, 1, 6, 7, 14}, {0, 1}})
                .itemRowSize(new int[][]{{1, 1, 1, 1, 2}, {1, 1}})
                .itemColumnSize(new int[][]{{3, 3, 4, 4, 3}, {2, 2}})
                .fadingWidth(DensityUtils.dp2px(this, 80));;
        ContentViewProxy info1 = new ContentViewProxy(adapter1);

        // info2
        Drawable[][] picList2 = new Drawable[][]{
                {getResources().getDrawable(R.drawable.content_1_2),
                        getResources().getDrawable(R.drawable.content_1_4),
                        getResources().getDrawable(R.drawable.content_1_5),
                        getResources().getDrawable(R.drawable.content_1_7),
                        getResources().getDrawable(R.drawable.content_1_8),
                        getResources().getDrawable(R.drawable.content_1_10)}};
        String[][] titleList2 = new String[][]{{"部落冲突", "城市猎人", "纪念碑",
                "飞行游戏-勇士的天宫_Online", "刀塔传奇", "海贼王 - 大航海家"}
        };
        String[][] subtitleList2 = new String[][]{{"621565+", "621565+", "621565+", "621565+", "621565+", "621565+"}};
        ContentViewProxy.BuiltInAdapter adapter2 = new ContentViewProxy.BuiltInAdapter()
                .pageCount(1)
                .perPageStyle(new int[]{CONTENT_ITEM_STYLE_ICON_TOP})
                .perPageItemCount(new int[]{6})
                .pictures(picList2)
                .titles(titleList2)
                .subtitles(subtitleList2)
                .titleSize(20)
                .titleColor(Color.WHITE)
                .subtitleSize(15)
                .subtitleColor(Color.LTGRAY)
                .rows(2)
                .columns(5)
                .rowSpacing(10)
                .columnSpacing(10)
                .itemStartIndex(new int[][]{{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, {0, 1}})
                .itemRowSize(new int[][]{{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1}})
                .itemColumnSize(new int[][]{{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1}})
                .fadingWidth(DensityUtils.dp2px(this, 80));;
        ContentViewProxy info2 = new ContentViewProxy(adapter2);

        List<ContentViewProxy> contentViewProxies = new ArrayList<>();
        contentViewProxies.add(info1);
        contentViewProxies.add(info2);

        return new NavigationFragment.DataHolder()
                .infoList(contentViewProxies)
                .subHolder(subMenuHolder)
                .subMenuWidth(DensityUtils.dp2px(this, 175))
                .subMenuMarginRight(10);
    }

    private NavigationFragment.DataHolder getFragmentDemo4() {
        String[] subList = new String[]{"消费记录", "已购买", "已订阅"};
        String[] tagList = new String[]{"", "12", "967"};
        List<Pair<String, Drawable>> pairList = new ArrayList<>();
        pairList.add(new Pair<>(subList[0], getResources().getDrawable(R.drawable.sub_9)));
        pairList.add(new Pair<>(subList[1], getResources().getDrawable(R.drawable.sub_10)));
        pairList.add(new Pair<>(subList[2], getResources().getDrawable(R.drawable.sub_11)));
        SubMenu.DataHolder subMenuHolder = new SubMenu.DataHolder()
                .subPairs(pairList)
                .itemStyle(SubMenu.DataHolder.SUBMENU_ITEM_STYLE_ICON_TOP)
                .fullDisplayNumber(3)
                .textColor(Color.WHITE)
                .textSize(20)
                .tagList(Arrays.asList(tagList))
                .rowSpacing(DensityUtils.dp2px(this, 10));

        // grid view
        // info1
        String[][] titleList1 = new String[][]{{"2014年 本月", "2014年 07月", "2014年 06月", "2014年 05月", "2014年 04月", "2014年 03月",
                "2014年 02月", "2014年 01月", "2013年 12月", "2013年 11月", "2013年 10月", "2013年 09月"}};
        String[][] subtitleList1 = new String[][]{{"12.00", "43.00", "7.00", "1125.00", "586.00", "63.00",
                "367.00", "12.00", "71.00", "422.00", "9.00", "68.00"}};
        ContentViewProxy.BuiltInAdapter adapter1 = new ContentViewProxy.BuiltInAdapter()
                .pageCount(1)
                .perPageStyle(new int[]{CONTENT_ITEM_STYLE_ONLY_TEXT})
                .perPageItemCount(new int[]{12})
                .titles(titleList1)
                .subtitles(subtitleList1)
                .titleSize(20)
                .titleColor(Color.LTGRAY)
                .subtitleSize(36)
                .subtitleColor(Color.LTGRAY)
                .rows(3)
                .columns(4)
                .rowSpacing(10)
                .columnSpacing(10)
                .itemStartIndex(new int[][]{ {0, 3, 6, 9, 1, 4, 7, 10, 2, 5, 8, 11} })
                .itemRowSize(new int[][]{ {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1} })
                .itemColumnSize(new int[][]{ {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1} })
                .marginRight(DensityUtils.dp2px(this, 50));
        ContentViewProxy info1 = new ContentViewProxy(adapter1);

        // info2
        Drawable[][] picList2 = new Drawable[][]{
                {getResources().getDrawable(R.drawable.content_1_5),
                        getResources().getDrawable(R.drawable.content_1_5),
                        getResources().getDrawable(R.drawable.content_1_5),
                        getResources().getDrawable(R.drawable.content_1_5),
                        getResources().getDrawable(R.drawable.content_1_5),
                        getResources().getDrawable(R.drawable.content_1_5),
                        getResources().getDrawable(R.drawable.content_1_5),
                        getResources().getDrawable(R.drawable.content_1_5),
                        getResources().getDrawable(R.drawable.content_1_5)},
                {getResources().getDrawable(R.drawable.content_1_5),
                        getResources().getDrawable(R.drawable.content_1_5),
                        getResources().getDrawable(R.drawable.content_1_5),
                        getResources().getDrawable(R.drawable.content_1_5),
                        getResources().getDrawable(R.drawable.content_1_5),
                        getResources().getDrawable(R.drawable.content_1_5)}};
        String[][] titleList2 = new String[][]{{"宠物斗地主", "宠物斗地主", "宠物斗地主",
                "宠物斗地主", "宠物斗地主", "宠物斗地主", "宠物斗地主", "宠物斗地主", "宠物斗地主"},
                {"宠物斗地主", "宠物斗地主", "宠物斗地主", "宠物斗地主", "宠物斗地主", "宠物斗地主"}
        };
        String[][] subtitleList2 = new String[][]{{"购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元"},
                {"购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                        "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                        "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                        "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                        "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                        "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元"}};
        ContentViewProxy.BuiltInAdapter adapter2 = new ContentViewProxy.BuiltInAdapter()
                .pageCount(2)
                .perPageStyle(new int[]{CONTENT_ITEM_STYLE_ICON_LEFT, CONTENT_ITEM_STYLE_ICON_LEFT})
                .perPageItemCount(new int[]{9, 6})
                .pictures(picList2)
                .titles(titleList2)
                .subtitles(subtitleList2)
                .titleSize(19)
                .titleColor(Color.WHITE)
                .subtitleSize(15)
                .subtitleColor(Color.LTGRAY)
                .rows(3)
                .columns(3)
                .rowSpacing(10)
                .columnSpacing(10)
                .itemStartIndex(new int[][]{{0, 1, 2, 3, 4, 5, 6, 7, 8}, {0, 1, 2, 3, 4, 5}})
                .itemRowSize(new int[][]{{1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1}})
                .itemColumnSize(new int[][]{{1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1}})
                .fadingWidth(DensityUtils.dp2px(this, 80));
        ContentViewProxy info2 = new ContentViewProxy(adapter2);

        // info3
        Drawable[][] picList3 = new Drawable[][]{
                {getResources().getDrawable(R.drawable.content_1_1),
                        getResources().getDrawable(R.drawable.content_1_1),
                        getResources().getDrawable(R.drawable.content_1_1),
                        getResources().getDrawable(R.drawable.content_1_1),
                        getResources().getDrawable(R.drawable.content_1_1),
                        getResources().getDrawable(R.drawable.content_1_1),
                        getResources().getDrawable(R.drawable.content_1_1),
                        getResources().getDrawable(R.drawable.content_1_1),
                        getResources().getDrawable(R.drawable.content_1_1)},
                {getResources().getDrawable(R.drawable.content_1_1),
                        getResources().getDrawable(R.drawable.content_1_1),
                        getResources().getDrawable(R.drawable.content_1_1),
                        getResources().getDrawable(R.drawable.content_1_1),
                        getResources().getDrawable(R.drawable.content_1_1),
                        getResources().getDrawable(R.drawable.content_1_1)}};
        String[][] titleList3 = new String[][]{{"宠物斗地主", "宠物斗地主", "宠物斗地主",
                "宠物斗地主", "宠物斗地主", "宠物斗地主", "宠物斗地主", "宠物斗地主", "宠物斗地主"},
                {"宠物斗地主", "宠物斗地主", "宠物斗地主", "宠物斗地主", "宠物斗地主", "宠物斗地主"}
        };
        String[][] subtitleList3 = new String[][]{{"购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元"},
                {"购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                        "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                        "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                        "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                        "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元",
                        "购买时间: 2016/01/12 12:30\n有效期: 永久\n金额: 5:00元"}};
        ContentViewProxy.BuiltInAdapter adapter3 = new ContentViewProxy.BuiltInAdapter()
                .pageCount(2)
                .perPageStyle(new int[]{CONTENT_ITEM_STYLE_ICON_LEFT, CONTENT_ITEM_STYLE_ICON_LEFT})
                .perPageItemCount(new int[]{9, 6})
                .pictures(picList3)
                .titles(titleList3)
                .subtitles(subtitleList3)
                .titleSize(19)
                .titleColor(Color.WHITE)
                .subtitleSize(15)
                .subtitleColor(Color.LTGRAY)
                .rows(3)
                .columns(3)
                .rowSpacing(10)
                .columnSpacing(10)
                .itemStartIndex(new int[][]{{0, 1, 2, 3, 4, 5, 6, 7, 8}, {0, 1, 2, 3, 4, 5}})
                .itemRowSize(new int[][]{{1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1}})
                .itemColumnSize(new int[][]{{1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1}})
                .fadingWidth(DensityUtils.dp2px(this, 80));
        ContentViewProxy info3 = new ContentViewProxy(adapter3);

        List<ContentViewProxy> contentViewProxies = new ArrayList<>();
        contentViewProxies.add(info1);
        contentViewProxies.add(info2);
        contentViewProxies.add(info3);

        return new NavigationFragment.DataHolder()
                .infoList(contentViewProxies)
                .subHolder(subMenuHolder)
                .subMenuWidth(DensityUtils.dp2px(this, 175))
                .subMenuMarginRight(10);
    }

//    private NavigationFragment.DataHolder getFragmentDemo5() {
//        // no submenu
//        // grid view
//        //ContentViewProxy info = new ContentViewProxy();
//        //info.customAdapter = new DemoAdapt(MainActivity.this);
//
//        List<ContentViewProxy> contentViewProxies = new ArrayList<>();
//        contentViewProxies.add(info);
//
//        return new NavigationFragment.DataHolder()
//                .infoList(contentViewProxies);
//    }
}
