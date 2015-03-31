package com.pro.appinfo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.pro.adapter.FLAdapter;
import com.pro.model.FenLei;
import com.pro.net.HttpUtils;
import com.pro.utils.Utils;
//�����б���ʾ
public class FenLeiActivity extends Activity {

	//�����˳��Ի���
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Builder builder=new Builder(this);
		builder.setTitle("�˳�");
		builder.setMessage("ȷ���˳�����");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				FenLeiActivity.this.finish();
			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.create().show();
	}
	private ListView datalist;
	private List<FenLei> fl=new ArrayList<FenLei>();
	private FLAdapter adapter;
	private ProgressDialog dialog;
	private String uri="app!fenleilist.action";
	private ImageView search,download;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fenlei);
		Intent intent=new Intent(this,UpdateService.class);
		this.startService(intent);
		search=(ImageView)super.findViewById(R.id.search);
		download=(ImageView)super.findViewById(R.id.download);
		datalist=(ListView)super.findViewById(R.id.datalist);
		adapter=new FLAdapter(this, fl);
		datalist.setAdapter(adapter);
		datalist.setOnItemClickListener(new OnItemClickListenerImpl());
		search.setOnClickListener(new OnClickListenerImpl());
		download.setOnClickListener(new OnClickListenerImpl());
	}
	//�󶨰�ť����¼�
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if(but.getId()==R.id.search){
				Intent intent=new Intent(FenLeiActivity.this,AppListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				FenLeiActivity.this.startActivity(intent);
			}
			if(but.getId()==R.id.download){
				//�������ؽ���
				Intent intent=new Intent(FenLeiActivity.this,DownLoadActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				FenLeiActivity.this.startActivity(intent);
			}
		}
		
	}
	//���ݵ���¼�
	class OnItemClickListenerImpl implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			// TODO Auto-generated method stub
			FenLei fenlei=fl.get(pos);
			Intent intent=new Intent(FenLeiActivity.this,AppListActivity.class);
			intent.putExtra("id", fenlei.getId());
			intent.putExtra("name", fenlei.getName());
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			FenLeiActivity.this.startActivity(intent);
		}
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		dialog=new ProgressDialog(this);
		dialog.setMessage("���ڻ�ȡ����");
		dialog.show();
		new GetDate().start();
	}
	//��������
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			dialog.cancel();
			if(msg.what==1){
				adapter.notifyDataSetChanged();
			}else{
				Toast.makeText(FenLeiActivity.this, "��ȡ����ʧ��", Toast.LENGTH_SHORT).show();
			}
		}
		
	};
	//��ȡ����
	class GetDate extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			String html=HttpUtils.HttpGet(uri);
			if(html!=null){
				List<FenLei> lf=Utils.getallFenlei(html);
				fl.clear();
				fl.addAll(lf);
				handler.sendEmptyMessage(1);
			}else{
				handler.sendEmptyMessage(2);
			}
		}
		
	}
	

}
