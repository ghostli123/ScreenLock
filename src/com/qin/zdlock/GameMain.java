package com.qin.zdlock;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

;


public class GameMain extends Activity {

	private static String TAG = "QINZDLOCK";

	private SliderRelativeLayout sliderLayout = null;
	private SliderRelativeLayout sliderLayout2 = null;
	private SliderRelativeLayout sliderLayout3 = null;
	private SliderRelativeLayout sliderLayout4 = null;
	private ImageView imgView_getup_arrow; // ����ͼƬ
	private ImageView imgView_getup_arrow2; // ����ͼƬ
	private ImageView imgView_getup_arrow3; // ����ͼƬ
	private ImageView imgView_getup_arrow4; // ����ͼƬ
	private AnimationDrawable animArrowDrawable = null;
	private AnimationDrawable animArrowDrawable2 = null;
	private AnimationDrawable animArrowDrawable3 = null;
	private AnimationDrawable animArrowDrawable4 = null;
	private TextView score = null;
	private TextView textQuestion = null;
	private TextView textView1 = null;
	private TextView textView2 = null;
	private TextView textView3 = null;
	private TextView textView4 = null;

	private Button tools = null;
	private TextView correct=null;
	private TextView wrong=null;
	private static Random rand = new Random();

	private Context mContext = null;

	public static int MSG_LOCK_SUCESS = 1;
	public static int MSG_LOCK_FAILURE = 0;
	
	private static int scores=0;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = GameMain.this;
		/* ����ȫ�����ޱ��� */
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.game);
		initViews();

		startService(new Intent(GameMain.this, ZdLockService.class));

		sliderLayout.setMainHandler(mHandler);
		sliderLayout2.setMainHandler(mHandler);
		sliderLayout3.setMainHandler(mHandler);
		sliderLayout4.setMainHandler(mHandler);
		resetView();
	}

	private void initViews() {
		sliderLayout = (SliderRelativeLayout) findViewById(R.id.slider_layout);
		sliderLayout2 = (SliderRelativeLayout) findViewById(R.id.slider_layout2);
		sliderLayout3 = (SliderRelativeLayout) findViewById(R.id.slider_layout3);
		sliderLayout4 = (SliderRelativeLayout) findViewById(R.id.slider_layout4);
		// ��ö���������ʼת��
		imgView_getup_arrow = (ImageView) findViewById(R.id.getup_arrow);
		imgView_getup_arrow2 = (ImageView) findViewById(R.id.getup_arrow2);
		imgView_getup_arrow3 = (ImageView) findViewById(R.id.getup_arrow3);
		imgView_getup_arrow4 = (ImageView) findViewById(R.id.getup_arrow4);
		animArrowDrawable = (AnimationDrawable) imgView_getup_arrow
				.getBackground();
		animArrowDrawable2 = (AnimationDrawable) imgView_getup_arrow2
				.getBackground();
		animArrowDrawable3 = (AnimationDrawable) imgView_getup_arrow3
				.getBackground();
		animArrowDrawable4 = (AnimationDrawable) imgView_getup_arrow4
				.getBackground();
		score=(TextView) findViewById(R.id.socre);
		textQuestion = (TextView) findViewById(R.id.text_question);
		textView1 = (TextView) findViewById(R.id.text_view1);
		textView2 = (TextView) findViewById(R.id.text_view2);
		textView3 = (TextView) findViewById(R.id.text_view3);
		textView4 = (TextView) findViewById(R.id.text_view4);
		tools = (Button) findViewById(R.id.tools);
		
		sliderLayout.setAnswer(textView1.getText().toString());
		sliderLayout2.setAnswer(textView2.getText().toString());
		sliderLayout3.setAnswer(textView3.getText().toString());
		sliderLayout4.setAnswer(textView4.getText().toString());

	}

	@Override
	protected void onResume() {
		super.onResume();
		// ���ö���
		mHandler.postDelayed(AnimationDrawableTask, 300); // ��ʼ���ƶ���
	}

	@Override
	protected void onPause() {
		super.onPause();
		animArrowDrawable.stop();
	}

	protected void onDestory() {
		super.onDestroy();
	}

	// ͨ����ʱ���Ƶ�ǰ����bitmap��λ�����
	private Runnable AnimationDrawableTask = new Runnable() {

		public void run() {
			animArrowDrawable.start();
			animArrowDrawable2.start();
			animArrowDrawable3.start();
			animArrowDrawable4.start();
			mHandler.postDelayed(AnimationDrawableTask, 300);
		}
	};

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

			Log.i(TAG, "handleMessage :  #### ");

			if (MSG_LOCK_SUCESS == msg.what)
			{
				//finish(); // �����ɹ�ʱ���������ǵ�Activity����
				//plus 10 scores
				//correct.setVisibility(View.VISIBLE);
				scores+=10;
				tools.setText("Correct, plus 10!! go to next challenge");
				tools.setVisibility(View.VISIBLE);
			}
			if (MSG_LOCK_FAILURE == msg.what)
			{
				//wrong.setVisibility(View.VISIBLE);
				scores-=5;
				tools.setText("Wrong, minus 5!! go to next challenge");
				tools.setVisibility(View.VISIBLE);
				//resetView(); // �����ɹ�ʱ���������ǵ�Activity����
			}
			score.setText("Stage one, Score "+scores);
				
				
		}
	};

	private void resetView() {
		String question = getQuestion();
		textQuestion.setText(question);
		String[] answers = getAnswers();
		textView1.setText(answers[0]);
		textView2.setText(answers[1]);
		textView3.setText(answers[2]);
		textView4.setText(answers[3]);
		sliderLayout.setAnswer(answers[0]);
		sliderLayout2.setAnswer(answers[1]);
		sliderLayout3.setAnswer(answers[2]);
		sliderLayout4.setAnswer(answers[3]);
		Log.d("Yang", "AAAAA " + times + " " + answers[0]);
		times = times + 1;
	}

	private static int times;

	private String getQuestion() {
		times = rand.nextInt(9);
		if (times % 9 == 0) {
			return "What movie were you watching by using QQmovie yesterday night?";
		} else if (times % 9 == 1) {
			return "Who were you talking with by using Skype this morning?";
		} else if (times % 9 == 2) {
			return "Which game were you playing this afternoon?";
		} else if (times % 9 == 3) {
			return "How long is the film you watch by using QQmovie yesterday evening?";
		} else if (times % 9 == 4) {
			return "Which character do you like most by using QQmovie this morning?";
		} else if (times % 9 == 5) {
			return "Whose state do you reply by using Renren yesterday afternoon?";
		} else if (times % 9 == 6) {
			return "What's the highest score you get by playing TampleRun yesterday evening?";
		} else if (times % 9 == 7) {
			return "What's the name of the hero in the film by using QQmovie yesterday afternoon?";
		} else {
			return "where are you this morning?";
		}
	}

	private String[] getAnswers() {
		if (times % 9 == 0) {
			String[] database = { "Avata", "Avengers", "Dark Knight",
					"Titanic", "Drogen Story", "Taiji 4", "Steel Kings", "AI",
					"Burger Guy", "Shao Ling", "Spring Story" };
			String[] answers = new String[4];
			boolean flag = false;
			for (int i = 0; i < 4; i++) {
				String temp = database[rand.nextInt(9)];
				answers[i] = temp;
				// for(int j=i;j>0;j--)
				// {
				// if(temp.equals(answers[j]))
				// {
				// flag=true;
				// answers[i]="Spring Story";
				// }
				// }
				// if(flag==false)
				// {
				// answers[i]=temp;
				// flag=false;
				// }
			}
			answers[2] = "Titanic";
			return answers;
		} else if (times % 9 == 1) {
			String[] database = { "Lisa", "Kelly", "Leehom", "Larus", "Telly",
					"Polly", "Wudomi", "Qianxi", "Koe", "Xing" };
			String[] answers = new String[4];
			boolean flag = false;
			for (int i = 0; i < 4; i++) {
				String temp = database[rand.nextInt(9)];
				answers[i] = temp;
				// for(int j=i;j>0;j--)
				// {
				// if(temp.equals(answers[j]))
				// {
				// flag=true;
				// answers[i]="Xing";
				// }
				// }
				// if(flag==false)
				// {
				// answers[i]=temp;
				// flag=false;
				// }
			}
			answers[3] = "Kelly";
			return answers;
		} else if (times % 9 == 2) {
			String[] database = { "Angry Birds", "Fruit Ninja",
					"Plants vs. Zombies", "Asphalt 7", "Temple Run",
					"Kaueskul", "Chess", "Burger King", "Soccer Winner",
					"Fisher" };
			String[] answers = new String[4];
			boolean flag = false;
			for (int i = 0; i < 4; i++) {
				String temp = database[rand.nextInt(9)];
				answers[i] = temp;
				// for(int j=i;j>0;j--)
				// {
				// if(temp.equals(answers[j]))
				// {
				// flag=true;
				// answers[i]="Fisher";
				// }
				// }
				// if(flag==false)
				// {
				// answers[i]=temp;
				// flag=false;
				// }
			}
			answers[3] = "Angry Birds";
			return answers;
		} else if (times % 9 == 3) {
			int answer = 254;
			int range = answer / 2;
			String[] answers = new String[4];
			int interval;
			for (int i = 0; i < 4; i++) {
				interval = rand.nextInt(range);
				if (interval % 2 == 0) {
					int value = answer + interval;
					answers[i] = "" + value;
				} else {
					int value = answer - interval;
					answers[i] = "" + value;
				}
			}
			answers[1] = "231";
			return answers;
		} else if (times % 9 == 4) {
			String[] database = { "Harry Potter", "Yoda", "Shaggy", "Bella",
					"Timmy", "Jarry", "Larus", "Dink", "Yahama", "Sakula",
					"Tims" };
			String[] answers = new String[4];
			boolean flag = false;
			for (int i = 0; i < 4; i++) {
				String temp = database[rand.nextInt(9)];
				answers[i] = temp;
				// for(int j=i;j>0;j--)
				// {
				// if(temp.equals(answers[j]))
				// {
				// flag=true;
				// answers[i]="Tims";
				// }
				// }
				// if(flag==false)
				// {
				// answers[i]=temp;
				// flag=false;
				// }
			}
			answers[0] = "Jackson";
			return answers;
		} else if (times % 9 == 5) {
			String[] database = { "Lisa", "Kelly", "Leehom", "Larus", "Telly",
					"Polly", "Wudomi", "Qianxi", "Koe", "Xing" };
			String[] answers = new String[4];
			boolean flag = false;
			for (int i = 0; i < 4; i++) {
				String temp = database[rand.nextInt(9)];
				answers[i] = temp;
				// for(int j=i;j>0;j--)
				// {
				// if(temp.equals(answers[j]))
				// {
				// flag=true;
				// answers[i]="Xing";
				// }
				// }
				// if(flag==false)
				// {
				// answers[i]=temp;
				// flag=false;
				// }
			}
			answers[0] = "Kelly";
			return answers;
		} else if (times % 9 == 6) {
			int answer = 254;
			int range = answer / 2;
			String[] answers = new String[4];
			int interval;
			for (int i = 0; i < 4; i++) {
				interval = rand.nextInt(range);
				if (interval % 2 == 0) {
					int value = answer + interval;
					answers[i] = "" + value;
				} else {
					int value = answer - interval;
					answers[i] = "" + value;
				}
			}
			answers[0] = "86";
			return answers;
		} else if (times % 9 == 7) {
			String[] database = { "Jefferson Smith", "Spartacus", "Luke",
					"Jackson", "Jay", "Kelly", "Ekao Peter", "Suie", "Lsehile",
					"Standen" };
			String[] answers = new String[4];
			boolean flag = false;
			for (int i = 0; i < 4; i++) {
				String temp = database[rand.nextInt(9)];
				answers[i] = temp;
				// for(int j=i;j>0;j--)
				// {
				// if(temp.equals(answers[j]))
				// {
				// flag=true;
				// answers[i]="Standen";
				// }
				// }
				// if(flag==false)
				// {
				// answers[i]=temp;
				// flag=false;
				// }
			}
			answers[3] = "Bella";
			return answers;
		} else {
			String[] database = { "restaurant", "home", "book store", "office",
					"factory", "CMU", "Kaller", "Akerman", "CVS", "SSN office" };
			String[] answers = new String[4];
			boolean flag = false;
			for (int i = 0; i < 4; i++) {
				String temp = database[rand.nextInt(9)];
				answers[i] = temp;
				// for(int j=i;j>0;j--)
				// {
				// if(temp.equals(answers[j]))
				// {
				// flag=true;
				// answers[i]="Standen";
				// }
				// }
				// if(flag==false)
				// {
				// answers[i]=temp;
				// flag=false;
				// }
			}
			answers[1] = "restaurant";
			return answers;
		}
	}


	public void jump(View v) {
		tools.setVisibility(View.GONE);
		resetView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("KK","onActivityResult");
		finish();
		if (requestCode == 1) {

			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("result");
				finish();
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}// onActivityResult

}