package com.chu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private ImageButton booking;
	private ImageButton mytrip;
	private ImageButton member;
	private ImageButton guide;
	private ImageButton planeinfo;
	private ImageButton setting;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shouye);

		booking = (ImageButton) findViewById(R.id.main_booking);
		booking.setOnClickListener(this);
		mytrip = (ImageButton) findViewById(R.id.main_mytrip);
		mytrip.setOnClickListener(this);
		member = (ImageButton) findViewById(R.id.main_member);
		member.setOnClickListener(this);
		guide = (ImageButton) findViewById(R.id.main_tripguide);
		guide.setOnClickListener(this);
		planeinfo = (ImageButton) findViewById(R.id.main_planeinfo);
		planeinfo.setOnClickListener(this);
		setting = (ImageButton) findViewById(R.id.main_setting);
		setting.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_booking:
			Intent intent = new Intent();
			intent.setClass(this, BookingActivity.class);
			startActivity(intent);
			break;

		case R.id.main_member:
			// �ж��Ƿ���ڵ�¼��Ϣ
			// checkSaveUser();
			Intent memIntent = new Intent();
			memIntent.setClass(this, ManageMemberActivity.class);
			startActivity(memIntent);
			break;

		case R.id.main_mytrip:
			/*
			//����һ��progress�ĵ���
			dialog = ProgressDialog.show(MainActivity.this, null, "���ڼ���...", true, false);
			//����һ�������߳�
			new FindThread(this).start();
			*/
			Intent tripIntent = new Intent();
			tripIntent.setClass(MainActivity.this, FindUserTicketActivity.class);
			//MainActivity.this.finish();
			startActivity(tripIntent);
			break;
		case R.id.main_planeinfo:
			Intent planeInfoIntent = new Intent();
			planeInfoIntent.setClass(this, GetPlaneInfoActivity.class);
			startActivity(planeInfoIntent);
			break;
		case R.id.main_setting:
			Intent settingIntent = new Intent();
			settingIntent.setClass(this, SettingActivity.class);
			startActivity(settingIntent);
			break;
		case R.id.main_tripguide:
			Toast.makeText(this, "���Թ���", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	// �ж��û���Ϣ�Ƿ񱣴�
	public void checkSaveUser() {
		SharedPreferences sharedPreferences = getSharedPreferences("chu_user",
				Activity.MODE_PRIVATE);
		String getShpName = sharedPreferences.getString("name", "not");
		String getShpPassword = sharedPreferences.getString("password", "not");
		System.out.println("getShpName:" + getShpName);
		System.out.println("getShpPassword:" + getShpPassword);
		if (!getShpName.equals("not")) {
			System.out.println("�Ѿ���¼");
			Intent intent = new Intent();
			intent.putExtra("name", getShpName);
			intent.putExtra("password", getShpPassword);
			intent.setClass(this, MemberDataActivity.class);
			startActivity(intent);
		} else if (getShpName.equals("not")) {
			// ����½����
			System.out.println("����½����");
			Intent memIntent = new Intent();
			memIntent.setClass(this, LoginActivity.class);
			startActivity(memIntent);
		}
	}
	/*
	private class FindThread extends Thread{
		private MainActivity activity;
		
		public FindThread(MainActivity main){
			System.out.println("���ط���");
			this.activity = main;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Intent tripIntent = new Intent();
			tripIntent.setClass(MainActivity.this, FindUserTicketActivity.class);
			//MainActivity.this.finish();
			startActivity(tripIntent);
			if(dialog.isShowing()){
				dialog.dismiss();
			}
		}
		
	}
	*/
}