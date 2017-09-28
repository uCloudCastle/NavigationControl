package com.china_liantong.navigationcontrol;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.china_liantong.navigationcontrol.utils.CommonUtils;
import com.china_liantong.navigationcontrol.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by randal on 2017/9/14.
 */

public class NavigationBar extends RelativeLayout implements ViewTreeObserver.OnGlobalFocusChangeListener {
    private Context mContext;
    private ArrayList<TextView> mTextViews = new ArrayList<>();
    private ArrayList<Rect> mRects = new ArrayList<>();
    private View mFocusView;

    private View mGetFocusView;
    private DataHolder mDataHolder;
    private NavigationBarListener mListener;
    private int mFirstItemTag;
    private int mLastPaddingLeft;
    private boolean animDisable = false;
    private boolean needCircle = false;

    public NavigationBar(Context context) {
        this(context, null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        getViewTreeObserver().addOnGlobalFocusChangeListener(this);
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
        if (mDataHolder.titles == null || mDataHolder.titles.size() == 0) {
            return;
        }

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
            view.setId(CommonUtils.generateViewId());

            if (i == 0) {
                mFirstItemTag = mDataHolder.titles.get(0).hashCode();
            }
            view.setTag(mFirstItemTag + i);
            RelativeLayout.LayoutParams itemParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            itemParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            if (i != 0) {
                itemParams.addRule(RelativeLayout.RIGHT_OF, mTextViews.get(mTextViews.size() - 1).getId());
                itemParams.setMargins(mDataHolder.titleSpacing, 0, 0, 0);
            }
            addView(view, itemParams);
            mTextViews.add(view);
        }
        initListener();
    }

    private void initListener() {
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                asyncRelayout();
                return true;
            }
        });

        final int size = mTextViews.size();
        if (size >= 2) {
            mTextViews.get(0).setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        if (needCircle) {
                            mTextViews.get(size - 1).requestFocus();
                            needCircle = false;
                            return true;
                        }
                        needCircle = true;
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        needCircle = false;
                    }
                    return false;
                }
            });
            mTextViews.get(size - 1).setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        if (needCircle) {
                            mTextViews.get(0).requestFocus();
                            needCircle = false;
                            return true;
                        }
                        needCircle = true;
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        needCircle = false;
                    }
                    return false;
                }
            });
        }
    }

    private void asyncRelayout() {
        for (int i = 0; i < mTextViews.size(); ++i) {
            Rect rect = new Rect();
            mTextViews.get(i).getGlobalVisibleRect(rect);
            mRects.add(rect);

            RelativeLayout.LayoutParams itemParams = (RelativeLayout.LayoutParams) mTextViews.get(i).getLayoutParams();
            itemParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            itemParams.setMargins(
                    rect.left - mRects.get(0).left + (int) getResources().getDimension(R.dimen.navigation_bar_content_spacing),
                    0, 0,
                    (int) getResources().getDimension(R.dimen.navigation_bar_content_spacing));
            mTextViews.get(i).setLayoutParams(itemParams);

            if (i == mDataHolder.fullDisplayNumber) {
                ViewGroup.LayoutParams mainParams = getLayoutParams();
                mainParams.width = rect.centerX() - mRects.get(0).left + (int) getResources().getDimension(R.dimen.navigation_bar_content_spacing);
                setLayoutParams(mainParams);
            }
        }

        RelativeLayout.LayoutParams focusViewParams = (RelativeLayout.LayoutParams) mFocusView.getLayoutParams();
        focusViewParams.addRule(RelativeLayout.ALIGN_BOTTOM, mTextViews.get(0).getId());
        focusViewParams.width = getTextViewWidth(0) + 2 * mDataHolder.drawableMargin;
        focusViewParams.height = mTextViews.get(0).getHeight() + 2 * mDataHolder.drawableMargin;
        focusViewParams.setMargins((int) getResources().getDimension(R.dimen.navigation_bar_content_spacing) - mDataHolder.drawableMargin,
                0, 0, -mDataHolder.drawableMargin);
        mFocusView.setLayoutParams(focusViewParams);
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        LogUtils.d(LogUtils.printObject(oldFocus) + " " + LogUtils.printObject(newFocus));
        if (!checkIfInnerView(oldFocus) && checkIfInnerView(newFocus)) {     // outer to inner
            if (mGetFocusView != null && mGetFocusView.getTag() != newFocus.getTag()) {
                animDisable = true;
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
            if (animDisable) {
                animDisable = false;
                return;
            }
            asyncMoveViews((int) oldFocus.getTag() - mFirstItemTag, (int) newFocus.getTag() - mFirstItemTag);
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
        if (mListener != null) {
            mListener.onItemGetFocus((int) v.getTag() - mFirstItemTag);
        }
    }

    private void asyncMoveViews(final int oldPos, final int newPos) {
        if (oldPos < 0 || newPos < 0 || oldPos >= mRects.size()
                || newPos >= mRects.size()) {
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
        // circle move
        if (Math.abs(oldPos - newPos) == mTextViews.size() - 1) {
            if (oldPos == 0) {
                shifting = mRects.get(mDataHolder.fullDisplayNumber).centerX()
                        - mRects.get(newPos).right
                        - (mDataHolder.drawableMargin + (int) getResources().getDimension(R.dimen.navigation_bar_content_spacing));
            } else if (newPos == 0) {
                shifting = -mLastPaddingLeft;
            }
        }

        // cell move
        if (oldPos >= mDataHolder.fullDisplayNumber - 1 && newPos >= mDataHolder.fullDisplayNumber - 1) {
            if (newPos == mTextViews.size() - 1) {
                shifting = mRects.get(newPos).centerX() - mRects.get(newPos).right
                        - (mDataHolder.drawableMargin + (int) getResources().getDimension(R.dimen.navigation_bar_content_spacing));
            } else if (oldPos == mTextViews.size() - 1) {
                shifting = -mRects.get(oldPos).centerX() + mRects.get(oldPos).right
                        + (mDataHolder.drawableMargin + (int) getResources().getDimension(R.dimen.navigation_bar_content_spacing));
            } else if (newPos > oldPos) {
                shifting = mRects.get(newPos).centerX() - mRects.get(newPos + 1).centerX();
            } else {        // oldPos > newPos
                shifting = mRects.get(oldPos + 1).centerX() - mRects.get(oldPos).centerX();
            }
        }

        if (shifting != 0) {
            ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
            anim.setDuration(200);
            final int cp = mLastPaddingLeft;
            final int ep = cp + shifting;
            mLastPaddingLeft = ep;
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float currentValue = (float) animation.getAnimatedValue();
                    setPadding((int) (cp + (ep - cp) * currentValue), 0, 0, 0);
                }
            });
            anim.start();
        }
    }

    private void moveFocusView(final int oldPos, final int newPos) {
        final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mFocusView.getLayoutParams();
        final int startWidth = lp.width;
        final int startHeight = lp.height;
        final int startMarginLeft = mRects.get(oldPos).left - mRects.get(0).left
                + (int) getResources().getDimension(R.dimen.navigation_bar_content_spacing);
        final int endWidth = getTextViewWidth(newPos) + 2 * mDataHolder.drawableMargin;
        final int endHeight = mTextViews.get(newPos).getHeight() + 2 * mDataHolder.drawableMargin;
        final int endMarginLeft = mRects.get(newPos).left - mRects.get(0).left
                + (int) getResources().getDimension(R.dimen.navigation_bar_content_spacing);

        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                lp.width = (int) (startWidth + (endWidth - startWidth) * currentValue);
                lp.height = (int) (startHeight + (endHeight - startHeight) * currentValue);
                lp.setMargins((int) (startMarginLeft + (endMarginLeft - startMarginLeft) * currentValue) - mDataHolder.drawableMargin,
                        0, 0, -mDataHolder.drawableMargin);
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
        return (int) mTextViews.get(pos).getPaint().measureText(str);
    }

    public interface NavigationBarListener {
        void onItemGetFocus(int newPos);
    }

    public static class DataHolder {
        private List<String> titles;
        private int titleSpacing;
        private int fullDisplayNumber;
        private int textColor;
        private int textFocusColor;
        private int textSize;
        private int textFocusSize;
        private Drawable selectDrawable;
        private Drawable focusDrawable;
        private int drawableMargin;

        public DataHolder() {
        }

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

        public DataHolder drawableMargin(int margin) {
            drawableMargin = margin;
            return this;
        }

        public DataHolder titleSpacing(int spacing) {
            titleSpacing = spacing;
            return this;
        }
    }
}
