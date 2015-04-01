package com.yfm.pro;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yfm.adapter.TcpserverAdapter;
import com.yfm.pojo.TcpConnectClient;
import com.yfm.pojo.TcpConnectServer;

public class TCPserverFragment extends BaseActivity {

	@Override
	public void setvalue(String msg) {
		// TODO Auto-generated method stub
		super.setvalue(msg);
		if(sendcon!=null){
			sendcon.setText(msg);
		}
	}
	@Override
	public void setpeizhi() {
		// TODO Auto-generated method stub
		super.setpeizhi();
		showConfig();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ioacceptor.unbind();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.getParent().onBackPressed();
	}

	
	private TextView con;
	private ImageView mimg;
	private EditText port, sendcon;
	private Button sendbut;
	private CheckBox start;
	private NioSocketAcceptor ioacceptor;
	private IoHandlerAdapter iohandler;
	private InetSocketAddress address;
	private IoSession iosession = null;
	private LinearLayout configbut;
	private Dialog dialog, setdialog;
	private View view, setview;
	private List<TcpConnectServer> lcs = new ArrayList<TcpConnectServer>();
	private GridView datagird;
	private TcpserverAdapter adapter;
	private TcpConnectServer currcs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i("mt", "oncreat");
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_tcpserver);
		ioacceptor = new NioSocketAcceptor();
		ioacceptor.setReuseAddress(true);
		iohandler = new IoHandlerAdapterImpl();
		ioacceptor.setHandler(iohandler);
		ioacceptor.addListener(new IoServiceListenerImpl(handler));
		
		
		datagird = (GridView) this.findViewById(R.id.datagrid);
		adapter = new TcpserverAdapter(this, lcs);
		datagird.setAdapter(adapter);
		datagird.setOnItemClickListener(new OnClickListenerImpl());
		datagird.setOnItemLongClickListener(new OnClickListenerImpl());

		mimg = (ImageView) this.findViewById(R.id.mimg);
		con = (TextView) this.findViewById(R.id.con);
		sendcon = (EditText) this.findViewById(R.id.sendcon);
		sendcon.addTextChangedListener(new OnClickListenerImpl());
		sendbut = (Button) this.findViewById(R.id.sendbut);
		configbut = (LinearLayout) this.findViewById(R.id.config);
		sendbut.setOnClickListener(new OnClickListenerImpl());
		configbut.setOnClickListener(new OnClickListenerImpl());
	}

	private void showset() {
		if (setdialog == null) {
			setdialog = new Dialog(this);
			setdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setview = LayoutInflater.from(this).inflate(
					R.layout.tcpserver_itemset, null);
			setdialog.setContentView(setview);
			Utils.setDialog(this, setdialog);
		}
		Button showtype = (Button) setview.findViewById(R.id.showtype);
		TextView ipport = (TextView) setview.findViewById(R.id.ipport);

		Button sendtype = (Button) setview.findViewById(R.id.sendtype);
		Button fktype = (Button) setview.findViewById(R.id.fktype);
		Button duankai = (Button) setview.findViewById(R.id.duankai);
		Button clear = (Button) setview.findViewById(R.id.clear);
		CheckBox huanhang = (CheckBox) setview.findViewById(R.id.huanhang);
		ipport.setText("ip:" + currcs.getIp() + " port:" + currcs.getPort());
		showtype.setText(currcs.getShowtype());
		sendtype.setText(currcs.getSendtype());
		fktype.setText(currcs.getFktype());
		huanhang.setChecked(currcs.isHuanhang());
		showtype.setOnClickListener(new OnClickListenerImpl());
		fktype.setOnClickListener(new OnClickListenerImpl());
		sendtype.setOnClickListener(new OnClickListenerImpl());

		duankai.setOnClickListener(new OnClickListenerImpl());
		clear.setOnClickListener(new OnClickListenerImpl());

		huanhang.setOnCheckedChangeListener(new OnClickListenerImpl());
		setdialog.show();

	}

	// ��������
	private void showConfig() {
		if (dialog == null) {
			dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			view = LayoutInflater.from(this).inflate(R.layout.tcpserver_config,
					null);
			port = (EditText) view.findViewById(R.id.port);
			port.setText(getconfig("tcpport"));
			start = (CheckBox) view.findViewById(R.id.start);
			start.setOnCheckedChangeListener(new OnClickListenerImpl());
			dialog.setContentView(view);
			Utils.setDialog(this, dialog);
		}
		dialog.show();
	}

	// ��Ϣ������
	class IoHandlerAdapterImpl extends IoHandlerAdapter {

		@Override
		public void sessionClosed(IoSession session) throws Exception {
			// TODO Auto-generated method stub
			super.sessionClosed(session);
			TcpConnectServer cs = getServer(session);
			lcs.remove(cs);
			Message msg = handler.obtainMessage();
			msg.what = 1;
			handler.sendMessage(msg);
		}

		@Override
		public void messageSent(IoSession session, Object message)
				throws Exception {
			// TODO Auto-generated method stub
			super.messageSent(session, message);
		}

		@Override
		public void sessionCreated(IoSession session) throws Exception {
			// TODO Auto-generated method stub
			Log.i("tcpserver", "��⵽�¿ͻ���"
					+ session.getRemoteAddress().toString());
			super.sessionCreated(session);
			TcpConnectServer cs = new TcpConnectServer(handler);
			cs.setIosession(session);
			lcs.add(cs);
			currcs = adapter.getserver();
			Message msg = handler.obtainMessage();
			msg.what = 1;
			handler.sendMessage(msg);
		}

		@Override
		public void exceptionCaught(IoSession session, Throwable cause)
				throws Exception {
			// TODO Auto-generated method stub
			Log.i("tcpserver", cause.getMessage());
			super.exceptionCaught(session, cause);
		}

		@Override
		public void messageReceived(IoSession session, Object message)
				throws Exception {
			// TODO Auto-generated method stub
			super.messageReceived(session, message);
			try {
				IoBuffer ib=(IoBuffer)message;
				TcpConnectServer cs = getServer(session);
				byte[] p=new byte[ib.limit()];
				ib.get(p);
				Log.i("msg", "recive"+p.length);
				String mg="";
				if (cs.isHuanhang()) {
					cs.getSb().append("\r\n");
				}
				if ("�ı�".equals(cs.getShowtype())) {
					mg=new String(p,"GBK");
				}
				if ("ʮ������".equals(cs.getShowtype())) {
					mg=Utils.getHexString(p);
				}
				if(cs.getSb().length()>600){
					cs.getSb().delete(0, 500);
				}
				cs.getSb().append(mg);
				handler.sendEmptyMessage(3);
				if ("�Զ�".equals(cs.getFktype())) {
					cs.SendMsg();
				}
				if ("͸��".equals(cs.getFktype())) {
					cs.SendMsg(mg);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				Log.i("tcpserver", "����gridview");
				adapter.notifyDataSetChanged();
			}
			if (msg.what == 3) {
				TcpConnectServer cs = adapter.getserver();
				if (cs != null) {
					String nc = cs.getSb().toString();
					con.setText(nc);
				}

			}
			if (msg.what == 4) {
				Toast.makeText(TCPserverFragment.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
			}
			if(msg.what==5){
				Toast.makeText(
						TCPserverFragment.this,
						"�������˿�:" + port.getText().toString()
								+ "\r\n������IP:" + getLocalIpAddress(),
						Toast.LENGTH_SHORT).show();
				mimg.setImageResource(R.drawable.serveron2);
			}
			if(msg.what==6){
				Toast.makeText(TCPserverFragment.this, "���������쳣���˿ڱ�ռ��",
						Toast.LENGTH_SHORT).show();
				mimg.setImageResource(R.drawable.serveroff);
			}
			if(msg.what==7){
				Toast.makeText(TCPserverFragment.this, "�����ѹر�",
						Toast.LENGTH_SHORT).show();
				mimg.setImageResource(R.drawable.serveroff);
			}
		}

	};

	public TcpConnectServer getServer(IoSession session) {
		for (int i = 0; i < lcs.size(); i++) {
			if (session.equals(lcs.get(i).getIosession())) {
				return lcs.get(i);
			}
		}
		return null;
	}

	class OnClickListenerImpl implements OnClickListener,
			OnItemLongClickListener, OnItemClickListener,
			OnCheckedChangeListener, TextWatcher {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.config) {
				showConfig();
			}
			if (v.getId() == R.id.sendbut) {
				TcpConnectServer cs = adapter.getserver();
				if (cs != null) {
					su.insert(sendcon.getText().toString());
					cs.SendMsg(sendcon.getText().toString());
				}else{
					Toast.makeText(TCPserverFragment.this, "δѡ������", Toast.LENGTH_SHORT).show();
				}

			}
			if (v.getId() == R.id.showtype) {
				if ("�ı�".equals(currcs.getShowtype())) {
					currcs.setShowtype("ʮ������");
				} else {
					currcs.setShowtype("�ı�");
				}
				Button bv = (Button) v;
				bv.setText(currcs.getShowtype());
			}
			if (v.getId() == R.id.sendtype) {
				if ("�ı�".equals(currcs.getSendtype())) {
					currcs.setSendtype("ʮ������");
				} else {
					currcs.setSendtype("�ı�");
				}
				Button bv = (Button) v;
				bv.setText(currcs.getSendtype());
			}
			if (v.getId() == R.id.fktype) {
				if ("�ֶ�".equals(currcs.getFktype())) {
					currcs.setFktype("�Զ�");
				} else if ("�Զ�".equals(currcs.getFktype())) {
					currcs.setFktype("͸��");
				} else {
					currcs.setFktype("�ֶ�");
				}
				Button bv = (Button) v;
				bv.setText(currcs.getFktype());
			}
			if (v.getId() == R.id.duankai) {
				currcs.getIosession().close(true);
				lcs.remove(currcs);
				adapter.notifyDataSetChanged();
				setdialog.cancel();
			}
			if (v.getId() == R.id.clear) {
				currcs.getSb().delete(0, currcs.getSb().length());
				con.setText(adapter.getserver().getSb());
			}
		}

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			adapter.setCid(arg2);
			adapter.notifyDataSetChanged();
			currcs = lcs.get(arg2);
			con.setText(currcs.getSb().toString());
			sendcon.setText(currcs.getMsg());
			showset();
			return false;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			adapter.setCid(arg2);
			adapter.notifyDataSetChanged();
			currcs = lcs.get(arg2);
			con.setText(currcs.getSb().toString());
			sendcon.setText(currcs.getMsg());
		}

		@Override
		public void onCheckedChanged(CompoundButton but, boolean arg1) {
			// TODO Auto-generated method stub
			Log.i("change", arg1 + "");
			if (but.getId() == R.id.start) {
				if (arg1) {
					setconfig("ipport", port.getText().toString());
					address = new InetSocketAddress(Integer.parseInt(port
							.getText().toString()));
					try {
						ioacceptor.bind(address);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(6);
					}
				} else {
					ioacceptor.unbind(address);
				}
				dialog.cancel();
			}
			if (but.getId() == R.id.huanhang) {
				currcs.setHuanhang(arg1);
			}

		}
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			TcpConnectServer cs = adapter.getserver();
			if (cs != null) {
				cs.setMsg(sendcon.getText().toString());
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

	}

	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(inetAddress
									.getHostAddress())) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return "127.0.0.1";
	}

}
