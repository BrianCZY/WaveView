package com.example.brian.waveview.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.brian.waveview.R;

import java.util.ArrayList;
import java.util.List;

public class WaveView extends TextView {

    Context context = null;
    /**
     * 扩散圆圈颜色
     */
    private int mColor = getResources().getColor(R.color.colorAccent);
    /**
     * 圆圈中心颜色
     */
    private int mCoreColor = getResources().getColor(R.color.colorPrimary);
    /**
     * 圆圈中心图片
     */
    private Bitmap mBitmap;
    /**
     * 中心圆半径
     */
    private float mCoreRadius = 150;
    /**
     * 扩散圆宽度
     */
    private int mDiffuseWidth = 3;
    /**
     * 最大宽度
     */
    private Integer mMaxWidth = 255;
    /**
     * 是否正在扩散中
     */
    private boolean mIsDiffuse = false;
    // 透明度集合
    private List<Integer> mAlphas = new ArrayList<>();
    // 扩散圆半径集合
    private List<Integer> mWidths = new ArrayList<>();
    private Paint mPaint;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DiffuseView, defStyleAttr, 0);
        mColor = a.getColor(R.styleable.DiffuseView_wave_color, mColor);
        mCoreColor = a.getColor(R.styleable.DiffuseView_wave_coreColor, mCoreColor);
        mCoreRadius = a.getFloat(R.styleable.DiffuseView_wave_coreRadius, mCoreRadius);
        mDiffuseWidth = a.getInt(R.styleable.DiffuseView_wave_width, mDiffuseWidth);
        mMaxWidth = a.getInt(R.styleable.DiffuseView_wave_maxWidth, mMaxWidth);
        int imageId = a.getResourceId(R.styleable.DiffuseView_wave_coreImage, -1);
        if (imageId != -1) mBitmap = BitmapFactory.decodeResource(getResources(), imageId);
        a.recycle();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mAlphas.add(255);
        mWidths.add(0);
    }

    @Override
    public void invalidate() {
        if (hasWindowFocus()) {
            super.invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        // 绘制扩散圆
        mPaint.setColor(mColor);
        for (int i = 0; i < mAlphas.size(); i++) {
            // 设置透明度
            Integer alpha = mAlphas.get(i);
            mPaint.setAlpha(alpha);
            // 绘制扩散圆
            Integer width = mWidths.get(i);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mCoreRadius + width, mPaint);

            if (alpha > 0 && width < mMaxWidth) {
                mAlphas.set(i, alpha - 1);
                mWidths.set(i, width + 1);
            }
        }
        // 判断当扩散圆扩散到指定宽度时添加新扩散圆
        if (mWidths.get(mWidths.size() - 1) == mMaxWidth / mDiffuseWidth) {
            mAlphas.add(255);
            mWidths.add(0);
        }
        // 超过10个扩散圆，删除最外层
        if (mWidths.size() >= 5) {
            mWidths.remove(0);
            mAlphas.remove(0);
        }

        // 绘制中心圆及图片
        mPaint.setAlpha(255);
        mPaint.setColor(mCoreColor);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mCoreRadius, mPaint);

        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, getWidth() / 2 - mBitmap.getWidth() / 2
                    , getHeight() / 2 - mBitmap.getHeight() / 2, mPaint);
        }
        mPaint.setAlpha(255);
        mPaint.setColor(000);
//        canvas.drawText("领取任务",getWidth() / 2, getHeight() / 2,mPaint);


//// 方法1
        String string = getText().toString();
        Paint mPaint = new Paint();
        mPaint.setStrokeWidth(3);
        mPaint.setTextSize(getTextSize());
        mPaint.setColor(getTextColors().getDefaultColor());
        mPaint.setTextAlign(Paint.Align.LEFT);
        Rect bounds = new Rect();
        mPaint.getTextBounds(string, 0, string.length(), bounds);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(string, getMeasuredWidth() / 2 - bounds.width() / 2, baseline, mPaint);
////方法2
////        TextPaint textPaint = new TextPaint();
////        textPaint.setTextSize(getTextSize());
////        textPaint.setColor(getTextColors().getDefaultColor());
////        String string = getText().toString();
////        StaticLayout sl = new StaticLayout(string,textPaint,getWidth()/2, Layout.Alignment.ALIGN_NORMAL,1.0f,0.0f,true);
//////从0,0开始绘制
////        canvas.translate(0,0);
////        sl.draw(canvas);
////        canvas.restore();
////方法3
//        TextPaint textPaint = new TextPaint();
//        textPaint.setTextSize(getTextSize());
//        textPaint.setColor(getTextColors().getDefaultColor());
//        textPaint.setAntiAlias(true);
//        StaticLayout layout = new StaticLayout(string, textPaint, 300,Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
//        // 这里的参数300，表示字符串的长度，当满300时，就会换行，也可以使用“\r\n”来实现换行
//        canvas.save();
//        canvas.translate(getMeasuredWidth() / 2 - bounds.width() / 2,baseline);//从100，100开始画
//        layout.draw(canvas);
//        canvas.restore();//别忘了restore
        if (mIsDiffuse) {
            invalidate();
        }
    }

    /**
     * 开始扩散
     */
    public void start() {
        mIsDiffuse = true;
        invalidate();
    }

    /**
     * 停止扩散
     */
    public void stop() {
        mIsDiffuse = false;
        mAlphas.clear();
        mWidths.clear();
        init();
        invalidate();
    }

    /**
     * 暂停
     */
    public void pause() {
        mIsDiffuse = false;
    }

    /**
     * 是否扩散中
     */
    public boolean isDiffuse() {
        return mIsDiffuse;
    }

    /**
     * 设置扩散圆颜色
     */
    public void setColor(int colorId) {
        mColor = colorId;
    }

    /**
     * 设置中心圆颜色
     */
    public void setCoreColor(int colorId) {
        mCoreColor = colorId;
    }

    /**
     * 设置中心圆图片
     */
    public void setCoreImage(int imageId) {
        mBitmap = BitmapFactory.decodeResource(getResources(), imageId);
    }

    /**
     * 设置中心圆半径
     */
    public void setCoreRadius(int radius) {
        mCoreRadius = radius;
    }

    /**
     * 设置扩散圆宽度(值越小宽度越大)
     */
    public void setDiffuseWidth(int width) {
        mDiffuseWidth = width;
    }

    /**
     * 设置最大宽度
     */
    public void setMaxWidth(int maxWidth) {
        mMaxWidth = maxWidth;
    }
}