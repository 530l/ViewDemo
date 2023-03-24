package com.lyf.viewdemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.lyf.viewdemo.Arc.ProgressTrackBar
import com.lyf.viewdemo.voiceAnim.VoiceAnimView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val trackBar = findViewById<ProgressTrackBar>(R.id.progress_track_bar)
        trackBar.setStartAngle(-90F) // 从-90度开始读条
        trackBar.setOnProgressListener { // 进度回调
            Log.d("ProgressTrackBar", "progress is $it")
        }
        trackBar.startTask(50) { // 开始计时，传入读条结束的回调
            Log.d("ProgressTrackBar", "progress run finish")
        }

        // 从0开始计时
        findViewById<ProgressTrackBar>(R.id.progress_track_bar2).startTask(60)

        // 从20开始计时
        findViewById<ProgressTrackBar>(R.id.progress_track_bar3).startTask(80)
    }
}