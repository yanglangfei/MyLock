package com.yf.igame.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/10/26.
 */

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if(action.equals(Intent.ACTION_UNINSTALL_PACKAGE)){
            String pm = context.getPackageName();
            Toast.makeText(context, "uninstall ", Toast.LENGTH_SHORT).show();
        }


        if(action.equals(Intent.ACTION_INSTALL_PACKAGE)){
            Toast.makeText(context, "install ", Toast.LENGTH_SHORT).show();
        }
    }
}
