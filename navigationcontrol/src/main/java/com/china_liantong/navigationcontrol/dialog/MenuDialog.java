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
import com.china_liantong.navigationcontrol.utils.DensityUtils;

/**
 * Created by randal on 2017/10/12.
 */

public class MenuDialog extends Dialog {
    private static final int GRADIENT_WIDTH = 10;

    private Context mContext;
    private View mFocusView;
    private FrameLayout mLayout;
    private int screen[] = new int[2];
    private int location[] = new int[4];

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
        getScreenSize(screen);
        getLocation(location);
        mLayout = new FrameLayout(mContext);
        mLayout.setLayoutParams(new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        setContentView(mLayout);

        setConstantBackground();
        setGradientBackground();
    }

    private void setGradientBackground() {
        int gradient_px = DensityUtils.dp2px(mContext, GRADIENT_WIDTH);

        View gradLeft = new View(mContext);
        gradLeft.setBackground(mContext.getResources().getDrawable(R.drawable.menudialog_background_left));
        FrameLayout.LayoutParams lParams = new FrameLayout.LayoutParams(
                gradient_px, location[3] - location[1]);
        lParams.setMargins(location[0], location[1], 0, 0);
        mLayout.addView(gradLeft, lParams);

        View gradUp = new View(mContext);
        gradUp.setBackground(mContext.getResources().getDrawable(R.drawable.menudialog_background_up));
        FrameLayout.LayoutParams uParams = new FrameLayout.LayoutParams(
                location[2] - location[0], gradient_px);
        uParams.setMargins(location[0], location[1], 0, 0);
        mLayout.addView(gradUp, uParams);

        View gradRight = new View(mContext);
        gradRight.setBackground(mContext.getResources().getDrawable(R.drawable.menudialog_background_right));
        FrameLayout.LayoutParams rParams = new FrameLayout.LayoutParams(
                gradient_px, location[3] - location[1]);
        rParams.setMargins(location[2] - gradient_px, location[1], 0, 0);
        mLayout.addView(gradRight, rParams);

        View gradDown = new View(mContext);
        gradDown.setBackground(mContext.getResources().getDrawable(R.drawable.menudialog_background_down));
        FrameLayout.LayoutParams dParams = new FrameLayout.LayoutParams(
                location[2] - location[0], gradient_px);
        dParams.setMargins(location[0], location[3] - gradient_px, 0, 0);
        mLayout.addView(gradDown, dParams);
    }

    private void setConstantBackground() {
        View maskLeftUp = new View(mContext);
        maskLeftUp.setBackgroundColor(mContext.getResources().getColor(R.color.common_thirty_percent_mask));
        FrameLayout.LayoutParams luParams = new FrameLayout.LayoutParams(
                location[0], location[1]);
        mLayout.addView(maskLeftUp, luParams);

        View maskLeft = new View(mContext);
        maskLeft.setBackgroundColor(mContext.getResources().getColor(R.color.common_thirty_percent_mask));
        FrameLayout.LayoutParams lParams = new FrameLayout.LayoutParams(
                location[0], location[3] - location[1]);
        lParams.setMargins(0, location[1], 0, 0);
        mLayout.addView(maskLeft, lParams);

        View maskLeftDown = new View(mContext);
        maskLeftDown.setBackgroundColor(mContext.getResources().getColor(R.color.common_thirty_percent_mask));
        FrameLayout.LayoutParams ldParams = new FrameLayout.LayoutParams(
                location[0], screen[1] - location[3]);
        ldParams.setMargins(0, location[3], 0, 0);
        mLayout.addView(maskLeftDown, ldParams);

        View maskUp = new View(mContext);
        maskUp.setBackgroundColor(mContext.getResources().getColor(R.color.common_thirty_percent_mask));
        FrameLayout.LayoutParams uParams = new FrameLayout.LayoutParams(
                location[2] - location[0], location[1]);
        uParams.setMargins(location[0], 0, 0, 0);
        mLayout.addView(maskUp, uParams);

        View maskUpRight = new View(mContext);
        maskUpRight.setBackgroundColor(mContext.getResources().getColor(R.color.common_thirty_percent_mask));
        FrameLayout.LayoutParams urParams = new FrameLayout.LayoutParams(
                screen[0] - location[2], location[1]);
        urParams.setMargins(location[2], 0, 0, 0);
        mLayout.addView(maskUpRight, urParams);

        View maskRight = new View(mContext);
        maskRight.setBackgroundColor(mContext.getResources().getColor(R.color.common_thirty_percent_mask));
        FrameLayout.LayoutParams mrParams = new FrameLayout.LayoutParams(
                screen[0] - location[2], location[3] - location[1]);
        mrParams.setMargins(location[2], location[1], 0, 0);
        mLayout.addView(maskRight, mrParams);

        View maskRightDown = new View(mContext);
        maskRightDown.setBackgroundColor(mContext.getResources().getColor(R.color.common_thirty_percent_mask));
        FrameLayout.LayoutParams rdParams = new FrameLayout.LayoutParams(
                screen[0] - location[2], screen[1] - location[3]);
        rdParams.setMargins(location[2], location[3], 0, 0);
        mLayout.addView(maskRightDown, rdParams);

        View maskDown = new View(mContext);
        maskDown.setBackgroundColor(mContext.getResources().getColor(R.color.common_thirty_percent_mask));
        FrameLayout.LayoutParams mdParams = new FrameLayout.LayoutParams(
                location[2] - location[0], screen[1] - location[3]);
        mdParams.setMargins(location[0], location[3], 0, 0);
        mLayout.addView(maskDown, mdParams);
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

        int gradient_px = DensityUtils.dp2px(mContext, GRADIENT_WIDTH);
        location[0] -= gradient_px;
        location[1] -= gradient_px;
        location[2] += gradient_px;
        location[3] += gradient_px;

        location[0] -= (int)mContext.getResources().getDimension(R.dimen.gridview_select_padding_left);
        location[1] -= (int)mContext.getResources().getDimension(R.dimen.gridview_select_padding_top);
        location[2] += (int)mContext.getResources().getDimension(R.dimen.gridview_select_padding_right);
        location[3] += (int)mContext.getResources().getDimension(R.dimen.gridview_select_padding_bottom);
    }
}
