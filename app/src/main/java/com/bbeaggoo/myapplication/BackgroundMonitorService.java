package com.bbeaggoo.myapplication;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.FileObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;

public class BackgroundMonitorService extends Service {
    public BackgroundMonitorService() {
    }

    public static String TAG = "APVM";
    Thread mThread;
    private static int THREAD_SLEEP_TIME = 3000;
    private final static String SCREENSHOTS_DIR_NAME = "Screenshots";
    MyContentObserver co;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "Service onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.v(TAG, "Service is started onStartCommand()");
        mThread = new Thread(worker);
        mThread.setDaemon(true);
        mThread.start();
        return START_REDELIVER_INTENT;
    }

    Runnable worker = new Runnable() {
        public void run() {
            runGet();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "FileObserver is stopWatching()");

        getContentResolver().unregisterContentObserver(co);

        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
            Log.v(TAG, "Thread is stopped in onDestroy()");
        }
        //handler.removeMessages(MSG_WORK);
    }

    private void runGet() {
        //File myFile = new File("sdcard/Pictures/Screenshots");
        File myFile = new File("storage/emulated/0/Download");

        Log.i(TAG, "Observing is clicked...  path : " + myFile.getPath());

        NewFileObserver observer = new NewFileObserver(myFile.getAbsolutePath());
        observer.startWatching();

        Log.i(TAG, "Oberving is started");

        //monitorAllFiles(myFile);
        Handler handler = new Handler(Looper.getMainLooper());
        co = new MyContentObserver(this, handler);
        //getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, co);
        final Uri uri = MediaStore.Files.getContentUri("external");
        getContentResolver().registerContentObserver(uri, true, co);
        try {
            while(!Thread.currentThread().isInterrupted()) {
                Log.i(TAG, "I am observer sdcard");
                Thread.sleep(THREAD_SLEEP_TIME);
            }
        } catch(InterruptedException e) {
        }
    }

    private static class NewFileObserver extends FileObserver {
        private String mAddedPath = null;

        NewFileObserver(String path) {
            super(path, FileObserver.CREATE);
        }
/*
03-25 22:58:56.202 1320-1339/com.bbeaggoo.myapplication D/APVM: Detected new file added %5B삼성SDI_경력%5D 입사지원서_성명_희망분야 (1).docx.crdownload
03-25 22:58:56.202 1320-1339/com.bbeaggoo.myapplication D/APVM: uri : storage/emulated/0/Download/%5B삼성SDI_경력%5D 입사지원서_성명_희망분야 (1).docx.crdownload
0

 */
        @Override
        public void onEvent(int event, String path) {
            Log.d(TAG, "hihi");
            Uri uri = Uri.parse("storage/emulated/0/Download/"+path);
            Log.d(TAG, String.format("Detected new file added %s", path, "  type : %s", getMimeType(path)));
            Log.d(TAG, "uri : "+ uri);
            synchronized (this) {
                mAddedPath = path;
                notify();
            }
        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
        // https://code.i-harness.com/ko/q/83114d
        // http://susemi99.tistory.com/896
    }
}
