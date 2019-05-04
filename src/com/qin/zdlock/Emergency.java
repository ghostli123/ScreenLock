package com.qin.zdlock;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

;


public class Emergency extends Activity {
	private EditText et;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);
		et = (EditText)findViewById(R.id.phonenumber);
	}

	public void call(View v)
	{
		String inputStr = et.getText().toString();
		if(inputStr.trim().length()!=0)
		{
			if(inputStr.trim().equals("911"))
			{
				Intent phoneIntent = new Intent("android.intent.action.CALL",Uri.parse("tel:" + inputStr));
				startActivity(phoneIntent);
			}
			else
			{
				Toast.makeText(this, "emergency call only", Toast.LENGTH_LONG).show();
			}
		}
		else{
			Toast.makeText(this, "please input an emergency call", Toast.LENGTH_LONG).show();
		}
	}
	
	public void Emergency(View v) {
		Intent intent = new Intent();
		intent.setClass(this, Tools.class);
		startActivity(intent);
	}

	public void Skip(View v) {
		finish();
	}

}
