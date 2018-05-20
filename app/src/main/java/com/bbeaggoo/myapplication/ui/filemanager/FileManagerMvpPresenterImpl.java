package com.bbeaggoo.myapplication.ui.filemanager;

import android.graphics.Color;
import android.os.Environment;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import com.bbeaggoo.myapplication.adapter.AdapterContract;
import com.bbeaggoo.myapplication.common.BaseMvpView;
import com.bbeaggoo.myapplication.common.RxPresenter;
import com.bbeaggoo.myapplication.datas.ItemObjects;
import com.bbeaggoo.myapplication.listener.OnItemClickListener;
import com.bbeaggoo.myapplication.singletons.FileItemsManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

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

    ArrayList<String> list;
    @Override
    public void setPathToTextView(String curPath) { // presenter랑 view랑 메서드 명이 이렇게 같으면 안될거같어
        //list = new ArrayList<>();
        Log.i("JYN", "cur " + curPath);

        list = new ArrayList<String>(Arrays.asList(curPath.split("\\/")));
        list.remove(0);
        final int[] ii = {0};
        list.forEach(v -> {
            Log.i("JYN", "" + ii[0]++ + "    " + v);
        } );

        //ArrayList<PositionsOfEachWord> array = new ArrayList<>();
        ArrayList<Integer> initialArray = new ArrayList<Integer>();
        ArrayList<Integer> finalArray = new ArrayList<Integer>();

        if (curPath.length() > 0) {
            int i = 0;
            int l = -1;
            do{
                l = curPath.indexOf("/", l + 1);
                if ( l != -1 ) {
                    Log.i("JYN", i + "번 째 위치에 add합니다" + l);
                    initialArray.add(l);
                    /*
                    if (i != 0) {
                        //array.add(i);
                    } else {
                        //array.add
                    }
                    */
                    //Log.i("JYN", i++ + "번 째 위치 : " + l);
                }
            } while(l+1 < curPath.length() && l != -1);
            Log.i("JYN", i + "번 째 위치에 add합니다" + (list.size()-1));
            initialArray.add(curPath.length() - 1);

            // 1, 7
            // 9, 16
            // 18, 18
            // 20, 29 (length + 1)
            final int[] jj = {0};
            initialArray.forEach(v -> {
                Log.i("JYN", "" + jj[0]++ + "    " + v);
            });

            ArrayList<Integer> convertArray = new ArrayList<>();
            initialArray.forEach(v -> {
                Log.i("JYN", "" + jj[0]++ + "    " + v);
                if (v == 0 ) {
                    convertArray.add(v + 1);
                } else if ( v == curPath.length() - 1) {
                    convertArray.add(v - 1);
                } else {
                    convertArray.add(v - 1);
                    convertArray.add(v + 1);
                }
            });

            final int[] kk = {0};
            convertArray.forEach(v -> {
                Log.i("JYN", "" + kk[0]++ + "    " + v);
            });

            //Collections.copy(finalArray, convertArray);
            finalArray.addAll(convertArray);
            //convertArray 의 사이즈가 8이라면 4번의 setSpan 이 필요 하다.
        }

        SpannableString totalSpan = new SpannableString("");
        int needToMakeObject = list.size();
        //for (int j = 0 ; j < needToMakeObject ; j++) {
        int k = 0;
        SpannableString spannableString = new SpannableString(curPath);
        for (int j = 0 ; j < finalArray.size()/2 ; j++) {
            //SpannableString spannableString = new SpannableString(list.get(j));
            Log.i("JYN", "[for]" + j + "    " + spannableString);

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    // 해당 텍스트를 클릭했을 때 실행할
                    view.showToast(" kkkk ");
                    Log.i("JYN", "[FileManagerMvpPresenterImpl][ClickableSpan] " + widget.toString() + "  clickableSpan : " + spannableString );
                }
                @Override
                public void updateDrawState(TextPaint textPaint) {
                    textPaint.setColor(Color.rgb(243, 110, 84)); // 해당 텍스트 색상 변경
                    textPaint.setUnderlineText(true); // 해당 텍스트 언더라인
                    textPaint.setFakeBoldText(true); // 해당 텍스트 두껍게 처리
                }
            };

            //spannableString.setSpan(clickableSpan, 0, spannableString.length(), 0);
            //totalSpan = (SpannableString) TextUtils.concat(totalSpan, " " , spannableString);
            //위처럼 하지말고
            spannableString.setSpan(clickableSpan, finalArray.get(k), finalArray.get(k+1), 0 ); //이렇게 해야될듯
            Log.i("JYN", "[for]" + j + "  k(" + k + ") " + finalArray.get(k)+ "  k + 1(" + (k + 1) + ") " + finalArray.get(k+1));

            k = k+2;
        }

        //SpannableString spannableString = new SpannableString( curPath );
        /////출처: http://altongmon.tistory.com/443 [IOS를 Java]
        /////spannableString.setSpan(clickableSpan1, 클릭이벤트를 넣고 싶은 문자열의 시작 index, 클릭이벤트를 넣고 싶은 문자열의 끝 index, 0);
        //spannableString.setSpan(clickableSpan1, 0, spannableString.length(), 0);

        //view.setPathToTextView(curPath);

        view.setPathToTextView(totalSpan);
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

                setPathToTextView(path);
            } else {
                view.showToast(item.getName() + " is file");
            }
            Log.i("JYN", "[FileManagerMvpPresenterImpl][onItemClick] " + item.getName() + " is dir");
        } else {
            Log.i("JYN", "[FileManagerMvpPresenterImpl][onItemClick] " + item.getName() + " is file");
        }
        view.showToast("Clicked item = " + item.getName());
     }

     //아래를 for문에서 i만큼 생성해 줘야하고..
    //onclick내부에서는 path에 해당하는 adapter를 load 해 달라고 해야하고..
    //updateDrawState 에서는 클릭된 단어를 white로 해주도록 해야하고.
    ClickableSpan clickableSpan1 = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            // 해당 텍스트를 클릭했을 때 실행할
            view.showToast(" kkkk ");
            Log.i("JYN", "[FileManagerMvpPresenterImpl][ClickableSpan] kkk" );

        }

        @Override
        public void updateDrawState(TextPaint textPaint) {
            textPaint.setColor(Color.rgb(243, 110, 84)); // 해당 텍스트 색상 변경
            textPaint.setUnderlineText(true); // 해당 텍스트 언더라인
            textPaint.setFakeBoldText(true); // 해당 텍스트 두껍게 처리
        }
    };
    //출처: http://altongmon.tistory.com/443 [IOS를 Java]

    class PositionsOfEachWord {
        int startPosition = -1;
        int endPosition = -1;

        public PositionsOfEachWord(int startPosition, int endPosition) {
            this.startPosition = startPosition;
            this.endPosition = endPosition;
        }
    }
}
