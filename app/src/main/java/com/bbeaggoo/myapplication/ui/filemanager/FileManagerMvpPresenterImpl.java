package com.bbeaggoo.myapplication.ui.filemanager;

import android.os.Environment;
import android.util.Log;

import com.bbeaggoo.myapplication.adapter.AdapterContract;
import com.bbeaggoo.myapplication.common.BaseMvpView;
import com.bbeaggoo.myapplication.common.RxPresenter;
import com.bbeaggoo.myapplication.datas.ItemObjects;
import com.bbeaggoo.myapplication.listener.OnItemClickListener;
import com.bbeaggoo.myapplication.singletons.FileItemsManager;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by junyoung on 2018. 4. 26..
 */

public class FileManagerMvpPresenterImpl <MvpView extends BaseMvpView> extends RxPresenter
        implements FileManagerMvpPresenter<MvpView>, OnItemClickListener {

    private FileManagerMvpView view; //view를 갖고있다.

    private AdapterContract.Model adapterModel;
    private AdapterContract.View adapterView;

    private String root= Environment.getExternalStorageDirectory().getAbsolutePath();
    //private String CurPath=Environment.getExternalStorageDirectory().getAbsolutePath();
    // itemFiles는 화면에 display되는 파일이나 폴더 이름이 되고,
    // pathFiles list는 화면에 display되는 list의 경로와 이름이 붙어있는 목록.
    private ArrayList<String> itemFiles = new ArrayList<String>();
    private ArrayList<String> pathFiles = new ArrayList<String>();

    @Override
    public void attachView(MvpView view) {
        this.view = (FileManagerMvpView) view;
    }

    @Override
    public void destroy() {

    }

    ////////// FilemanagerMvpPresenter //////////
    @Override
    public void loadItemList(String curPath, boolean isNeededClear) {
        getDirInfo(curPath);
        ArrayList<ItemObjects> itemList = FileItemsManager.getFileItemsData(curPath, itemFiles, pathFiles);   // 1. Model

        if (isNeededClear) {
            adapterModel.clearItem();
        }

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
            //adapter를 MVP로 수정한 현재 상황에서
            //위의 onUpdateItemList() 는 딱히 하는 일이 없는듯??
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
        this.adapterView.setOnClickListener(this);
    }
    ///////////////////////////////////////////////

    private void getDirInfo(String dirPath)
    {
        if(!dirPath.endsWith("/")) dirPath = dirPath+"/";
        File f = new File(dirPath);
        File[] files = f.listFiles();
        if( files == null ) return;

        itemFiles.clear();
        pathFiles.clear();

        if( !root.endsWith("/") ) root = root+"/";
        if( !root.equals(dirPath) ) {
            itemFiles.add("../");
            pathFiles.add(f.getParent());
        }

        for(int i=0; i < files.length; i++){
            File file = files[i];
            pathFiles.add(file.getPath());

            if(file.isDirectory())
                itemFiles.add(file.getName() + "/");
            else
                itemFiles.add(file.getName());
        }
    }

    @Override
    public void onItemClick(int position) {
        ItemObjects item = adapterModel.get(position);
        //data를 받아온 다음에 activity를 활용한다.

        if (item == null) {
            view.showToast("Clicked item is null.. return");
            return;
        }
        String path = item.path;
        File file = new File(path);
        if (file.isDirectory()) {
            if (file.canRead()) {
                //CurPath = path;
                //setupAdapter();
                loadItemList(path, true);
            } else {
                view.showToast(item.getName() + " is file");
            }
            Log.i("JYN", "[FileManagerMvpPresenterImpl][onItemClick] " + item.getName() + " is dir");
        } else {
            Log.i("JYN", "[FileManagerMvpPresenterImpl][onItemClick] " + item.getName() + " is file");
        }
        view.showToast("Clicked item = " + item.getName());
     }
}
