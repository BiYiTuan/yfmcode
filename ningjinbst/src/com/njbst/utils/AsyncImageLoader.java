package com.njbst.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.ImageView;

/**
 * ͼƬ�첽������
 * 
 * @author Leslie.Fang
 * 
 */
public class AsyncImageLoader {
	private Context context;
	// �ڴ滺��Ĭ�� 5M
	static final int MEM_CACHE_DEFAULT_SIZE = 5 * 1024 * 1024;
	// �ļ�����Ĭ�� 10M
	static final int DISK_CACHE_DEFAULT_SIZE = 10 * 1024 * 1024;
	// һ���ڴ滺����� LruCache
	private LruCache<String, Bitmap> memCache;
	// �����ļ�������� DiskLruCache
	private DiskLruCache diskCache;

	public AsyncImageLoader(Context context) {
		this.context = context;
		initMemCache();
		initDiskLruCache();
	}

	/**
	 * ��ʼ���ڴ滺��
	 */
	private void initMemCache() {
		memCache = new LruCache<String, Bitmap>(MEM_CACHE_DEFAULT_SIZE) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
			}
		};
	}

	/**
	 * ��ʼ���ļ�����
	 */
	private void initDiskLruCache() {
		try {
			File cacheDir = getDiskCacheDir(context, "bitmap");
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			diskCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1,
					DISK_CACHE_DEFAULT_SIZE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ڴ滺������
	 * 
	 * @param url
	 */
	public Bitmap getBitmapFromMem(String url) {
		return memCache.get(url);
	}

	/**
	 * ���뵽�ڴ滺����
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void putBitmapToMem(String url, Bitmap bitmap) {
		memCache.put(url, bitmap);
	}

	/**
	 * ���ļ���������
	 * 
	 * @param url
	 */
	public Bitmap getBitmapFromDisk(String url) {
		try {
			String key = hashKeyForDisk(url);
			DiskLruCache.Snapshot snapShot = diskCache.get(key);
			if (snapShot != null) {
				InputStream is = snapShot.getInputStream(0);
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				return bitmap;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * �� url ����ͼƬ
	 * 
	 * @param imageView
	 * @param imageUrl
	 */
	public Bitmap loadImage(String imageUrl, ImageView imageView) {
		if (imageUrl == null) {
			return null;
		}
		imageView.setTag(imageUrl);
		// �ȴ��ڴ�����
		Bitmap bitmap = getBitmapFromMem(imageUrl);

		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			return bitmap;
		}

		// �ٴ��ļ�����
		bitmap = getBitmapFromDisk(imageUrl);
		if (bitmap != null) {
			// ���»��浽�ڴ���
			putBitmapToMem(imageUrl, bitmap);
			imageView.setImageBitmap(bitmap);
			return bitmap;
		}

		// �ڴ���ļ��ж�û���ٴ���������
		if (!TextUtils.isEmpty(imageUrl)) {
			new ImageDownloadTask(imageView).execute(imageUrl);
		}

		return null;
	}

	class ImageDownloadTask extends AsyncTask<String, Integer, Bitmap> {
		private String imageUrl;
		private ImageView imageView;

		public ImageDownloadTask(ImageView imageView) {
			this.imageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
				imageUrl = params[0];
				Bitmap bitmap = downLoad(imageUrl);
				if (bitmap != null) {
					// ��ͼƬ���뵽�ڴ滺����
					putBitmapToMem(imageUrl, bitmap);
				}
				return bitmap;

		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (result != null) {
				// ͨ�� tag ����ֹͼƬ��λ
				if (imageView != null && imageView.getTag() != null
						&& imageView.getTag().equals(imageUrl)) {
					imageView.setImageBitmap(result);
				}
			}
		}

	}

	
	
	
	
	public Bitmap downLoad(String imageUrl){
		Bitmap bitmap=null;
		try {
			String key = hashKeyForDisk(imageUrl);
			// ���سɹ���ֱ�ӽ�ͼƬ��д���ļ�����
			DiskLruCache.Editor editor = diskCache.edit(key);
			if (editor != null) {
				OutputStream outputStream = editor.newOutputStream(0);
				if (downloadUrlToStream(imageUrl, outputStream)) {
					editor.commit();
				} else {
					editor.abort();
				}
			}
			diskCache.flush();
			bitmap = getBitmapFromDisk(imageUrl);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	private boolean downloadUrlToStream(String urlString,
			OutputStream outputStream) {
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		try {
			final URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			in = new BufferedInputStream(urlConnection.getInputStream(),
					8 * 1024);
			out = new BufferedOutputStream(outputStream, 8 * 1024);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			return true;
		} catch (final IOException e) {

		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	private int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}

	private String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	private String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
}