package com.qin.zdlock;

import android.app.Activity;
import android.content.Context;
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
public class LocalCommunication {

    private DB_local db_local;

    private boolean initialied = false;
    private Basic_Information basic_information;

    public LocalCommunication(Context context) {
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






    String device_info_past = "";
    public boolean updateToDataBase_device()
    {
        String device_info = basic_information.getPhoneInformation();
        Log.v("device", "phone_info_submitted: "+ device_info);

        if(device_info.equals(device_info_past))
        {
            return false;
        }
        device_info_past = device_info;
        Log.v("device", "db_local_added: "+device_info);
        Boolean temp = db_local.addDeviceData(device_info);
        Log.v("device", "temp is "+ temp);
        return temp;
    }

    String internet_info_past = "";
    public Boolean updateToDataBase_internet()
    {
        String internet_info = basic_information.getInternetRecords();
        Log.v("internet", "internet_info: " + internet_info);

        if (internet_info.equals(internet_info_past))
        {
            return false;
        }
        internet_info_past = internet_info;

        Log.v("internet", "db_local_added: "+internet_info);
        Boolean temp = db_local.addInternetData(internet_info);
        Log.v("internet", "temp is "+ temp);
        return temp;
    }

    String phoneCall_info_past = "";
    public boolean updateToDataBase_phoneCall() {

        String phoneCall_info = basic_information.getPhoneCallInfo();
        Log.v("phoneCall", "phoneCall_info: " + phoneCall_info);

        if (phoneCall_info.equals(phoneCall_info_past))
        {
            return false;
        }
        phoneCall_info_past = phoneCall_info;

        Log.v("phoneCall", "db_local_added: "+phoneCall_info);
        Boolean temp = db_local.addPhoneCallData(phoneCall_info);
        Log.v("phoneCall", "temp is "+ temp);
        return temp;
    }

    String topActivity_info_past = "";
    public boolean updateToDataBase_topActivity() {
        String topActivity_info = basic_information.getTopActivity();
        Log.v("topActivity", "topActivity_info: " + topActivity_info);

        if (topActivity_info.equals(topActivity_info_past))
        {
            return false;
        }
        topActivity_info_past = topActivity_info;

        Log.v("topActivity", "db_local_added: "+topActivity_info);
        Boolean temp = db_local.addTopActivityData(topActivity_info);
        Log.v("topActivity_info", "temp is "+ temp);
        return temp;
    }

    String sms_info_past = "";
    public boolean updateToDataBase_sms()
    {

        String sms_info = basic_information.getSmsInPhone();
        Log.v("sms", "sms_info: " + sms_info);

        if (sms_info.equals(sms_info_past))
        {
            return false;
        }
        sms_info_past = sms_info;

        Log.v("sms", "db_local_added: "+sms_info);
        Boolean temp = db_local.addSMSData(sms_info);
        Log.v("sms", "temp is "+ temp);
        return temp;

    }


    String longitude_past = "";
    String latitude_past = "";
    String direct_past = "";
    String speed_past = "";
    public void updateToDataBase_gps(String longitude, String latitude, String direct, String speed, String time)
    {

        //Log.v("location", "db_local: "+db_local.getCount());

        if ((longitude.equals(longitude_past)) && (latitude.equals(latitude_past)) && (direct.equals(direct_past)) && (speed.equals(speed_past)))
        {
            return;
        }
        longitude_past = longitude;
        latitude_past= latitude;
        direct_past= direct;
        speed_past= speed;

        Log.v("location", "db_local_added: "+longitude);
        Boolean temp = db_local.addGpsData(longitude, latitude, direct, speed, time);
        Log.v("location", "db_local_added: "+db_local.getCount());
        //Log.v("location", "db_local_added: "+temp);


   /*     HttpPost request = new HttpPost("http://"
                + Ip_addr.server_ip_address
                + "/DyAuthen/uploadGPS.php");

        //request.setHeader("Range","bytes="+"");

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        String phone_info = basic_information.getPhoneInformation();
        Log.v("new", "phone_info: "+ phone_info);

        String imei = phone_info.split("\\|")[0];

        params.add(new BasicNameValuePair("imei", imei));

        params.add(new BasicNameValuePair("longitude", longitude));
        params.add(new BasicNameValuePair("latitude", latitude));
        params.add(new BasicNameValuePair("direct", direct));
        params.add(new BasicNameValuePair("speed", speed));

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
        }*/

    }





    public void close()
    {
        if (db_local != null)
        {
            db_local.closeDB();
            db_local = null;
        }
    }



}
