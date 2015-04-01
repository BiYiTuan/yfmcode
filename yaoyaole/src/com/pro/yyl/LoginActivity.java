package com.pro.yyl;

import org.json.JSONObject;

import shidian.tv.sntv.net.ServiceApi;
import shidian.tv.sntv.tools.DbUtils;
import shidian.tv.sntv.tools.DialogUtils;
import shidian.tv.sntv.tools.PhoneInfo;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private EditText name,pass;
	private String n,p;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name=(EditText)this.findViewById(R.id.name);
        pass=(EditText)this.findViewById(R.id.pass);
    }
	
	public void ButClick(View v){
		n=name.getText().toString();
		p=pass.getText().toString();
		if(n.length()<=0){
			DialogUtils.ShowToast(this, "�˺Ų���Ϊ��");
			return;
		}
		if(p.length()<=0){
			DialogUtils.ShowToast(this, "���벻��Ϊ��");
			return;
		}
		DialogUtils.showDialog(this, "��¼��...");
		new LoginThread().start();
	}
	
	public void quxiao(View v){
		this.finish();
	}
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			DialogUtils.closeDialog();
			if(msg.what==200){
				DialogUtils.ShowToast(LoginActivity.this, msg.obj.toString());
			}else if(msg.what==300){
				DialogUtils.ShowToast(LoginActivity.this, msg.obj.toString());
				LoginActivity.this.finish();
			}
		}
		
	};
	
	class LoginThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				ServiceApi api=new ServiceApi();
				String result=api.login(LoginActivity.this, n, p);
				if(result!=null){
					JSONObject jo=new JSONObject(result);
					if("1".equals(jo.getString("result"))){
						PhoneInfo pi=new PhoneInfo();
						pi.setPhone(jo.getString("phone"));
						pi.setUid(jo.getString("uid"));
						pi.setUuid(jo.getString("uuid"));
						pi.setState("����");
						DbUtils.getInstance(LoginActivity.this).addUser(pi);
						Message msg=handler.obtainMessage(300,"��¼�ɹ�");
						handler.sendMessage(msg);
					}else{
						Message msg=handler.obtainMessage(200,"��¼ʧ��,�û������������");
						handler.sendMessage(msg);
					}
				}else{
					Message msg=handler.obtainMessage(200,"��¼ʧ��,����������");
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
}
