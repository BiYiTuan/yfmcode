package com.chu.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class UserService {
	private static HttpResponse httpResponse;
	private static HttpEntity httpEntity;
	private static String baseUrl = "http://10.0.2.2:8080/AppServer/dataServlet";
	/**
	 * ��֤�û���¼�Ƿ�Ϸ�
	 * ����ֵ�������Ƿ�ɹ�
	 * @throws Exception 
	 */
	
	//get��ʽ���ݲ���HttpUrlConnection�����ݵ�½ʱ������
	public static String check(String name,String psw) throws Exception{
		//192.168.0.101
		//Androidģ����Ĭ�ϵĵ�ַ��10.0.2.2
		String urlStr = "http://10.0.2.2:8080/AppServer/doLoginServlet";
		//���û������������HashMap��
		Map<String,String> params = new HashMap<String, String>();
		params.put("username", name);
		params.put("password", psw);
		try{
			return sendGETRequest(urlStr,params,"UTF-8");
		}
		catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	//���ݶ�Ʊʱ������
	public static String sendChooseTicket(String name,String from,
			String to,String time,String price,String airport,String level){
		String urlStr = "http://10.0.2.2:8080/AppServer/userTicketServlet";
		//����map��
		Map<String,String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("from", from);
		params.put("to", to);
		params.put("time", time);
		params.put("price", price);
		params.put("airport", airport);
		params.put("level", level);
		try{
			return sendGETRequest(urlStr, params, "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	//����ע��ʱ������
	public static String senRegData(String username,String password){
		String urlStr = "http://10.0.2.2:8080/AppServer/insertServlet";
		Map<String,String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("password", password);
		try{
			return sendGETRequest(urlStr, params, "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	//get�������ʺϴ�������������ĵ�ʱ��
	private static String sendGETRequest(String url,
			Map<String,String> params,String encode) throws MalformedURLException,IOException, Exception {
		String result = "";
		StringBuilder sb = new StringBuilder(url);
		sb.append("?");
		//����map
		for(Map.Entry<String, String> entry:params.entrySet()){
			sb.append(entry.getKey()).append("=");
			sb.append(URLEncoder.encode(entry.getValue(),encode));
			sb.append("&");
		}
		//ɾ�����һ��&
		sb.deleteCharAt(sb.length() - 1);
		System.out.println(sb);
		HttpURLConnection conn = (HttpURLConnection) new URL(sb.toString()).openConnection();
		conn.setConnectTimeout(5000);
		// ���������Ӧ����200�����ʾ�ɹ�
		if(conn.getResponseCode()==200){
			// ��÷�������Ӧ������
			//���ݲ���
			BufferedReader in = new BufferedReader(new 
					InputStreamReader(conn.getInputStream(),encode));
			//����
			String retData = null;
			
			while((retData = in.readLine()) !=null){
				result +=retData;
			}
			in.close();
			System.out.println("----->resDat=  " + result);
			
		}
		return result;
	}
	
	//Get����һ�ַ�ʽ���ݲ���HttpClient
	public static String doGetByHttpClient(String url) throws IOException, IOException{
		//url��ʽΪ:http://10.0.2.2:8080/AppServer/dataServlet?username= &password= ;
		//������һ���������
		HttpGet httpGet = new HttpGet(url);
		//����һ��Http�ͻ��˶���
		HttpClient httpClient = new DefaultHttpClient();
		InputStream inputStream;
		//ʹ��http�ͻ��˷�����������������һ��HttpResponse����,����ͻ��˷����������������ص���Ӧ
		httpResponse = httpClient.execute(httpGet);
		httpEntity = httpResponse.getEntity();//��Ӧ����
		inputStream = httpEntity.getContent();
		//ȡ��������
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = br.readLine())!=null){
			result +=line;
		}
		inputStream.close();
		return result;
	}
	
	//post��ʽ�ύ����
	public static String doByPost(String name,String password) throws IOException{
		NameValuePair nameValuePair1 = new BasicNameValuePair("username", name);
		NameValuePair nameValuePair2 = new BasicNameValuePair("password", password);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(nameValuePair1);
		list.add(nameValuePair2);
		HttpEntity requestHttpEntity = new UrlEncodedFormEntity(list);
		HttpPost httpPost = new HttpPost(baseUrl);//����Ҫ��url�и�������
		httpPost.setEntity(requestHttpEntity);
		HttpClient httpClient = new DefaultHttpClient();
		InputStream inputStream;
		//ʹ��http�ͻ��˷�����������������һ��HttpResponse����,����ͻ��˷����������������ص���Ӧ
		httpResponse = httpClient.execute(httpPost);
		httpEntity = httpResponse.getEntity();//��Ӧ����
		inputStream = httpEntity.getContent();
		//ȡ��������
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = br.readLine())!=null){
			result +=line;
		}
		inputStream.close();
		return result;
	}
	//�޸����룬��������
	public static String modify(String name,String psw) throws Exception{
		//192.168.0.101
		//Androidģ����Ĭ�ϵĵ�ַ��10.0.2.2
		String urlStr = "http://10.0.2.2:8080/AppServer/modifyPswServlet";
		//���û������������HashMap��
		Map<String,String> params = new HashMap<String, String>();
		params.put("username", name);
		params.put("password", psw);
		try{
			return sendGETRequest(urlStr,params,"UTF-8");
		}
		catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	//ȡ��Ԥ��
	public static String deleteUserTicket(String username,String from,String to){
		String urlStr = "http://10.0.2.2:8080/AppServer/deleteUserTicketServlet";
		Map<String,String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("from", from);
		params.put("to", to);
		/*
		params.put("time", time);
		params.put("price", price);
		params.put("airport", airport);
		params.put("level", level);
		*/
		try {
			return sendGETRequest(urlStr, params, "UTF-8");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
}
