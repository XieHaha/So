package com.yzq.zxinglibrary.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.google.zxing.ResultPoint;
import com.yzq.zxinglibrary.R;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.camera.CameraManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dundun
 */
public final class ViewfinderView extends View {
    /**
     * 界面刷新间隔时间
     */
    private static final long ANIMATION_DELAY = 80L;
    private static final int CURRENT_POINT_OPACITY = 0xA0;
    private static final int MAX_RESULT_POINTS = 20;
    private static final int POINT_SIZE = 6;
    private CameraManager cameraManager;
    private Paint paint, scanLinePaint, reactPaint, frameLinePaint, wordsPaint;
    private Bitmap resultBitmap, scanLineBitmap;
    /**
     * 取景框外的背景颜色
     */
    private int maskColor;
    /**
     * result Bitmap的颜色
     */
    private int resultColor;
    /**
     * 特征点的颜色
     */
    private int resultPointColor;
    /**
     * 四个角的颜色
     */
    private int reactColor;
    /**
     * 扫描线的颜色
     */
    private int scanLineColor;
    /**
     * 边框线的颜色
     */
    private int frameLineColor = -1;
    private List<ResultPoint> possibleResultPoints;
    private List<ResultPoint> lastPossibleResultPoints;
    /**
     * 扫描线移动的y
     */
    private int scanLineTop;
    private ZxingConfig config;
    private ValueAnimator valueAnimator;
    private Rect rect, textRect;
    private String hintText = "将二维码放入框中，即可自动扫描";

    public ViewfinderView(Context context) {
        this(context, null);
    }

    public ViewfinderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setZxingConfig(ZxingConfig config) {
        this.config = config;
        reactColor = ContextCompat.getColor(getContext(), config.getReactColor());
        if (config.getFrameLineColor() != -1) {
            frameLineColor = ContextCompat.getColor(getContext(), config.getFrameLineColor());
        }
        scanLineColor = ContextCompat.getColor(getContext(), config.getScanLineColor());
        initPaint();
    }

    public ViewfinderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        maskColor = ContextCompat.getColor(getContext(), R.color.viewfinder_mask);
        resultColor = ContextCompat.getColor(getContext(), R.color.result_view);
        resultPointColor = ContextCompat.getColor(getContext(), R.color.possible_result_points);
        possibleResultPoints = new ArrayList<ResultPoint>(10);
        lastPossibleResultPoints = null;
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        /*四个角的画笔*/
        reactPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        reactPaint.setColor(reactColor);
        reactPaint.setStyle(Paint.Style.FILL);
        reactPaint.setStrokeWidth(dp2px(1));

        /*边框线画笔*/
        if (frameLineColor != -1) {
            frameLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            frameLinePaint.setColor(ContextCompat.getColor(getContext(), config.getFrameLineColor()));
            frameLinePaint.setStrokeWidth(dp2px(1));
            frameLinePaint.setStyle(Paint.Style.STROKE);
        }



        /*扫描线画笔*/
        scanLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scanLinePaint.setStrokeWidth(dp2px(2));
        scanLinePaint.setStyle(Paint.Style.FILL);
        scanLinePaint.setDither(true);
        scanLinePaint.setColor(scanLineColor);
        //扫描线 bitmap
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.pic_camera_line);
        scanLineBitmap = ((BitmapDrawable)drawable).getBitmap();
        //文字画笔
        wordsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wordsPaint.setColor(ContextCompat.getColor(getContext(), R.color.defaultColor));
        wordsPaint.setTextSize(sp2px(16));
    }

    private void initAnimator() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(rect.top, rect.bottom);
            valueAnimator.setDuration(3000);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scanLineTop = (int)animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.start();
        }
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    public void stopAnimator() {
        if (valueAnimator != null) {
            valueAnimator.end();
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        if (cameraManager == null) {
            return;
        }
        // rect为取景框
        rect = cameraManager.getFramingRect();
        Rect previewFrame = cameraManager.getFramingRectInPreview();
        if (rect == null || previewFrame == null) {
            return;
        }
        initAnimator();
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        textRect = new Rect(0, rect.bottom + dp2px(40), width, rect.bottom + dp2px(100));
        /*绘制遮罩*/
        drawMaskView(canvas, rect, width, height);

        /*绘制取景框边框*/
        drawFrameBounds(canvas, rect);
        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            // 如果有二维码结果的Bitmap，在扫取景框内绘制不透明的result Bitmap
            paint.setAlpha(CURRENT_POINT_OPACITY);
            canvas.drawBitmap(resultBitmap, null, rect, paint);
        }
        else {
            /*绘制扫描线*/
            drawScanLight(canvas, rect);
            /*绘制闪动的点*/
            // drawPoint(canvas, rect, previewFrame);
        }
        //绘制文字
        drawHintText(canvas, textRect, rect, width);
    }

    private void drawPoint(Canvas canvas, Rect rect, Rect previewFrame) {
        float scaleX = rect.width() / (float)previewFrame.width();
        float scaleY = rect.height() / (float)previewFrame.height();
        // 绘制扫描线周围的特征点
        List<ResultPoint> currentPossible = possibleResultPoints;
        List<ResultPoint> currentLast = lastPossibleResultPoints;
        int frameLeft = rect.left;
        int frameTop = rect.top;
        if (currentPossible.isEmpty()) {
            lastPossibleResultPoints = null;
        }
        else {
            possibleResultPoints = new ArrayList<ResultPoint>(5);
            lastPossibleResultPoints = currentPossible;
            paint.setAlpha(CURRENT_POINT_OPACITY);
            paint.setColor(resultPointColor);
            synchronized (currentPossible) {
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frameLeft + (int)(point.getX() * scaleX), frameTop + (int)(point.getY() * scaleY),
                                      POINT_SIZE, paint);
                }
            }
        }
        if (currentLast != null) {
            paint.setAlpha(CURRENT_POINT_OPACITY / 2);
            paint.setColor(resultPointColor);
            synchronized (currentLast) {
                float radius = POINT_SIZE / 2.0f;
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frameLeft + (int)(point.getX() * scaleX), frameTop + (int)(point.getY() * scaleY),
                                      radius, paint);
                }
            }
        }
        // Request another update at the animation interval, but only
        // repaint the laser line,
        // not the entire viewfinder mask.
        postInvalidateDelayed(ANIMATION_DELAY, rect.left - POINT_SIZE, rect.top - POINT_SIZE, rect.right + POINT_SIZE,
                              rect.bottom + POINT_SIZE);
    }

    private void drawMaskView(Canvas canvas, Rect rect, int width, int height) {
        // Draw the exterior (i.e. outside the framing rect) darkened
        // 绘制取景框外的暗灰色的表面，分四个矩形绘制
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        /*上面的框*/
        canvas.drawRect(0, 0, width, rect.top, paint);
        /*绘制左边的框*/
        canvas.drawRect(0, rect.top, rect.left, rect.bottom + 1, paint);
        /*绘制右边的框*/
        canvas.drawRect(rect.right + 1, rect.top, width, rect.bottom + 1, paint);
        /*绘制下面的框*/
        canvas.drawRect(0, rect.bottom + 1, width, height, paint);
    }

    /**
     * 绘制取景框边框
     *
     * @param canvas
     * @param rect
     */
    private void drawFrameBounds(Canvas canvas, Rect rect) {
        /*扫描框的边框线*/
        if (frameLineColor != -1) {
            canvas.drawRect(rect, frameLinePaint);
        }

        /*四个角的长度和宽度*/
        int width = rect.width();
        int corLength = (int)(width * 0.07);
        int corWidth = (int)(corLength * 0.2);
        corWidth = corWidth > 15 ? 15 : corWidth;

        /*角在线外*/
        // 左上角
        canvas.drawRect(rect.left, rect.top, rect.left + corWidth, rect.top + corLength, reactPaint);
        canvas.drawRect(rect.left, rect.top, rect.left + corLength, rect.top + corWidth, reactPaint);
        // 右上角
        canvas.drawRect(rect.right - corWidth, rect.top, rect.right , rect.top + corLength, reactPaint);
        canvas.drawRect(rect.right - corLength, rect.top, rect.right , rect.top + corWidth, reactPaint);
        // 左下角
        canvas.drawRect(rect.left, rect.bottom - corLength, rect.left + corWidth, rect.bottom, reactPaint);
        canvas.drawRect(rect.left, rect.bottom - corWidth, rect.left + corLength, rect.bottom, reactPaint);
        // 右下角
        canvas.drawRect(rect.right - corWidth, rect.bottom - corLength, rect.right, rect.bottom, reactPaint);
        canvas.drawRect(rect.right - corLength, rect.bottom - corWidth, rect.right, rect.bottom, reactPaint);
    }

    /**
     * 绘制移动扫描线
     *
     * @param canvas
     * @param rect
     */
    private void drawScanLight(Canvas canvas, Rect rect) {
        canvas.drawBitmap(scanLineBitmap, rect.left + dp2px(20), scanLineTop, scanLinePaint);
    }

    /**
     * 绘制提示文字
     *
     * @param canvas
     * @param textRect
     * @param rect
     * @param width
     */
    private void drawHintText(Canvas canvas, Rect textRect, Rect rect, int width) {
        wordsPaint.getTextBounds(hintText, 0, hintText.length(), textRect);
        int wordWidth = textRect.width();
        canvas.drawText(hintText, width / 2f - wordWidth / 2f, rect.bottom + dp2px(60), wordsPaint);
    }

    public void drawViewfinder() {
        Bitmap resultBitmap = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap != null) {
            resultBitmap.recycle();
        }
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live
     * scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        List<ResultPoint> points = possibleResultPoints;
        synchronized (points) {
            points.add(point);
            int size = points.size();
            if (size > MAX_RESULT_POINTS) {
                // trim it
                points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (scanLineBitmap != null || !scanLineBitmap.isRecycled()) {
//            scanLineBitmap.recycle();
            scanLineBitmap = null;
            System.gc();
        }
    }

    private int dp2px(int dp) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param fontScale（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public float sp2px(float spValue, float fontScale) {
        return (spValue * fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public float sp2px(float spValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return sp2px(spValue, scale);
    }
}
