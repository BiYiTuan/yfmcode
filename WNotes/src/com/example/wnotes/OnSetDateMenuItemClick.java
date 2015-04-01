package com.example.wnotes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ��˵�������������¼�����
 * 
 */
public class OnSetDateMenuItemClick extends MenuItemClickParent implements
		OnMenuItemClickListener, OnClickListener, OnDateChangedListener,
		android.view.View.OnClickListener {
	private DatePicker dpSelectDate;
	private LinearLayout myDateLayout;
	private TextView tvDate;
	private Button btDate;
	private TextView tvLunarDate;
	private AlertDialog.Builder builder;
	private AlertDialog adMyDate;
	private int year, month, day;
	private String remindDate;

	public OnSetDateMenuItemClick(Activity activity) {
		super(activity);
		myDateLayout = (LinearLayout) this.activity.getLayoutInflater()
				.inflate(R.layout.mydate, null);
		dpSelectDate = (DatePicker) myDateLayout
				.findViewById(R.id.dpSelectDate);

	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��");
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(year, monthOfYear, dayOfMonth);
		if (tvDate != null)
			tvDate.setText(sdf.format(calendar.getTime()));
		else
			adMyDate.setTitle(sdf.format(calendar.getTime()));
		if (tvLunarDate == null)
			return;

	}

	// �����ȷ��ʱΪ�����ո�ֵ
	@Override
	public void onClick(DialogInterface dialog, int which) {
		
	}
class OnDateSetListenerImpl implements OnDateSetListener{

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		year = dpSelectDate.getYear();
		month = dpSelectDate.getMonth();
		day = dpSelectDate.getDayOfMonth();
		remindDate = year + "-" + month + "-" + day;
	}
	
}
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		DatePickerDialog dpd=new DatePickerDialog(activity, new OnDateSetListenerImpl(), calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		dpd.setTitle("ָ������");
		dpd.show();

		return true;
	}

	// ��ť���ȷ��ʱ
	@Override
	public void onClick(View view) {
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		dpSelectDate.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), this);
		onDateChanged(dpSelectDate, dpSelectDate.getYear(),
				dpSelectDate.getMonth(), dpSelectDate.getDayOfMonth());
	}

	// ��������
	public String getRemindDate() {
		return remindDate;
	}

}
