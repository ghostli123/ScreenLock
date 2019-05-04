package com.qin.zdlock;


import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;

public class StartServiceReceiver extends BroadcastReceiver {
    //重写onReceive方法
    @Override
    public void onReceive(Context context, Intent intent) {
        //后边的XXX.class就是要启动的服务
        Intent service = new Intent(context,testService.class);
        service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(service);
        Log.v("startAfterBoot", "LALALALALA开机自动服务自动启动.....");
        //启动应用，参数为需要自动启动的应用的包名
        //Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        //context.startActivity(intent );

    }

}