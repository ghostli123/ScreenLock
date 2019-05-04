package com.qin.zdlock;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;



import com.umeng.analytics.MobclickAgent;

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

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class OtherActivity extends Activity implements OnLocationListener {
	private EditText editText1 = null;
    private TextView textView1 = null;
	private String result;
    public static String question_id;
	private String activity_id;

    public static String uploaded_information;

    public String challenge;

    private ServerCommunication serverCommunication;

    public static String Longitude;
    public static String Latitude;

    private Basic_Information basic_information;

    private GPSServiceReal gpsServiceReal;
    private boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,IBinder service) {
            GPSServiceReal.LocalBinder binder = (GPSServiceReal.LocalBinder) service;
            gpsServiceReal = binder.getService();
            mBound = true;
        }

        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }

    };


	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.asdfmain);

        if(testService.networkAccurate == false)
        {
            this.finish();
        }
        editText1 = (EditText) findViewById(R.id.editText_answer);
        textView1 = (TextView) findViewById(R.id.text_question);
        serverCommunication = new ServerCommunication(this);
        String question = serverCommunication.fetchQuestion();
        textView1.setTextColor(Color.BLACK);
        textView1.setTextSize(13.0f);

        if (question == null || question.length()<3)
        {
            finish();
            return;
        }
        String[] question_info = question.split("\\|");
        String question_id = question_info[1];
        question_id = question_id.trim().substring(1,question_id.length()-1);
        this.question_id = question_id;
        String question_content = question_info[2];
        question_content = question_content.trim().substring(1,question_content.length()-2);

        textView1.setText(question_content);



        //test location information
        //Intent geo_intent = new Intent(this, GPSServiceReal.class);
        //bindService(geo_intent, mConnection, Context.BIND_AUTO_CREATE);



/*		Intent intent=getIntent();
		String type=intent.getStringExtra("type");
		activity_id=intent.getStringExtra("activity_id");
        //Log.v("Other", activity_id);
		String question=intent.getStringExtra("question");
		//int number=intent.getIntExtra("number", 0);
		//int number2=intent.getIntExtra("number2", 0);
		TextView textView=(TextView)this.findViewById(R.id.textView1);
		textView.setText("question: "+question);
		//textView.setText("gongsiming: "+company+"; number: "+number+"; number2: "+number2);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

        basic_information = new Basic_Information();
        basic_information.initialize(this);

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

        Log.v("new", basic_information.getPhoneInformation());

        fetchQuestion();


        //get information periodically
        new Thread(new Runnable() {
            public void run() {
                while(true) {
                    String topActivity = basic_information.getTopActivity();
                    Log.v("new", topActivity);

                    //writeBasicInformationToLocal();

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/


	}

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        Log.v("see test test", Basic_Information.getTopActivity());
        if(testService.networkAccurate == false)
        {
            this.finish();
        }
        sayHelloWithoutClick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);
    }

    public void Start(View v)
    {
        Intent intent = new Intent();
        intent.setClass(this, GameMain.class);
        startActivity(intent);
    }

    public void okclick(View v)
    {
        //upload stuff in the local file/storage

        String answer = ""+editText1.getText();
        serverCommunication.updateToDataBase_answer(answer);

        //Start(v);
        //fetchQuestion();
        this.finish();
        //Longitude;
        //Latitude;
    }

    public void sayHelloWithoutClick() {
        Log.v("test see see", Boolean.toString(mBound));
        if (mBound) {
            // 用Service的对象，去读取随机数
            int num = gpsServiceReal.getRandomNumber();
            Log.v("test see see", "activity level" + num);
            gpsServiceReal.setOnLocationListener(OtherActivity.this);
            Log.v("test see see", "setOnLocationListener successfully");
            //Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
        }
    }


    public void sayHello(View view) {
        Log.v("test see see", Boolean.toString(mBound));
        if (mBound) {
            // 用Service的对象，去读取随机数
            int num = gpsServiceReal.getRandomNumber();
            Log.v("test see see", "activity level" + num);
            gpsServiceReal.setOnLocationListener(OtherActivity.this);
            Log.v("test see see", "setOnLocationListener successfully");
            //Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void updateUI(gpsdata cdata) {

        Log.v("wocaoaaa", "Longitude: " + Double.toString(cdata.Longitude));
        Log.v("wocaoaaa", "Latitude: " + Double.toString(cdata.Latitude));

        String longitude = Double.toString(cdata.Longitude);
        String latitude = Double.toString(cdata.Latitude);
        String direct = Double.toString(cdata.Direct);
        String speed = Double.toString(cdata.Speed);


        //upload it into database
        //serverCommunication.updateToDataBase_gps(longitude, latitude, direct, speed);

        return;
    }


	
	public void closeActivity(View v)
	{
		Intent data=new Intent();
		data.putExtra("result", "kkkkkk");
		setResult(30,data);
		this.finish();
	}

    /*Activity info: Activity_ID, Activity_Type,  Activity_Name, Activity_Content, Time, User_ID
    Contact info: Contact_ID, Contact_Method (phone/SMS/website), Contact_Content (who/site), Time, User_ID
    Location info:  Location_ID, Longitude, Latitude, Location_Content, Time, User_ID
    Phone info: Phone_ID, IMEI, CPU_info, ip_address, process_info, os_info, Time, User_ID
    Direct Question info (for filling in “Content” field in above info):  Question_ID, Question Type, Answer, User_ID
    Reserved info*/


    private void writeBasicInformationToLocal()
    {

    }


    private void uploadBasicInformation()
    {
        HttpPost request = new HttpPost("http://"
                + Ip_addr.server_ip_address
                //+ "/bbb/storeMobileInfomation.php");
                + "/DyAuthen/storeMobileInfomation.php");

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        // data constructor
        // IMEI OK
        String answer = editText1.getText().toString();
        Log.i("Other", answer);
        params.add(new BasicNameValuePair("detailedActivity", answer));
        //params.add(new BasicNameValuePair("activity_id", activity_id));
        params.add(new BasicNameValuePair("activity_id", "21"));
        //Log.i("Yang", answer);
        try {
            //Log.i("echo", "here we go");
            request.setEntity(new UrlEncodedFormEntity(params,
                    HTTP.UTF_8));
            HttpResponse response = new DefaultHttpClient()
                    .execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response
                        .getEntity());
                // Toast.makeText(this, result,
                // Toast.LENGTH_LONG).show();
                //writeNewFileData("sql.txt", "");
                //writeNewFileData("sql.txt", result);
                Log.i("echo", result);
            }
            else
            {
                Log.i("echo", "something wrong");
            }
        } catch (Exception e) {

            // Toast.makeText(this, e.getMessage().toString(),
            // Toast.LENGTH_LONG).show();
            Log.i("ghostli123456", e.getMessage().toString());
            e.printStackTrace();
        }
        this.finish();

    }

    public void submitActivity(View v)
	{
        //sayHello(v);

		HttpPost request = new HttpPost("http://"
				+ Ip_addr.server_ip_address
				//+ "/bbb/storeMobileInfomation.php");
				+ "/DyAuthen/storeMobileInfomation.php");

        Log.v("echo", "http://"
                + Ip_addr.server_ip_address
                //+ "/bbb/storeMobileInfomation.php");
                + "/DyAuthen/storeMobileInfomation.php");
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		// data constructor
		// IMEI OK
		String answer = editText1.getText().toString();
		Log.i("Other", answer);
		params.add(new BasicNameValuePair("detailedActivity", answer));
		//params.add(new BasicNameValuePair("activity_id", activity_id));
        params.add(new BasicNameValuePair("activity_id", "21"));
		//Log.i("Yang", answer);
		try {
			//Log.i("echo", "here we go");
			request.setEntity(new UrlEncodedFormEntity(params,
					HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient()
					.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response
                        .getEntity());
				// Toast.makeText(this, result,
				// Toast.LENGTH_LONG).show();
				//writeNewFileData("sql.txt", "");
				//writeNewFileData("sql.txt", result);
				Log.i("echo", result);
			}
			else
			{
				Log.i("echo", "something wrong");
			}
		} catch (Exception e) {

			// Toast.makeText(this, e.getMessage().toString(),
			// Toast.LENGTH_LONG).show();
			Log.i("ghostli123456", e.getMessage().toString());
			e.printStackTrace();
		}
		this.finish();

	}
}
