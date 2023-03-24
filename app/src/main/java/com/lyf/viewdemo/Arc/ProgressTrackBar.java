package com.lyf.viewdemo.Arc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.lyf.viewdemo.R;

public class ProgressTrackBar extends View {


    private static final int DEFAULT_FIRST_COLOR = Color.WHITE;
    private static final int DEFAULT_SECOND_COLOR = Color.parseColor("#FFA12F");

    private static final int PROGRESS_WIDTH = 6;
    private static final float MAX_PROGRESS = 360F;
    private static final int DEFAULT_SPEED = 1;

    private Paint mPaint;
    private float startAngle = 0;
    private int firstLayerColor = DEFAULT_FIRST_COLOR;
    private int secondLayerColor = DEFAULT_SECOND_COLOR;
    private final RectF oval = new RectF(); // 圆形轨迹
    private float maxProgress = MAX_PROGRESS; // 最大进度:ms
    private float currentProgress = 0F; // 当前进度:ms
    private int speed = DEFAULT_SPEED; // 速度(多长时间更新一次UI):ms
    private int progressWidth = PROGRESS_WIDTH; // 进度条宽度

    private OnProgressFinished onProgressFinished;

    private Handler taskHandler;
    private OnProgress runnable; //进度回调

    // 顶层颜色是否是透明
    private boolean isSecondColorTransparent = false;

    public ProgressTrackBar(Context context) {
        super(context);
        init();
    }

    public ProgressTrackBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressTrackBar);
        firstLayerColor = typedArray.getColor(R.styleable.ProgressTrackBar_p_first_color, DEFAULT_FIRST_COLOR);
        secondLayerColor = typedArray.getColor(R.styleable.ProgressTrackBar_p_second_color, DEFAULT_SECOND_COLOR);
        startAngle = typedArray.getFloat(R.styleable.ProgressTrackBar_p_start, 0F);
        progressWidth = typedArray.getDimensionPixelSize(R.styleable.ProgressTrackBar_p_width, PROGRESS_WIDTH);
        maxProgress = typedArray.getDimension(R.styleable.ProgressTrackBar_p_max_progress, MAX_PROGRESS);
        typedArray.recycle();
        init();
    }

    public ProgressTrackBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        refresh();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(progressWidth);
    }

    public void setFirstLayerColor(int firstLayerColor) {
        this.firstLayerColor = firstLayerColor;
    }

    public void setSecondLayerColor(int secondLayerColor) {
        this.secondLayerColor = secondLayerColor;
        refresh();
    }

    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    public void setOnProgressListener(OnProgress runnable) {
        this.runnable = runnable;
    }

    public void setOnProgressFinished(OnProgressFinished onProgressFinished) {
        this.onProgressFinished = onProgressFinished;
    }

    private void initTask() {
        taskHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (currentProgress < maxProgress) {
                    currentProgress += speed;
                    postInvalidate();
                    if (runnable != null) {
                        runnable.onProgress(currentProgress);
                    }
                    taskHandler.sendEmptyMessageDelayed(0, speed);
                } else {
                    stopTask();
                }
            }
        };
    }

    private void refresh() {
        isSecondColorTransparent = (secondLayerColor == Color.parseColor("#00000000"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLUE);

        Paint p1 = new Paint();
        p1.setColor(Color.YELLOW);
        p1.setStrokeWidth(16);

        //控件宽高/2，取最小的 【减去】 progressWidth 作为半径
        int x = getWidth() >> 1;
        int y = getHeight() >> 1;
        canvas.drawPoint(x, y, p1);

        int center = Math.min(x, y);
        int radius = center - progressWidth;

        //画在控件中间，减去半径，正一个圆在空间中间显示
        p1.setColor(Color.RED);

        int left = x - radius;
        int top = y - radius;
        canvas.drawPoint(left, top, p1);

        int right = x + radius;
        int bottom = y + radius;
        canvas.drawPoint(right, bottom, p1);

        oval.set(left, top, right, bottom);

        // 这里需要处理一下上层是透明的情况
        if (isSecondColorTransparent) {
            // 用下层颜色 绘制 剩下的弧度
            mPaint.setColor(firstLayerColor);
            float leaveAngle = ((maxProgress - currentProgress) / maxProgress) * 360;
            //起始位置和扫过角度
            canvas.drawArc(oval, startAngle, leaveAngle, false, mPaint);
        } else {
            // 绘制下层
            mPaint.setColor(firstLayerColor);
            canvas.drawCircle(x, y, radius, mPaint);

            // 绘制上层
            mPaint.setColor(secondLayerColor);
            float sweepAngle = (currentProgress / maxProgress) * 360;
            canvas.drawArc(oval, startAngle, sweepAngle, false, mPaint);
        }
    }

    public void startTask(int progress) {
        currentProgress = progress;
        initTask();
        taskHandler.sendEmptyMessage(0);
    }

    public void startTask(int progress, OnProgressFinished onProgressFinished) {
        this.onProgressFinished = onProgressFinished;
        currentProgress = progress;
        initTask();
        taskHandler.sendEmptyMessage(0);
    }

    public void stopTask() {
        if (onProgressFinished != null) {
            onProgressFinished.onFinished();
        }
        if (taskHandler != null) {
            taskHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTask();
    }

    public interface OnProgressFinished {
        void onFinished();
    }

    public interface OnProgress {
        void onProgress(float progress);
    }
}