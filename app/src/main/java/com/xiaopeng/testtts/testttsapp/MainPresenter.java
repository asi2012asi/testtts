package com.xiaopeng.testtts.testttsapp;

import android.os.Handler;
import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.xiaopeng.speech.protocol.SpeechUtils;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by luyaoming on 2018/10/27.
 */

public class MainPresenter implements IMainPresenter {
    private static final String TAG = "MainPresenter";
    private IMainView mMainView;
    private NVWebSocketClient mNvWebSocketClient;
//    private final String URI_CONNECT_TO_SERVER = "ws://10.8.1.129:8080/websocket";
    private final String URI_CONNECT_TO_SERVER = "ws://10.8.1.36:8080/websocket";
    private final String URI_GET_RESULT = "http://10.8.0.31/api/match/";
    private final String KEY_APP = "c676a82dd86e11e89b8d54e1ad8f142a";
    private Handler mUiHandler;

    public MainPresenter(Handler uiHandler) {
        mUiHandler = uiHandler;
    }


    @Override
    public void startConnect2Server() {
        Log.d(TAG,"startConnect2Server mainView = "+mMainView);
        if(mNvWebSocketClient!=null){
            Log.d(TAG,"startConnect2Server not null");
            return;
        }
//        SpeechUtils.speak("ready go go go");
//        SpeechUtils.speak("开始啦");

        mNvWebSocketClient = new NVWebSocketClient(URI_CONNECT_TO_SERVER);
        mNvWebSocketClient.connect();
        mNvWebSocketClient.setWebSocketListener(new NVWebSocketClient.WebSocketListener() {
            @Override
            public void onConnected(com.neovisionaries.ws.client.WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                Log.d(TAG, "onConnected callback");
                if(mMainView!=null){
                    mMainView.updateStatusView("onConnected callback");
                }
            }

            @Override
            public void onTextMessage(com.neovisionaries.ws.client.WebSocket websocket, String text) throws Exception {
                Log.d(TAG, "onTextMessage callback text = "+text);
                if(mMainView!=null){
                    mMainView.showChengyu(text);

                    try {
                       parseSpeakText(text);
                    }catch (Exception e){
                        Log.e(TAG,"onTextMessage error = "+e);
                    }
                }
            }
        });
    }

    private void getResultRequest(String matchId){
        String url = URI_GET_RESULT+matchId+"?appKey="+KEY_APP;
        Log.d(TAG,"getResultRequest url = "+url);
        AsyncHttpClient.getDefaultInstance().executeString(new AsyncHttpGet(url), new AsyncHttpClient.StringCallback() {
            // Callback is invoked with any exceptions/errors, and the result, if available.
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse response, String result) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                System.out.println("I got a string: " + result);
            }
        });
    }

    private void parseSpeakText(String text){
        Log.d(TAG,"parseSpeakText text = "+text);
        if("START".equals(text)){
//            SpeechUtils.speak("开始啦，we are nobody");
            if(mMainView!=null){
                mMainView.showWaitView();
            }
        }else if(text.contains("GAMEOVER")){
//            SpeechUtils.speak("结束啦 sure win");
            //// TODO: 2018/10/27 访问裁判机获取结果
            if(mMainView!=null){
                mMainView.showEndView(true);
            }
            String[] split = text.split(":");
            if(split!=null&&split.length==2){
                getResultRequest(split[1]);
            }
        }else{
            String[] split = text.split(":");
            if(split.length==2){
                SpeechUtils.speak(split[1]);
                Log.d(TAG,"speak = "+split[1]);
                if(mMainView!=null){
                    mMainView.showSpeakingView(split[1]);
                    if(mUiHandler!=null){
                        mUiHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mMainView.showWaitView();
                            }
                        },2000);
                    }
                }
            }
        }
    }

    @Override
    public void stopConnect2Server() {
        if(mNvWebSocketClient!=null){
            mNvWebSocketClient.disconnect();
            mNvWebSocketClient = null;
        }
    }


    @Override
    public void attachView(IMainView view) {
        mMainView = view;
    }

    @Override
    public void detachView() {
        mMainView = null;
        stopConnect2Server();
    }

    @Override
    public void testShowStatus(String text) {
        if(mMainView!=null){
            mMainView.updateTestView(text);
        }
    }
}
