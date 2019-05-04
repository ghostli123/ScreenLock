package com.qin.zdlock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.Date;
import java.util.Iterator;

/**
 * Created by yang on 2/11/15.
 */
public class GPSServiceReal extends Service {
    private Context context;
    private OnLocationListener mOnLocationListener;

    private LocationManager lm;
    private Location loc;
    private Criteria ct;
    private String provider;
    private GpsStatus gpsstatus;

    //private DBGps dbgps;

    //private boolean isLocation = false;// �Ƿ�λ
    private int timespace = 5000;
    private AutoThread athread = new AutoThread();

    public GPSServiceReal() {
    }

    public GPSServiceReal(Context context) {
        Log.v("test see see", "GPSServiceReal Initialized");
        this.context = context;
        //dbgps = new DBGps(context);
        initLocation();
    }

    private final LocationListener locationListener = new LocationListener()
    {

        public void onLocationChanged(Location arg0)
        {
            //isLocation = true;
            if (!athread.isAlive())
                athread.start();
        }

        public void onProviderDisabled(String arg0)
        {
            showInfo(null, -1);
        }

        public void onProviderEnabled(String arg0)
        {
        }

        public void onStatusChanged(String arg0, int arg1, Bundle arg2)
        {
            //isLocation = true;
            if (!athread.isAlive())
                athread.start();
        }
    };

    private GpsStatus.Listener statuslistener = new GpsStatus.Listener()
    {

        public void onGpsStatusChanged(int event)
        {
            gpsstatus = lm.getGpsStatus(null);
            switch (event)
            {
                case GpsStatus.GPS_EVENT_FIRST_FIX:// ��һ�ζ�λ
                    int c = gpsstatus.getTimeToFirstFix();
                    Log.i("AlimysoYang", String.valueOf(c));
                    break;
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                {
                    Iterable<GpsSatellite> allgps = gpsstatus.getSatellites();
                    Iterator<GpsSatellite> items = allgps.iterator();
                    int i = 0;
                    int ii = 0;
                    while (items.hasNext())
                    {
                        GpsSatellite tmp = (GpsSatellite) items.next();
                        if (tmp.usedInFix())
                            ii++;
                        i++;
                    }
                    Log.v("gps", String.format("number of available satellite:%d", i));
                    Log.v("gps", String.format("number of available satellite for location:%d", ii));
                    break;
                }
                case GpsStatus.GPS_EVENT_STARTED:
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    break;
            }
        }
    };

    private void initLocation()
    {
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            ct = new Criteria();
            // ct.setAccuracy(Criteria.ACCURACY_FINE);// �߾��ȶ�λ��
            ct.setAccuracy(Criteria.ACCURACY_COARSE);//�Ǹ߾��ȶ�λ����
            ct.setAltitudeRequired(true);// ��ʾ����
            ct.setBearingRequired(true);// ��ʾ����
            ct.setSpeedRequired(true);// ��ʾ�ٶ�
            ct.setCostAllowed(true);// �������л���
            ct.setPowerRequirement(Criteria.POWER_LOW);// �͹���
            provider = lm.getBestProvider(ct, true);
            // λ�ñ仯����,Ĭ��1��һ��,0��ʾ�����Ǿ���仯(����10������)
            lm.requestLocationUpdates(provider, 1000, 0, locationListener);
            lm.addGpsStatusListener(statuslistener);
        } else
            showInfo(null, -1);
    }


    private gpsdata getLastPosition()
    {
        gpsdata result = new gpsdata();
        loc = lm.getLastKnownLocation(provider);
        if (loc != null)
        {
            //result.Latitude = (int) (loc.getLatitude() * 1E6);
            //result.Longitude = (int) (loc.getLongitude() * 1E6);
            result.Latitude = loc.getLatitude();
            result.Longitude = loc.getLongitude();
            result.High = loc.getAltitude();
            result.Direct = loc.getBearing();
            result.Speed = loc.getSpeed();
            Date d = new Date();
            //d.setTime(loc.getTime() + 28800000);// UTCʱ��,ת����ʱ��+8Сʱ,��ͬ���ֻ�GPSʱ�䲻ͬ,�е��ֻ�GPSʱ�䲻��Ҫ+8Сʱ
            d.setTime(loc.getTime());// UTCʱ��,ת����ʱ��+8Сʱ,��ͬ���ֻ�GPSʱ�䲻ͬ,�е��ֻ�GPSʱ�䲻��Ҫ+8Сʱ
            result.GpsTime = DateFormat.format("yyyy-MM-dd kk:mm:ss", d).toString();
            d = null;
        }
        return result;
    }

    public void setOnLocationListener(OnLocationListener l) {
        mOnLocationListener = l;
        Log.v("test see see", "alive or not: " + athread.isAlive());
        if (!athread.isAlive()) {
            //dbgps = new DBGps(this);
            initLocation();
            athread.start();
        }
    }

    private void showInfo(gpsdata cdata, int infotype)
    {
        Log.v("gps", "showInfo");
        if (cdata == null)
        {
            Log.v("gps", "cdata is null");
            if (infotype == -1)
            {
                Log.v("gps", "no GPS");
                Log.v("gps", "");
                Log.v("gps", "");
                Log.v("gps", "");
                Log.v("gps", "");
                Log.v("gps", "");
                Log.v("gps", "");
                Log.v("gps", "");

                //btnmanual.setEnabled(false);
                //btnsettimespace.setEnabled(false);
                //etSetTimeSpace.setEnabled(false);
            }
        } else
        {
            Log.v("gps", "cdata is not null");
            Log.v("gps", String.format("Latitude:%f", cdata.Latitude));
            Log.v("gps", String.format("Longitude:%f", cdata.Longitude));
            Log.v("gps", String.format("High:%f", cdata.High));
            Log.v("gps", String.format("Direct:%f", cdata.Direct));
            Log.v("gps", String.format("Speed:%fm/s", cdata.Speed));
            Log.v("gps", String.format("Speed:%fkm/h", (cdata.Speed * 3600.0) / 1000.0));
            Log.v("gps", String.format("GpsTime:%s", cdata.GpsTime));
            cdata.InfoType = infotype;
            switch (infotype)
            {
                case 1:
                    Log.v("gps", "manual update");
                    break;
                case 2:
                    Log.v("gps", "location automatic update");
                    break;
			/*
			 * case 3: tvInfoType.setText("��Ϣ��Դ״̬:λ�øı����"); break;
			 */
            }

            //dbgps.addGpsData(cdata);
            if(mOnLocationListener != null)
                mOnLocationListener.updateUI(cdata);

        }

    }

    public void manualUpdate()
    {
        showInfo(getLastPosition(), 1);
    }

    public void autoUpdate(int timespace)
    {
        this.timespace = timespace;
        Log.v("gps", "start");
    }

    /*
    public void onClick(View v)
    {
        if (v.equals(btnmanual))
        {
            // if (isLocation)
            showInfo(getLastPosition(), 1);
        }
        if (v.equals(btnsettimespace))
        {
            if (TextUtils.isEmpty(etSetTimeSpace.getText().toString()))
            {
                Toast.makeText(this, "setting time interval", Toast.LENGTH_LONG).show();
                etSetTimeSpace.requestFocus();
                return;
            }

            timespace = Integer.valueOf(etSetTimeSpace.getText().toString()) * 1000;
        }
        if (v.equals(btnexit))
            android.os.Process.killProcess(android.os.Process.myPid());
    }
    */

 /*   public void close()
    {
        if (dbgps != null)
        {
            dbgps.closeDB();
            dbgps = null;
        }
    }*/


    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {

        public GPSServiceReal getService() {

            return GPSServiceReal.this;

        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public int getRandomNumber() {
        return 100;
    }

    /*
    @Override
    protected void onDestroy()
    {
        if (dbgps != null)
        {
            dbgps.closeDB();
            dbgps = null;
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
    */

    private class AutoThread extends Thread
    {
        private boolean running = true;
        private Handler h = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                showInfo(getLastPosition(), 2);
            }
        };

        public AutoThread()
        {

        }

        @Override
        public void run()
        {
            while (running)
            {
                try
                {
                    h.sendEmptyMessage(0);
                    Thread.sleep(timespace);
                } catch (Exception e)
                {

                }
            }
        }

    }
}
