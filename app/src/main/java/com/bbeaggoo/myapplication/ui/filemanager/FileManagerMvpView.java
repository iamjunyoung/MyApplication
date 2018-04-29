package com.bbeaggoo.myapplication.ui.filemanager;

import com.bbeaggoo.myapplication.common.BaseMvpView;
import com.bbeaggoo.myapplication.datas.ItemObjects;

import java.util.List;

/**
 * Created by junyoung on 2018. 4. 26..
 */

interface FileManagerMvpView extends BaseMvpView {
    // MainActivity 가 implements할 method들
    // 이 method들은 MainMvpPresenterImpl 이 호출한다.

    void onUpdateItemList(List<ItemObjects> itemList);

    void onCreatedItem(ItemObjects itemObjects);

    void showEmtpyView();

}
