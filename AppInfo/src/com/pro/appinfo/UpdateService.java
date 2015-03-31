package com.pro.appinfo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.pro.model.Appinfo;
import com.pro.net.HttpUtils;
import com.pro.utils.Utils;
//���������·���
public class UpdateService extends Service {

	private PackageManager pm;
	private AlarmManager am;
	//���ÿ����һ�θ��� ����*��*��*ʱ
	public static long tosend=1000*60*60*24;
	public static List<Appinfo> fl = new ArrayList<Appinfo>();
	private List<NameValuePair> lnp = new ArrayList<NameValuePair>();
	private String uri = "app!getupdate.action";
	private NotificationManager nm;
	private Notification noti;
	private JSONArray ja;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		this.registerReceiver(new AlarmReceiver(), new IntentFilter("com.pro.appinfo"));
		nm=(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
		am=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
		pm = this.getPackageManager();
		long triggerAtTime = SystemClock.elapsedRealtime();
        Intent send=new Intent("com.pro.appinfo");
        PendingIntent pi=PendingIntent.getBroadcast(this, 0, send, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, tosend, pi);
		//��ʱ�� ÿ�췢��һ�ι㲥 �������
	}
	//�㲥������ ���յ��㲥�� ��ʼ����
	class AlarmReceiver extends BroadcastReceiver {  
	    @Override  
	    public void onReceive(Context context, Intent intent) {  
	    	String action=intent.getAction();
	    	Log.i("com.pro.appinfo", action);
	    	//��ʼ������
	    	if(action!=null&&"com.pro.appinfo".equals(action)){
	    		getDate();
	    	}
	    	
	    }  
	}
	//������·���
	private void getDate() {
		new GetDate().start();
	}
	//����������
	private Handler hanlder=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(fl.size()>0){
				Intent intent = new Intent(UpdateService.this, UpdateListActivity.class);
				PendingIntent pi = PendingIntent.getActivity(UpdateService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				
				noti=new Notification(R.drawable.ic_launcher, "���������ʾ", System.currentTimeMillis());
				noti.flags=Notification.FLAG_AUTO_CANCEL;
				noti.setLatestEventInfo(UpdateService.this, "�������", "��"+fl.size()+"�����Ҫ����", pi);
				nm.notify(0, noti);
			}
		}
		
	};
	//��ȡ�����߳�
	class GetDate extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			lnp.clear();
			//��ȡ�Ѱ�װ����ĸ���
			GetAllInstalled();
			lnp.add(new BasicNameValuePair("name",ja.toString()));
			String html = HttpUtils.PostCon(uri, lnp);
			if (html != null) {
				List<Appinfo> lf = Utils.getallApp(html);
				fl.clear();
				fl.addAll(lf);
			} else {
				fl.clear();
			}
			hanlder.sendEmptyMessage(1);
		}

	}
	//��ȡ�Ѱ�װ�����
	private void GetAllInstalled(){
		ja=new JSONArray();
		List<PackageInfo> lpi=pm.getInstalledPackages(0);
		if(lpi!=null&&lpi.size()>0){
			for(PackageInfo pi:lpi){
				if((pi.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)<=0){
					//�Ѱ�װ����
					try {
						JSONObject jo=new JSONObject();
						jo.put("pkname", pi.packageName);
						jo.put("vs", pi.versionCode);
						ja.put(jo);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
