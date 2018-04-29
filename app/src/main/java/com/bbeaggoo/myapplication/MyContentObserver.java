package com.bbeaggoo.myapplication;

/**
 * Created by junyoung on 16. 11. 3..
 */

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class MyContentObserver extends ContentObserver {
    private Context mContext;
    private static String TAG = "APVM_CO";

    public MyContentObserver(Handler handler) {
        super(handler);
    }

    public MyContentObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
    }

/*
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.i(TAG, "change is occured");

        StringBuffer stacktrace = new StringBuffer();
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        for(int x=0; x<stackTrace.length; x++)
        {
            stacktrace.append(stackTrace[x].toString() + " ");
        }
        Log.e(TAG, "stacktrace");
        Log.e(TAG, stacktrace.toString());
        // 페북에서 저장시 두번찍힌다 왜그러나??
    }
*/
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange);
        //super.onChange(selfChange, uri);
        Log.i(TAG, "change is occured. uri : " + uri + "    LastPathSegment : " + uri.getLastPathSegment());
        fetchLastedFiles();
        shareTo(uri);
        //share to other people.
        //move this file to other folder. (or create other folder)

        //fetchLatestImages();

        //파일을 다운받았을 때,
        //03-25 23:16:20.747 5941-5941/com.bbeaggoo.myapplication I/APVM_CO: change is occured. uri : content://media/external/file/25014    LastPathSegment : 25014
        //03-25 23:16:20.855 5941-5941/com.bbeaggoo.myapplication I/APVM_CO: getLastestImage uri  /storage/emulated/0/Pictures/Screenshots/Screenshot_2018-03-25-22-51-24.png

        //이미지 파일 스크린샷 할 때,
        //03-25 23:19:02.890 5941-5941/com.bbeaggoo.myapplication I/APVM_CO: change is occured. uri : content://media/external/file/25016    LastPathSegment : 25016
        //03-25 23:19:03.217 5941-5941/com.bbeaggoo.myapplication I/APVM_CO: getLastestImage uri  /storage/emulated/0/Pictures/Screenshots/Screenshot_2018-03-25-23-19-02.png
    }

    public void scanFile() {
        File myFile = new File("sdcard/Pictures/Screenshots");
        File list[] = myFile.listFiles();

        for (int i = 0 ; i < list.length ; i++) {
            Log.i(TAG, i + " : " + list[i].toString());
        }
    }

    public void fetchLastedFiles() {
        // DATA는 이미지 파일의 스트림 데이터 경로를 나타냅니다.
        String[] projection = { MediaStore.Images.Media.DATA };

        final Uri uri = MediaStore.Files.getContentUri("external");
        Cursor imageCursor = mContext.getContentResolver().query(
                uri, // 이미지 컨텐트 테이블
                projection, // DATA를 출력
                null,       // 모든 개체 출력
                null,
                null);      // 정렬 안 함

        int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);

        if (imageCursor == null) {
            // Error 발생
            // 적절하게 handling 해주세요
        } else if (imageCursor.moveToLast()) {
            String filePath = imageCursor.getString(dataColumnIndex);
            Uri fileuUri = Uri.parse(filePath);

            Log.i(TAG, "getLastestFile uri  " + fileuUri);
            //해당 파일의 날짜? 갖고오기

        } else {
            // imageCursor가 비었습니다.
        }
        imageCursor.close();
    }

    public void fetchLatestImages() {
        // DATA는 이미지 파일의 스트림 데이터 경로를 나타냅니다.
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor imageCursor = mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA를 출력
                null,       // 모든 개체 출력
                null,
                null);      // 정렬 안 함

        int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);

        if (imageCursor == null) {
            // Error 발생
            // 적절하게 handling 해주세요
        } else if (imageCursor.moveToLast()) {
            String filePath = imageCursor.getString(dataColumnIndex);
            Uri imageUri = Uri.parse(filePath);

            Log.i(TAG, "getLastestImage uri  " + imageUri);
            //해당 파일의 날짜? 갖고오기

        } else {
            // imageCursor가 비었습니다.
        }
        imageCursor.close();
    }

    //public void shareTo(String title, String clipboardText, Uri uri) {
    public void shareTo(Uri uri) {
        Log.i(TAG, "call shareTo");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        //intent.setType("text/plain");
        //intent.setPackage("com.google.android.keep");
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        //intent.putExtra(Intent.EXTRA_SUBJECT, title);
        //intent.putExtra(Intent.EXTRA_TEXT, clipboardText);

        //intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);

        try {
            mContext.startActivity(Intent.createChooser(intent, "Test For JY"));
        } catch (Exception e) {
            Toast.makeText(mContext, "해당 앱이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
