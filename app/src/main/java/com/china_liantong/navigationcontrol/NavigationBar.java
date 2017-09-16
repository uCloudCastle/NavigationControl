package com.china_liantong.navigationcontrol;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.BoringLayout;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
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
    private static final int CONTENT_SPACING_DP = 20;
    private static final int FOCUS_DRAWABLE_MARGIN = 32;

    Context mContext;
    ArrayList<TextView> mTextViews = new ArrayList<>();
    ArrayList<Rect> mRects = new ArrayList<>();
    View mGetFocusView;
    View mFocusView;

    private DataHolder mDataHolder;
    private NavigationBarListener mListener;
    private int mFirstItemTag;
    private int mLastPaddingLeft;

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
        RelativeLayout.LayoutParams focusViewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        focusViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
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
                    itemParams.setMargins(rect.left - mRects.get(0).left + DensityUtils.dp2px(mContext, CONTENT_SPACING_DP),
                            0, 0, DensityUtils.dp2px(mContext, CONTENT_SPACING_DP));
                    mTextViews.get(i).setLayoutParams(itemParams);

                    if (i == mDataHolder.fullDisplayNumber) {
                        RelativeLayout.LayoutParams mainParams = (RelativeLayout.LayoutParams) getLayoutParams();
                        mainParams.width = rect.centerX() - mRects.get(0).left + DensityUtils.dp2px(mContext, CONTENT_SPACING_DP);
                        setLayoutParams(mainParams);
                    }
                }

                RelativeLayout.LayoutParams focusViewParams = (RelativeLayout.LayoutParams) mFocusView.getLayoutParams();
                focusViewParams.addRule(RelativeLayout.ALIGN_BOTTOM, mTextViews.get(0).getId());
                focusViewParams.width = getTextViewWidth(0) + 2 * FOCUS_DRAWABLE_MARGIN;
                focusViewParams.height = mTextViews.get(0).getHeight() + 2 * FOCUS_DRAWABLE_MARGIN;
                focusViewParams.setMargins(DensityUtils.dp2px(mContext, CONTENT_SPACING_DP) - FOCUS_DRAWABLE_MARGIN,
                        0, 0, -FOCUS_DRAWABLE_MARGIN);
                mFocusView.setLayoutParams(focusViewParams);
            }
        }, 50);
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
            asyncMoveViews((int)oldFocus.getTag() - mFirstItemTag, (int)newFocus.getTag() - mFirstItemTag);
        }
    }

    private boolean checkIfInnerView(View view) {
        if (view == null || view.getTag() == null) {
            return false;
        }
        int tag = (int) view.getTag();
        return ((tag >= mFirstItemTag) && (tag <= mFirstItemTag + mTextViews.size() - 1));
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
        //mFocusView.setBackgroundColor(Color.CYAN);
        if (mListener != null) {
            mListener.onItemGetFocus((int) v.getTag() - mFirstItemTag);
        }
    }

    private void asyncMoveViews(final int oldPos, final int newPos) {
        if (oldPos < 0 || newPos < 0 || oldPos >= mRects.size()
                || newPos >= mRects.size() || Math.abs(oldPos - newPos) != 1) {
            return;
        }

        // async for get really textview width
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                moveMainLayout(oldPos, newPos);
                moveFocusView(oldPos, newPos);
            }
        }, 50);
    }

    private void moveMainLayout(final int oldPos, final int newPos) {
        int shifting = 0;
        if (oldPos >= mDataHolder.fullDisplayNumber - 1 && newPos >= mDataHolder.fullDisplayNumber - 1) {
            if (newPos == mTextViews.size() - 1) {
                shifting = mRects.get(newPos).centerX() - mRects.get(newPos).right - DensityUtils.dp2px(mContext, FOCUS_DRAWABLE_MARGIN + CONTENT_SPACING_DP);
            } else if (oldPos == mTextViews.size() - 1) {
                shifting = -mRects.get(oldPos).centerX() + mRects.get(oldPos).right + DensityUtils.dp2px(mContext, FOCUS_DRAWABLE_MARGIN + CONTENT_SPACING_DP);
            } else if (newPos > oldPos) {
                shifting = mRects.get(newPos).centerX() - mRects.get(newPos + 1).centerX();
            } else {        // oldPos > newPos
                shifting = mRects.get(oldPos + 1).centerX() - mRects.get(oldPos).centerX();
            }

            final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
            ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
            anim.setDuration(200);
            final int cp = mLastPaddingLeft;
            final int ep = cp + shifting;
            mLastPaddingLeft = ep;
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float currentValue = (float) animation.getAnimatedValue();
                    setPadding((int)(cp + (ep - cp) * currentValue), 0, 0, 0);
                    setLayoutParams(lp);
                }
            });
            anim.start();
        }
    }

    private void moveFocusView(final int oldPos, final int newPos) {
        final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mFocusView.getLayoutParams();
        final int startWidth = lp.width;
        final int startHeight = lp.height;
        final int startMarginLeft = mRects.get(oldPos).left - mRects.get(0).left + DensityUtils.dp2px(mContext, CONTENT_SPACING_DP);
        final int endWidth = getTextViewWidth(newPos) + 2 * FOCUS_DRAWABLE_MARGIN;
        final int endHeight = mTextViews.get(newPos).getHeight() + 2 * FOCUS_DRAWABLE_MARGIN;
        final int endMarginLeft = mRects.get(newPos).left - mRects.get(0).left + DensityUtils.dp2px(mContext, CONTENT_SPACING_DP);

        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                lp.width = (int)(startWidth + (endWidth - startWidth) * currentValue);
                lp.height = (int)(startHeight + (endHeight - startHeight) * currentValue);
                lp.setMargins((int)(startMarginLeft + (endMarginLeft - startMarginLeft) * currentValue) - FOCUS_DRAWABLE_MARGIN,
                        0, 0, -FOCUS_DRAWABLE_MARGIN);
                mFocusView.setLayoutParams(lp);
            }
        });
        anim.start();
    }

    private int getTextViewWidth(int pos) {
        if (pos < 0 || pos > mTextViews.size()) {
            return 0;
        }
        String str = mTextViews.get(pos).getText().toString();
        return (int)mTextViews.get(pos).getPaint().measureText(str);
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
