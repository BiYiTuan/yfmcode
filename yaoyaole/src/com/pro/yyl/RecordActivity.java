package com.pro.yyl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import shidian.tv.sntv.tools.DbUtils;
import shidian.tv.sntv.tools.DialogUtils;
import shidian.tv.sntv.tools.Result;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;


public class RecordActivity extends Activity {

	private List<Result> lpn=new ArrayList<Result>();
	private ListView datalist;
	private RecordAdapter adapter;
	private CheckBox wlq;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private String state="δ��ȡ";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        datalist=(ListView)this.findViewById(R.id.datalist);
        adapter=new RecordAdapter(this, lpn);
        datalist.setAdapter(adapter);
        datalist.setOnCreateContextMenuListener(new ListenerImpl());
        wlq=(CheckBox)this.findViewById(R.id.wlq);
        wlq.setOnCheckedChangeListener(new ListenerImpl());
    }

    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	DbUtils.getInstance(this).getAllRecord(lpn, state);
    	adapter.notifyDataSetChanged();
    }
    
    class ListenerImpl implements OnCreateContextMenuListener,OnCheckedChangeListener{


		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
			AdapterContextMenuInfo menuinfo= (AdapterContextMenuInfo)menuInfo;
			Result pi=lpn.get(menuinfo.position);
			menu.setHeaderTitle("����");
			menu.add(0,1,0,"ɾ��");
			if(pi.getState().equals("δ��ȡ")){
				menu.add(0,2,0,"����ȡ");
			}else{
				menu.add(0,2,0,"δ��ȡ");
			}
			
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked){
				state="δ��ȡ";
				DbUtils.getInstance(RecordActivity.this).getAllRecord(lpn, state);
		    	adapter.notifyDataSetChanged();
			}else{
				state=null;
				DbUtils.getInstance(RecordActivity.this).getAllRecord(lpn, state);
		    	adapter.notifyDataSetChanged();
			}
			
		}
    	
    }
    
    public synchronized void export(View v){
    	DialogUtils.showDialog(this, "������...");
    	Map<String, String> ms=DbUtils.getInstance(this).export(state);
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
    		File file=new File(Environment.getExternalStorageDirectory(), sdf.format(new Date())+".csv");
    		FileOutputStream fos=null;
    		OutputStreamWriter osw=null;
    		BufferedWriter bw=null;
    		try {
				fos=new FileOutputStream(file);
				osw=new OutputStreamWriter(fos, "UTF-8");
				bw=new BufferedWriter(osw);
				Set<String> keys=ms.keySet();
				int i=1;
				for(String key:keys){
					bw.write("\""+i+"\",\""+key+"\",\""+ms.get(key)+"\"\r\n");
					i++;
				}
				DialogUtils.ShowToast(this, "�����ɹ��ļ�������"+file.getAbsolutePath());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				DialogUtils.ShowToast(this, "����ʧ��");
			}finally{
				if(bw!=null){
					try {
						bw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(osw!=null){
					try {
						osw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(fos!=null){
					try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
    	}else{
    		DialogUtils.ShowToast(this, "�洢��������");
    	}
    	DialogUtils.closeDialog();
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	AdapterContextMenuInfo menuinfo= (AdapterContextMenuInfo)item.getMenuInfo();
    	Result pi=lpn.get(menuinfo.position);
    	int itemid=item.getItemId();
    	System.out.println(itemid);
    	if(itemid==1){
    		DbUtils.getInstance(this).deleGift(pi);
    		lpn.remove(pi);
    		adapter.notifyDataSetChanged();
    		DialogUtils.ShowToast(this, "ɾ���ɹ�");
    	}else if(itemid==2){
    		pi.setState(item.getTitle().toString());
    		DbUtils.getInstance(this).UpdateGift(pi);
    		adapter.notifyDataSetChanged();
    		DialogUtils.ShowToast(this, "�޸ĳɹ�");
    	}
    	
    	return super.onContextItemSelected(item);
    }
    

}
