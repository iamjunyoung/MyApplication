package com.bbeaggoo.myapplication.ui.filemanager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bbeaggoo.myapplication.R;
import com.bbeaggoo.myapplication.adapter.ListRecyclerViewAdapter;
import com.bbeaggoo.myapplication.common.BaseActivity;
import com.bbeaggoo.myapplication.datas.ItemObjects;

import java.util.List;

public class FileManagerActivity extends BaseActivity implements FileManagerMvpView {
    private View emptyView;
    RecyclerView recyclerView;
    private ListRecyclerViewAdapter listViewAdapter;
    private LinearLayoutManager layoutManager;// For MVP
    public FileManagerMvpPresenter<FileManagerActivity> presenter;



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

        presenter.setImageAdapterModel(listViewAdapter);
        presenter.setImageAdapterView(listViewAdapter);
        presenter.loadItemList();
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

    public class AdapterAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }
}
