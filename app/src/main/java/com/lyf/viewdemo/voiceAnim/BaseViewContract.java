package com.lyf.viewdemo.voiceAnim;

import android.view.View;


public interface BaseViewContract {
    void playAnim();
    void pauseAnim();
    void stopAnim();
    void setFactor(float factor);
    float getFactor();
    void release();

    abstract class AgentCallback {
        public void playAnim(View v) {}
        public void pauseAnim(View v) {}
        public void stopAnim(View v) {}
        public void setFactor(View v, float factor) {}
    }
}
