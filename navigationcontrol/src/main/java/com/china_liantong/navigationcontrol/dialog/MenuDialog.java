package com.china_liantong.navigationcontrol.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.china_liantong.navigationcontrol.R;
import com.china_liantong.navigationcontrol.utils.DensityUtils;

/**
 * Created by randal on 2017/10/12.
 */

public class MenuDialog extends Dialog {
    public static final int ICON_LEFT = 0;
    public static final int ICON_UP = 1;
    public static final int ICON_RIGHT = 2;
    public static final int ICON_DOWN = 3;

    public interface MenuDialogListener {
        void onIconClicked(int which);
    }

    private static final int GRADIENT_WIDTH = 10;
    private static final int ICON_SIZE = 66;
    private static final int ICON_LAYOUT_WIDTH = 80;

    private Context mContext;
    private View mFocusView;
    private FrameLayout mLayout;
    private int screen[] = new int[2];
    private int location[] = new int[4];
    private int mCurrentFocus = -1;
    private MenuDialogListener mListener;

    private View mIconLeft;
    private View mIconUp;
    private View mIconRight;
    private View mIconDown;

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

    public void setMenuDialogListener(MenuDialogListener l) {
        if (l != null) {
            mListener = l;
        }
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
        setIcons();
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyEvent.getKeyCode()) {
                        case KeyEvent.KEYCODE_DPAD_LEFT:
                            mIconLeft.setBackground(mContext.getResources().getDrawable(R.drawable.icn_delete_pre));
                            mIconUp.setBackground(mContext.getResources().getDrawable(R.drawable.icn_working));
                            mIconRight.setBackground(mContext.getResources().getDrawable(R.drawable.icn_cancel));
                            mIconDown.setBackground(mContext.getResources().getDrawable(R.drawable.icn_update));
                            mCurrentFocus = ICON_LEFT;
                            break;
                        case KeyEvent.KEYCODE_DPAD_UP:
                            mIconLeft.setBackground(mContext.getResources().getDrawable(R.drawable.icn_delete));
                            mIconUp.setBackground(mContext.getResources().getDrawable(R.drawable.icn_working_pre));
                            mIconRight.setBackground(mContext.getResources().getDrawable(R.drawable.icn_cancel));
                            mIconDown.setBackground(mContext.getResources().getDrawable(R.drawable.icn_update));
                            mCurrentFocus = ICON_UP;
                            break;
                        case KeyEvent.KEYCODE_DPAD_RIGHT:
                            mIconLeft.setBackground(mContext.getResources().getDrawable(R.drawable.icn_delete));
                            mIconUp.setBackground(mContext.getResources().getDrawable(R.drawable.icn_working));
                            mIconRight.setBackground(mContext.getResources().getDrawable(R.drawable.icn_cancel_pre));
                            mIconDown.setBackground(mContext.getResources().getDrawable(R.drawable.icn_update));
                            mCurrentFocus = ICON_RIGHT;
                            break;
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            mIconLeft.setBackground(mContext.getResources().getDrawable(R.drawable.icn_delete));
                            mIconUp.setBackground(mContext.getResources().getDrawable(R.drawable.icn_working));
                            mIconRight.setBackground(mContext.getResources().getDrawable(R.drawable.icn_cancel));
                            mIconDown.setBackground(mContext.getResources().getDrawable(R.drawable.icn_update_pre));
                            mCurrentFocus = ICON_DOWN;
                            break;
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                            if (mCurrentFocus != -1) {
                                if (mListener != null) {
                                    mListener.onIconClicked(mCurrentFocus);
                                }
                                dismiss();
                            }
                            break;
                    }
                }
                return false;
            }
        });
    }

    private void setIcons() {
        LinearLayout leftLayout = new LinearLayout(mContext);
        leftLayout.setOrientation(LinearLayout.VERTICAL);
        leftLayout.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams lParams = new FrameLayout.LayoutParams(ICON_LAYOUT_WIDTH, location[3] - location[1]);
        lParams.setMargins(location[0] - ICON_LAYOUT_WIDTH, location[1], 0, 0);
        mLayout.addView(leftLayout, lParams);
        mIconLeft = new View(mContext);
        mIconLeft.setBackground(mContext.getResources().getDrawable(R.drawable.icn_delete));
        FrameLayout.LayoutParams liParams = new FrameLayout.LayoutParams(ICON_SIZE, ICON_SIZE);
        leftLayout.addView(mIconLeft, liParams);
        TextView textLeft = new TextView(mContext);
        textLeft.setText("卸载");
        textLeft.setTextSize(22);
        textLeft.setTextColor(Color.parseColor("#d9d9d9"));
        FrameLayout.LayoutParams ltParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ltParams.setMargins(0, 8, 0, 0);
        leftLayout.addView(textLeft, ltParams);

        LinearLayout upLayout = new LinearLayout(mContext);
        upLayout.setOrientation(LinearLayout.VERTICAL);
        upLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        FrameLayout.LayoutParams uParams = new FrameLayout.LayoutParams(ICON_LAYOUT_WIDTH, 100);
        uParams.setMargins((location[0] + location[2] - ICON_LAYOUT_WIDTH) / 2, location[1] - 102, 0, 0);
        mLayout.addView(upLayout, uParams);
        mIconUp = new View(mContext);
        mIconUp.setBackground(mContext.getResources().getDrawable(R.drawable.icn_working));
        FrameLayout.LayoutParams uiParams = new FrameLayout.LayoutParams(ICON_SIZE, ICON_SIZE);
        upLayout.addView(mIconUp, uiParams);
        TextView textUp = new TextView(mContext);
        textUp.setText("运行");
        textUp.setTextSize(22);
        textUp.setTextColor(Color.parseColor("#d9d9d9"));
        FrameLayout.LayoutParams utParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        utParams.setMargins(0, 8, 0, 0);
        upLayout.addView(textUp, utParams);

        LinearLayout rightLayout = new LinearLayout(mContext);
        rightLayout.setOrientation(LinearLayout.VERTICAL);
        rightLayout.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams rParams = new FrameLayout.LayoutParams(ICON_LAYOUT_WIDTH, location[3] - location[1]);
        rParams.setMargins(location[2], location[1], 0, 0);
        mLayout.addView(rightLayout, rParams);
        mIconRight = new View(mContext);
        mIconRight.setBackground(mContext.getResources().getDrawable(R.drawable.icn_cancel));
        FrameLayout.LayoutParams riParams = new FrameLayout.LayoutParams(ICON_SIZE, ICON_SIZE);
        rightLayout.addView(mIconRight, riParams);
        TextView textRight = new TextView(mContext);
        textRight.setText("退订");
        textRight.setTextSize(22);
        textRight.setTextColor(Color.parseColor("#d9d9d9"));
        FrameLayout.LayoutParams rtParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rtParams.setMargins(0, 8, 0, 0);
        rightLayout.addView(textRight, rtParams);

        LinearLayout downLayout = new LinearLayout(mContext);
        downLayout.setOrientation(LinearLayout.VERTICAL);
        downLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        FrameLayout.LayoutParams dParams = new FrameLayout.LayoutParams(ICON_LAYOUT_WIDTH, 100);
        dParams.setMargins((location[0] + location[2] - ICON_LAYOUT_WIDTH) / 2, location[3], 0, 0);
        mLayout.addView(downLayout, dParams);
        mIconDown = new View(mContext);
        mIconDown.setBackground(mContext.getResources().getDrawable(R.drawable.icn_update));
        FrameLayout.LayoutParams diParams = new FrameLayout.LayoutParams(ICON_SIZE, ICON_SIZE);
        downLayout.addView(mIconDown, diParams);
        TextView textDown = new TextView(mContext);
        textDown.setText("更新");
        textDown.setTextSize(22);
        textDown.setTextColor(Color.parseColor("#d9d9d9"));
        FrameLayout.LayoutParams dtParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dtParams.setMargins(0, 8, 0, 0);
        downLayout.addView(textDown, dtParams);
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
