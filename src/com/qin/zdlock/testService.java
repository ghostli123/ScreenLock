package com.qin.zdlock;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by yang on 2/25/15.
 */
public class testService extends Service implements OnLocationListener{

    public static boolean networkAccurate = false;


    private ServerCommunication serverCommunication;
    private LocalCommunication localCommunication;

    private GPSServiceReal gpsServiceReal;
    private boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,IBinder service) {
            Log.v("bindService", "onServiceConnected");
            GPSServiceReal.LocalBinder binder = (GPSServiceReal.LocalBinder) service;
            gpsServiceReal = binder.getService();
            mBound = true;
            Log.v("bindService", Boolean.toString(mBound));
        }

        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }

    };

    private final IBinder binder = new LocalBinder();



    public class LocalBinder extends Binder {

        public testService getService() {

            return testService.this;

        }

    }

    public void sayHelloWithoutClick() {
        Log.v("test see see", Boolean.toString(mBound));
        if (mBound) {
            // 用Service的对象，去读取随机数
            int num = gpsServiceReal.getRandomNumber();
            Log.v("test see see", "activity level" + num);
            gpsServiceReal.setOnLocationListener(testService.this);
            Log.v("test see see", "setOnLocationListener successfully");
            //Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    final private String TAG = "startAfterBoot";
    @Override
    public void onCreate() {
        //Toast.makeText(this, "My Service created", Toast.LENGTH_LONG).show();
        Log.i(TAG, "onCreate");

        new Thread(new IpAddrThread()).start();


        serverCommunication = new ServerCommunication(this);
        localCommunication = new LocalCommunication(this);

        //test location information
        Intent geo_intent = new Intent(this, GPSServiceReal.class);
        bindService(geo_intent, mConnection, Context.BIND_AUTO_CREATE);


        new Thread(new MyThread()).start();

        new Thread(new BasicInfoUploadThread()).start();

    }



    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    sayHelloWithoutClick();
                    break;
                case 2:
                    uploadBasicInformation();
                    break;
                default:
                    break;
            }


        }
    };


    public class MyThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //while (true) {
                try {
                    Thread.sleep(3000);// 线程暂停3秒，单位毫秒
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);// 发送消息
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            //}
        }
    }

    public class BasicInfoUploadThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
            try {
                Thread.sleep(5000);// 线程暂停3秒，单位毫秒
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);// 发送消息
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            }
        }
    }



    public void uploadBasicInformation()
    {


        boolean a = localCommunication.updateToDataBase_device();
        //Log.v("device", "a is "+a);
        if(a && testService.networkAccurate == true)
        {
            serverCommunication.updateToDataBase_device();
        }

        boolean b = localCommunication.updateToDataBase_internet();
        if(b && testService.networkAccurate == true)
        {
            serverCommunication.updateToDataBase_internet();
        }

        boolean c = localCommunication.updateToDataBase_phoneCall();
        if(c && testService.networkAccurate == true)
        {
            serverCommunication.updateToDataBase_phoneCall();
        }

        boolean d = localCommunication.updateToDataBase_topActivity();
        if(d && testService.networkAccurate == true)
        {
            serverCommunication.updateToDataBase_topActivity();
        }

        boolean e = localCommunication.updateToDataBase_sms();
        if(e && testService.networkAccurate == true)
        {
            serverCommunication.updateToDataBase_sms();
        }



        /*if (testService.networkAccurate == false)
        {
            return;
        }

        Log.v("haha", "upload basic information");*/

        //serverCommunication.updateToDataBase_device();
        //serverCommunication.updateToDataBase_internet();
        //serverCommunication.updateToDataBase_phoneCall();
        //serverCommunication.updateToDataBase_topActivity();
        //serverCommunication.updateToDataBase_sms();

        //localCommunication.updateToDataBase_device();
        //localCommunication.updateToDataBase_internet();
        //localCommunication.updateToDataBase_topActivity();
        //localCommunication.updateToDataBase_sms();



        //test topActivity
        //String topActivity = basic_information.getTopActivity();
        //Log.v("new", topActivity);


        //test contact
        //String telephone_info = basic_information.getTelephoneInfo();
        //Log.v("new", telephone_info);

        //test sms
        //basic_information.getSmsInPhone();
        //Log.v("new", "message: " + sms_info);

        //test internet information
        //basic_information.getInternetRecords();



        //test phone information
        //String phone_info = basic_information.getPhoneInformation();
        //Log.v("new", "phone_info: "+phone_info);
    }

    String longitude_past = "";
    String latitude_past = "";
    String direct_past = "";
    String speed_past = "";
    @Override
    public void updateUI(gpsdata cdata) {

        String longitude = Double.toString(cdata.Longitude);
        String latitude = Double.toString(cdata.Latitude);
        String direct = Double.toString(cdata.Direct);
        String speed = Double.toString(cdata.Speed);
        String time = cdata.GpsTime;

        if ((longitude.equals(longitude_past)) && (latitude.equals(latitude_past)) && (direct.equals(direct_past)) && (speed.equals(speed_past)))
        {
            return;
        }
        longitude_past = longitude;
        latitude_past= latitude;
        direct_past= direct;
        speed_past= speed;

        Log.v("wocaoaaa", "Longitude: " + Double.toString(cdata.Longitude));
        Log.v("wocaoaaa", "Latitude: " + Double.toString(cdata.Latitude));



        localCommunication.updateToDataBase_gps(longitude, latitude, direct, speed, time);
        if (testService.networkAccurate == true)
        {
            serverCommunication.updateToDataBase_gps();
        }



        return;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "My Service Stoped", Toast.LENGTH_LONG).show();
        Log.i(TAG, "onDestroy");
        localCommunication.close();
        serverCommunication.close();

    }

    @Override
    public void onStart(Intent intent, int startid) {
        //Toast.makeText(this, "My Service Start", Toast.LENGTH_LONG).show();
        //Log.i(TAG, "onStart");

    }



    class IpAddrThread extends Thread {
        //int IpNum;

        public IpAddrThread() {
            //this.IpNum = IpNum;
        }

        @Override
        public void run() {
            while(true) {
                Log.v("db_related", "abababa");
                try {

                    Process p = Runtime.getRuntime().exec(
                            "ping -c 1 -w 5 " + Ip_addr.server_ip_address_withoutPort);
                    int status = p.waitFor();

                    if (status == 0) {

                        // pass
                        // mPingIpAddrResult = "连接正常";
                        //setThisIpTrue(IpNum);
                        networkAccurate = true;
                        Log.v("db_related", "cool");
                    } else {

                        // Fail:Host unreachable
                        // mPingIpAddrResult = "连接不可达到";
                        networkAccurate = false;
                        /*Message message = new Message();
                        message.what = 3;
                        handler.sendMessage(message);// 发送消息*/
                        Log.v("db_related", "no link");
                    }
                    Thread.sleep(1000);
                } catch (UnknownHostException e) {
                    Log.v("db_related", "error");
                    // Fail: Unknown Host
                    // mPingIpAddrResult = "出现未知连接";
                } catch (IOException e) {
                    Log.v("db_related", "error");
                    // Fail: IOException
                    // mPingIpAddrResult = "连接出现IO异常";
                } catch (InterruptedException e) {
                    Log.v("db_related", "error");
                    // Fail: InterruptedException
                    // mPingIpAddrResult = "连接出现中断异常";
                }

            }
        }
    }

}
