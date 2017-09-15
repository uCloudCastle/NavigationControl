package com.china_liantong.navigationcontrol;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.china_liantong.navigationcontrol.utils.DensityUtils;
import com.china_liantong.navigationcontrol.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by randal on 2017/9/14.
 */

public class NavigationBar extends RelativeLayout implements ViewTreeObserver.OnGlobalFocusChangeListener {
    private static final int CONTENT_SPACING_DP = 10;

    Context mContext;
    ArrayList<TextView> mTextViews = new ArrayList<>();
    ArrayList<Rect> mRects = new ArrayList<>();
    //LinearLayout itemLayout;
    View mGetFocusView;
    View mFocusView;

    private DataHolder mDataHolder;
    private NavigationBarListener mListener;
    private int mFirstItemTag;

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

    public void setNavigationBarListener(NavigationBarListener l) {
        if (l != null) mListener = l;
    }

    private void initView() {
        mFocusView = new View(mContext);
        mFocusView.setBackground(mDataHolder.focusDrawable);
        RelativeLayout.LayoutParams focusViewParams = new RelativeLayout.LayoutParams(100, 62);
        addView(mFocusView, focusViewParams);

        for (int i = 0; i < mDataHolder.titles.size(); ++i) {
            TextView view = new TextView(mContext);
            view.setText(mDataHolder.titles.get(i));
            view.setFocusable(true);
            view.setLines(1);
            view.setTextColor(mDataHolder.textColor);
            view.setTextSize(mDataHolder.textSize);
            view.setId(View.generateViewId());

            if (i == 0) {
                mFirstItemTag = mDataHolder.titles.get(0).hashCode();
            }
            view.setTag(mFirstItemTag + i);
            RelativeLayout.LayoutParams itemParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            itemParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            if (i != 0) {
                itemParams.addRule(RelativeLayout.RIGHT_OF, mTextViews.get(mTextViews.size() - 1).getId());
                itemParams.setMargins(mDataHolder.textSpacing, 0, 0, 0);
            }
            addView(view, itemParams);
            mTextViews.add(view);
        }
        asyncRelayout();
    }

    private void asyncRelayout() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mTextViews.size(); ++i) {
                    Rect rect = new Rect();
                    mTextViews.get(i).getGlobalVisibleRect(rect);
                    mRects.add(rect);
                    LogUtils.d(i + rect.toString() + " " + rect.centerX());

                    RelativeLayout.LayoutParams itemParams = (RelativeLayout.LayoutParams) mTextViews.get(i).getLayoutParams();
                    itemParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    itemParams.setMargins(rect.left - mRects.get(0).left + DensityUtils.dp2px(mContext, CONTENT_SPACING_DP)
                            , 0, 0, DensityUtils.dp2px(mContext, CONTENT_SPACING_DP));
                    mTextViews.get(i).setLayoutParams(itemParams);

                    if (i == mDataHolder.fullDisplayNumber) {
                        RelativeLayout.LayoutParams mainParams = (RelativeLayout.LayoutParams) getLayoutParams();
                        mainParams.width = rect.centerX() - mRects.get(0).left;
                        setLayoutParams(mainParams);
                    }
                }
                moveFocusView(0);
            }
        }, 200);
    }

    private void moveFocusView(int pos) {
        RelativeLayout.LayoutParams focusViewParams = (RelativeLayout.LayoutParams) mFocusView.getLayoutParams();
        focusViewParams.addRule(RelativeLayout.ALIGN_LEFT, mTextViews.get(pos).getId());
        focusViewParams.addRule(RelativeLayout.ALIGN_TOP, mTextViews.get(pos).getId());
        focusViewParams.addRule(RelativeLayout.ALIGN_RIGHT, mTextViews.get(pos).getId());
        focusViewParams.addRule(RelativeLayout.ALIGN_BOTTOM, mTextViews.get(pos).getId());
        focusViewParams.setMargins(-20, -20, -20, -20);
//        focusViewParams.width = (int) mRects.get(0).width() * 1.5;
//        focusViewParams.height = mRects.get(0).height() * 2;
//        focusViewParams.setMargins(-mRects.get(0).width() / 3, 0, 0, -mRects.get(0).height() / 3);
        mFocusView.setLayoutParams(focusViewParams);
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        //LogUtils.d(LogUtils.printObject(oldFocus) + " " + LogUtils.printObject(newFocus));

        if (!checkIfInnerView(oldFocus) && checkIfInnerView(newFocus)){     // outer to inner
            if (mGetFocusView != null && mGetFocusView.getTag() != newFocus.getTag()) {
                mGetFocusView.requestFocus();
                return;
            }
            mGetFocusView = newFocus;
            setFocusStyle((TextView) newFocus);
        } else if (checkIfInnerView(oldFocus) && !checkIfInnerView(newFocus)) {    // inner to outer
            setUnFocusStyle((TextView) oldFocus);
        } else if (checkIfInnerView(oldFocus) && checkIfInnerView(newFocus)) {     // inner to inner
            mGetFocusView = newFocus;
            setNormalStyle((TextView) oldFocus);
            setFocusStyle((TextView) newFocus);
            moveFocusView((int)oldFocus.getTag() - mFirstItemTag, (int)newFocus.getTag() - mFirstItemTag);
        }
    }

    private boolean checkIfInnerView(View view) {
        if (view == null || view.getTag() == null) {
            return false;
        }
        int tag = (int) view.getTag();
        return ((tag >= mFirstItemTag) && (tag <= mFirstItemTag + mDataHolder.titles.size() - 1));
    }

    private void setNormalStyle(TextView v) {
        v.setTextColor(mDataHolder.textColor);
        v.setTextSize(mDataHolder.textSize);
    }

    private void setUnFocusStyle(TextView v) {
        v.setTextColor(mDataHolder.textFocusColor);
        v.setTextSize(mDataHolder.textFocusSize);
        mFocusView.setBackground(mDataHolder.selectDrawable);
    }

    private void setFocusStyle(TextView v) {
        v.setTextColor(mDataHolder.textFocusColor);
        v.setTextSize(mDataHolder.textFocusSize);
        mFocusView.setBackground(mDataHolder.focusDrawable);
        if (mListener != null) {
            mListener.onItemGetFocus((int) v.getTag() - mFirstItemTag);
        }
    }

    private void moveFocusView(int oldPos, int newPos) {
        if (oldPos < 0 || newPos < 0 || oldPos >= mRects.size()
                || newPos >= mRects.size()) {
            return;
        }

        if (oldPos >= mDataHolder.fullDisplayNumber - 1 && newPos >= mDataHolder.fullDisplayNumber - 1) {
            // move scrollView
//            final int shifting = (mRects.get(newPos).left - mRects.get(oldPos).left);
//            //LogUtils.d("111 " + (mRects.get(newPos).left - mRects.get(oldPos).left));
//            final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) itemLayout.getLayoutParams();
////            final int start = mFocusView.getLeft();
////            final int end = v.getLeft();
//            ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
//            anim.setDuration(300);
//            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    float currentValue = (float) animation.getAnimatedValue();
//                    itemLayout.setPadding((int) (0 + (shifting) * currentValue), 0, 0, 0);
//                    mFocusView.setLayoutParams(lp);
//                }
//            });
//            anim.start();
        }

//        final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mFocusView.getLayoutParams();
//        final int start = mFocusView.getLeft();
//        final int end = v.getLeft();
//        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
//        anim.setDuration(300);
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float currentValue = (float) animation.getAnimatedValue();
//                lp.setMargins((int)(start + (end - start) * currentValue), 0, 0, 0);
//                mFocusView.setLayoutParams(lp);
//            }
//        });
//        anim.start();
    }

    public interface NavigationBarListener {
        void onItemGetFocus(int newPos);
    }

    public static class DataHolder {
        private List<String> titles;
        private int fullDisplayNumber;
        private int textColor;
        private int textFocusColor;
        private int textSize;
        private int textFocusSize;
        private Drawable selectDrawable;
        private Drawable focusDrawable;
        private int textSpacing;

        public DataHolder() {}

        public DataHolder titles(List<String> s) {
            titles = s;
            return this;
        }

        public DataHolder fullDisplayNumber(int num) {
            fullDisplayNumber = num;
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

        public DataHolder selectDrawable(Drawable drawable) {
            selectDrawable = drawable;
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
