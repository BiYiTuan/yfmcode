package com.pro.ltax;

import org.jsoup.nodes.Document;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.pro.net.HtmlUtils;

public class ContentActivity extends Activity {

	/**
	 * ��ϸ����ҳ������չʾ
	 */
	private HtmlUtils hu=null;
	private TextView ctitle,ctime,content;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hu=new HtmlUtils();
		setContentView(R.layout.activity_content);
		ctitle=(TextView)super.findViewById(R.id.ctitle);
		ctime=(TextView)super.findViewById(R.id.ctime);
		content=(TextView)super.findViewById(R.id.content);
		Bundle bundle=this.getIntent().getExtras();
		if(bundle!=null){
			String url=bundle.getString("url");
			//�첽��ȡurl��ҳ����
			new GetDataTask().execute(url);
			//��ʾ��ȡ���ݶԻ���
			pd=new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
			pd.setTitle("��ȡ����");
			pd.setMessage("���Ժ�");
			pd.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	class GetDataTask extends AsyncTask<String, Integer, Document>{

		@Override
		protected void onPostExecute(Document result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				pd.cancel();
				//����������ʾ
				ctitle.setText(result.getElementById("title").text());
				ctime.setText(result.getElementById("pubdate").text());
				content.setText(result.getElementById("contentText1").text());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected Document doInBackground(String... params) {
			// TODO Auto-generated method stub
			//��ȡ���ݣ�����������
			return hu.GetHtml(params[0]);
		}
		
	}

}
