package com.example.wnotes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wnotes.db.DBService;

public class Main extends ListActivity {
	public static List<String> recordArray;
	public static ArrayAdapter<String> arrayAdapter;
	public static List<Integer> idList = new ArrayList<Integer>();
	public static ListActivity myListActivity;
	public static DBService dbService = null;// ���ݷ��ʶ���
	public static MediaPlayer mediaPlayer;// ���ֲ�����
	public static Vibrator vibrator;
	private MenuItem miNewRecord;
	private MenuItem miModifyRecord;
	private MenuItem miDeleteRecord;
	public AlarmManager am;// ��Ϣ������
	private EditText stext;
	private Button sbut;
	private OnEditRecordMenuItemClick editRecordMenuItemClick = new OnEditRecordMenuItemClick(
			this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.main);
		stext=(EditText)super.findViewById(R.id.stext);
		sbut=(Button)super.findViewById(R.id.sbut);
		// ��ʼ�����ݷ��ʶ���
		if (dbService == null) {
			dbService = new DBService(this);
		}
		if (am == null) {
			am = (AlarmManager) getSystemService(ALARM_SERVICE);
		}
		if (recordArray == null)
			recordArray = new ArrayList<String>();

		if (arrayAdapter == null)
			arrayAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, recordArray);
		else
			arrayAdapter.clear();

		idList.clear();
		// ��ѯ�����еļ�¼
		Cursor cursor = dbService.query();
		while (cursor.moveToNext()) {
			arrayAdapter.add(cursor.getString(1));
			idList.add(cursor.getInt(0));

		}
		// ���ñ�������
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��");
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		setTitle(sdf.format(calendar.getTime()));// ����������Ϊ��ǰʱ��
		setListAdapter(arrayAdapter);
		myListActivity = null;
		myListActivity = this;
		try {
			Intent intent = new Intent(myListActivity, CallAlarm.class);
			PendingIntent sender = PendingIntent.getBroadcast(myListActivity,
					0, intent, 0);
			am.setRepeating(AlarmManager.RTC, 0, 60 * 1000, sender);
		} catch (Exception e) {
			e.printStackTrace();
		}
		getListView().setOnItemLongClickListener(new OnItemLongClickListenerImpl());
		sbut.setOnClickListener(new OnClickListenerImpl());
	}
	//�������ʱ����
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String st=stext.getText().toString();
			initdate(st);
		}
		
	}
	//��ѯ������ʾ
	public void initdate(String title){
		idList.clear();
		// ��ѯ�����еļ�¼
		arrayAdapter.clear();

		idList.clear();
		// ��ѯ�����еļ�¼
		Cursor cursor = dbService.querydata(title);
		while (cursor.moveToNext()) {
			arrayAdapter.add(cursor.getString(1));
			idList.add(cursor.getInt(0));

		}
		setListAdapter(arrayAdapter);
	}
	//��������ɾ���Ի���
	class OnItemLongClickListenerImpl implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				final int arg2, long arg3) {
			// TODO Auto-generated method stub
			Builder builder=new Builder(Main.this);
			builder.setTitle("ɾ��");
			builder.setMessage("ȷ��ɾ��");
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dbService.deleteRecord(idList.get(arg2));
					initdate(null);
				}
			});
			builder.setNegativeButton("ȡ��", null);
			builder.create().show();
			return true;
		}

	}
	// ��List�е�item�����ǵ��ø÷���
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		startEditRecordActivity(position);// �����޸ļ�¼����
	}

	// ����µļ�¼
	class OnAddRecordMenuItemClick extends MenuItemClickParent implements
			OnMenuItemClickListener {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			Intent intent = new Intent(activity, Record.class);
			activity.startActivity(intent);
			return true;
		}

		public OnAddRecordMenuItemClick(Activity activity) {
			super(activity);
		}

	}

	// �޸ļ�¼
	public void startEditRecordActivity(int index) {
		Intent intent = new Intent(this, Record.class);
		intent.putExtra("edit", true);
		intent.putExtra("id", idList.get(index));
		intent.putExtra("index", index);
		startActivity(intent);
	}

	// �޸�/�鿴��¼
	class OnEditRecordMenuItemClick extends MenuItemClickParent implements
			OnMenuItemClickListener {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			Main allRecord = (Main) activity;

			int index = allRecord.getSelectedItemPosition();
			if (index < 0)
				return false;
			allRecord.startEditRecordActivity(index);
			return true;
		}

		public OnEditRecordMenuItemClick(Activity activity) {
			super(activity);
		}

	}

	// ɾ����¼��Ϣ
	class OnDeleteRecordMenuItemClick extends MenuItemClickParent implements
			OnMenuItemClickListener {

		public OnDeleteRecordMenuItemClick(Activity activity) {
			super(activity);
		}

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			Main allRecord = (Main) activity;
			int index = allRecord.getSelectedItemPosition();
			System.out.println("ɾ����" + index);
			if (index < 0)
				return false;
			recordArray.remove(index);
			int id = idList.get(index);
			idList.remove(index);
			allRecord.setListAdapter(arrayAdapter);
			dbService.deleteRecord(id);
			return true;
		}

	}
	class OnMenuItemClickListenerImpl implements OnMenuItemClickListener{

		@Override
		public boolean onMenuItemClick(MenuItem arg0) {
			// TODO Auto-generated method stub
			if(Environment.getExternalStorageState()!=Environment.MEDIA_MOUNTED){
				try {
					String name=System.currentTimeMillis()+".cvs";
					File file=new File(Environment.getExternalStorageDirectory(), name);
					if(!file.exists()){
						file.createNewFile();
					}
					FileOutputStream fos=new FileOutputStream(file);
					OutputStreamWriter osw=new OutputStreamWriter(fos);
					BufferedWriter bw=new BufferedWriter(osw);
					bw.write("id,title, content, record_date,remind_time, remind, shake, ring\r\n");
					Cursor cursor=dbService.querydata(null);
					while(cursor.moveToNext()){
						bw.write(cursor.getString(0)+","+cursor.getString(1)+","+cursor.getString(2)+","+cursor.getString(3)+","+cursor.getString(4)+","+cursor.getString(5)+","+cursor.getString(6)+","+cursor.getString(7)+"\r\n");
					}
					cursor.close();
					bw.close();
					osw.close();
					fos.close();
					Toast.makeText(Main.this, "�����ɹ�,�ѱ��浽sd�����ļ���Ϊ"+name, Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else{
				Toast.makeText(Main.this, "SD�洢��������", Toast.LENGTH_SHORT).show();
			}
			return false;
		}
		
	}
	// �����˵�
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		miNewRecord = menu.add(0, 1, 1, "��Ӽ�¼");
		miNewRecord.setIcon(R.drawable.newrecord);
		miModifyRecord = menu.add(0, 2, 2, "�޸�/�鿴");
		miModifyRecord.setIcon(R.drawable.rrlist);
		miDeleteRecord = menu.add(0, 4, 4, "ɾ����¼");
		miDeleteRecord.setIcon(R.drawable.festival);
		MenuItem miAbout = menu.add(0, 5, 5, "����");
		MenuItem miHelp = menu.add(0, 6, 6, "����");
		MenuItem export = menu.add(0, 7, 7, "����");
		export.setOnMenuItemClickListener(new OnMenuItemClickListenerImpl());
		miNewRecord.setOnMenuItemClickListener(new OnAddRecordMenuItemClick(
				this));

		miModifyRecord.setOnMenuItemClickListener(editRecordMenuItemClick);
		miDeleteRecord
				.setOnMenuItemClickListener(new OnDeleteRecordMenuItemClick(
						this));

		// ����
		miAbout.setIcon(R.drawable.about);
		// ���ڲ˵�����¼�
		miAbout.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem mi) {
				Intent intent = new Intent(myListActivity, About.class);
				myListActivity.startActivity(intent);
				return true;
			}
		});

		// ����
		miHelp.setIcon(R.drawable.help);
		// �����˵�����¼�
		miHelp.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem mi) {
				Intent intent = new Intent(myListActivity, Help.class);
				myListActivity.startActivity(intent);
				return true;
			}
		});
		return true;
	}

}