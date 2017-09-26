package com.china_liantong.navigationcontrol;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.china_liantong.navigationcontrol.utils.CommonUtils;
import com.china_liantong.navigationcontrol.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by randal on 2017/9/18.
 */

public class FragmentContainer extends FrameLayout implements NavigationBar.NavigationBarListener {
    private Activity mActivity;
    private FragmentManager mFgManager;
    private List<Fragment> mFragmentList = new ArrayList<>();

    private List<NavigationFragment.DataHolder> mDataHolders;
    private int mId;

    public FragmentContainer(Context context) {
        this(context, null);
    }

    public FragmentContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mId = CommonUtils.generateViewId();
        setId(mId);
    }

    public void setDataHolder(Activity activity, List<NavigationFragment.DataHolder> list) {
        if (activity != null && list != null) {
            mActivity = activity;
            mDataHolders = list;
            initView();
        }
    }

    private void initView() {
        if (mDataHolders.size() == 0) {
            return;
        }

        mFgManager = mActivity.getFragmentManager();
        FragmentTransaction fragTransaction = mFgManager.beginTransaction();
        for (int i = 0; i < mDataHolders.size(); ++i) {
            NavigationFragment fragment = new NavigationFragment();
            fragment.setDataHolder(mActivity, mDataHolders.get(i));
            mFragmentList.add(fragment);
            fragTransaction.add(mId, fragment);
        }
        fragTransaction.commit();
    }

    @Override
    public void onItemGetFocus(int newPos) {
        if (newPos >= 0 && newPos < mFragmentList.size()) {
            LogUtils.d("replace fragment : " + newPos);
            showFragmentInPos(newPos);
        }
    }

    private void showFragmentInPos(int pos) {
        if (pos >= mFragmentList.size()) {
            return;
        }
        FragmentTransaction fragTransaction = mFgManager.beginTransaction();
        for (int i = 0; i < mFragmentList.size(); ++i) {
            if (i != pos) {
                fragTransaction.hide(mFragmentList.get(i));
            }
        }
        fragTransaction.show(mFragmentList.get(pos)).commitAllowingStateLoss();
    }
}
