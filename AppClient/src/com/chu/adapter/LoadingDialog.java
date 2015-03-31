package com.chu.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.os.Message;

/**
 * ��װThread��Handler���û����Է���ĵ��ý��ȶԻ���<b>���첽ִ�д���</b>���ڴ���ִ����ɺ󣬽������߳�UI�ĸ��¡� ���������
 * {@link LoadingDialogExecute}������ʹ�á�
 * 
 * @see LoadingDialogExecute
 * @author �Ż����
 * @version 0.2
 */
public class LoadingDialog {
	private ProgressDialog prDialog;
	private Context context;
	private String dialogTitle = "������ʾ";
	private String dialogMessage = "���ڼ��أ����Ժ򡭡�";
	private LoadingDialogExecute loadingDialogExecute;
	private Thread thread;
	private boolean isShowDialog = true;

	/**
	 * ��ȡ�Ƿ���ʾ���ȶԻ���
	 * 
	 * @return
	 */
	public boolean getIsShowDialog() {
		return isShowDialog;
	}

	/**
	 * �����Ƿ���ʾ���ȶԻ���Ϊfalseʱ�����ȶԻ�����ʾ�����첽������Ȼ����ִ��
	 * 
	 * @param isShowDialog
	 */
	public ProgressDialog setIsShowDialog(boolean isShowDialog) {
		this.isShowDialog = isShowDialog;
		return getProgressDialog();
	}

	/**
	 * ��ȡ���ȶԻ���
	 * 
	 * @return ���ȶԻ���
	 */
	public ProgressDialog getProgressDialog() {
		return prDialog;
	}

	/**
	 * ��ȡContext
	 * 
	 * @return Context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * ����Context
	 * 
	 * @param _c
	 */
	public void setContext(Context _c) {
		this.context = _c;
	}

	/**
	 * ��ȡHandler
	 * 
	 * @return
	 */
	public Handler getHandler() {
		return handler;
	}

	/**
	 * ���öԻ������
	 * 
	 * @param _s
	 */
	public void setDialogTitle(String _s) {
		this.dialogTitle = _s;
		if (prDialog != null) {
			prDialog.setTitle(_s);
		}
	}

	/**
	 * ���öԻ�������
	 * 
	 * @param _s
	 */
	public void setDialogMessage(String _s) {
		this.dialogMessage = _s;
		if (prDialog != null) {
			prDialog.setMessage(_s);
		}
	}

	/**
	 * ��ȡThread
	 * 
	 * @return
	 */
	public Thread getThread() {
		return this.thread;
	}

	private void initProgressDialog() {
		prDialog = new ProgressDialog(context);
		prDialog.setTitle(dialogTitle);
		prDialog.setMessage(dialogMessage);
		prDialog.setCanceledOnTouchOutside(false);
		prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		prDialog.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				thread.interrupt(); // ֹͣ�߳�
			}
		});
		prDialog.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				thread.interrupt(); // ֹͣ�߳�
			}
		});
	}

	/**
	 * ���ȶԻ����캯��
	 * 
	 * @param context
	 *            Context
	 * @param loadingDialogExecute
	 *            ��Ҫʵ��LoadingDialogExecute�����������
	 */
	public LoadingDialog(Context context, LoadingDialogExecute loadingDialogExecute) {
		this.context = context;
		this.loadingDialogExecute = loadingDialogExecute;
		initProgressDialog();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (prDialog != null && prDialog.isShowing()) {
				prDialog.dismiss();
			}
			switch (msg.what) {
			case 0:
				loadingDialogExecute.executeSuccess();
				break;
			case 1:
				loadingDialogExecute.executeFailure();
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * ��ʼִ�н��ȶԻ���
	 */
	public void start() {
		if (prDialog == null) {
			initProgressDialog();
		}
		if (getIsShowDialog()) {
			prDialog.show();
		}

		thread = new Thread() {
			public void run() {
				boolean b = loadingDialogExecute.execute();
				if (b) {
					handler.sendEmptyMessage(0);
				} else {
					handler.sendEmptyMessage(1);
				}
			}
		};
		thread.start();
	}

	/**
	 * ִֹͣ�н��ȶԻ��򣬶Ի���رգ��߳�ֹͣ
	 */
	public void stop() {
		prDialog.dismiss();
		handler.sendEmptyMessage(1);
	}

}
