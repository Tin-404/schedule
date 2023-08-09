package com.neu.edu.schedule.thread;

import android.os.Looper;
import android.util.Log;

/**
 * Created by wjd
 */
public class UiRefresh extends Thread {

    private static final String TAG = "zhujin";

    private static UiRefresh uiRefresh;
    private Runnable runnable;

    private UiRefresh(Runnable runnable){
        this.runnable = runnable;
    }

    public static UiRefresh getInstance(Runnable runnable){
        if(uiRefresh == null){
            uiRefresh = new UiRefresh(runnable);
            Log.i(TAG, "getInstance: new a UiRefreshThread");
        }
        return uiRefresh;
    }

    @Override
    public void run() {
        Looper.prepare();
        runnable.run();
    }
}