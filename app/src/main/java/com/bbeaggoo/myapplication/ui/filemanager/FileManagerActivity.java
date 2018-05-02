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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

    EditText editView;
    TextView textView;

    //private String root= Environment.getExternalStorageDirectory().getAbsolutePath();    // 최상위 폴더
    private String curPath=Environment.getExternalStorageDirectory().getAbsolutePath();  // 현재 탐색하는 폴더


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        layoutManager = new ItemLayoutManger(this);
        recyclerView.setLayoutManager(layoutManager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listViewAdapter = new ListRecyclerViewAdapter(this);
        recyclerView.setAdapter(listViewAdapter);

        presenter.setImageAdapterModel(listViewAdapter);
        presenter.setImageAdapterView(listViewAdapter);
        presenter.loadItemList(curPath, false);
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
        protected String doInBackground(String... params) { // viewholder에서 onClick을 받을 때, curPath를 execute(curPath) 형태로 넘겨줄 수 있도록.
            String curPath = params[0];
            presenter.loadItemList(curPath, true); // -> 이거 메서드 명을 바꿔야 할듯? loadCurrentPathFileInfo ?? 이런 식으로??

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mDlg.dismiss();
            listViewAdapter = new ListRecyclerViewAdapter(mContext);
            layoutManager = new ItemLayoutManger(mContext);
            recyclerView.setAdapter(listViewAdapter);

            String searchText = editView.getText().toString();
            if( listViewAdapter!=null ) listViewAdapter.fillter(searchText);

            textView.setText("Location: " + curPath);
        }

    }
}
