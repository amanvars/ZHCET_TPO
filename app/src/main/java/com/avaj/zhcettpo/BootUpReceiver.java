package com.avaj.zhcettpo;

/**
 * Created by aman_AV on 19-07-2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootUpReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        /****** For Start Activity *****/
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);


    }
}