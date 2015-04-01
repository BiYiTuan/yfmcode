package com.pro.ltax;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class LtaxBroadcastReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i("servicefuwu", intent.getAction());
		sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
		if("com.pro.ltax".equals(intent.getAction())&&sp.getBoolean("flag", true)){
				//������
				Intent dialog=new Intent(context,TingXingDialog.class);
				dialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(dialog);
		}else{
			//�������ѷ���
			Intent service=new Intent(context,TingXingService.class);
			service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(service);
		}
	}

}
