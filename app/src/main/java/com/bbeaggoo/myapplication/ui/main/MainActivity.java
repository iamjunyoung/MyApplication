package com.bbeaggoo.myapplication.ui.main;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bbeaggoo.myapplication.AlwaysOnTopService;
import com.bbeaggoo.myapplication.R;
import com.bbeaggoo.myapplication.ui.filemanager.FileManagerActivity;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "JYN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.i(TAG, "path : " + sdcardPath);
        //출처: http://indienote.tistory.com/31 [인디노트]
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        ComponentName compo = new ComponentName("com.bbeaggoo.myapplication", "com.bbeaggoo.myapplication.BackgroundMonitorService");
        Intent intentForOb = new Intent();
        switch (v.getId()) {
            case R.id.startService:
                intentForOb.setComponent(compo);
                startService(intentForOb);

                Intent intentForAOTS = new Intent(this, AlwaysOnTopService.class);
                startService(intentForAOTS);
                Log.i(TAG, "Start BackgroundMonitorService and AlwaysOnTopService service");
                break;
            case R.id.stopService:
                intentForOb.setComponent(compo);
                stopService(intentForOb);

                Intent intentForStopAOTS = new Intent(this, AlwaysOnTopService.class);
                stopService(intentForStopAOTS);
                Log.i(TAG, "End BackgroundMonitorService and AlwaysOnTopService Service");
                break;
            case R.id.fileManagerActivity:
                Intent intent = new Intent(MainActivity.this, FileManagerActivity.class);
                startActivity(intent);
                Log.i(TAG, "End BackgroundMonitorService and AlwaysOnTopService Service");
                break;

        }
    }
}
