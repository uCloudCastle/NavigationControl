package com.china_liantong.navigationcontrol.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.china_liantong.navigationcontrol.R;

/**
 * Created by randal on 2017/10/12.
 */

public class MenuDialog extends Dialog {
    private Context mContext;
    private View mFocusView;

    public MenuDialog(Context context) {
        this(context, R.style.CustomDialog);
    }

    public MenuDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public void setFocusView(View view) {
        mFocusView = view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mFocusView == null) {
            return;
        }

        FrameLayout mainLayout = new FrameLayout(mContext);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        int screen[] = new int[2];
        int location[] = new int[4];
        getScreenSize(screen);
        getLocation(location);

        View maskLeftUp = new View(mContext);
        maskLeftUp.setBackgroundColor(mContext.getResources().getColor(R.color.common_thirty_percent_mask));
        FrameLayout.LayoutParams luParams = new FrameLayout.LayoutParams(
                location[0], location[1]);
        mainLayout.addView(maskLeftUp, luParams);

        View maskLeft = new View(mContext);
        maskLeft.setBackgroundColor(mContext.getResources().getColor(R.color.common_thirty_percent_mask));
        FrameLayout.LayoutParams lParams = new FrameLayout.LayoutParams(
                location[0], location[3] - location[1]);
        lParams.setMargins(0, location[1], 0, 0);
        mainLayout.addView(maskLeft, lParams);

        View maskLeftDown = new View(mContext);
        maskLeftDown.setBackgroundColor(mContext.getResources().getColor(R.color.common_thirty_percent_mask));
        FrameLayout.LayoutParams ldParams = new FrameLayout.LayoutParams(
                location[0], screen[1] - location[3]);
        ldParams.setMargins(0, location[3], 0, 0);
        mainLayout.addView(maskLeftDown, ldParams);

        View maskUp = new View(mContext);
        maskUp.setBackgroundColor(mContext.getResources().getColor(R.color.common_thirty_percent_mask));
        FrameLayout.LayoutParams uParams = new FrameLayout.LayoutParams(
                location[2] - location[0], location[1]);
        uParams.setMargins(location[0], 0, 0, 0);
        mainLayout.addView(maskUp, uParams);

        View maskUpRight = new View(mContext);
        maskUp.setBackgroundColor(mContext.getResources().getColor(R.color.common_thirty_percent_mask));
        FrameLayout.LayoutParams urParams = new FrameLayout.LayoutParams(
                screen[2] - location[2], location[1]);
        urParams.setMargins(location[2], 0, 0, 0);
        mainLayout.addView(maskUp, urParams);

        View maskRight = new View(mContext);
        maskRight.setBackgroundColor(mContext.getResources().getColor(R.color.common_thirty_percent_mask));
        FrameLayout.LayoutParams mrParams = new FrameLayout.LayoutParams(
                screen[0] - location[2], screen[1] - location[1]);
        mrParams.setMargins(location[2], location[1], 0, 0);
        mainLayout.addView(maskRight, mrParams);

        View maskDown = new View(mContext);
        maskDown.setBackgroundColor(mContext.getResources().getColor(R.color.common_thirty_percent_mask));
        FrameLayout.LayoutParams mdParams = new FrameLayout.LayoutParams(
                location[2] - location[0], screen[1] - location[3]);
        mdParams.setMargins(location[0], location[3], 0, 0);
        mainLayout.addView(maskDown, mdParams);



//        View maskUp = new View(mContext);
//        FrameLayout.LayoutParams muParams = new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, location[1]);
//        mainLayout.addView(maskUp, muParams);
//        View maskUp = new View(mContext);
//        FrameLayout.LayoutParams muParams = new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, location[1]);
//        mainLayout.addView(maskUp, muParams);




        setContentView(mainLayout);

//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                mFocusView.getWidth(), mFocusView.getHeight());
//        params.setMargins(location[0], location[1], 0, 0);
//        mainLayout.addView(view, params);

    }

    private void getScreenSize(int[] screen) {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screen[0] = outMetrics.widthPixels;
        screen[1] = outMetrics.heightPixels;
    }

    private void getLocation(int[] location) {
        int tmp[] = new int[2];
        mFocusView.getLocationInWindow(tmp);
        location[0] = tmp[0];
        location[1] = tmp[1];
        location[2] = tmp[0] + mFocusView.getWidth();
        location[3] = tmp[1] + mFocusView.getHeight();

        location[0] -= (int)mContext.getResources().getDimension(R.dimen.gridview_select_padding_left);
        location[1] -= (int)mContext.getResources().getDimension(R.dimen.gridview_select_padding_top);
        location[2] += (int)mContext.getResources().getDimension(R.dimen.gridview_select_padding_right);
        location[3] += (int)mContext.getResources().getDimension(R.dimen.gridview_select_padding_bottom);
    }
}
