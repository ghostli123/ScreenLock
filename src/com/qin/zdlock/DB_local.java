

package com.qin.zdlock;

/**
 * Created by yang on 2/11/15.
 */

 import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Environment;
 import android.util.Log;
 import android.widget.Toast;

        import java.io.File;
 import java.text.SimpleDateFormat;
 import java.util.Date;

//import android.database.sqlite.SQLiteOpenHelper;

public class DB_local
{
    //private static final String dbname = "gpsdata.db";
    private final Context ct;
    private static final String dbpath = Environment.getExternalStorageDirectory().getPath() + "/gpsdata.db";
    private SQLiteDatabase db;
	/*private SQLiteDB sdb;

	private static class SQLiteDB extends SQLiteOpenHelper
	{
		public SQLiteDB(Context context)
		{
			super(context, dbname, null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase sdb)
		{
			//����
			sdb.execSQL("create table tab_gps (infotype integer,latitude integer,longitude integer,high double,direct double,speed double,gpstime date);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase sdb, int oldVersion, int newVersion)
		{
			sdb.execSQL("drop table if exists tab_gps");
			onCreate(sdb);
		}
	}*/

    //��ʼ�����ݿ�
    public DB_local(Context context)
    {
        ct = context;
        createDB();
        //sdb = new SQLiteDB(ct);
    }

    private void createDB()
    {
        File f = new File(dbpath);
        f.delete();
        if (!f.exists())
        {
            db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
            db.execSQL("create table tab_gps (infotype integer,latitude varchar(20),longitude varchar(20),high varchar(20),direct varchar(20),speed varchar(20),gpstime date);");
            db.execSQL("create table tab_gpsstatus (time date,status integer);");
            db.execSQL("create table tab_device (device varchar(100),time date);");
            db.execSQL("create table tab_internet (internet varchar(200),time date);");
            db.execSQL("create table tab_phoneCall (phoneCall varchar(100),time date);");
            db.execSQL("create table tab_topActivity (topActivity varchar(100),time date);");
            db.execSQL("create table tab_sms (sms varchar(200),time date);");
        }
        else
            db = SQLiteDatabase.openDatabase(dbpath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    //�����ݿ�
	/*public void openDB()
	{
		db = sdb.getWritableDatabase();
	}*/

    public void closeDB()
    {
        //sdb.close();
        db.close();
    }

    public boolean addGpsData(String longitude, String latitude, String direct, String speed, String time)
    {
        boolean result = true;
        try
        {
            Log.v("location", "db_local_added: addGpsData");
            /*String StrSql = String.format("insert into tab_gps (infotype,latitude,longitude,high,direct,speed,gpstime) values (%d,%f,%f,%.1f,%.1f,%.1f,'%s')",
                    cdata.InfoType, cdata.Latitude, cdata.Longitude, cdata.High, cdata.Direct, cdata.Speed, cdata.GpsTime);*/
            Log.v("location", "db_local_added: "+longitude);
            String StrSql = String.format("insert into tab_gps (latitude,longitude,direct,speed,gpstime) values ('%s','%s','%s','%s','%s')",
                    latitude, longitude, direct, speed, time);
            Log.v("location", "db_local_added: addGpsData");
            db.execSQL(StrSql);
            result = true;
            Log.v("location", "db_local_added: addGpsData");
        }catch(Exception e)
        {
            result = false;
            //Toast.makeText(ct, "storage error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return result;
    }

    public boolean addGpsStatusData(String gpstime,int status)
    {
        boolean result = true;
        try
        {
            String StrSql = String.format("insert into tab_gpsstatus (time,status) values ('%s',%d)",
                    gpstime, status);
            db.execSQL(StrSql);
            result = true;
        }catch(Exception e)
        {
            result = false;
            Toast.makeText(ct, "����GPS״̬ʧ��:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return result;
    }



    public long getCount(){

        Cursor cursor =db.rawQuery("select count(*) from tab_gps", null);
        cursor.moveToFirst();
        long reslut=cursor.getLong(0);
        return reslut;
    }

    public Cursor rawQuery_gps() {
        Cursor c=db.rawQuery("SELECT * FROM tab_gps where 1", null);
        return c;
    }

    public Cursor rawQuery_device() {
        Cursor c=db.rawQuery("SELECT * FROM tab_device where 1", null);
        return c;
    }

    public Cursor rawQuery_internet() {
        Cursor c=db.rawQuery("SELECT * FROM tab_internet where 1", null);
        return c;
    }

    public Cursor rawQuery_phoneCall() {
        Cursor c=db.rawQuery("SELECT * FROM tab_phoneCall where 1", null);
        return c;
    }

    public Cursor rawQuery_topActivity() {
        Cursor c=db.rawQuery("SELECT * FROM tab_topActivity where 1", null);
        return c;
    }

    public Cursor rawQuery_sms() {
        Cursor c=db.rawQuery("SELECT * FROM tab_sms where 1", null);
        return c;
    }

    public void remove_gps() {
        db.delete("tab_gps", null, null);
        //db.rawQuery("DELETE FROM tab_gps WHERE 1", null);
    }

    public void remove_device() {
        db.delete("tab_device", null, null);
        //db.rawQuery("DELETE FROM tab_gps WHERE 1", null);
    }

    public void remove_internet() {
        db.delete("tab_internet", null, null);
        //db.rawQuery("DELETE FROM tab_gps WHERE 1", null);
    }

    public void remove_phoneCall() {
        db.delete("tab_phoneCall", null, null);
        //db.rawQuery("DELETE FROM tab_gps WHERE 1", null);
    }

    public void remove_sms() {
        db.delete("tab_sms", null, null);
        //db.rawQuery("DELETE FROM tab_gps WHERE 1", null);
    }

    public void remove_topActivity() {
        db.delete("tab_topActivity", null, null);
        //db.rawQuery("DELETE FROM tab_gps WHERE 1", null);
    }

    public Boolean addDeviceData(String device_info) {

        boolean result = false;
        try
        {
            //String StrSql = String.format("insert into tab_device (latitude,longitude,direct,speed,gpstime) values ('%s','%s','%s','%s','%s')",
                    //latitude, longitude, direct, speed, time);
            String StrSql = String.format("insert into tab_device (device, time) values ('%s', '%s')",device_info, getTime());
            db.execSQL(StrSql);
            result = true;
            Log.v("device", "db_local_added: addDeviceData");
        }catch(Exception e)
        {
            Log.v("device", "db_local_added: wrong");
            Log.v("device", "error info: "+e.toString());
            result = false;
            //Toast.makeText(ct, "storage error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return result;

    }

    private String getTime(){
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
        //Log.e("msg", t1);
    }

    public Boolean addInternetData(String internet_info) {

        boolean result = false;
        try
        {
            String StrSql = String.format("insert into tab_internet (internet, time) values ('%s', '%s')",internet_info, getTime());
            db.execSQL(StrSql);
            result = true;
            Log.v("internet", "db_local_added: addInternetData");
        }catch(Exception e)
        {
            Log.v("internet", "db_local_added: wrong");
            Log.v("internet", "error info: "+e.toString());
            result = false;
            //Toast.makeText(ct, "storage error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return result;
    }


    public Boolean addPhoneCallData(String phoneCall_info) {

        boolean result = false;
        try
        {
            String StrSql = String.format("insert into tab_phoneCall (phoneCall, time) values ('%s', '%s')",phoneCall_info, getTime());
            db.execSQL(StrSql);
            result = true;
            Log.v("phoneCall", "db_local_added: addPhoneCallData");
        }catch(Exception e)
        {
            Log.v("phoneCall", "db_local_added: wrong");
            Log.v("phoneCall", "error info: "+e.toString());
            result = false;
            //Toast.makeText(ct, "storage error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return result;
    }

    public Boolean addTopActivityData(String topActivity_info) {

        boolean result = false;
        try
        {
            String StrSql = String.format("insert into tab_topActivity (topActivity, time) values ('%s', '%s')",topActivity_info, getTime());
            db.execSQL(StrSql);
            result = true;
            Log.v("topActivity", "db_local_added: addTopActivityData");
        }catch(Exception e)
        {
            Log.v("topActivity", "db_local_added: wrong");
            Log.v("topActivity", "error info: "+e.toString());
            result = false;
            //Toast.makeText(ct, "storage error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return result;
    }

    public Boolean addSMSData(String sms_info) {

        boolean result = false;
        try
        {
            String StrSql = String.format("insert into tab_sms (sms, time) values ('%s', '%s')",sms_info, getTime());
            db.execSQL(StrSql);
            result = true;
            Log.v("sms", "db_local_added: addSMSData");
        }catch(Exception e)
        {
            Log.v("sms", "db_local_added: wrong");
            Log.v("sms", "error info: "+e.toString());
            result = false;
            //Toast.makeText(ct, "storage error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return result;
    }
}