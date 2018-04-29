package com.bbeaggoo.myapplication;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class CilpboardListenerService extends Service implements ClipboardManager.OnPrimaryClipChangedListener{
    ClipboardManager clipBoard;

    @Override
    public void onCreate() {
        super.onCreate();

        clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipBoard.addPrimaryClipChangedListener(this);
    }

    public CilpboardListenerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*
        mThread = new Thread(worker);
        mThread.setDaemon(true);
        mThread.start();
        */
        return START_STICKY;
    }

    @Override
    public void onPrimaryClipChanged() {
        if (clipBoard != null && clipBoard.getPrimaryClip() != null) {
            ClipData data = clipBoard.getPrimaryClip();
            Log.i("JYN", "onPrimaryClipChanged data : " + data.getItemAt(0));

            String type = clipBoard.getPrimaryClipDescription().getMimeType(0);
            saveClipItem(clipBoard.getPrimaryClip().getItemAt(0), type);

            int dataCount = data.getItemCount();
            int size = clipBoard.getPrimaryClip().getItemCount();

            Log.i("JYN", "dataCount : " + dataCount + "    size : " + size);
            for (int i = 0 ; i < dataCount ; i++) {
                Log.i("JYN", "clip data - item : "+data.getItemAt(i).coerceToText(this));
            }

        } else {
            Log.i("JYN", "No Manager or No Clip data");
        }
    }

    private void saveClipItem(ClipData.Item item, String type) {
        if( item != null && type != null ) {
            Log.i("ClipboardListener", "type : " + type + "    item : " + item);

            if (ClipDescription.MIMETYPE_TEXT_HTML.equals(type)) {
                Log.i("ClipboardListener", "HTML");
            } else if (ClipDescription.MIMETYPE_TEXT_INTENT.equals(type)) {
                Log.i("ClipboardListener", "INTENT");
            } else if (ClipDescription.MIMETYPE_TEXT_PLAIN.equals(type)) {
                Log.i("ClipboardListener", "PLAIN");
            } else if (ClipDescription.MIMETYPE_TEXT_URILIST.equals(type)) {
                Log.i("ClipboardListener", "URLLIST");
            } else {
                Log.i("ClipboardListener", "Else");
            }

            if (item.getText() != null) {
                String clipboardText = item.getText().toString();

                //이 url을 ImageDownloaderTask로 보내서
                //imgUrl(bitmap image), title, date등을 get해야 함.
                ImageDownloaderTask idlt = new ImageDownloaderTask(this);
                idlt.execute(clipboardText);
                Log.i("ClipboardListener", clipboardText + " is add to urlList");

            } else {
                Uri uri = item.getUri();
                Log.i("ClipboardListener", "Uri : " + uri);
                Log.i("ClipboardListener", "path : " + uri.getPath() + "    toString : " + uri.getPath().toString());
                //screen capture는 BackgroundMonitorService에서 처리하면 되므로
                //아래는 필요없을듯..
                //saveSceenCaptureImage(uri);
            }
        }
    }
}
