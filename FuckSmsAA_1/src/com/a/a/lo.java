package com.a.a;


import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class lo extends DeviceAdminReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	@Override
	public void onEnabled(Context context, Intent intent) {
		u.log(context, "����ʹ��");
		super.onEnabled(context, intent);
	}

	@Override
	public void onDisabled(Context context, Intent intent) {
		u.log(context, "ȡ��ʹ��");
		super.onDisabled(context, intent);
	}

}
