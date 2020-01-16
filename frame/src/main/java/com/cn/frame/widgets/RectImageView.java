package com.cn.frame.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class RectImageView extends android.support.v7.widget.AppCompatImageView {
    public RectImageView(Context context) {
        super(context);
    }

    public RectImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RectImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int h = this.getMeasuredHeight();
        int w = this.getMeasuredWidth();
        int curSquareDim = Math.max(w, h);
        setMeasuredDimension(curSquareDim, curSquareDim);
    }
}
