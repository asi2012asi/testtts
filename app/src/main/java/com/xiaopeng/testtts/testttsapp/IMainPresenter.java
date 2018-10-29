package com.xiaopeng.testtts.testttsapp;

/**
 * Created by luyaoming on 2018/10/27.
 */

public interface IMainPresenter {


    void startConnect2Server();

    void stopConnect2Server();


    void attachView(IMainView view);

    void detachView();

    void testShowStatus(String text);

}
