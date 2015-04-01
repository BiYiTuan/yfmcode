package com.example.wnotes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.EditText;

public class Record extends Activity {
	private EditText etTitle;
	private EditText etContent;
	private OnSettingMenuItemClick setTimeClick;
	private OnSetDateMenuItemClick setDateClick;
	private boolean edit = false;
	private int id, index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record);
		etTitle = (EditText) findViewById(R.id.etTitle);
		etContent = (EditText) findViewById(R.id.etContent);
		setTimeClick = new OnSettingMenuItemClick(this);// ����ʱ��
		setDateClick = new OnSetDateMenuItemClick(this);// ��������
		Intent intent = getIntent();
		edit = intent.getBooleanExtra("edit", false);
		if (edit) {
			id = intent.getIntExtra("id", 0);
			index = intent.getIntExtra("index", -1);
			Cursor cursor = Main.dbService.query(id);
			if (cursor.moveToLast()) {
				etTitle.setText(cursor.getString(0));
				etContent.setText(cursor.getString(1));
				setTimeClick
						.setShake(Boolean.parseBoolean(cursor.getString(2)));
				setTimeClick.setRing(Boolean.parseBoolean(cursor.getString(3)));
			}
		}

	}

	// ���������ļ�¼
	class OnSaveMenuItemClick extends MenuItemClickParent implements
			OnMenuItemClickListener {

		public OnSaveMenuItemClick(Activity activity) {
			super(activity);
		}

		@Override
		public boolean onMenuItemClick(MenuItem item) {

			// �޸�
			if (edit) {
				Main.dbService.updateRecord(id, etTitle.getText().toString(),
						etContent.getText().toString(),
						setDateClick.getRemindDate(),
						setTimeClick.getRemindTime(), setTimeClick.isShake(),
						setTimeClick.isRing());
				Main.recordArray.set(index, etTitle.getText().toString());
				Main.myListActivity.setListAdapter(Main.arrayAdapter);

			} else {
				// ���
				Main.dbService.insertRecord(etTitle.getText().toString(),
						etContent.getText().toString(),
						setDateClick.getRemindDate(),
						setTimeClick.getRemindTime(), setTimeClick.isShake(),
						setTimeClick.isRing());
				Main.arrayAdapter.insert(etTitle.getText().toString(), 0);
				Main.idList.add(0, Main.dbService.getMaxId());// ���б������idֵ����������¼

			}

			activity.finish();
			return true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem miSaveRecord = menu.add(0, 1, 1, "���");
		miSaveRecord.setIcon(R.drawable.rrlist1);
		MenuItem miSetDate = menu.add(0, 2, 2, "������������");
		MenuItem miSetTime = menu.add(0, 3, 3, "��������ʱ��");
		miSetTime.setIcon(R.drawable.clock);

		miSetDate.setIcon(R.drawable.calendar_small);// ��������ͼ��
		miSaveRecord.setOnMenuItemClickListener(new OnSaveMenuItemClick(this));
		miSetTime.setOnMenuItemClickListener(setTimeClick);
		miSetDate.setOnMenuItemClickListener(setDateClick);
		return true;
	}

}

