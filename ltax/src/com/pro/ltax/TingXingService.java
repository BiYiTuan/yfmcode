package com.pro.ltax;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.pro.db.DbUtils;
/**
 * ���ѷ���
 * @author lenovo
 *
 */
public class TingXingService extends Service {

	private DbUtils db;


	private AlarmManager am;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		db=new DbUtils(getApplicationContext());
		List<Date> ld=db.gettixing();
		Log.i("servicefuwu", "ʱ��"+ld.size());
		am=(AlarmManager)super.getSystemService(Context.ALARM_SERVICE);
		Intent intent=new Intent("com.pro.ltax");
		PendingIntent operation=PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		//ѭ�����붨ʱʱ��
		for(int i=0;ld!=null&&i<ld.size();i++){
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(ld.get(i));
			calendar.set(Calendar.HOUR_OF_DAY, 9);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			if(calendar.getTime().after(new Date())){
				am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), operation);
				Log.i("servicefuwu", ""+calendar.getTime());
			}
			
		}
		
		Log.i("servicefuwu", "��������");
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
