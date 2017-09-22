package com.china_liantong.navigationcontrol.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.china_liantong.navigationcontrol.R;

/**
 * Created by randal on 2017/9/22.
 */

public class SquareMaskView extends View {
    private static final int SHADOW_COEFFICIENT = 22;

    private Bitmap mBitmapImage;
    private Bitmap mBitmapMask;
    private PorterDuffXfermode mXfermode;
    private Paint mPaint;
    private RectF mRectF;

    public SquareMaskView(Context context) {
        this(context, null);
    }

    public SquareMaskView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SquareMaskView, 0, 0);
        int imageSrc = array.getResourceId(R.styleable.SquareMaskView_image, 0);
        int maskSrc = array.getResourceId(R.styleable.SquareMaskView_mask, 0);

        if (imageSrc != 0) {
            mBitmapImage = BitmapFactory.decodeResource(getResources(), imageSrc);
        }
        if (maskSrc != 0) {
            mBitmapMask = BitmapFactory.decodeResource(getResources(), maskSrc);
        }
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        array.recycle();

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                resizeViews();
                return true;
            }
        });
    }

    public void setForegroundImage(Drawable drawable) {
        if (drawable != null) {
            mBitmapImage = ((BitmapDrawable) drawable).getBitmap();
            resizeViews();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = widthMeasureSpec < heightMeasureSpec ? widthMeasureSpec : heightMeasureSpec;
        super.onMeasure(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setXfermode(mXfermode);
        canvas.saveLayer(mRectF, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(mBitmapImage, 0, 0, null);
        canvas.drawBitmap(mBitmapMask, 0, 0, mPaint);
        canvas.restore();
    }

    private void resizeViews() {
        int actualWidth = getWidth() - (getWidth() / SHADOW_COEFFICIENT);
        int actualHeight = getHeight() - (getHeight() / SHADOW_COEFFICIENT);
        if (actualWidth <= 0 || actualHeight <= 0) {
            return;
        }

        mRectF = new RectF(0, 0, actualWidth, actualHeight);
        mBitmapImage = Bitmap.createScaledBitmap(mBitmapImage, actualWidth, actualHeight, true);
        mBitmapMask = Bitmap.createScaledBitmap(mBitmapMask, actualWidth, actualHeight, true);
    }
}
