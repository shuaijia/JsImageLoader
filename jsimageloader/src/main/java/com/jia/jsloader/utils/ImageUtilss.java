package com.jia.jsloader.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 当前类完成的操作是获取到网络中的图片
 * @author hm
 *
 */
public class ImageUtilss {

	private static Bitmap netBitmap;
	protected static final int SUCESS = 1;
	protected static final int ERROR = 2;

	private static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCESS:
				Bitmap bitmap = (Bitmap) msg.obj;
				netBitmap=bitmap;
				break;

			case ERROR:

				break;
			}

		};
	};
	/**
	 *
	 * @param path
	 * @param isSave
	 * @return
	 */
	public static Bitmap getBitmapFromNet(String path, boolean isSave){
		getBitmap(path,isSave);
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return netBitmap;
	}


	private static void getBitmap(final String path, final boolean isSave) {
		new Thread() {
			public void run() {
				try {
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream is = conn.getInputStream();
						Bitmap bitmap = BitmapFactory.decodeStream(is);
						Message msg = Message.obtain();
						msg.obj = bitmap;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
	

    
    public static File saveMyBitmap(String filename, Bitmap bit) {
		File dir = new File("/sdcard/img_interim/");
		if (!dir.exists()) {
			dir.mkdir();
		}
		File f = new File("/sdcard/img_interim/" + filename);
		try {
			f.createNewFile();
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			bit.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			f = null;
			e1.printStackTrace();
		}

		return f;
	}
}
