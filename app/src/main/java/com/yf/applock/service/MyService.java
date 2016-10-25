package com.yf.applock.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yf.applock.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */

public class MyService extends Service {
    private HandlerThread mThread;
    private Handler mHandler;
    private boolean isUnLockActivity = false;
    private static long cycleTime = 100;
    private final static int LOOPHANDLER = 0;
    private List<String> lockName=new ArrayList<>();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mThread=new HandlerThread("count_thread");
        mThread.start();
        lockName.add("com.tencent.mobileqq");

        mHandler=new Handler(mThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case LOOPHANDLER:
                        if(isLockName()&&!isUnLockActivity){
                            Intent intent = new Intent(MyService.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            //调用了解锁界面之后，需要设置一下isUnLockActivity的值
                            isUnLockActivity = true;
                        }
                        break;
                }
                mHandler.sendEmptyMessageDelayed(LOOPHANDLER, cycleTime);

            }
        };
        mHandler.sendEmptyMessage(LOOPHANDLER);

    }

    /**
     * 判断当前的Activity是不是我们开启解锁界面的app
     * @return
     */
    private boolean isLockName(){
        //获取栈顶activity的包名
        ActivityManager mActivityManager;
        mActivityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName topActivity = mActivityManager.getRunningTasks(1).get(0).topActivity;
        String packageName = topActivity.getPackageName();

        //如果当前的Activity是桌面app,那么就需要将isUnLockActivity清空值
        if(getHomes().contains(packageName)){
            isUnLockActivity = false;
        }
        if("com.tencent.mobileqq".equals(packageName)){
            return true;
        }
        return false;
    }

    /**
     * 返回所有桌面app的包名
     * @return
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        //属性
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo ri : resolveInfo){
            names.add(ri.activityInfo.packageName);
            System.out.println(ri.activityInfo.packageName);
        }
        return names;
    }
}
