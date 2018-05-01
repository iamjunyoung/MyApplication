package com.bbeaggoo.myapplication.ui.filemanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bbeaggoo.myapplication.R;
import com.bbeaggoo.myapplication.adapter.ListRecyclerViewAdapter;
import com.bbeaggoo.myapplication.common.BaseActivity;
import com.bbeaggoo.myapplication.datas.ItemObjects;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileManagerActivity extends BaseActivity implements FileManagerMvpView {
    private View emptyView;
    RecyclerView recyclerView;
    private ListRecyclerViewAdapter listViewAdapter;
    private LinearLayoutManager layoutManager;// For MVP
    public FileManagerMvpPresenter<FileManagerActivity> presenter;


    private String root= Environment.getExternalStorageDirectory().getAbsolutePath();    // 최상위 폴더
    private String curPath=Environment.getExternalStorageDirectory().getAbsolutePath();  // 현재 탐색하는 폴더
    // itemFiles는 화면에 display되는 파일이나 폴더 이름이 되고,
    // pathFiles list는 화면에 display되는 list의 경로와 이름이 붙어있는 목록.
    private ArrayList<String> itemFiles = new ArrayList<String>();
    private ArrayList<String> pathFiles = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listViewAdapter = new ListRecyclerViewAdapter(this);

        //recyclerView.setAdapter(rcAdapter);

        presenter.setImageAdapterModel(listViewAdapter);
        presenter.setImageAdapterView(listViewAdapter);
        presenter.loadItemList(false);
    }

    @Override
    public void inject() {

    }

    //Q. initPresenter()는 누가 호출해주냐?
    // BaseActivity의 onCreate() 에서 호출해준다.
    @Override
    public void initPresenter(BaseActivity baseActivity) {
        // 1. Activity/Fragment/View에서 필요한 Presenter을 직접 생성
        // 2. setView을 전달한다
        presenter = new FileManagerMvpPresenterImpl<>(); //realm 작업을 해줘야 할듯..
        presenter.attachView(this);
        //attachView()가 setView()인듯.

    }

    /////////////////////////////////////////////////////////////////
    @Override
    public void onUpdateItemList(List<ItemObjects> itemList) {
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void onCreatedItem(ItemObjects itemObjects) {

    }

    @Override
    public void showEmtpyView() {

    }
    ///////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////
    public class AdapterAsyncTask extends AsyncTask<String, Void, String> {
        private ProgressDialog mDlg;
        Context mContext;

        public AdapterAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDlg = new ProgressDialog(mContext);
            mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDlg.setMessage( "loading" );
            mDlg.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            getDirInfo(curPath);

            //현재 getDirInfo()한 애의 정보를 load
            for(int i=0;i<itemFiles.size();i++){
                ItemObjects item = new ItemObjects();
                item.checked = false;
                item.name = itemFiles.get(i);
                item.path = pathFiles.get(i);
                listAllItems.add(item);
            }
            presenter.loadItemList(curPath, true); // -> 이거 메서드 명을 바꿔야 할듯? loadCurrentDirFileInfo ?? 이런 식으로??

            if (listAllItems != null) {
                Collections.sort(listAllItems, nameComparator);
            }
            listDispItems.addAll(listAllItems);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mDlg.dismiss();
            listadapter=new ListRecyclerViewAdapter(mContext);
            listView.setAdapter(listadapter);              // listView -> recyclerView

            String searchText = editView.getText().toString();
            if( listadapter!=null ) listadapter.fillter(searchText);

            textView.setText("Location: " + curPath);
        }
        private final Comparator<ItemObjects> nameComparator
                = new Comparator<ItemObjects>() {
            public final int
            compare(ItemObjects a, ItemObjects b) {
                return collator.compare(a.name, b.name);
            }
            private final Collator collator = Collator.getInstance();
        };
    }

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
}
