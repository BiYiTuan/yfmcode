package com.yfm.web;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!isNetworkAvailable()){
			tuichu();
		}
	}

	private WebView webview;
	private ProgressBar progress;
	private WebSettings ws;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// ������Ļ��
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		webview = (WebView) super.findViewById(R.id.webView);
		progress = (ProgressBar) super.findViewById(R.id.progress);
		//progress.setVisibility(View.GONE);
		webview.setWebChromeClient(new WebChromeClientImpl(handler));
		webview.setWebViewClient(wc);
		// webview.setInitialScale(25);
		ws = webview.getSettings();
		ws.setJavaScriptCanOpenWindowsAutomatically(true);
		ws.setJavaScriptEnabled(true);
		ws.setPluginState(PluginState.ON);
		ws.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
		Log.i("---", "����");
		// ws.setSupportZoom(true);
		// ws.setBuiltInZoomControls(true);//����֧������
		// ws.setUseWideViewPort(true);
		webview.loadUrl("http://aaaadc.com");
		//webview.loadUrl("http://www.oceanthree.hk/apps_shop/app_index.php");

	}


	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				progress.setProgress(msg.arg1);
				if(msg.arg1==100){
					progress.setVisibility(View.GONE);
				}else{
					progress.setVisibility(View.VISIBLE);
				}
			}
			if (msg.what == 100) {
				MainActivity.this.finish();
			}
		}
	};
	private WebViewClient wc = new WebViewClient() {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return true;
		}

	};

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (webview.canGoBack()) {
			webview.goBack();
		} else {
			Exit();
		}
	}

	private void Exit() {
		Builder builder = new Builder(this);
		builder.setTitle("�˳�");
		builder.setMessage("�˳�����");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MainActivity.this.finish();
			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.create().show();
	}
	private void tuichu() {
		Builder builder = new Builder(this);
		builder.setTitle("��ʾ");
		builder.setMessage("���粻ͨ���ȴ�����");
		builder.setCancelable(false);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MainActivity.this.finish();
			}
		});
		builder.create().show();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo == null) {
			return false;
		}
		if (netinfo.isConnected()) {
			return true;
		}
		return false;
	}
}
