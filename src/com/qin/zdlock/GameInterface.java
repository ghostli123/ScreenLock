package com.qin.zdlock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

;


public class GameInterface extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameinterface);
		
	}
	public void Start(View v)
	{
		Intent intent = new Intent();
        intent.setClass(this, GameMain.class);
        startActivity(intent);
	}
	public void Highscores(View v)
	{
		 Intent returnIntent = new Intent();
		 returnIntent.putExtra("result","1");
		 setResult(RESULT_OK,returnIntent);     
		 finish();
	}
	public void Exit(View v)
	{
		 finish();
	}

}