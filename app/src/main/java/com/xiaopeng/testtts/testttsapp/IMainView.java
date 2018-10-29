package com.xiaopeng.testtts.testttsapp;

/**
 * Created by luyaoming on 2018/10/27.
 */

public interface IMainView {

    void showStartView();

    void showEndView(boolean isWin);

    void showWaitView();

    void showSpeakingView(String text);

    void updateStatusView(String text);

    void showChengyu(String chengyu);

    void updateTestView(String text);

}
