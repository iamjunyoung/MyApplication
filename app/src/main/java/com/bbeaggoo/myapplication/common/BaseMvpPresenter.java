package com.bbeaggoo.myapplication.common;

/**
 * Created by junyoung on 2018. 04. 26..
 */

public interface BaseMvpPresenter<T extends BaseMvpView> {

    void attachView(T view);

    void destroy();

    //BasePresenter는 다음과 같이 항상 start() 함수를 가지고 있습니다.
    //사실 BasePresenter에는 딱히 정의할게 많지는 않은듯합니다.
    //http://thdev.tech/androiddev/2016/06/14/Android-TODO-MVP-Example.html
    //위 블로그에는 저렇게 설명이 되어있는데
    //아직 이해가 되지 않는다.


}
