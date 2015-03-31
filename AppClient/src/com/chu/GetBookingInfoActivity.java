package com.chu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.chu.adapter.LoadingDialog;
import com.chu.adapter.LoadingDialogExecute;
import com.chu.servlet.UserService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class GetBookingInfoActivity extends ListActivity implements
		OnClickListener {
	private ImageView iv;
	private List<HashMap<String, String>> list;
	private String getFromCity;
	private String getToCity;
	private String getLevel;
	private String date;
	private JSONObject obj;
	private JSONArray array;
	private String baseUrl = "http://10.0.2.2:8080/AppServer/dataServlet";
	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_booking_info);

		iv = (ImageView) findViewById(R.id.booking_back);
		iv.setOnClickListener(this);

		getFromCity = (String) getIntent().getStringExtra("getFromSp");
		getToCity = (String) getIntent().getStringExtra("getToSp");
		getLevel = (String) getIntent().getStringExtra("getLevelSp");

		int getYear = (int) getIntent().getIntExtra("mYear", 0);
		int getMonth = (int) getIntent().getIntExtra("mMonth", 0) + 1;// ��dateDialogȡ�õ��·ݱ�ʵ���·�С1
		int getDay = (int) getIntent().getIntExtra("mDay", 0);

		date = getYear + "��" + getMonth + "��" + getDay + "��";
		System.out.println("---->" + getFromCity);
		System.out.println("---->" + date);

		loadingDialog = new LoadingDialog(this, new LoadingDialogExecute() {

			@Override
			public void executeSuccess() {
				// TODO Auto-generated method stub
				setListAdapter();
			}

			@Override
			public void executeFailure() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean execute() {
				// TODO Auto-generated method stub
				try {
					showList(); // ��ʾ�б�
					return true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}

			}
		});
		loadingDialog.start();

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.booking_back:
			GetBookingInfoActivity.this.finish();
			break;

		default:
			break;
		}
	}

	private void showList() throws IOException, Exception {
		String urlStr = baseUrl + "?getFromCity=" + getFromCity + "&getToCity="
				+ getToCity + "&getLevel=" + getLevel + "&getDate=" + date;
		System.out.println(urlStr);

		list = new ArrayList<HashMap<String, String>>();
		String body = JsonTest.getJsonContent(urlStr);
		System.out.println(body);
		array = new JSONArray(body);
		for (int i = 0; i < array.length(); i++) {
			obj = array.getJSONObject(i);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("_from_text", obj.getString("_from"));
			map.put("_to_text", obj.getString("_to"));
			map.put("time_text", obj.getString("time"));
			map.put("price", obj.getString("price"));
			map.put("airport", obj.getString("airport"));
			map.put("level", obj.getString("level"));
			list.add(map);
		}

	}

	private void setListAdapter() {
		SimpleAdapter simpleAdapter = new SimpleAdapter(
				GetBookingInfoActivity.this, list,
				R.layout.get_json_plane_item, new String[] { "_from_text",
						"_to_text", "time_text" }, new int[] { R.id._from_text,
						R.id._to_text, R.id.time_text });
		setListAdapter(simpleAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		HashMap<String, String> map = list.get(position);
		final String from = map.get("_from_text");
		final String to = map.get("_to_text");
		final String time = map.get("time_text");
		final String price = map.get("price");
		final String airport = map.get("airport");
		final String level = map.get("level");
		System.out.println("----->��������" + from);
		/**
		 * listview�е�����������Ի���
		 */
		new AlertDialog.Builder(this)
				.setTitle("�Ƿ�Ԥ��?")
				.setMessage(
						"��Ʊ��Ϣ:\r\n����:" + from + "---->" + to + "\r\n�ǻ�ʱ��: "
								+ time + "\r\nƱ��:" + price + "\r\n��λ:" + level)
				.setPositiveButton("Ԥ��", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						if (LoginActivity.loginTag == true) {
							System.out.println("�Ѿ���¼");
							SharedPreferences sharedPreferences = getSharedPreferences(
									"chu_user", Activity.MODE_PRIVATE);
							String getShpName = sharedPreferences.getString(
									"name", "null");
							String getShpPassword = sharedPreferences
									.getString("password", "null");
							System.out.println("getShpName:" + getShpName);
							System.out.println("getShpPassword:"
									+ getShpPassword);
							String getRespData = UserService.sendChooseTicket(
									getShpName, from, to, time, price, airport,
									level);
							if (getRespData.equals("OK")) {
								Toast.makeText(GetBookingInfoActivity.this,
										"Ԥ���ɹ�", Toast.LENGTH_SHORT).show();
							}

							else if (getRespData.equals("Exist")) {
								Toast.makeText(GetBookingInfoActivity.this,
										"���Ѿ�Ԥ���û�Ʊ", Toast.LENGTH_SHORT).show();
							}
							dialog.dismiss();
						}
					}
				}

				)
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).show();
	}

	/*
	 * public static void show(Context context,String Msg, Thread thread){ final
	 * Thread th = thread; progressDialog = new ProgressDialog(context);
	 * progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//����ΪԲ��
	 * progressDialog.setTitle(null); progressDialog.setIcon(null); //����������Ϣ
	 * progressDialog.setMessage(Msg); //�����Ƿ����ͨ�����ؼ�ȡ��
	 * progressDialog.setCancelable(true);
	 * progressDialog.setIndeterminate(false); //����ȡ������
	 * progressDialog.setOnCancelListener(new OnCancelListener() {
	 * 
	 * public void onCancel(DialogInterface dialog) { // TODO Auto-generated
	 * method stub th.interrupt(); } }); progressDialog.show(); }
	 */

}
