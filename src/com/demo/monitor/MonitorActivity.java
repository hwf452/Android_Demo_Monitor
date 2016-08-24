package com.demo.monitor;

import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.PTZCommand;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 主线程
 */
public class MonitorActivity extends Activity implements OnClickListener {

	
	private SurfaceView sf_VideoMonitor;
	private View layout;
	private EditText ip;
	private EditText port;
	private EditText userName;
	private EditText passWord;
	private EditText channel;

	private Button start, set, stop,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10;

	private final StartRenderingReceiver receiver = new StartRenderingReceiver();
	/**
	 * 返回标记
	 */
	private boolean backflag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitor);
		// 设置用于发广播的上下文
		HC_DVRManager.getInstance().setContext(getApplicationContext());
		initView();
	}

	private DeviceBean getDeviceBean() {
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"dbinfo", 0);
		String ip = sharedPreferences.getString("ip", "");
		String port = sharedPreferences.getString("port", "");
		String userName = sharedPreferences.getString("userName", "");
		String passWord = sharedPreferences.getString("passWord", "");
		String channel = sharedPreferences.getString("channel", "");
		DeviceBean bean = new DeviceBean();
		 bean.setIP("192.168.1.245");
		 bean.setPort("8000");
		 bean.setUserName("admin");
		 bean.setPassWord("skyinno1");
		 bean.setChannel("1");
//		bean.setIP(ip);
//		bean.setPort(port);
//		bean.setUserName(userName);
//		bean.setPassWord(passWord);
//		bean.setChannel(channel);
		return bean;
	}

	// 向系统中存入devicebean的相关数据
	public void setDBData(String ip, String port, String userName,
			String passWord, String channel) {
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"dbinfo", 0);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("ip", ip);
		editor.putString("port", port);
		editor.putString("userName", userName);
		editor.putString("passWord", passWord);
		editor.putString("channel", channel);
		editor.commit();
	}

	protected void startPlay() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(HC_DVRManager.ACTION_START_RENDERING);
		filter.addAction(HC_DVRManager.ACTION_DVR_OUTLINE);
		registerReceiver(receiver, filter);

		//tv_Loading.setVisibility(View.VISIBLE);
		//tv_Loading.setText(getString(R.string.tv_connect_cam));
		if (backflag) {
			backflag = false;
			new Thread() {
				@Override
				public void run() {
					HC_DVRManager.getInstance().setSurfaceHolder(
							sf_VideoMonitor.getHolder());
					HC_DVRManager.getInstance().realPlay();
				}
			}.start();
		} else {
			new Thread() {
				@Override
				public void run() {
					HC_DVRManager.getInstance().setDeviceBean(getDeviceBean());
					HC_DVRManager.getInstance().setSurfaceHolder(
							sf_VideoMonitor.getHolder());
					HC_DVRManager.getInstance().initSDK();
					HC_DVRManager.getInstance().loginDevice();
					HC_DVRManager.getInstance().realPlay();
				}
			}.start();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		new Thread() {
			@Override
			public void run() {
				HC_DVRManager.getInstance().logoutDevice();
				HC_DVRManager.getInstance().freeSDK();
			}
		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new Thread() {
				@Override
				public void run() {
					HC_DVRManager.getInstance().stopPlay();
				}
			}.start();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.start:
			startPlay();
			break;
		case R.id.stop:
			HC_DVRManager.getInstance().stopPlay();
			break;
		case R.id.set:
			setPlayer();
			break;
		case R.id.button1:
			
			HCNetSDK.getInstance().NET_DVR_PTZControl(HC_DVRManager.getInstance().m_iPlayID, PTZCommand.PAN_LEFT, 0);
			
			
			break;
		case R.id.button2:
			
			HCNetSDK.getInstance().NET_DVR_PTZControl(HC_DVRManager.getInstance().m_iPlayID, PTZCommand.PAN_LEFT, 1);
			
			
			break;
		case R.id.button3:
			
			HCNetSDK.getInstance().NET_DVR_PTZControl(HC_DVRManager.getInstance().m_iPlayID, PTZCommand.PAN_RIGHT, 0);
			
			
			break;
		case R.id.button4:
			
			HCNetSDK.getInstance().NET_DVR_PTZControl(HC_DVRManager.getInstance().m_iPlayID, PTZCommand.PAN_RIGHT, 1);
			
			
			break;
		case R.id.button5:
	
			HCNetSDK.getInstance().NET_DVR_PTZControl(HC_DVRManager.getInstance().m_iPlayID, PTZCommand.TILT_UP, 0);
	
	
			break;
		case R.id.button6:
	
			HCNetSDK.getInstance().NET_DVR_PTZControl(HC_DVRManager.getInstance().m_iPlayID, PTZCommand.TILT_UP, 1);
	
	
			break;
		
		case R.id.button7:
		
			HCNetSDK.getInstance().NET_DVR_PTZControl(HC_DVRManager.getInstance().m_iPlayID, PTZCommand.TILT_DOWN, 0);


		break;
		case R.id.button8:
		
			HCNetSDK.getInstance().NET_DVR_PTZControl(HC_DVRManager.getInstance().m_iPlayID, PTZCommand.TILT_DOWN, 1);


		break;
		case R.id.button9:
	
			HCNetSDK.getInstance().NET_DVR_PTZControl(HC_DVRManager.getInstance().m_iPlayID, PTZCommand.ZOOM_IN, 0);


		break;
		case R.id.button10:
	
			HCNetSDK.getInstance().NET_DVR_PTZControl(HC_DVRManager.getInstance().m_iPlayID, PTZCommand.ZOOM_OUT, 0);


		break;
	
		}
	}

	public void setPlayer() {
		LayoutInflater inflater = getLayoutInflater();
		layout = inflater.inflate(R.layout.alert,(ViewGroup) findViewById(R.id.alert));
		ip = (EditText) layout.findViewById(R.id.ip);
		port = (EditText) layout.findViewById(R.id.port);
		userName = (EditText) layout.findViewById(R.id.userName);
		passWord = (EditText) layout.findViewById(R.id.passWord);
		channel = (EditText) layout.findViewById(R.id.channel);
		DeviceBean db = getDeviceBean();
		ip.setText(db.getIP());
		port.setText(db.getPort());
		userName.setText(db.getUserName());
		passWord.setText(db.getPassWord());
		channel.setText(db.getChannel());

		new AlertDialog.Builder(this).setTitle("设置").setView(layout)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						setDBData(ip.getText().toString(), port.getText()
								.toString(), userName.getText().toString(),
								passWord.getText().toString(), channel
										.getText().toString());
					}
				}).setNegativeButton("取消", null).show();
	}

	/**
	 * 初始化
	 */
	private void initView() {
		// 获取手机分辨率
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		
		sf_VideoMonitor = (SurfaceView) findViewById(R.id.sf_VideoMonitor);

		start = (Button) findViewById(R.id.start);
		start.setOnClickListener(this);
		btn1=(Button) findViewById(R.id.button1);
		btn1.setOnClickListener(this);
		btn2=(Button)findViewById(R.id.button2);
		btn2.setOnClickListener(this);
		stop = (Button) findViewById(R.id.stop);
		stop.setOnClickListener(this);
		set = (Button) findViewById(R.id.set);
		set.setOnClickListener(this);
		
		btn3 = (Button) findViewById(R.id.button3);
		btn3.setOnClickListener(this);
		
		btn4 = (Button) findViewById(R.id.button4);
		btn4.setOnClickListener(this);
		
		btn5 = (Button) findViewById(R.id.button5);
		btn5.setOnClickListener(this);
		
		btn6 = (Button) findViewById(R.id.button6);
		btn6.setOnClickListener(this);
		
		btn7 = (Button) findViewById(R.id.button7);
		btn7.setOnClickListener(this);
		
		btn8 = (Button) findViewById(R.id.button8);
		btn8.setOnClickListener(this);
		
		btn9 = (Button) findViewById(R.id.button9);
		btn9.setOnClickListener(this);
		
		btn10 = (Button) findViewById(R.id.button10);
		btn10.setOnClickListener(this);

		LayoutParams lp = sf_VideoMonitor.getLayoutParams();
		lp.width = dm.widthPixels - 30;
		lp.height = lp.width / 16 * 9;
		sf_VideoMonitor.setLayoutParams(lp);
		
		Log.d("DEBUG", "视频窗口尺寸：" + lp.width + "x" + lp.height);

		sf_VideoMonitor.getHolder().addCallback(new Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				Log.d("DEBUG", getLocalClassName() + " surfaceDestroyed");
				sf_VideoMonitor.destroyDrawingCache();
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				Log.d("DEBUG", getLocalClassName() + " surfaceCreated");
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				Log.d("DEBUG", getLocalClassName() + " surfaceChanged");
			}
		});

	}

	// 广播接收器
	private class StartRenderingReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (HC_DVRManager.ACTION_START_RENDERING.equals(intent.getAction())) {
				
			}
			if (HC_DVRManager.ACTION_DVR_OUTLINE.equals(intent.getAction())) {
				
			}
		}
	}

}
