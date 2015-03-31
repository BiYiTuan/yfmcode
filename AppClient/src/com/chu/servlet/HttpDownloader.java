package com.chu.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpDownloader {
    private static URL url=null;
    
    /*
     * ����URL�����ļ���ǰ��������ļ����е��������ı��������ķ���ֵ�����ļ����е�����
     * 1.����һ��URL����
     * 2.ͨ��URL���󣬴���һ��HttpURLConnection����
     * 3.�õ�InputStream
     * 4.��InputStream���ж�ȡ����
     */
    
    public InputStream  downloadInput(String urlStr) {
    	InputStream inputStream = null;
    	//����һ��URL����
  		 try {
			url=new URL(urlStr);
			 HttpURLConnection urlConn=(HttpURLConnection) url.openConnection();
	  		 inputStream = urlConn.getInputStream();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		 //����һ��Http����
             catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		
    	return inputStream;
    }
    //ֱ�Ӵӷ������˶�ȡxml�ļ�����
    public String download(String urlStr){
      	 StringBuffer sb=new StringBuffer();
      	 String line=null;
      	 BufferedReader buffer=null;
      	 try{
      		 //����һ��URL����
      		 url=new URL(urlStr);
      		 //����һ��Http����
      		 HttpURLConnection urlConn=(HttpURLConnection) url.openConnection();
      		 //ʹ��IO��ȡ����,����һ�ζ�ȡһ������
      		 buffer=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
      		 while((line=buffer.readLine())!=null){
      			 sb.append(line);
      		 }
      	 }catch(Exception e){
      		 e.printStackTrace();
      	 }finally{
      		 try{
      			 buffer.close();
      		 }catch(Exception e){
      			 e.printStackTrace();
      		 }
      	 }
      	 return sb.toString();
       }

}
