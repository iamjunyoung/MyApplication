package com.bbeaggoo.myapplication.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by junyoung on 2018. 04. 26..
 */
//공통적으로 쓸만한 애들은 Base로 빼놨다.
public abstract class BaseActivity extends AppCompatActivity implements BaseMvpView {
    //protected ActivityComponent component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        Dagger관련
        component = DaggerActivityComponent
                .builder()
                .applicationComponent(getApplicationComponet())
                .activityModule(new ActivityModule(this))
                .build();

        inject();
        */

        initPresenter(this);
    }

}
