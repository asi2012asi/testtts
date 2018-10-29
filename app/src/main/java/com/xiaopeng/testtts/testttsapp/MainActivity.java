package com.xiaopeng.testtts.testttsapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IMainView {

    private static final String TAG = "MainActivity";
    private MainPresenter mMainPresenter;
    private TextView mTvStart;
    private TextView mTvStatus;
    private TextView mTvTest;
    private TextView mTvOver;
    private TextView mTvChengyu;
    private WebView mWebView;
    private Button mBtnReload;
    private Handler mUIHandler;
    private Button mBtnHide;
    private static final String JS_NOMAL = "javascript:setStatusToBoring()";
    private static final String JS_WAITTING = "javascript:setStatusToWaiting()";
    private static final String JS_SPEAK = "javascript:setStatusToSpeak({content})";
    private static final String JS_WIN = "javascript:setStatusToSuccess()";
    private static final String JS_LOSE = "javascript:setStatusToFail()";
    private Button mBtnSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUIHandler = new Handler();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMainPresenter != null) {
            mMainPresenter.detachView();
        }
    }

    private void initPresenter() {
        mMainPresenter = new MainPresenter(mUIHandler);
        mMainPresenter.attachView(MainActivity.this);
    }

    private void initView() {
        mTvStart = (TextView) findViewById(R.id.tv_start);
        mTvOver = (TextView) findViewById(R.id.tv_over);
        mTvStatus = (TextView) findViewById(R.id.tv_status);
        mTvTest = (TextView) findViewById(R.id.tv_test);
        mTvChengyu = (TextView) findViewById(R.id.tv_chengyu);
        mBtnReload = (Button) findViewById(R.id.btn_reload);
        mBtnHide = (Button) findViewById(R.id.btn_hide);
        mBtnSpeak = (Button) findViewById(R.id.btn_speak);
        mTvStart.setOnClickListener(this);
        mTvTest.setOnClickListener(this);
        mTvOver.setOnClickListener(this);
        mBtnReload.setOnClickListener(this);
        mBtnHide.setOnClickListener(this);
        mBtnSpeak.setOnClickListener(this);

        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings wSet = mWebView.getSettings();
        wSet.setJavaScriptEnabled(true);

        startConnect2Server();
    }


    private void startConnect2Server(){
        initPresenter();
        mMainPresenter.startConnect2Server();
        showWebView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_test:
//                SpeechUtils.speak("大家好，我们是nobody战队，偶耶！");

                break;
            case R.id.tv_start:
                startConnect2Server();

                break;
            case R.id.tv_over:
                mMainPresenter.stopConnect2Server();
                break;
            case R.id.btn_reload:
                if (mWebView != null) {
                    mWebView.reload();
                    Log.d(TAG, "webview reload");
                }
                break;
            case R.id.btn_speak:
                showSpeakingView("一路顺风");
                break;
            case R.id.btn_hide:
                hideWebView();
                break;


            default:
                break;
        }
    }

    private void hideWebView() {
        if (mWebView != null) {
            mWebView.setVisibility(View.GONE);
            mBtnHide.setVisibility(View.GONE);
            mBtnReload.setVisibility(View.GONE);
        }
    }

    private void showWebView() {
        mWebView.setVisibility(View.VISIBLE);
        mBtnHide.setVisibility(View.VISIBLE);
        mBtnReload.setVisibility(View.VISIBLE);
//        mWebView.loadUrl("file:///android_asset/html/index.html");
        mWebView.loadUrl("http://10.192.144.28:3000");
        mUIHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showJSAnim("javascript:setStatusToNormal()", null);
            }
        }, 2000);

    }

    private void showJSAnim(String jsString, String speaktext) {
        if (mWebView == null || TextUtils.isEmpty(jsString)) {
            return;
        }
        final String newSpeaktext;
        if (!TextUtils.isEmpty(speaktext) && JS_SPEAK.equals(jsString)) {
//            newSpeaktext = speaktext.replace("{content}", "'"+speaktext+"'");
            newSpeaktext = "javascript:setStatusToSpeak('"+speaktext+"')";
        }else{
            newSpeaktext = jsString;
        }
        Log.d(TAG,"showJSAnim jsString = "+newSpeaktext);

        mUIHandler.post(new Runnable() {
            @Override
            public void run() {
                mWebView.evaluateJavascript(newSpeaktext, null);
            }
        });

    }

    @Override
    public void showStartView() {
        Log.d(TAG, "showStartView");
        showJSAnim(JS_NOMAL,null);
    }

    @Override
    public void showEndView(boolean isWin) {
        Log.d(TAG, "showEndView isWin = "+isWin);
        if (isWin) {
            showJSAnim(JS_WIN,null);
        } else {
            showJSAnim(JS_LOSE,null);
        }
    }

    @Override
    public void showWaitView() {
        Log.d(TAG, "showWaitView");
        showJSAnim(JS_WAITTING,null);
    }

    @Override
    public void showSpeakingView(String text) {
        Log.d(TAG, "showSpeakingView text = " + text);
        showJSAnim(JS_SPEAK,text);

    }

    @Override
    public void updateStatusView(String text) {
        Log.d(TAG, "updateStatusView text = " + text);
//        mTvStatus.setText(text.toString());
    }

    @Override
    public void showChengyu(String chengyu) {
        Log.d(TAG, "showChengyu text = " + chengyu);
//        mTvChengyu.setText(chengyu.toString());
    }

    @Override
    public void updateTestView(String text) {
        Log.d(TAG, "updateTestView text = " + text);
//        mTvStatus.setText(text.toString());
    }
}
