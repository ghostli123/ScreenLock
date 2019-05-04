package com.qin.zdlock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
        import android.view.MenuItem;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.net.UnknownHostException;


public class testActivity extends Activity {


/*    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    //sayHelloWithoutClick();
                    break;
                case 2: //no network
                    uploadBasicInformation();
                    break;
                default:
                    break;
            }


        }
    };*/

    private DB_local db_local;

    private boolean networkAccurate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //db_local = new DB_local(this);
        //Cursor c = db_local.rawQuery_gps();

        //db_local.remove_gps();
        //Log.v("aaa", Long.toString(db_local.getCount()));

       /* String latitude = "";
        if(c.moveToFirst()){//判断游标是否为空
            for(int i=0;i<c.getCount();i++){
                c.move(i);//移动到指定记录
                latitude = c.getString(c.getColumnIndex("latitude"));
                Log.v("aaa", latitude);
                //String password = c.getString(c.getColumnIndex("password"));
            }
        }*/
        //Log.v("aaa", latitude);

        //new Thread(new IpAddrThread()).start();

    }

    public void startService(View view)
    {
        //DBGps db = new DBGps(this);
        //Log.v("db_related", Long.toString(db.getCount()));
        //Log.v("db_related", Boolean.toString(networkAccurate));
        //if (networkAccurate == true) {
            Intent service = new Intent(this, testService.class);
            service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startService(service);
            Log.v("startAfterBoot", "LALALALALA.....");
       // }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /*    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            return false;
        } else if(cm.getActiveNetworkInfo() == null) {
            return false;
        }
        else
        {
            return cm.getActiveNetworkInfo().isAvailable();
        }
    }*/


}