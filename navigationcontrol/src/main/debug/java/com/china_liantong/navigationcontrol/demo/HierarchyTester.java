package com.china_liantong.navigationcontrol.demo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.china_liantong.navigationcontrol.R;
import com.china_liantong.navigationcontrol.fragment.ContentViewProxy;

import static com.china_liantong.navigationcontrol.fragment.ContentViewProxy.BuiltInAdapter.CONTENT_ITEM_STYLE_ICON_TOP;

/**
 * Created by randal on 2017/10/10.
 */

public class HierarchyTester {
    static Context mContext;
    static Drawable[][] picList;

    public static void setContext(Context context) {
        if (picList == null) {
            mContext = context;
            picList = new Drawable[][]{
                    {context.getResources().getDrawable(R.drawable.content_1_5),
                            context.getResources().getDrawable(R.drawable.content_1_5),
                            context.getResources().getDrawable(R.drawable.content_1_5),
                            context.getResources().getDrawable(R.drawable.content_1_5),
                            context.getResources().getDrawable(R.drawable.content_1_5),
                            context.getResources().getDrawable(R.drawable.content_1_5),
                            context.getResources().getDrawable(R.drawable.content_1_5),
                            context.getResources().getDrawable(R.drawable.content_1_5),
                            context.getResources().getDrawable(R.drawable.content_1_5),
                            context.getResources().getDrawable(R.drawable.content_1_5)}};
        }
    }

    public static void testHierarchyModule(ContentViewProxy proxy, int position, int hierarchy) {
        if (position == 1 && hierarchy == 0) {
            String[][] titleList = new String[][]{{"二级模组", "进入三级模组", "二级模组", "二级模组", "二级模组",
                    "二级模组", "二级模组", "二级模组", "二级模组", "二级模组"}};
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
            ContentViewProxy temp = new ContentViewProxy(adapter);                                              // 静态加载
            proxy.getNotifier().notifyEnterNextHierarchy(temp);                                              // 进入下级模组

        } else if (position == 1 && hierarchy == 1) {
            final String[][] titleList = new String[][]{{"三级模组", "三级模组", "三级模组", "三级模组", "自定义模组",
                    "三级模组", "三级模组", "三级模组"}};
            TextView loadingView = new TextView(mContext);
            loadingView.setBackgroundColor(Color.parseColor("#c0c0c0"));
            loadingView.setText("加载中...");
            loadingView.setTextSize(36);
            loadingView.setGravity(Gravity.CENTER);
            loadingView.setTextColor(Color.WHITE);

            final ContentViewProxy temp = new ContentViewProxy(new ContentViewProxy.CustomViewHolder(loadingView, null, null));
            proxy.getNotifier().notifyEnterNextHierarchy(temp);                                        // 进入下级模组
            temp.getNotifier().notifyDataLoadStart();

            new Handler().postDelayed(new Runnable() {                                          // 测试延时加载内置GridView
                @Override
                public void run() {
                    ContentViewProxy.BuiltInAdapter adapter = new ContentViewProxy.BuiltInAdapter()
                            .pageCount(1)
                            .perPageStyle(new int[]{CONTENT_ITEM_STYLE_ICON_TOP, CONTENT_ITEM_STYLE_ICON_TOP})
                            .perPageItemCount(new int[]{8})
                            .pictures(picList)
                            .titles(titleList)
                            .titleSize(20)
                            .titleColor(Color.WHITE)
                            .rows(2)
                            .columns(5)
                            .rowSpacing(10)
                            .columnSpacing(10)
                            .itemStartIndex(new int[][]{{0, 1, 2, 3, 4, 5, 6, 7}})
                            .itemRowSize(new int[][]{{1, 1, 1, 1, 1, 1, 1, 1}})
                            .itemColumnSize(new int[][]{{1, 1, 1, 1, 1, 1, 1, 1}});
                    temp.setBuiltInAdapter(adapter);
                    temp.getNotifier().notifyDataLoadDone(true);
                }
            }, 1000);

        } else if (position == 4 && hierarchy == 2) {
            TextView loadingView = new TextView(mContext);
            loadingView.setBackgroundColor(Color.parseColor("#c0c0c0"));
            loadingView.setText("加载中...");
            loadingView.setTextSize(36);
            loadingView.setGravity(Gravity.CENTER);
            loadingView.setTextColor(Color.WHITE);

            final LinearLayout mLayout = new LinearLayout(mContext);
            Button btn1 = new Button(mContext);
            btn1.setText("Left Button");
            Button btn2 = new Button(mContext);
            btn2.setText("Right Button");
            blockBackKey(proxy, btn1);
            blockBackKey(proxy, btn2);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            lp.setMargins(20, 0, 0, 0);
            mLayout.addView(btn1, lp);
            mLayout.addView(btn2, lp);

            final ContentViewProxy temp = new ContentViewProxy(new ContentViewProxy.CustomViewHolder(loadingView, null, null));
            proxy.getNotifier().notifyEnterNextHierarchy(temp);                             // 进入下级模组
            temp.getNotifier().notifyDataLoadStart();

            new Handler().postDelayed(new Runnable() {                                        // 测试延时加载自定义View
                @Override
                public void run() {
                    temp.getCustomViewHolder().setContentView(mLayout);
                    temp.getNotifier().notifyDataLoadDone(true);
                }
            }, 1000);
        }
    }

    static void blockBackKey(final ContentViewProxy proxy, View view) {
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {                            // 自定义View的下级模组需要拦截back按键
                    proxy.getNotifier().notifyReturnPreHierarchy();                         // 并调用该方法通知组件返回上一模组
                    return true;
                }
                return false;
            }
        });
    }
}
