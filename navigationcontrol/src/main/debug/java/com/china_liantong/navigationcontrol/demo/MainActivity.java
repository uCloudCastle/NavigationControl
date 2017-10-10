package com.china_liantong.navigationcontrol.demo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.china_liantong.navigationcontrol.NavigationBar;
import com.china_liantong.navigationcontrol.NavigationControl;
import com.china_liantong.navigationcontrol.NavigationControlListener;
import com.china_liantong.navigationcontrol.R;
import com.china_liantong.navigationcontrol.SubMenu;
import com.china_liantong.navigationcontrol.fragment.ContentViewProxy;
import com.china_liantong.navigationcontrol.fragment.NavigationFragment;
import com.china_liantong.navigationcontrol.utils.DensityUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.china_liantong.navigationcontrol.fragment.ContentViewProxy.BuiltInAdapter.CONTENT_ITEM_STYLE_BACKGROUND_COVERED;
import static com.china_liantong.navigationcontrol.fragment.ContentViewProxy.BuiltInAdapter.CONTENT_ITEM_STYLE_ICON_TOP;

public class MainActivity extends Activity {
    NavigationControl mNavigationControl;
    TextView loadingView;
    TextView loadFailView;
    int mCurrentPage;
    int mCurrentSubPage;
    ContentViewProxy proxy_2;
    ContentViewProxy proxy_4_1;
    ContentViewProxy proxy_4_2;
    ContentViewProxy proxy_4_3;
    ContentViewProxy proxy_5_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingView = new TextView(MainActivity.this);
        loadingView.setBackgroundColor(getResources().getColor(R.color.common_light_grey));
        loadingView.setText("加载中...");
        loadingView.setTextSize(36);
        loadingView.setGravity(Gravity.CENTER);
        loadingView.setTextColor(Color.WHITE);

        loadFailView = new TextView(MainActivity.this);
        loadFailView.setBackgroundColor(getResources().getColor(R.color.common_light_grey));
        loadFailView.setText("加载失败");
        loadFailView.setTextSize(36);
        loadFailView.setGravity(Gravity.CENTER);
        loadFailView.setTextColor(Color.WHITE);

        String[] list = new String[]{"已安装", "推荐", "游戏", "动态加载-我的消费", "自定义View", "运动竞技", "教育", "公开课", "亲子栏目"};
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
        nfHolderList.add(getFragmentDemo5());

        /* ************ Set Config and Show() ********** */
        mNavigationControl.navigationControlHolder(ncHolder)
                .navigationBarHolder(nbHolder)
                .navigationFragmentHolder(nfHolderList).init();
        mNavigationControl.setListener(new NavigationControlListener() {
            @Override
            public void onBuiltInItemGetFocus(View focusView, int position) {
                Toast.makeText(MainActivity.this, "OnBuiltInItemGetFocus : " + focusView.toString()
                         + " " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBuiltInItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "OnBuiltInItemClick : " + view.toString()
                        + " " + position, Toast.LENGTH_SHORT).show();
                if (mCurrentPage == 1 && mCurrentSubPage == 0 && position == 1) {        // 排行榜

                    Drawable[][] picList = new Drawable[][]{
                            {getResources().getDrawable(R.drawable.content_1_5),
                                    getResources().getDrawable(R.drawable.content_1_5),
                                    getResources().getDrawable(R.drawable.content_1_5),
                                    getResources().getDrawable(R.drawable.content_1_5),
                                    getResources().getDrawable(R.drawable.content_1_5),
                                    getResources().getDrawable(R.drawable.content_1_5),
                                    getResources().getDrawable(R.drawable.content_1_5),
                                    getResources().getDrawable(R.drawable.content_1_5),
                                    getResources().getDrawable(R.drawable.content_1_5),
                                    getResources().getDrawable(R.drawable.content_1_5)}};
                    String[][] titleList = new String[][]{{"二级模组", "二级模组", "二级模组", "二级模组", "二级模组",
                            "二级模组-2", "二级模组", "二级模组", "二级模组", "二级模组"}};
                    ContentViewProxy.BuiltInAdapter adapter = new ContentViewProxy.BuiltInAdapter()
                            .pageCount(1)
                            .perPageStyle(new int[]{CONTENT_ITEM_STYLE_ICON_TOP, CONTENT_ITEM_STYLE_ICON_TOP})
                            .perPageItemCount(new int[]{10})
                            .pictures(picList)
                            .titles(titleList)
                            .titleSize(20)
                            .titleColor(Color.WHITE)
                            .rows(2)
                            .columns(5)
                            .rowSpacing(10)
                            .columnSpacing(10)
                            .itemStartIndex(new int[][]{{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}})
                            .itemRowSize(new int[][]{{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}})
                            .itemColumnSize(new int[][]{{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}});
                    proxy_2.setBuiltInAdapter(adapter);
                    proxy_2.getNotifier().notifyDataLoadDone(true);
                }
            }

            @Override
            public void onPageChanged(int page, int subpage) {
                Toast.makeText(MainActivity.this, "onPageChanged : "
                        + page + " " + subpage, Toast.LENGTH_SHORT).show();
                mCurrentPage = page;
                mCurrentSubPage = subpage;

                if (page == 3 && subpage == 0) {
                    if (proxy_4_1.getBuiltInAdapter() != null) {                   // 已经下载好
                        return;
                    }
                    new Thread(new DynamicLoadRunnable(MainActivity.this, proxy_4_1, page, subpage)).start();
                } else if (page == 3 && subpage == 1) {
                    if (proxy_4_2.getBuiltInAdapter() != null) {                   // 已经下载好
                        return;
                    }
                    new Thread(new DynamicLoadRunnable(MainActivity.this, proxy_4_2, page, subpage)).start();
                } else if (page == 3 && subpage == 2) {
                    if (proxy_4_3.getBuiltInAdapter() != null) {                   // 已经下载好
                        return;
                    }
                    new Thread(new DynamicLoadRunnable(MainActivity.this, proxy_4_3, page, subpage)).start();
                } else if (page == 4 && subpage == 1) {
                    if (proxy_5_2.getCustomViewHolder().getContentView() != null) {                   // 已经下载好
                        return;
                    }
                    new Thread(new DynamicLoadRunnable(MainActivity.this, proxy_5_2, page, subpage)).start();
                }
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

        List<ContentViewProxy> proxies = new ArrayList<>();
        proxies.add(info);

        return new NavigationFragment.DataHolder()
                .infoList(proxies);
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
        proxy_2 = new ContentViewProxy(adapter);

        List<ContentViewProxy> proxies = new ArrayList<>();
        proxies.add(proxy_2);

        return new NavigationFragment.DataHolder()
                .infoList(proxies);
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

        List<ContentViewProxy> proxies = new ArrayList<>();
        proxies.add(info1);
        proxies.add(info2);

        return new NavigationFragment.DataHolder()
                .infoList(proxies)
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
        // info1          有loading view
        proxy_4_1 = new ContentViewProxy(new ContentViewProxy.CustomViewHolder(loadingView, loadFailView, null));
        // info2          无loading view
        proxy_4_2 = new ContentViewProxy();
        // info3          loading view + load fail view
        proxy_4_3 = new ContentViewProxy(new ContentViewProxy.CustomViewHolder(loadingView, loadFailView, null));
        List<ContentViewProxy> proxies = new ArrayList<>();
        proxies.add(proxy_4_1);
        proxies.add(proxy_4_2);
        proxies.add(proxy_4_3);

        return new NavigationFragment.DataHolder()
                .infoList(proxies)
                .subHolder(subMenuHolder)
                .subMenuWidth(DensityUtils.dp2px(this, 175))
                .subMenuMarginRight(10);
    }

    private NavigationFragment.DataHolder getFragmentDemo5() {
        // submenu
        String[] subList = new String[]{"静态加载", "动态加载"};
        List<Pair<String, Drawable>> pairList = new ArrayList<>();
        pairList.add(new Pair<>(subList[0], getResources().getDrawable(R.drawable.sub_1)));
        pairList.add(new Pair<>(subList[1], getResources().getDrawable(R.drawable.sub_2)));
        SubMenu.DataHolder subMenuHolder = new SubMenu.DataHolder()
                .subPairs(pairList)
                .itemStyle(SubMenu.DataHolder.SUBMENU_ITEM_STYLE_ICON_LEFT)
                .fullDisplayNumber(4)
                .textColor(Color.WHITE)
                .textSize(20)
                .rowSpacing(8);

        // proxy 1         静态自定义View
        View view = new View(MainActivity.this);
        view.setBackground(getResources().getDrawable(R.drawable.content_3_3));
        ContentViewProxy proxy1 = new ContentViewProxy(new ContentViewProxy.CustomViewHolder(null, null, view));

        // proxy 2         动态自定义View
        proxy_5_2 = new ContentViewProxy(new ContentViewProxy.CustomViewHolder(loadingView, loadFailView, null));
        List<ContentViewProxy> proxies = new ArrayList<>();
        proxies.add(proxy1);
        proxies.add(proxy_5_2);

        return new NavigationFragment.DataHolder()
                .infoList(proxies)
                .subHolder(subMenuHolder)
                .subMenuWidth(DensityUtils.dp2px(this, 225))
                .subMenuMarginRight(10);
    }
}
