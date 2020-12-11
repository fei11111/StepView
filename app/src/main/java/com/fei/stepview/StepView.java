package com.fei.stepview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @ClassName: StepView
 * @Description: 步数View
 * @Author: Fei
 * @CreateDate: 2020/12/10 19:24
 * @UpdateUser: Fei
 * @UpdateDate: 2020/12/10 19:24
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class StepView extends View {

    private int mOriginalColor = Color.BLUE;//初始颜色
    private int mRunningColor = Color.RED;//滚动时颜色
    private float mStrokeWidth = 20f;//圆弧边框
    private int mFontSize = 20;//字体大小
    private int mFontColor = Color.RED;//字体颜色

    private Paint mOriginalPaint;//外弧画笔
    private Paint mRunningPaint;//内弧画笔
    private Paint mFontPaint;//字体画笔

    private int defaultWidth = 200;//默认宽
    private int defaultHeight = 200;//默认高

    private int mBeginDegree = 135;//开始角度
    private int mEndDegree = 270;//结束角度

    private int mMaxProgress = 1000;//最大值进度
    private int mCurrentProgress = 430;//当前进度

    private RectF mRect;//圆弧区域
    private int mCenterX;//中心位置
    private int mCenterY;//中心位置
    private int mRadius;//半径
    private Rect bounds = new Rect();//字体区域

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取attrs值
        initAttrs(context, attrs);
        //初始化画笔
        initPaint();
    }

    /**
     * 初始化区域
     */
    private void initRect(int width, int height) {
        mCenterX = width / 2;
        mCenterY = height / 2;
        float halfStrokeWidth = mStrokeWidth / 2;
        float left = halfStrokeWidth;
        float top = halfStrokeWidth;
        float right = width * 1.0f - halfStrokeWidth;
        float bottom = height * 1.0f - halfStrokeWidth;
        mRect = new RectF(left, top, right, bottom);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mOriginalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOriginalPaint.setStrokeWidth(mStrokeWidth);
        mOriginalPaint.setColor(mOriginalColor);
        mOriginalPaint.setStyle(Paint.Style.STROKE);
        mOriginalPaint.setStrokeCap(Paint.Cap.ROUND);

        mRunningPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRunningPaint.setStrokeWidth(mStrokeWidth);
        mRunningPaint.setColor(mRunningColor);
        mRunningPaint.setStyle(Paint.Style.STROKE);
        mRunningPaint.setStrokeCap(Paint.Cap.ROUND);

        mFontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFontPaint.setColor(mFontColor);
        mFontPaint.setTextSize(mFontSize);
    }

    /**
     * 获取属性值
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StepView);
        mOriginalColor = typedArray.getColor(R.styleable.StepView_originalColor, mOriginalColor);
        mRunningColor = typedArray.getColor(R.styleable.StepView_runningColor, mRunningColor);
        mFontColor = typedArray.getColor(R.styleable.StepView_fontColor, mFontColor);
        mStrokeWidth = typedArray.getDimension(R.styleable.StepView_strokeWidth, mStrokeWidth);
        mFontSize = typedArray.getDimensionPixelSize(R.styleable.StepView_fontSize, mFontSize);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //当它为AT_MOST是用默认宽高

        int withMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = 0;
        int height = 0;
        if (withMode == MeasureSpec.AT_MOST) {
            width = defaultWidth;
        } else {
            width = widthSize;
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            height = defaultHeight;
        } else {
            height = heightSize;
        }

        //取宽高最小值，需要做成正方形
        width = Math.min(width, height);
        height = width;

        //暂时没考虑padding
        setMeasuredDimension(width, height);

        //初始化区域
        initRect(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画外弧
        canvas.drawArc(mRect, mBeginDegree, mEndDegree, false, mOriginalPaint);
        //画内弧
        if (mCurrentProgress != 0) {
            int currentDegree = (int) ((mCurrentProgress * 1.0) / mMaxProgress * mEndDegree);
            canvas.drawArc(mRect, mBeginDegree, currentDegree, false, mRunningPaint);
        }
        //画字体
        String fontStr = mCurrentProgress + "";
        mFontPaint.getTextBounds(fontStr, 0, fontStr.length(), bounds);
        int width = bounds.width();
        int x = mCenterX - width / 2;
        Paint.FontMetricsInt fontMetricsInt = mFontPaint.getFontMetricsInt();
        //bottom是正值，top是负值，获取字体中间到baseline距离
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int y = mCenterY + dy;//获取基线
        canvas.drawText(fontStr, x, y, mFontPaint);
    }

    public void setMaxProgress(int mMaxProgress) {
        this.mMaxProgress = mMaxProgress;
    }

    public void setCurrentProgress(int mCurrentProgress) {
        this.mCurrentProgress = mCurrentProgress;
        invalidate();
    }
}
