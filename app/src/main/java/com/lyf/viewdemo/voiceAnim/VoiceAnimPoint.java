package com.lyf.viewdemo.voiceAnim;

/**
 *  动画分解
 * 动画有两部分，
 *
 * 第一个是背景，这个直接画bitmap就可以了。
 * 第二个就是这个波浪，我们仔细观察这个波浪其实是一个有规律的，
 * 基于每一个点的原点Y轴方向的不断拉伸。 但是，每一个点，其拉伸的量不一致，而且时间也有差错。
 *
 * 根据UI提供的详细动画细节，可以知道：
 * 动效总共时间为2S，之后反复循环，每秒帧数为24帧，其中圆形元素控件大小为6px，拉伸都是以圆心为拉伸中心点进行拉伸。
 * 0-1s为从左到右的拉伸动画，
 * 1s-2s为从右到左的拉伸动画，之后为循环。
 *
 * 每一个点，都有5种不同的拉伸量，这里我们把对应的拉伸后的Y的高度命名为：
 * 其中还有一种拉伸量为0。maxHeight是最大的高度，threeHeight为次最高高度。
 */

public class VoiceAnimPoint {
    public int centerX, centerY;
    public float maxHeight;//最大高度
    public float threeHeight;//次高度
    public float halfHeight;//半高
    public float oneHeight;//一个 高度

    public VoiceAnimPoint(int centerX,
                          int centerY,
                          float maxHeight,
                          float threeHeight,
                          float halfHeight,
                          float oneHeight) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.maxHeight = maxHeight;
        this.threeHeight = threeHeight;
        this.halfHeight = halfHeight;
        this.oneHeight = oneHeight;
    }
}
