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

public class FileManagerActivity extends AppCompatActivity implements FileManagerMvpView {
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

        presenter.setImageAdapterModel(listViewAdapter);
        presenter.setImageAdapterView(listViewAdapter);
        presenter.loadItemList();
    }

    @Override
    public void inject() {

    }

    @Override
    public void initPresenter(BaseActivity baseActivity) {

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
