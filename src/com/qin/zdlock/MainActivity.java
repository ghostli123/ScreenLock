package com.qin.zdlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class MainActivity extends Activity {

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
	private TextView textQuestion = null;
	private TextView textView1 = null;
	private TextView textView2 = null;
	private TextView textView3 = null;
	private TextView textView4 = null;

	private Button tools = null;

	private static Random rand = new Random();

	private Context mContext = null;

	public static int MSG_LOCK_SUCESS = 1;
	public static int MSG_LOCK_FAILURE = 0;

    private Basic_Information basic_information;
    private String result;
    public String challenge;
    public static String challenge_id;
    public static String challenge_answer;
    public static String rate = "";
    public static String feedback = "";

    private EditText et = null;



    private boolean service_started = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = MainActivity.this;

        if (service_started == false) {
            Intent service = new Intent(this, testService.class);
            service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startService(service);
            Log.v("startAfterBoot", "LALALALALA开机自动服务自动启动.....");
        }

        if(testService.networkAccurate == false)
        {
            this.finish();
        }

        basic_information = new Basic_Information();
        basic_information.initialize(this);




		/* ����ȫ�����ޱ��� */
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);
		initViews();

		startService(new Intent(MainActivity.this, ZdLockService.class));


        RadioGroup group = (RadioGroup)this.findViewById(R.id.radioGroup);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {

                int radioButtonId = arg0.getCheckedRadioButtonId();

                        RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                        rate = ""+rb.getText();

                }
            });

        et = (EditText) findViewById(R.id.editText);

        et.setOnClickListener(new OnClickListener() {
             public void onClick(View v) {
             if(et.getText().toString().equals("any feedback on this challenge is welcome here"))
             {
                 et.setText("");

             }
                 Log.v("echo", "clicked");
             }
        });


        new Thread(new Runnable() {
            public void run() {
                while(true) {
                    //String topActivity = basic_information.getTopActivity();
                    //Log.v("new", topActivity);

                    try {
                        Thread.sleep(60000*300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //writeBasicInformationToLocal();
                    mHandler.sendEmptyMessage(100);



                }
            }
        }).start();

		sliderLayout.setMainHandler(mHandler);
		sliderLayout2.setMainHandler(mHandler);
		sliderLayout3.setMainHandler(mHandler);
		sliderLayout4.setMainHandler(mHandler);



        //Log.v("challenge", "appear");
		resetView();

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();


	}

	private void initViews() {
		sliderLayout = (SliderRelativeLayout) findViewById(R.id.slider_layout);
		sliderLayout2 = (SliderRelativeLayout) findViewById(R.id.slider_layout2);
		sliderLayout3 = (SliderRelativeLayout) findViewById(R.id.slider_layout3);
		sliderLayout4 = (SliderRelativeLayout) findViewById(R.id.slider_layout4);

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
		textQuestion = (TextView) findViewById(R.id.text_question);
		textView1 = (TextView) findViewById(R.id.text_view1);
		textView2 = (TextView) findViewById(R.id.text_view2);
		textView3 = (TextView) findViewById(R.id.text_view3);
		textView4 = (TextView) findViewById(R.id.text_view4);
		tools = (Button) findViewById(R.id.tools);
		
		textQuestion.setClickable(true);
		textQuestion.setFocusable(true);
		textQuestion.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				resetView();
			}
		});

		sliderLayout.setAnswer(textView1.getText().toString());
		sliderLayout2.setAnswer(textView2.getText().toString());
		sliderLayout3.setAnswer(textView3.getText().toString());
		sliderLayout4.setAnswer(textView4.getText().toString());

	}

	@Override
	protected void onResume() {
		super.onResume();
        MobclickAgent.onResume(this);
        if(testService.networkAccurate == false)
        {
            this.finish();
        }
		// ���ö���
		mHandler.postDelayed(AnimationDrawableTask, 300); // ��ʼ���ƶ���
	}

	@Override
	protected void onPause() {
		super.onPause();
		animArrowDrawable.stop();
        MobclickAgent.onResume(this);
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
                updateToDataBase_correctAnswer();
                SliderRelativeLayout.wrongTimes = 0;
				finish(); // �����ɹ�ʱ���������ǵ�Activity����
			if (MSG_LOCK_FAILURE == msg.what)
				resetView(); // �����ɹ�ʱ���������ǵ�Activity����

            if(msg.what == 100)
            {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, OtherActivity.class);
                // startActivity(intent);
                startActivityForResult(intent, 1);
            }
		}
	};


    public void updateToDataBase_correctAnswer()
    {



        Log.v("echo", "rate: "+MainActivity.rate);

        String wrongTimes = Integer.toString(SliderRelativeLayout.wrongTimes);
        String correctTimes = Integer.toString(1);
        feedback = ""+et.getText();

        String challenge_id = MainActivity.challenge_id;
        Log.v("echo", "original id: "+challenge_id);
        HttpPost request = new HttpPost("http://"
                + Ip_addr.server_ip_address
                + "/DyAuthen/correctAnswer.php");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String result;

        if(et.getText().toString().equals("any feedback on this challenge is welcome here"))
        {
            feedback="";
        }

        params.add(new BasicNameValuePair("challenge_id", challenge_id));
        params.add(new BasicNameValuePair("wrongTimes", wrongTimes));
        params.add(new BasicNameValuePair("correctTimes", correctTimes));
        params.add(new BasicNameValuePair("rate", rate));
        params.add(new BasicNameValuePair("feedback", feedback));

        //params.add(new BasicNameValuePair("activity_id", "21"));

        //Log.i("Yang", answer);
        try {
            Log.i("echo_correctAnswer", "here we go");
            request.setEntity(new UrlEncodedFormEntity(params,
                    HTTP.UTF_8));
            HttpResponse response = new DefaultHttpClient()
                    .execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response
                        .getEntity());

                Log.i("echo_correctAnswer", result);
            }
            else
            {
                Log.i("echo_correctAnswer", "here we go5");
                Log.i("echo_correctAnswer", "something wrong");
            }
        } catch (Exception e) {

            Log.i("ghostli123456", e.getMessage().toString());
            e.printStackTrace();
        }

    }

	private void resetView() {

        //Log.v("challenge", );
        challenge = fetchChallenge();
        if(challenge == null || challenge.length()<5)
        {
            textQuestion.setText("no questions in the database");
            textView1.setText("no questions in the database");
            textView2.setText("no questions in the database");
            textView3.setText("no questions in the database");
            textView4.setText("no questions in the database");
            this.finish();
            return;
        }

        Log.v("challenge", challenge);

        String[] challenge_info = challenge.split("\\|");
        String challenge_id = challenge_info[1];
        challenge_id = challenge_id.trim().substring(1,challenge_id.length()-1);

        MainActivity.challenge_id = challenge_id;
        String challenge_content = challenge_info[3];
        challenge_content = challenge_content.trim().substring(1,challenge_content.length()-1);
        String challenge_answer = challenge_info[4];
        challenge_answer = challenge_answer.trim().substring(1,challenge_answer.length()-1);
        String challenge_potentialAnswer = challenge_info[5];
        challenge_potentialAnswer = challenge_potentialAnswer.trim().substring(1,challenge_potentialAnswer.length()-2);

        Log.v("challenge", challenge_id+"|"+challenge_content+"|"+challenge_answer+"|"+challenge_potentialAnswer);

        String challenge_potentialAnswer_one = challenge_potentialAnswer.split(":")[0];
        String challenge_potentialAnswer_two = challenge_potentialAnswer.split(":")[1];
        String challenge_potentialAnswer_three = challenge_potentialAnswer.split(":")[2];
        MainActivity.challenge_answer = challenge_answer;

        String[] answers = new String[4];

        String[] answer_database = { challenge_potentialAnswer_one, challenge_potentialAnswer_two, challenge_potentialAnswer_three, challenge_answer};
        List list = Arrays.asList(answer_database);
        Collections.shuffle(list);
        for (int i = 0; i < 4; i++) {
            //String temp = answer_database[rand.nextInt(4)];

            answers[i] = (String) list.get(i);
        }

		//String question = getQuestion();
		//textQuestion.setText(question);
        String question = challenge_content;
        textQuestion.setText(question);
		//String[] answers = getAnswers();
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

    public String fetchChallenge()
    {
        HttpPost request = new HttpPost("http://"
                + Ip_addr.server_ip_address
                //+ "/bbb/storeMobileInfomation.php");
                //+ "/DyAuthen/fetchChallenge.php");
                + "/DyAuthen/fetchChallenge.php");

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        // data constructor
        // IMEI OK
        //String answer = editText1.getText().toString();
        //Log.i("Other", answer);

        String phone_info = basic_information.getPhoneInformation();
        Log.v("new", "phone_info: "+ phone_info);
        //return phone_info;

        String imei = phone_info.split("\\|")[0];

        params.add(new BasicNameValuePair("imei", imei));

        //params.add(new BasicNameValuePair("activity_id", "21"));

        //Log.i("Yang", answer);
        try {
            Log.i("echo_challenge", "here we go");
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
                Log.i("echo_challenge", "challenge: "+result);
                challenge = new String(result);
            }
            else
            {
                Log.i("echo_challenge", "here we go5");
                Log.i("echo_challenge", "something wrong");
            }
        } catch (Exception e) {

            // Toast.makeText(this, e.getMessage().toString(),
            // Toast.LENGTH_LONG).show();
            Log.i("ghostli123456", e.getMessage().toString());
            e.printStackTrace();
        }
        //this.finish();
        Log.i("echo_challenge", "?"+result);
        return result;
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

	public void jump(View v) {
		Intent intent = new Intent();
		intent.setClass(this, Tools.class);
		// startActivity(intent);
		startActivityForResult(intent, 1);
	}


    public void feedback(View v) {
        Intent intent = new Intent();
        intent.setClass(this, Tools.class);
        // startActivity(intent);
        startActivityForResult(intent, 1);
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