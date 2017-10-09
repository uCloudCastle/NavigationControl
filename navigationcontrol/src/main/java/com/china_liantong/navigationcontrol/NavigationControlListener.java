package com.china_liantong.navigationcontrol;

import android.view.View;

/**
 * Created by randal on 2017/10/9.
 */

public interface NavigationControlListener {
    void onBuiltInItemGetFocus(View focusView, int position);
    void onBuiltInItemClick(View focusView, int position);
    void onPageChanged(int page, int subpage);
}
