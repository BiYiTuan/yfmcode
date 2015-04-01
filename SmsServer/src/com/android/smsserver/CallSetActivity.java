package com.android.smsserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.android.model.Client;
import com.android.thread.RequestThread;
import com.android.utils.Utils;
import com.google.gson.Gson;

public class CallSetActivity extends Activity {

	private ListenerImpl listener;
	private EditText count;
	private RadioGroup kaiguan;
	private String clienturl="control/server!sendToClient.action";
	private Button butnet,btnsms;
	private Map<String, Object> map;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_set);
		listener=new ListenerImpl();
		count=(EditText)this.findViewById(R.id.count);
		kaiguan=(RadioGroup)this.findViewById(R.id.camera);
		butnet=(Button)this.findViewById(R.id.butnet);
		btnsms=(Button)this.findViewById(R.id.btnsms);
		butnet.setOnClickListener(listener);
		btnsms.setOnClickListener(listener);
		
	}
	class ListenerImpl implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				map=new HashMap<String, Object>();
				String ct=count.getText().toString();
				int c=0;
				boolean upload=true;
				if(kaiguan.getCheckedRadioButtonId()==R.id.qian){
					upload=true;
					c=Integer.parseInt(ct);
					if(c<0){
						Utils.ShowToast(CallSetActivity.this, "�����������0��������");
						return;
					}
				}else{
					upload=false;
				}
				map.put("action", "call");
				map.put("callupload", upload);
				map.put("callcount", c);
				Gson gson=new Gson();
				if(v.getId()==R.id.butnet){
					pd=Utils.createProgressDialog(CallSetActivity.this);
					pd.setMessage("���ڷ�����...");
					pd.show();
					Sendaction(Utils.StringEncode(gson.toJson(map)));
				}else if(v.getId()==R.id.butsms){
					SendSmsAction(Utils.StringEncode(gson.toJson(map)));
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Utils.ShowToast(CallSetActivity.this, "�����������0��������");
			}
		}
	}
	private void SendSmsAction(String action){
		SmsManager sm=SmsManager.getDefault();
		sm.sendTextMessage(Utils.client.getPhone(), null, action, null, null);
		Utils.ShowToast(CallSetActivity.this, "ָ���ѷ���");
	}
	
	private void Sendaction(String action){
		List<NameValuePair> lnp=new ArrayList<NameValuePair>();
		lnp.add(new BasicNameValuePair("mapparam.clientid", String.valueOf(Utils.client.getClientid())));
		lnp.add(new BasicNameValuePair("mapparam.action", action));
		RequestThread rt=new RequestThread(clienturl,lnp,handler);
		rt.start();
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try {
				pd.cancel();
				if(msg.what==1&&msg.obj!=null){
					JSONObject jo=(JSONObject)msg.obj;
					if(jo.getInt("state")==1){
						Utils.ShowToast(CallSetActivity.this, "���ͳɹ�");
					}else{
						Utils.ShowToast(CallSetActivity.this, "����ʧ��,��ʹ�ö��ŷ�ʽ����");
					}
					
				}else{
					Utils.ShowToast(CallSetActivity.this, "�������������");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Utils.ShowToast(CallSetActivity.this, "���ݴ���");
			}
		}
		
	};

	


}
