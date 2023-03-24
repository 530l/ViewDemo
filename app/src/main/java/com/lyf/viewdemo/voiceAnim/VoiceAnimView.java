package com.lyf.viewdemo.voiceAnim;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.lyf.viewdemo.R;


public class VoiceAnimView extends View implements BaseViewContract {

    private Bitmap bgBitmap;
    private Paint paint;
    private int pointWidth = 6;
    private VoiceAnimPoint[] points;
    private int pointIndex = 0;
    private boolean isRevert = false;
    private boolean isStart = false;
    private final Runnable r = new Runnable() {
        @Override
        public void run() {
            VoiceAnimView.this.invalidate();
            //暴露开启动画，暂停动画的接口，根据动效的时间描述，
            // 我们应该每1000/24=42毫秒去重新绘制，也就是调用：invalidate()方法。
            VoiceAnimView.this.postDelayed(r, 42);
        }
    };

    public VoiceAnimView(Context context) {
        super(context);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public VoiceAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bgBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ai_chat_icon)).getBitmap();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xffC2E379);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(pointWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        points = new VoiceAnimPoint[5];

// TODO   列出几个关键帧（约定，从左到右将元素命名为元素1、元素2、元素3、元素4、元素5）
//        第1帧：初始化，每一个的点均为 6 px * 6 px
//        第2帧：元素1 高7.3px 对应 oneHeight，其他元素保持初始状态。
//        第3帧：元素1 高11px 对应 halfHeight，其他元素保持初始状态
//        第4帧：元素1 高14.7px 对应 threeHeight，元素2 高8.3px 对应 oneHeight，其他元素保持初始状态。
//        第5帧：元素1 高16px 对应 maxHeight，元素2 高15px 对应 halfHeight，其他元素保持初始状态。
//        第6帧：元素1 高14.7px 对应 threeHeight，元素2 高21.7px 对应 threeHeight，元素3 高10.1px 对应 oneHeight，其他元素保持初始状态。

        //是第几帧，x=0，第1帧时候，y=0，代表原始状态，
        // y=1，代表元素1 高7.3px 对应 oneHeight，
        // y=2，代表元素1 高11px 对应 halfHeight，
        // y=3，代表元素1 高11px 对应 threeHeight，
        // y=4，代表元素1 高16px 对应 maxHeight，，，


        points[0] = new VoiceAnimPoint(getWidth() / 2 - 24, getHeight() / 2,
                16f, 14.7f, 11f, 7.3f);

        points[1] = new VoiceAnimPoint(getWidth() / 2 - 12, getHeight() / 2,
                24f, 21.7f, 15f, 8.3f);

        points[2] = new VoiceAnimPoint(getWidth() / 2, getHeight() / 2,
                38f, 33.9f, 22f, 10.1f);

        points[3] = new VoiceAnimPoint(getWidth() / 2 + 12, getHeight() / 2,
                24f, 21.7f, 15f, 8.3f);

        points[4] = new VoiceAnimPoint(getWidth() / 2 + 24, getHeight() / 2,
                16f, 14.7f, 11f, 7.3f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(bgBitmap.getWidth(), bgBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bgBitmap, getWidth() / 2 - bgBitmap.getWidth() / 2,
                getHeight() / 2 - bgBitmap.getHeight() / 2, paint);

        for (int i = 0; i < points.length; i++) {
            VoiceAnimPoint point = points[i];
            int y = indexChangeFunc(pointIndex - i * 2);
            switch (y) {
                case 0:
                    canvas.drawLine(point.centerX, point.centerY, point.centerX, point.centerY + 0.01f, paint);
                    break;
                case 2:
                    canvas.drawLine(point.centerX, point.centerY - point.halfHeight / 2 + pointWidth / 2,
                            point.centerX, point.centerY + point.halfHeight / 2 - pointWidth / 2, paint);
                    break;
                case 4:
                    canvas.drawLine(point.centerX, point.centerY - point.maxHeight / 2 + pointWidth / 2,
                            point.centerX, point.centerY + point.maxHeight / 2 - pointWidth / 2, paint);
                    break;
                case 1:
                    canvas.drawLine(point.centerX, point.centerY - point.oneHeight / 2 + pointWidth / 2,
                            point.centerX, point.centerY + point.oneHeight / 2 - pointWidth / 2, paint);
                    break;
                case 3:
                    canvas.drawLine(point.centerX, point.centerY - point.threeHeight / 2 + pointWidth / 2,
                            point.centerX, point.centerY + point.threeHeight / 2 - pointWidth / 2, paint);
                    break;
            }
        }
        if (!isRevert) {
            pointIndex++;
        } else {
            pointIndex--;
        }
        if (pointIndex == 23) {
            isRevert = true;
            pointIndex = 17;
        } else if (pointIndex == -6) {
            pointIndex = 0;
            isRevert = false;
        }
    }

    @Override
    public void playAnim() {
        if (!isStart) {
            isStart = true;
            this.post(r);
        }
    }

    @Override
    public void pauseAnim() {
        stopAnim();
    }

    @Override
    public void stopAnim() {
        if (isStart) {
            isStart = false;
            this.removeCallbacks(r);
        }
    }

    @Override
    public void setFactor(float factor) {

    }

    @Override
    public float getFactor() {
        return 0;
    }

    @Override
    public void release() {

    }

    /**
     * 动画轨迹其实符合一个函数
     * 这里传入对应的x，返回函数的y
     *
     * @param x 位置 [0,17]，相隔2，作为其他元素的坐标
     * @return y 4 ： 最大， 3：threeHeight， 2： 一半， 1：oneHeight， 0 ：0 。
     */
    private int indexChangeFunc(int x) {
        if (x < 0)
            return 0;
        else if (x < 4)
            return x;
        else if (x < 8)
            return -x + 8;
        else
            return 0;
    }
}
