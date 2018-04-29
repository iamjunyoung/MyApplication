package com.bbeaggoo.myapplication.ui.filemanager;

import android.util.Log;

import com.bbeaggoo.myapplication.adapter.AdapterContract;
import com.bbeaggoo.myapplication.common.BaseMvpView;
import com.bbeaggoo.myapplication.common.RxPresenter;
import com.bbeaggoo.myapplication.datas.ItemObjects;
import com.bbeaggoo.myapplication.singletons.FileItemsManager;

import java.util.List;

/**
 * Created by junyoung on 2018. 4. 26..
 */

public class FileManagerMvpPresenterImpl <MvpView extends BaseMvpView> extends RxPresenter
        implements FileManagerMvpPresenter<MvpView> {

    private FileManagerMvpView view; //view를 갖고있다.

    private AdapterContract.Model adapterModel;
    private AdapterContract.View adapterView;

    @Override
    public void attachView(MvpView view) {

    }

    @Override
    public void destroy() {

    }

    ////////// FilemanagerMvpPresenter //////////
    @Override
    public void loadItemList() {
        List<ItemObjects> itemList = FileItemsManager.getFileItemsData();   // 1. Model
        if (null != itemList && itemList.isEmpty()) {
            Log.i("JYN", "[FileManagerMvpPresenterImpl][loadItemList] showEmtpyView : " + itemList);

            view.showEmtpyView();
        } else {
            // 2. AdapterModel
            Log.i("JYN", "[FileManagerMvpPresenterImpl][loadItemList] addItems : " + itemList);
            adapterModel.addItems(itemList);
            // 3. AdapterView
            adapterView.refreshItemList();
            // 4. Veiw
            view.onUpdateItemList(itemList); // --> 이후 onUpdateItemList 에서는 rcAdapter.addItems(itemList);를 해주게 됨.
        }

    }

    @Override
    public void addItem(String title) {

    }

    @Override
    public void setImageAdapterModel(AdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }

    @Override
    public void setImageAdapterView(AdapterContract.View adapterView) {
        this.adapterView = adapterView;
    }
    ///////////////////////////////////////////////
}
