package com.qin.zdlock;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2/25/15.
 */
public class ServerCommunication {

    private DB_local db_local;

    private boolean initialied = false;
    private Basic_Information basic_information;

    public ServerCommunication(Activity activity) {
        if (initialied == true)
        {
            return;
        }
        db_local = new DB_local(activity);
        basic_information = new Basic_Information();
        basic_information.initialize(activity);
        initialied = true;
    }

    public ServerCommunication(Context context) {
        if (initialied == true)
        {
            return;
        }
        db_local = new DB_local(context);
        basic_information = new Basic_Information();
        basic_information.initialize(context);
        initialied = true;
        System.setProperty("http.keepAlive", "false");
    }

    public static void updateToDataBase_location()
    {/*
        String challenge_id = MainActivity.challenge_id;
        Log.v("echo", "original id: " + challenge_id);
        HttpPost request = new HttpPost("http://"
                + Ip_addr.server_ip_address
                //+ "/bbb/storeMobileInfomation.php");
                //+ "/DyAuthen/fetchChallenge.php");
                + "/DyAuthen/skip.php");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String result;


        params.add(new BasicNameValuePair("challenge_id", challenge_id));

        //params.add(new BasicNameValuePair("activity_id", "21"));

        //Log.i("Yang", answer);
        try {
            Log.i("echo", "here we go");
            request.setEntity(new UrlEncodedFormEntity(params,
                    HTTP.UTF_8));
            HttpResponse response = new DefaultHttpClient()
                    .execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response
                        .getEntity());

                Log.i("echo", result);
            }
            else
            {
                Log.i("echo", "here we go5");
                Log.i("echo", "something wrong");
            }
        } catch (Exception e) {

            Log.i("ghostli123456", e.getMessage().toString());
            e.printStackTrace();
        }*/

    }

    public static void updateToDataBase_answer(String answer)
    {
        String question_id = OtherActivity.question_id;
        //Log.v("echo", "original id: "+challenge_id);
        HttpPost request = new HttpPost("http://"
                + Ip_addr.server_ip_address
                + "/DyAuthen/submitAnswer.php");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String result;

        params.add(new BasicNameValuePair("question_id", question_id));
        params.add(new BasicNameValuePair("answer", answer));

        try {
            Log.i("echo", "here we go");
            request.setEntity(new UrlEncodedFormEntity(params,
                    HTTP.UTF_8));
            HttpResponse response = new DefaultHttpClient()
                    .execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response
                        .getEntity());

                Log.i("echo", result);
            }
            else
            {
                Log.i("echo", "here we go5");
                Log.i("echo", "something wrong");
            }
        } catch (Exception e) {

            Log.i("ghostli123456", e.getMessage().toString());
            e.printStackTrace();
        }

    }

    public String fetchChallenge()
    {
        HttpPost request = new HttpPost("http://"
                + Ip_addr.server_ip_address
                + "/DyAuthen/fetchChallenge.php");

        List<NameValuePair> params = new ArrayList<NameValuePair>();


        String phone_info = basic_information.getPhoneInformation();
        Log.v("new", "phone_info: "+ phone_info);

        String imei = phone_info.split("\\|")[0];

        params.add(new BasicNameValuePair("imei", imei));

        String result = "";

        try {
            Log.i("echo", "here we go");
            request.setEntity(new UrlEncodedFormEntity(params,
                    HTTP.UTF_8));
            HttpResponse response = new DefaultHttpClient()
                    .execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response
                        .getEntity());

                Log.i("echo", result);
            }
            else
            {
                Log.i("echo", "here we go5");
                Log.i("echo", "something wrong");
            }
        } catch (Exception e) {

            Log.i("ghostli123456", e.getMessage().toString());
            e.printStackTrace();
        }
        Log.i("echo", "?"+result);
        return result;
    }

    public String fetchQuestion()
    {
        HttpPost request = new HttpPost("http://"
                + Ip_addr.server_ip_address
                + "/DyAuthen/fetchQuestion.php");

        List<NameValuePair> params = new ArrayList<NameValuePair>();


        String phone_info = basic_information.getPhoneInformation();
        Log.v("new", "phone_info: "+ phone_info);

        String imei = phone_info.split("\\|")[0];

        params.add(new BasicNameValuePair("imei", imei));

        String result = "";

        try {
            Log.i("echo", "here we go");
            request.setEntity(new UrlEncodedFormEntity(params,
                    HTTP.UTF_8));
            HttpResponse response = new DefaultHttpClient()
                    .execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response
                        .getEntity());

                Log.i("echo", result);
            }
            else
            {
                Log.i("echo", "here we go5");
                Log.i("echo", "something wrong");
            }
        } catch (Exception e) {

            Log.i("ghostli123456", e.getMessage().toString());
            e.printStackTrace();
        }
        Log.i("echo", "?"+result);
        return result;
    }


    String topActivity_past = "";
    public void updateToDataBase_topActivity()
    {
        Cursor c = db_local.rawQuery_topActivity();
        String topActivity = "";
        String time = "";
        String topActivity_info = "";

        Log.v("topActivity", ""+c.getCount()+"data in the database");

        while(c.moveToNext()){
            topActivity = c.getString(c.getColumnIndex("topActivity"));
            time = c.getString(c.getColumnIndex("time"));
            topActivity_info += topActivity+"|"+time+"\n";
            //phoneCall_info += phoneCall;
        }

        db_local.remove_topActivity();

        //Log.v("location", "db_local: "+db_local.getCount());
        //Log.v("location", "location: "+location);
        if (topActivity == "")
        {
            Log.v("topActivity", "something wrong");
            return;
        }

        HttpPost request = new HttpPost("http://"
                + Ip_addr.server_ip_address
                + "/DyAuthen/uploadTopActivity.php");


        List<NameValuePair> params = new ArrayList<NameValuePair>();

        String phone_info = basic_information.getPhoneInformation();
        Log.v("new", "phone_info: "+ phone_info);

        String imei = phone_info.split("\\|")[0];

        params.add(new BasicNameValuePair("imei", imei));

        params.add(new BasicNameValuePair("topActivity_info", topActivity_info));
        Log.v("topActivity", "topActivity_info22: " + topActivity_info);

        String result = "";
        HttpResponse response = null;

        try {
            Log.i("echo_topActivity", "here we go");
            request.setEntity(new UrlEncodedFormEntity(params,
                    HTTP.UTF_8));
            response = new DefaultHttpClient()
                    .execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response
                        .getEntity());

                Log.i("echo_topActivity", result);
            }
            else
            {
                Log.i("echo_topActivity", "here we go5");
                Log.i("echo_topActivity", "something wrong");
            }
        } catch (Exception e) {

            Log.i("ghostli123456", e.getMessage().toString());
            e.printStackTrace();
        }

    }


    String phoneCall_info_past = "";
    //String topActivity_past = "";
    public void updateToDataBase_phoneCall()
    {
        Cursor c = db_local.rawQuery_phoneCall();
        String phoneCall = "";
        String time = "";
        String phoneCall_info = "";

        Log.v("phoneCall", ""+c.getCount()+"data in the database");

        while(c.moveToNext()){
            phoneCall = c.getString(c.getColumnIndex("phoneCall"));
            time = c.getString(c.getColumnIndex("time"));
            //internet_info += internet+"|"+time+"\n";
            phoneCall_info += phoneCall;
        }

        db_local.remove_phoneCall();

        //Log.v("location", "db_local: "+db_local.getCount());
        //Log.v("location", "location: "+location);
        if (phoneCall == "")
        {
            Log.v("phoneCall", "something wrong");
            return;
        }

        HttpPost request = new HttpPost("http://"
                + Ip_addr.server_ip_address
                + "/DyAuthen/uploadPhoneCall.php");


        List<NameValuePair> params = new ArrayList<NameValuePair>();

        String phone_info = basic_information.getPhoneInformation();
        Log.v("new", "phone_info: "+ phone_info);

        String imei = phone_info.split("\\|")[0];

        params.add(new BasicNameValuePair("imei", imei));

        params.add(new BasicNameValuePair("phoneCall_info", phoneCall_info));
        Log.v("phoneCall", "phoneCall_info22: " + phoneCall_info);

        String result = "";
        HttpResponse response = null;

        try {
            Log.i("echo_phoneCall", "here we go");
            request.setEntity(new UrlEncodedFormEntity(params,
                    HTTP.UTF_8));
            response = new DefaultHttpClient()
                    .execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response
                        .getEntity());

                Log.i("echo_phoneCall", result);
            }
            else
            {
                Log.i("echo_phoneCall", "here we go5");
                Log.i("echo_phoneCall", "something wrong");
            }
        } catch (Exception e) {

            Log.i("ghostli123456", e.getMessage().toString());
            e.printStackTrace();
        }


    }


    String sms_info_past = "";
    public void updateToDataBase_sms()
    {

        Cursor c = db_local.rawQuery_sms();
        String sms = "";
        String time = "";
        String sms_info = "";

        Log.v("sms", ""+c.getCount()+"data in the database");

        while(c.moveToNext()){
            sms = c.getString(c.getColumnIndex("sms"));
            time = c.getString(c.getColumnIndex("time"));
            //internet_info += internet+"|"+time+"\n";
            sms_info += sms;
        }

        db_local.remove_sms();

        //Log.v("location", "db_local: "+db_local.getCount());
        //Log.v("location", "location: "+location);
        if (sms == "")
        {
            Log.v("sms", "something wrong");
            return;
        }

        HttpPost request = new HttpPost("http://"
                + Ip_addr.server_ip_address
                + "/DyAuthen/uploadSMS.php");


        List<NameValuePair> params = new ArrayList<NameValuePair>();

        String phone_info = basic_information.getPhoneInformation();
        Log.v("new", "phone_info: "+ phone_info);

        String imei = phone_info.split("\\|")[0];

        params.add(new BasicNameValuePair("imei", imei));

        params.add(new BasicNameValuePair("sms_info", sms_info));
        Log.v("sms", "sms_info22: " + sms_info);

        String result = "";
        HttpResponse response = null;

        try {
            Log.i("echo_sms", "here we go");
            request.setEntity(new UrlEncodedFormEntity(params,
                    HTTP.UTF_8));
            response = new DefaultHttpClient()
                    .execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response
                        .getEntity());

                Log.i("echo_sms", result);
            }
            else
            {
                Log.i("echo_sms", "here we go5");
                Log.i("echo_sms", "something wrong");
            }
        } catch (Exception e) {

            Log.i("ghostli123456", e.getMessage().toString());
            e.printStackTrace();
        }

    }


    String phone_info_past = "";
    public void updateToDataBase_device()
    {

        //db_local.getCount()

        //db_local.addGpsData(longitude, latitude, direct, speed, time);

        Cursor c = db_local.rawQuery_device();

        String device = "";
        String time = "";

        String device_info = "";

        Log.v("device", ""+c.getCount()+"data in the database");
        while(c.moveToNext()){//判断游标是否为空
            //for(int i=0;i<c.getCount()-1;i++){
            //c.move(i);//移动到指定记录
            //c.move(0);//移动到指定记录
            device = c.getString(c.getColumnIndex("device"));

            time = c.getString(c.getColumnIndex("time"));

            //Log.v("device", "device: "+device + "; time: "+time);

            device_info += device+"|"+time+"\n";

        }

        db_local.remove_device();

        //Log.v("location", "db_local: "+db_local.getCount());
        //Log.v("location", "location: "+location);
        if (device == "")
        {
            Log.v("device", "something wrong");
            return;
        }

        HttpPost request = new HttpPost("http://"
                + Ip_addr.server_ip_address
                + "/DyAuthen/uploadDevice.php");


        List<NameValuePair> params = new ArrayList<NameValuePair>();

        String phone_info = basic_information.getPhoneInformation();
        Log.v("new", "phone_info: "+ phone_info);

        String imei = phone_info.split("\\|")[0];

        params.add(new BasicNameValuePair("imei", imei));

        params.add(new BasicNameValuePair("device_info", device_info));
        Log.v("device", "device_info22: " + device_info);
        //params.add(new BasicNameValuePair("device_time", time));
        //params.add(new BasicNameValuePair("latitude", latitude));
        //params.add(new BasicNameValuePair("direct", direct));
        //params.add(new BasicNameValuePair("speed", speed));

        String result = "";
        HttpResponse response = null;

        try {
            Log.i("echo_device", "here we go");
            request.setEntity(new UrlEncodedFormEntity(params,
                    HTTP.UTF_8));
            response = new DefaultHttpClient()
                    .execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response
                        .getEntity());

                Log.i("echo_device", result);
            }
            else
            {
                Log.i("echo_device", "here we go5");
                Log.i("echo_device", "something wrong");
            }
        } catch (Exception e) {

            Log.i("ghostli123456", e.getMessage().toString());
            e.printStackTrace();
        }


    }

    String internet_info_past = "";
    public void updateToDataBase_internet()
    {
        Cursor c = db_local.rawQuery_internet();
        String internet = "";
        String time = "";
        String internet_info = "";

        Log.v("internet", ""+c.getCount()+"data in the database");

        while(c.moveToNext()){
            internet = c.getString(c.getColumnIndex("internet"));
            time = c.getString(c.getColumnIndex("time"));
            //internet_info += internet+"|"+time+"\n";
            internet_info += internet;
        }

        db_local.remove_internet();

        //Log.v("location", "db_local: "+db_local.getCount());
        //Log.v("location", "location: "+location);
        if (internet == "")
        {
            Log.v("internet", "something wrong");
            return;
        }

        HttpPost request = new HttpPost("http://"
                + Ip_addr.server_ip_address
                + "/DyAuthen/uploadInternet.php");


        List<NameValuePair> params = new ArrayList<NameValuePair>();

        String phone_info = basic_information.getPhoneInformation();
        Log.v("new", "phone_info: "+ phone_info);

        String imei = phone_info.split("\\|")[0];

        params.add(new BasicNameValuePair("imei", imei));

        params.add(new BasicNameValuePair("internet_info", internet_info));
        Log.v("internet", "internet_info22: " + internet_info);

        String result = "";
        HttpResponse response = null;

        try {
            Log.i("echo_internet", "here we go");
            request.setEntity(new UrlEncodedFormEntity(params,
                    HTTP.UTF_8));
            response = new DefaultHttpClient()
                    .execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response
                        .getEntity());

                Log.i("echo_internet", result);
            }
            else
            {
                Log.i("echo_internet", "here we go5");
                Log.i("echo_internet", "something wrong");
            }
        } catch (Exception e) {

            Log.i("ghostli123456", e.getMessage().toString());
            e.printStackTrace();
        }

    }




    public void updateToDataBase_gps()
    {

        //db_local.getCount()

        //db_local.addGpsData(longitude, latitude, direct, speed, time);

        Cursor c = db_local.rawQuery_gps();

        String location = "";
        String latitude = "";
        String longitude= "";
        String direct= "";
        String speed= "";
        String gpstime= "";

        Log.v("latitude", ""+c.getCount()+"data in the database: "+db_local.getCount());
        while(c.moveToNext()){//判断游标是否为空
            //for(int i=0;i<c.getCount()-1;i++){
                //c.move(i);//移动到指定记录
                //c.move(0);//移动到指定记录
                latitude = c.getString(c.getColumnIndex("latitude"));

                longitude = c.getString(c.getColumnIndex("longitude"));
                Log.v("latitude", "longitude_getout: "+longitude);
                direct = c.getString(c.getColumnIndex("direct"));
                speed = c.getString(c.getColumnIndex("speed"));
                gpstime = c.getString(c.getColumnIndex("gpstime"));
                location += latitude+"|"+longitude+"|"+direct+"|"+speed+"|"+gpstime+"\n";

                Log.v("latitude", "latitude_getout: location"+longitude);
            //}
        }

        db_local.remove_gps();

        Log.v("location", "db_local: "+db_local.getCount());
        Log.v("location", "location: "+location);
        if (location == "")
        {
            return;
        }

        HttpPost request = new HttpPost("http://"
                + Ip_addr.server_ip_address
                + "/DyAuthen/uploadGPS.php");

        //request.setHeader("Range","bytes="+"");

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        String phone_info = basic_information.getPhoneInformation();
        Log.v("new", "phone_info: "+ phone_info);

        String imei = phone_info.split("\\|")[0];

        params.add(new BasicNameValuePair("imei", imei));

        params.add(new BasicNameValuePair("location", location));
        //params.add(new BasicNameValuePair("latitude", latitude));
        //params.add(new BasicNameValuePair("direct", direct));
        //params.add(new BasicNameValuePair("speed", speed));

        String result = "";
        HttpResponse response = null;

        try {
            Log.i("echo", "here we go");
            request.setEntity(new UrlEncodedFormEntity(params,
                    HTTP.UTF_8));
            response = new DefaultHttpClient()
                    .execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response
                        .getEntity());

                Log.i("echo", result);
            }
            else
            {
                Log.i("echo", "here we go5");
                Log.i("echo", "something wrong");
            }
        } catch (Exception e) {

            Log.i("ghostli123456", e.getMessage().toString());
            e.printStackTrace();
        }

    }

    /*public String fetchQuestion()
    {

        HttpPost request = new HttpPost("http://"
                + Ip_addr.server_ip_address
                + "/DyAuthen/fetchQuestion.php");

        List<NameValuePair> params = new ArrayList<NameValuePair>();


        String phone_info = basic_information.getPhoneInformation();
        //Log.v("new", "phone_info: "+ phone_info);

        String imei = phone_info.split("\\|")[0];

        params.add(new BasicNameValuePair("imei", imei));

        Log.v("new", "imei: "+ imei);

        String result = "";

        try {
            Log.i("echo", "here we go");
            request.setEntity(new UrlEncodedFormEntity(params,
                    HTTP.UTF_8));
            HttpResponse response = new DefaultHttpClient()
                    .execute(request);
            //Log.v("echo", Integer.toString(response.getStatusLine().getStatusCode()));
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response
                        .getEntity());

                Log.i("echo", result);
            }
            else
            {
                Log.i("echo", "here we go5");
                Log.i("echo", "something wrong");
            }
        } catch (Exception e) {

            Log.i("ghostli123456", e.getMessage().toString());
            e.printStackTrace();
        }
        Log.i("echo", "?"+result);
        return result;

    }*/

    public void close()
    {
        if (db_local != null)
        {
            db_local.closeDB();
            db_local = null;
        }
    }
}
