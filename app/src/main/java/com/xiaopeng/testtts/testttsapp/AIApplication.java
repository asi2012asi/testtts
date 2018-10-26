package com.xiaopeng.testtts.testttsapp;

import android.app.Application;

import com.xiaopeng.speech.SpeechClient;


/**
 * Created by luyaoming on 2018/10/26.
 */

public class AIApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initSpeech();
    }

    /**
     * 初始化语音组件
     */
    private void initSpeech() {
        SpeechClient.instance().init(this);
        SpeechClient.instance().setAppName("tts");
    }
}
