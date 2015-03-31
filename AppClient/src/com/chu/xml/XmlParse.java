package com.chu.xml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import chu.server.bean.PlaneInfo;

import com.chu.servlet.HttpDownloader;

public class XmlParse {
	//����xml�ļ�ת��Ϊ���ļ�
	public static InputStream downloadXml(String url){
		HttpDownloader httpDownloader = new HttpDownloader();
		InputStream result = null;
		try{
			result = httpDownloader.downloadInput(url);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
		
	}
	
	//����plane.xml�ļ�
	public static List<PlaneInfo> parsePlaneInfo(InputStream inputStream){
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		List<PlaneInfo> list = new ArrayList<PlaneInfo>();
		try{
			XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
			PlaneInfoContentHandler planeInfoContentHandler = new PlaneInfoContentHandler(list);
			xmlReader.setContentHandler(planeInfoContentHandler);
			InputSource inputSource = new InputSource(new InputStreamReader(inputStream,"GB2312"));
			xmlReader.parse(inputSource);
			
			for (Iterator<PlaneInfo> iterator = list.iterator(); iterator.hasNext();) {
				PlaneInfo planeInfo = (PlaneInfo) iterator.next();
				System.out.println("�����ɹ�:" + planeInfo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
		
	}

}
