package com.bbeaggoo.myapplication.common;

/**
 * Created by junyoung on 2018. 04. 26..
 */

public interface BaseMvpView {
    void inject();

    void initPresenter(BaseActivity baseActivity);
}
