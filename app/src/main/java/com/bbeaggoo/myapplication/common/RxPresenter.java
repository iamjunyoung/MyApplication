package com.bbeaggoo.myapplication.common;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by junyoung on 2017. 10. 2..
 */

public class RxPresenter {
    private CompositeDisposable disposables;

    protected RxPresenter() {
        disposables = new CompositeDisposable();
    }

    protected void dispose() {
        if (null != disposables && !disposables.isDisposed()) {
            disposables.dispose();
            disposables = null;
        }
    }

    public void add(Disposable disposable) {
        disposables.add(disposable);
    }
}
