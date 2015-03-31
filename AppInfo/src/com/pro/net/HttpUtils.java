package com.pro.net;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
//����ͷ���˽������ݽ���
public class HttpUtils {
	
	public static String baseuri="http://10.120.148.53:8080/App/";
	//��ȡ�������Ϣ
	public static String HttpGet(String uri){
		try {
			HttpClient hc=new DefaultHttpClient();
			HttpGet hg=new HttpGet(baseuri+uri);
			HttpResponse  hr=hc.execute(hg);
			if(hr.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				String html=EntityUtils.toString(hr.getEntity());
				return html;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	//����������ȡ�������Ϣ
	public static String PostCon(String uri,List<NameValuePair> lnp){
		try {
			HttpClient hc=new DefaultHttpClient();
			HttpPost hg=new HttpPost(baseuri+uri);
			hg.setEntity(new UrlEncodedFormEntity(lnp,"UTF-8"));
			HttpResponse  hr=hc.execute(hg);
			if(hr.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				String html=EntityUtils.toString(hr.getEntity());
				return html;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
}
