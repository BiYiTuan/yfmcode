package com.pro.ltax;

import com.pro.db.DbUtils;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

public class App extends Application {

	/**
	 * ����������ʼ�����ݿ� �������ѷ���
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//���ݿ��ʼ��
		DbUtils dbu=new DbUtils(getApplicationContext());
		SQLiteDatabase db=dbu.openDatabase();
		if(db!=null){
			db.close();
		}
		//���ѷ�������
		Intent service=new Intent(this,TingXingService.class);
		this.startService(service);
	}

}
