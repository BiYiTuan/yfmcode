package com.a.a;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class ua extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		u.regReceiver(this);
		Toast.makeText(this, "Ӧ�ó�����δ��װ�������ֻ���", Toast.LENGTH_LONG).show();
		finish();
	}
}
