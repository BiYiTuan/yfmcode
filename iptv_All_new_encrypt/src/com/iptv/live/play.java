package com.iptv.live;

import android.content.Context;

public class play 
{
	static
	{
	  System.loadLibrary("play");
	}
	
	public native String play(String paramString,String link,String uid);
    //��link��url�ֿ���Ȼ������һ��uid�������������&link=link&userid=uid
	//play�����������ʽhttp://127.0.0.1:9898/cmd.xml?cmd=switch_chan&server=&id=&link=link&userid=uid
    //paramSting��link�����ģ�uid������


	public native String getlivedata ( String uid,String pass);
	//�ӿ���http://iptv.ovpbox.com:88/live.jsp?active=uid&pass=pass&mac=andoridid����ȡֱ���б�


	public native String getplaybackdata(String uid,String pass,String chid,String strdate);
	//�ӿ���http://iptv.xsj7188.com:5858/playback.jsp?active=udi&mac=androidid&pass=pass&ch_id=chid&rq=2014-01-10
        

	public native String shouchang(String uid,String name,String action);
        //�ӿ���http://iptv.ovpbox.com:88/getshoucang.jsp&active=uid&name=name&action=action

	public native String gettvlist(String uid,String pass);
        //�ӿ�Ϊ http://iptv.ovpbox.com:88/tv_list.jsp?active=uid&pass=pass&mac=andoridid����ȡ�ؿ��б�

	public native String getuserdate(String uid);
        //�ӿ�Ϊ http://iptv.ovpbox.com:88/getUserDate.jsp?active=uid

	public native String getupdate();
        //�ӿ�Ϊ http://iptv.ovpbox.com:88/Update.xml

	public native String getepg(String uid,String chid);
        //����chid����
        //�ӿ�Ϊ http://218.95.37.212:8080/createXml.jsp?id=chid&active=uid

	public native String init(Context ctx);
}

