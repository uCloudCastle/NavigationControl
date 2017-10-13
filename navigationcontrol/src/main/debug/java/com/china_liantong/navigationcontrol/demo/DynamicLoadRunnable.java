package com.china_liantong.navigationcontrol.demo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.china_liantong.navigationcontrol.R;
import com.china_liantong.navigationcontrol.fragment.ContentViewProxy;
import com.china_liantong.navigationcontrol.utils.DensityUtils;
import com.china_liantong.navigationcontrol.utils.LogUtils;

import static com.china_liantong.navigationcontrol.fragment.ContentViewProxy.BuiltInAdapter.CONTENT_ITEM_STYLE_ICON_LEFT;
import static com.china_liantong.navigationcontrol.fragment.ContentViewProxy.BuiltInAdapter.CONTENT_ITEM_STYLE_ONLY_TEXT;

/**
 * Created by Randal on 2017-09-30.
 */

public class DynamicLoadRunnable implements Runnable {
    private Context mContext;
    private ContentViewProxy mProxy;
    private int mPage;
    private int mSubPage;

    public DynamicLoadRunnable(Context context, ContentViewProxy proxy, int page, int subPage) {
        mContext = context;
        mProxy = proxy;
        mPage = page;
        mSubPage = subPage;
    }

    @Override
    public void run() {
        switch (mPage) {
            case 3:
                if (mSubPage == 0) {                       // 消费记录
                    LogUtils.d("access");
                    try {
                        Thread.sleep(1000);                  // simulate
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mProxy.getNotifier().notifyDataLoadStart();

                    try {
                        Thread.sleep(3000);                  // simulate
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String[][] titleList1 = new String[][]{{"2014年 本月", "2014年 07月", "2014年 06月", "2014年 05月", "2014年 04月", "2014年 03月",
                            "2014年 02月", "2014年 01月", "2013年 12月", "2013年 11月", "2013年 10月", "2013年 09月"}};
                    String[][] subtitleList1 = new String[][]{{"12.00", "43.00", "7.00", "1125.00", "586.00", "63.00",
                            "367.00", "12.00", "71.00", "422.00", "9.00", "68.00"}};
                    ContentViewProxy.BuiltInAdapter adapter = new ContentViewProxy.BuiltInAdapter()
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
                            .marginRight(DensityUtils.dp2px(mContext, 50));
                    mProxy.setBuiltInAdapter(adapter);
                    mProxy.getNotifier().notifyDataLoadDone(true);

                } else if (mSubPage == 1) {                             // 已购买
                    mProxy.getNotifier().notifyDataLoadStart();

                    try {
                        Thread.sleep(1000);                  // simulate
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Drawable[][] picList2 = new Drawable[][]{
                            {mContext.getResources().getDrawable(R.drawable.content_1_5),
                                    mContext.getResources().getDrawable(R.drawable.content_1_5),
                                    mContext.getResources().getDrawable(R.drawable.content_1_5),
                                    mContext.getResources().getDrawable(R.drawable.content_1_5),
                                    mContext.getResources().getDrawable(R.drawable.content_1_5),
                                    mContext.getResources().getDrawable(R.drawable.content_1_5),
                                    mContext.getResources().getDrawable(R.drawable.content_1_5),
                                    mContext.getResources().getDrawable(R.drawable.content_1_5),
                                    mContext.getResources().getDrawable(R.drawable.content_1_5)},
                            {mContext.getResources().getDrawable(R.drawable.content_1_5),
                                    mContext.getResources().getDrawable(R.drawable.content_1_5),
                                    mContext.getResources().getDrawable(R.drawable.content_1_5),
                                    mContext.getResources().getDrawable(R.drawable.content_1_5),
                                    mContext.getResources().getDrawable(R.drawable.content_1_5),
                                    mContext.getResources().getDrawable(R.drawable.content_1_5)}};
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
                    ContentViewProxy.BuiltInAdapter adapter = new ContentViewProxy.BuiltInAdapter()
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
                            .fadingWidth(DensityUtils.dp2px(mContext, 80));

                    mProxy.setBuiltInAdapter(adapter);
                    mProxy.getNotifier().notifyDataLoadDone(true);
                } else if (mSubPage == 2) {
                    mProxy.getNotifier().notifyDataLoadStart();

                    try {
                        Thread.sleep(3000);                  // simulate
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mProxy.getNotifier().notifyDataLoadDone(false);
                }
                break;
            case 4:
                if (mSubPage == 1) {
                    LogUtils.d("000");
                    mProxy.getNotifier().notifyDataLoadStart();

                    try {
                        Thread.sleep(3000);                  // simulate
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    View view = new View(mContext);
                    view.setBackground(mContext.getResources().getDrawable(R.drawable.content_3_4));
                    mProxy.getCustomViewHolder().setContentView(view);
                    mProxy.getNotifier().notifyDataLoadDone(true);
                }
                break;
        }
    }
}
