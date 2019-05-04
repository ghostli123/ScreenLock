package com.qin.zdlock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;


;

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

public class Feedback extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools);
		
	}
	public void Emergency(View v)
	{
		Intent intent = new Intent();
        intent.setClass(this, Emergency.class);
        startActivity(intent);
	}
	public void Skip(View v)
	{
         updateToDataBase();

		 Intent returnIntent = new Intent();
		 returnIntent.putExtra("result","1");
		 setResult(RESULT_OK,returnIntent);     
		 finish();
	}

    public void updateToDataBase()
    {
        String challenge_id = MainActivity.challenge_id;
        Log.v("echo", "original id: "+challenge_id);
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
        }

    }



	// ���ε�Home��
		@SuppressLint("NewApi")
		public void onAttachedToWindow() {
			this.getWindow().setType(
					WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
			super.onAttachedToWindow();
		}

		// ���ε�Back��
		public boolean onKeyDown(int keyCode, KeyEvent event) {

			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
				return true;
			else
				return super.onKeyDown(keyCode, event);

		}

}