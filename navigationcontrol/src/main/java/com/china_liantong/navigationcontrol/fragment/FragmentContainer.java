package com.china_liantong.navigationcontrol.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.china_liantong.navigationcontrol.NavigationBar;
import com.china_liantong.navigationcontrol.NavigationControl;
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
    private NavigationControl.OnItemClickListener mItemClickListener;
    private List<NavigationFragment> mFragmentList = new ArrayList<>();

    private List<NavigationFragment.DataHolder> mDataHolders;
    private int mId;
    private int mCurPos;

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

    public void setOnItemClickListener(NavigationControl.OnItemClickListener l) {
        mItemClickListener = l;
        for (NavigationFragment fragment : mFragmentList) {
            fragment.setOnItemClickListener(l);
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
            fragment.setPagePos(i);
            mFragmentList.add(fragment);
            fragTransaction.add(mId, fragment);
        }
        fragTransaction.commit();
        showFragmentByPos(0);
    }

    @Override
    public void onItemGetFocus(int newPos) {
        if (mCurPos == newPos) {
            return;
        }

        if (newPos >= 0 && newPos < mFragmentList.size()) {
            LogUtils.d("replace fragment : " + newPos);
            showFragmentByPos(newPos);
            mCurPos = newPos;
        }
    }

    private void showFragmentByPos(int pos) {
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

        if (mItemClickListener != null) {
            mItemClickListener.onPageChanged(pos, 0);
        }
    }
}
