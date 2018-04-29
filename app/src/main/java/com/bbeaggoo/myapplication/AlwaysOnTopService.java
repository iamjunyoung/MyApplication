package com.bbeaggoo.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

/**
 * Created by junyoung on 16. 11. 27..
 */

public class AlwaysOnTopService extends Service {
    public static String TAG = "TouchAndKeep"+":AlwaysOnTopService";
    public static int longClickDuration = 500; // Time in miliseconds

    private long one;
    private long two;
    private boolean flagIsOne = true;
    Vibrator vibe ;

    Context mContext;

    //listview popup
    View pop_View;
    WindowManager.LayoutParams paramsForListPopup;
    //////

    ///// 새로운 방법
    LinearLayout linear;
    ////////

    WindowManager.LayoutParams params;
    WindowManager wm;
    private SeekBar mSeekBar;                       //���� ���� seek bar

    private float START_X, START_Y;                      //�����̱� ���� ��ġ�� ���� ��
    private int PREV_X, PREV_Y;                         //�����̱� ������ �䰡 ��ġ�� ��
    private int MAX_X = -1, MAX_Y = -1;                //���� ��ġ �ִ� ��


    private GestureDetector gestureDetector;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub

        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        // TODO Auto-generated method stub

        super.onRebind(intent);
    }

    public boolean isMoving = false;
    private OnTouchListener mViewTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:                //����� ��ġ �ٿ��̸�
                    if (flagIsOne) {
                        one = (long) System.currentTimeMillis();
                        Log.i("JYN", "one : " + one);
                        flagIsOne = false;
                    } else {
                        two = (long) System.currentTimeMillis();
                        Log.i("JYN", "two : " + two);
                        flagIsOne = true;
                    }
                    if ((two - one) > 0 && (two - one) < 500) {
                        Log.i("JYN", "Double touch has happened!");
                        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        //long[] pattern = {1000, 200, 1000, 2000, 1200};          // ����, ������, ���� ������ ������ �ð��� �����Ѵ�.
                        //vibe.vibrate(pattern, 0);                                         // ������ �����ϰ� �ݺ�Ƚ���� ����
                        vibe.vibrate(500);                                                   //1�� ���� ������ �︰��.

                    }
                    if(MAX_X == -1)
                        setMaxPosition();
                    START_X = event.getRawX();                    //��ġ ���� ��
                    START_Y = event.getRawY();                    //��ġ ���� ��
                    //PREV_X = params.x;                            //���� ���� ��
                    //PREV_Y = params.y;                            //���� ���� ��
                    PREV_X = paramsForListPopup.x;                            //���� ���� ��
                    PREV_Y = paramsForListPopup.y;                            //���� ���� ��
                    //나이스나이스

                    break;
                case MotionEvent.ACTION_MOVE:

                    int x = (int)(event.getRawX() - START_X);    //�̵��� �Ÿ�
                    int y = (int)(event.getRawY() - START_Y);    //�̵��� �Ÿ�

                    Log.i("JYN", "ACTION_MOVE updateViewLayout x : " + x + "     y : " + y);
                    if ( x > 10 || y > 10 || x < -10 || y < -10 ) {
                        Log.i("JYN", "isMoving is set to true");
                        isMoving = true;
                    }

                    paramsForListPopup.x = PREV_X + x;
                    paramsForListPopup.y = PREV_Y + y;

                    optimizePosition();        //���� ��ġ ����ȭ

                    wm.updateViewLayout(pop_View, paramsForListPopup);

                    break;
            }
            return false;
            //return true;
        }
    };

    private View.OnClickListener mViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isMoving) {
                Log.d("JYN", "OnClickListener");
                Toast.makeText(mContext, "onClick!!", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("JYN", "Must not be OnClickListener");
                Toast.makeText(mContext, "Must not be onClick!!. set isMoving to false", Toast.LENGTH_SHORT).show();
                isMoving = false;
            }
        }
    };
    //출처: http://superfelix.tistory.com/42 [☼ 꿈꾸는 도전자 Felix !]


    private OnLongClickListener mViewLongTouchListener = new OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            // TODO Auto-generated method stub
            Log.i("JYNN","LongPress");
            return true;
        }

    };


    public class GestureDoubleTap extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //some logic
            Log.i("JYNN","Double tab");
            return true;
        }

    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        //mContext = getApplicationContext();
        mContext = this;
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        params = new WindowManager.LayoutParams(
                200,
                200,
                //WindowManager.LayoutParams.WRAP_CONTENT,
                //WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,             //�׻� �� ������ �ְ�. status bar �ؿ� ����. ��ġ �̺�Ʈ ���� �� ����.
                //WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,     //�� �Ӽ��� ���ָ� ��ġ & Ű �̺�Ʈ�� �԰� �ȴ�.
                PixelFormat.TRANSLUCENT);                             //����
        //params.gravity = Gravity.LEFT | Gravity.CENTER;                 //���� ��ܿ� ��ġ�ϰ� ��.
        //params.gravity = Gravity.LEFT;
        params.gravity = Gravity.LEFT | Gravity.TOP;

        addOpacityController();     //�˾� ���� ���� �����ϴ� ��Ʈ�ѷ� �߰�

        Matrix m = new Matrix();
        m.postTranslate(50, 50);

        ///////
        pop_View = View.inflate(mContext, R.layout.layout_for_always_on_top, null);
        LinearLayout view_for_share = (LinearLayout)pop_View.findViewById(R.id.layout_for_share);
        LinearLayout view_for_shareToFixedApp = (LinearLayout)pop_View.findViewById(R.id.layout_for_share_to_fixed_app);
        LinearLayout view_for_modifySaveDir = (LinearLayout)pop_View.findViewById(R.id.layout_for_save);

        ImageView share = (ImageView)pop_View.findViewById(R.id.share);
        ImageView shareToFixedApp = (ImageView)pop_View.findViewById(R.id.sharetofixedapp);
        ImageView save = (ImageView)pop_View.findViewById(R.id.savetootherfolder);

        share.setOnTouchListener(mViewTouchListener);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMoving) {
                    Log.d("JYN", "[share] OnClickListener");
                    Toast.makeText(mContext, "[share] onClick!!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("JYN", "[share] Must not be OnClickListener");
                    Toast.makeText(mContext, "[share] Must not be onClick!!. set isMoving to false", Toast.LENGTH_SHORT).show();
                    isMoving = false;
                }
            }
        });
        shareToFixedApp.setOnTouchListener(mViewTouchListener);
        shareToFixedApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMoving) {
                    Log.d("JYN", "[shareToFixedApp] OnClickListener");
                    Toast.makeText(mContext, "[shareToFixedApp] onClick!!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("JYN", "[shareToFixedApp] Must not be OnClickListener");
                    Toast.makeText(mContext, "[shareToFixedApp] Must not be onClick!!. set isMoving to false", Toast.LENGTH_SHORT).show();
                    isMoving = false;
                }
            }
        });
        save.setOnTouchListener(mViewTouchListener);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMoving) {
                    Log.d("JYN", "[save] OnClickListener");
                    Toast.makeText(mContext, "[save] onClick!!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("JYN", "[save] Must not be OnClickListener");
                    Toast.makeText(mContext, "[save] Must not be onClick!!. set isMoving to false", Toast.LENGTH_SHORT).show();
                    isMoving = false;
                }
            }
        });

        paramsForListPopup = new WindowManager.LayoutParams(

                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,             //�׻� �� ������ �ְ�. status bar �ؿ� ����. ��ġ �̺�Ʈ ���� �� ����.
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,     //�� �Ӽ��� ���ָ� ��ġ & Ű �̺�Ʈ�� �԰� �ȴ�.
                PixelFormat.TRANSLUCENT);
        paramsForListPopup.gravity = Gravity.LEFT | Gravity.TOP;

        wm.addView(pop_View, paramsForListPopup);


    }

    private void setMaxPosition() {
        DisplayMetrics matrix = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(matrix);    //ȭ�� ������ �����ͼ�

        MAX_X = matrix.widthPixels;
        MAX_Y = matrix.heightPixels;

    }

    private void optimizePosition() {
        if(params.x > MAX_X) params.x = MAX_X;
        if(params.y > MAX_Y) params.y = MAX_Y;
        if(params.x < 0) params.x = 0;
        if(params.y < 0) params.y = 0;
    }

    private void addOpacityController() {
        mSeekBar = new SeekBar(this);     //���� ���� seek bar
        mSeekBar.setMax(100);              //�ƽ� �� ����.
        mSeekBar.setProgress(100);       //���� ���� ����. 100:������, 0�� ���� ����
        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                paramsForListPopup.alpha = progress / 100.0f;        //���İ� ����
                wm.updateViewLayout(pop_View, paramsForListPopup);    //�˾� �� ������Ʈ
            }
        });

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,             //�׻� �� ������ �ְ�. status bar �ؿ� ����. ��ġ �̺�Ʈ ���� �� ����.
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,    //�� �Ӽ��� ���ָ� ��ġ & Ű �̺�Ʈ�� �԰� �ȴ�.
                PixelFormat.TRANSLUCENT);                             //����
        params.gravity = Gravity.LEFT | Gravity.TOP;                     //���� ��ܿ� ��ġ�ϰ� ��.

        wm.addView(mSeekBar, params);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        setMaxPosition();     //�ִ밪 �ٽ� ����
        optimizePosition();       //�� ��ġ ����ȭ
    }


    public void addListViewPopup() {
        // 팝업
        Resources lang_res = mContext.getResources();
        DisplayMetrics lang_dm = lang_res.getDisplayMetrics();

        pop_View = View.inflate(mContext, R.layout.layout_for_always_on_top, null);
        PopupWindow popupWindow = new PopupWindow(pop_View, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);

        LinearLayout view_for_share = (LinearLayout)pop_View.findViewById(R.id.layout_for_share);
        LinearLayout view_for_shareToFixedApp = (LinearLayout)pop_View.findViewById(R.id.layout_for_share_to_fixed_app);
        LinearLayout view_for_modifySaveDir = (LinearLayout)pop_View.findViewById(R.id.layout_for_save);

        pop_View.setOnTouchListener(mViewTouchListener);
        pop_View.setOnClickListener(mViewClickListener);

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if(wm != null) {      //���� ����� �� ����. *�߿� : �並 �� ���� �ؾ���.

            if(mSeekBar != null) {
                wm.removeView(mSeekBar);
                mSeekBar = null;
            }

            Log.i("JYN", "isAttached? pop_View : "  + pop_View.isAttachedToWindow());
            if(pop_View != null && pop_View.isAttachedToWindow()) {
                wm.removeViewImmediate(pop_View);
                pop_View = null;
            }


            if(linear != null && linear.isAttachedToWindow()) {
                wm.removeViewImmediate(linear);
                linear = null;
            }

        }
        super.onDestroy();
    }
}
