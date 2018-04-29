package com.bbeaggoo.myapplication.ui.filemanager;

import com.bbeaggoo.myapplication.adapter.AdapterContract;
import com.bbeaggoo.myapplication.common.BaseMvpPresenter;
import com.bbeaggoo.myapplication.common.BaseMvpView;

/**
 * Created by junyoung on 2018. 4. 26..
 */

public interface FileManagerMvpPresenter<MvpView extends BaseMvpView> extends BaseMvpPresenter<MvpView> {
    // View -->
    // Activity(View)에서 호출할 메서드 들이라고 보면 될듯?
    // Activity(View)가 보여질 때 item들을 load해야 하며
    // 이때 Presenter의 loadItemList()를 호출한다.
    void loadItemList();

    void addItem(String title);
    // remove, modify 도 필요할듯?

    //void switchAdapter(RecyclerView.Adapter<ViewHolder> recyclerViewAdapter);

    void setImageAdapterModel(AdapterContract.Model adapterModel);

    void setImageAdapterView(AdapterContract.View adapterView);
}
