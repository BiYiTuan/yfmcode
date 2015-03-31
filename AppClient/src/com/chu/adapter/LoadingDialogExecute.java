package com.chu.adapter;

/**
 * ���ȶԻ���������࣬��Ҫ���û�ʵ��execute��executeSuccess��executeFailure���������� execute���������߳��첽ִ�У�������������߳�UI��executeSuccess��executeFailure�����������߳�ִ�У����Ը������߳�UI
 * 
 * @see LoadingDialog
 * @author �Ż����
 * @version 0.2
 */
public abstract class LoadingDialogExecute {
	private String errorInfo = "��������";

	/**
	 * ��ȡ���һ�δ�����Ϣ����
	 * 
	 * @return
	 */
	public String getErrorInfo() {
		return errorInfo;
	}

	/**
	 * �������һ�δ�����Ϣ����
	 * 
	 * @param errorInfo
	 */
	public void setErrorInfo(String errorInfo) {
		if (errorInfo == null)
			return;
		this.errorInfo = errorInfo;
	}

	/**
	 * �첽ִ�з������������û��ڴ˷����и������߳�UI����Ҫ�û����ж��쳣������
	 * 
	 * @return
	 */
	public abstract boolean execute();

	/**
	 * �첽����ִ�гɹ�ʱִ�еķ��������ڴ˷����и������߳�UI
	 */
	public abstract void executeSuccess();

	/**
	 * �첽����ִ��ʧ��ʱִ�еķ��������ڴ˷����и������߳�UI
	 */
	public abstract void executeFailure();
}
