package com.china_liantong.navigationcontrol;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by randal on 2017/9/14.
 */

public class NavigationBar extends RelativeLayout implements ViewTreeObserver.OnGlobalFocusChangeListener {
    Context mContext;
    View mGetFocusView;
    View mFocusView;

    private DataHolder mDataHolder;
    private NavigationBarListener mListener;
    private int mTagFirstItem;
    private int mVisibleWidth;

    public NavigationBar(Context context) {
        this(context, null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalFocusChangeListener(this);
        //setBackgroundColor(Color.GREEN);
    }

    public void setDataHolder(DataHolder holder) {
        if (holder != null) {
            mDataHolder = holder;
            initView();
        }
    }

    public void setVisibleWidth(int width) {
        mVisibleWidth = width;
    }

    public void setNavigationBarListener(NavigationBarListener l) {
        if (l != null) mListener = l;
    }

    private void initView() {
        ScrollView scrollView = new ScrollView(mContext);
        RelativeLayout.LayoutParams scrollViewParams = new RelativeLayout.LayoutParams(
                mVisibleWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
        addView(scrollView, scrollViewParams);

        LinearLayout itemLayout = new LinearLayout(mContext);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setGravity(Gravity.BOTTOM);

        mFocusView = new View(mContext);
        mFocusView.setBackground(mDataHolder.focusDrawable);
        RelativeLayout.LayoutParams focusViewParams = new RelativeLayout.LayoutParams(
                80, 80);
        addView(mFocusView, focusViewParams);

        for (int i = 0; i < mDataHolder.titles.size(); ++i) {
            LogUtils.d(mDataHolder.titles.get(i));

            TextView view = new TextView(mContext);
            view.setText(mDataHolder.titles.get(i));
            view.setFocusable(true);
            view.setTextColor(mDataHolder.textColor);
            view.setTextSize(mDataHolder.textSize);
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                itemParams.setMargins(mDataHolder.textSpacing, 0, 0, 0);
            } else {
                itemParams.setMargins(30, 0, 0, 0);
                mTagFirstItem = mDataHolder.titles.get(i).hashCode();
            }

            LogUtils.d("setTag = " + mTagFirstItem + i);
            view.setTag(mTagFirstItem + i);
            itemLayout.addView(view, itemParams);
        }

        RelativeLayout.LayoutParams itemLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        scrollView.addView(itemLayout, itemLayoutParams);
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        LogUtils.d(LogUtils.printObject(oldFocus) + " " + LogUtils.printObject(newFocus));

        if (!checkIfInnerView(oldFocus) && checkIfInnerView(newFocus)){     // outer to inner
            if (mGetFocusView != null && mGetFocusView.getTag() != newFocus.getTag()) {
                mGetFocusView.requestFocus();
                return;
            }
            mGetFocusView = newFocus;
            setFocusStyle((TextView) newFocus);
        } else if (checkIfInnerView(oldFocus) && !checkIfInnerView(newFocus)) {    // inner to outer
            //setUnFocusStyle((TextView) oldFocus);
        } else if (checkIfInnerView(oldFocus) && checkIfInnerView(newFocus)) {     // inner to inner
            mGetFocusView = newFocus;
            setNormalStyle((TextView) oldFocus);
            setFocusStyle((TextView) newFocus);
        }
    }

    private boolean checkIfInnerView(View view) {
        if (view == null || view.getTag() == null) {
            return false;
        }
        int tag = (int) view.getTag();
        return ((tag >= mTagFirstItem) && (tag <= mTagFirstItem + mDataHolder.titles.size() - 1));
    }

    private void setNormalStyle(TextView v) {
        v.setTextColor(mDataHolder.textColor);
        v.setTextSize(mDataHolder.textSize);
    }

//    private void setUnFocusStyle(TextView v) {
//        v.setBackground(getResources().getDrawable(R.drawable.layer_selectbar_unfocus));
//        v.setTextColor(getResources().getColor(R.color.common_white));
//        mFocusBackground.setVisibility(INVISIBLE);
//    }

    private void setFocusStyle(TextView v) {
        v.setTextColor(mDataHolder.textFocusColor);
        v.setTextSize(mDataHolder.textFocusSize);
        mFocusView.setVisibility(VISIBLE);

        final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mFocusView.getLayoutParams();
        final int start = mFocusView.getLeft();
        final int end = v.getLeft();
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                lp.setMargins((int)(start + (end - start) * currentValue), 0, 0, 0);
                mFocusView.setLayoutParams(lp);
            }
        });
        anim.start();

        if (mListener != null) {
            mListener.onItemGetFocus((int) v.getTag() - mTagFirstItem);
        }
    }

    public interface NavigationBarListener {
        void onItemGetFocus(int newPos);
    }

    public static class DataHolder {
        private List<String> titles;
        private int textColor;
        private int textFocusColor;
        private int textSize;
        private int textFocusSize;
        private Drawable focusDrawable;
        private int textSpacing;

        public DataHolder() {}

        public DataHolder titles(List<String> s) {
            titles = s;
            return this;
        }

        public DataHolder textColor(int color) {
            textColor = color;
            return this;
        }

        public DataHolder textFocusColor(int color) {
            textFocusColor = color;
            return this;
        }

        public DataHolder textSize(int size) {
            textSize = size;
            return this;
        }

        public DataHolder textFocusSize(int size) {
            textFocusSize = size;
            return this;
        }

        public DataHolder focusDrawable(Drawable drawable) {
            focusDrawable = drawable;
            return this;
        }

        public DataHolder textSpacing(int spacing) {
            textSpacing = spacing;
            return this;
        }
    }
}
