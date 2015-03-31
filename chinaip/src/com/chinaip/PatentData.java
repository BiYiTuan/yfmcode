package com.chinaip;

import java.util.ArrayList;
import java.util.List;

public class PatentData {

	List<Patent> lp=new ArrayList<Patent>();
	public PatentData(){
		addPatent(14, "�й�����ר��");
		addPatent(15, "�й�ʵ������");
		addPatent(16, "�й�������");
		addPatent(17, "�й�������Ȩ");
		addPatent("34,35,36", "�й�ʧЧר��");
		addPatent(5, "̨��ʡ");
		addPatent(6, "�������");
		addPatent(18, "����");
		addPatent(19, "�ձ�");
		addPatent(20, "Ӣ��");
		addPatent(21, "�¹�");
		addPatent(22, "����");
		addPatent(70, "���ô�");
		addPatent(23, "EPO");
		addPatent(24, "WIPO");
		addPatent(25, "��ʿ");
		addPatent(26, "����");
		addPatent(27, "����˹����ǰ������");
		addPatent(63, "�Ĵ�����");
		addPatent(97, "�����");
		addPatent(62, "�µ���");
		addPatent(29, "������");
		addPatent(28, "������");
		addPatent(85, "������");
		addPatent(122, "���");
		addPatent(60, "���޵���");
		addPatent(130, "�������Һ͵���");
		
	}
	public List<Patent> GetData(){
		return lp;
	}
	public void addPatent(int v,String name){
		Patent pt=new Patent();
		pt.setName(name);
		pt.setValue(String.valueOf(v));
		lp.add(pt);
	}
	public void addPatent(String v,String name){
		Patent pt=new Patent();
		pt.setName(name);
		pt.setValue(v);
		lp.add(pt);
	}
}
