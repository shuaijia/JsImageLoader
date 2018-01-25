package com.jia.jsloader.utils;

import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by hm on 2016/1/12.
 *
 * 这里是关于文件操作的工具类完成的主要操作： 1、获取指定路径下的文件的内容 2、将获取的数据保存到指定的路径中
 *
 */
public class FileUtil {

	// long saveTime = System.currentTimeMillis() - saveFile.lastModified();

	// 框架中数据的默认缓存时间为5minutes
	private static final long DATA_DEFAULT_SAVETIME = 300000L;

	/**
	 * 这个方法完成的操作是从文件中获取到文件中保存的字符串
	 * 
	 * @param netUrlorPath
	 *            网络路径标志
	 * @return
	 * @throws IOException
	 *             代码中可能出现的异常操作
	 */
	public static String getStringFromFile(long saveTime, String netUrlorPath) throws IOException {

		//System.out.println("从本地拿数据！！");
		long nativeSaveTime = saveTime > 0 ? saveTime : DATA_DEFAULT_SAVETIME;
		long actualSaveTime = 0L;
		String savedPath = null;

		if (null != netUrlorPath) {
			// 文件保存的绝对路径
			savedPath = getSavedPath(netUrlorPath);
		}
		if (null == savedPath) {
			//System.out.println("路径为null！！");
			return null;
		}
		File savedFile = new File(savedPath);
		if (!savedFile.exists()) {
			//System.out.println("文件不存在！！");
			// throw new StructException("需要的文件不存在！");
			return null;
		}
		// 文件的实际保存实际
		actualSaveTime = System.currentTimeMillis() - savedFile.lastModified();
		// 文件的实际保存时间大于期望的文件保存时间
		if (actualSaveTime > nativeSaveTime) {
			savedFile.delete();
		//	System.out.println("Json文件超时了！");
			return null;

		}
		// 这个操作new FileReader(savedFile)会出现FileNotFoundException
		BufferedReader bufReader = new BufferedReader(new FileReader(savedFile));
		String str = "";
		StringBuffer strBuf = new StringBuffer();
		// 这个操作bufReader.readLine()会出现IOException
		while ((str = bufReader.readLine()) != null) {
			strBuf.append(str);
		}

		return strBuf.toString();
	}

	/**
	 * 这里是将网络请求的结果写入到本地文件中
	 *
	 * @param netUrlorPath
	 *            想要保存的文件的路径通过下面代码获得 String md5Url =
	 *            Md5Util.md5(vo.getBaseUrl()); String filePath = new
	 *            File(vo.getmContext().getCacheDir(), URLEncoder.encode(md5Url)
	 *            + ".json").getAbsolutePath();
	 * @param content
	 *            要保存的文件的具体内容
	 * @throws IOException
	 */
	public static void writeJsonToFile(String netUrlorPath, String content) throws IOException {
		String path = null;
		if (null != netUrlorPath) {
			path = getHopeSavePath(netUrlorPath);
		}
		if (null == path) {
			return;
		}
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			byte[] bytes = content.getBytes("UTF-8");
			out.write(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				// 这个操作out.close()会出现IOException
				out.close();
			}
		}
	}

	/**
	 * 这里完成的操作是判断传递进来的路径是否包括Bitmap对象，如果存在将Bitmap对象返回 否则返回null
	 *
	 * @param saveTime
	 *            图片的保存时间
	 * @param netUrl
	 *            网络图片的网络路径作为文件名称
	 * @return
	 */
	public static Bitmap getBitmapFromSD(long saveTime, String netUrl) {

		long nativeSaveTime = saveTime > 0 ? saveTime : DATA_DEFAULT_SAVETIME;
		long actualSaveTime = 0L;
		if (null == netUrl) {
			return null;
		}
		String imageSavePath = getSavedPath(netUrl);
	//	System.out.println("已经存储的图片的路径：：" + imageSavePath);
		if (null == imageSavePath) {
			return null;
		}
		File imageFile = new File(imageSavePath);
		if (!imageFile.exists()) {
			// throw new StructException("需要的文件不存在！");
			return null;
		}
		actualSaveTime = System.currentTimeMillis() - imageFile.lastModified();
		if (actualSaveTime > nativeSaveTime) {
			imageFile.delete();
			//System.out.println("文件超时了！");
			return null;

		}
		/**
		 * 这里的逻辑是当文件对象存在的时候将该文件对象获取出来，并生成Bitmap对象并返回
		 */
		// Bitmap sdBitmap= BitmapFactory.decodeFile(imageSavePath);
		// 从SD卡中获取图片的时候直接进行图片的压缩处理防止OOM

		//System.out.println("保存的图片的链接：" + imageSavePath);
		Bitmap sdBitmap = ImageUtil.getCompressBitmapBYScreen(imageSavePath);
		return sdBitmap;

	}

	/**
	 * 当前方法完成的操作是将图片的原始版本保存到SD卡或者手机机身内存中
	 *
	 * 业务逻辑分析： 1、从网络获取的图片首先保存到本地的SD卡或者机身内存中，这个时候传递进来的参数是 a、netUrl
	 * 图片的网络路径，作为图片的名称 b、bitmap 下载的图片的Bitmap对象 2、在方法内部对当前手机的存储位置进行判断，分别包括
	 * a、手机机身内存 b、外置SD卡内存 3、将对应的图片保存到当前手机的符合要求的内存中
	 *
	 *
	 * @param netUrl
	 *            图片文件的名称，使用图片的网络路径作为图片的名称
	 * @param bitmap
	 *            要保存的图片文件
	 * @return
	 */
	public static boolean putBitmapToSD(String netUrl, Bitmap bitmap) {

		FileOutputStream fos = null;
		String saveBitmapPath = null;
		if (null != netUrl && null != bitmap) {
			// 得到SD卡中图片的保存路径
			saveBitmapPath = getHopeSavePath(netUrl);
			// System.out.println("希望存储的图片的路径：："+saveBitmapPath);
			if (null == saveBitmapPath) {
				return false;
			}
			try {
				// 将数据写入到文件中
				fos = new FileOutputStream(saveBitmapPath);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				// return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;

			} finally {
				try {
					fos.flush();
					fos.close();
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}

		} else {
			return false;
		}
	}

	/**
	 * 获取已经保存的数据的位置的路径
	 *
	 * @param netUrlorPath
	 * @return
	 */
	private static String getSavedPath(String netUrlorPath) {

		String savedPath = null;
		if (StorageUtil.isPhoneHaveSD()) {
			// imageSavePath=StorageUtil.getPathBySD()+netUrl;
			// imageSavePath=new File(new File(StorageUtil.getPathBySD()),
			// URLEncoder.encode(netUrl)).getAbsolutePath();

			// 创建以SD卡根目录为路径的File对象
			File fileBySD = new File(StorageUtil.getPathBySD());
			// 创建SD卡根目录下以当前应用包名为文件夹的文件对象，并验证是否存在当前目录
			File fileBySDSon = new File(fileBySD, PackageUtil.getAppPackageName());
			// File fileBySDSon=new File(fileBySD,"AA");
			if (fileBySDSon.exists()) {
				String md5Url = EncryptUtil.md5(netUrlorPath);
				// 以包名为文件夹的对象存在的时候，通过将文件对象和图片的名称的拼接构建文件对象
				File imageFile = new File(fileBySDSon, URLEncoder.encode(md5Url));
				if (imageFile.exists()) {
					// 图片文件对象存在的时候获取当前的图片对象对应的路径
					savedPath = imageFile.getAbsolutePath();
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {

			// System.out.println("手机中没有SD卡");

			// imageSavePath=StorageUtil.getPathBycache(context)+netUrl;
			// imageSavePath=new File(new
			// File(StorageUtil.getPathBycache(context)),
			// URLEncoder.encode(netUrl)).getAbsolutePath();

			// 创建以Cache根目录为路径的File对象
			File fileByCache = new File(StorageUtil.getPathBycache());
			// 创建SD卡根目录下以当前应用包名为文件夹的文件对象，并验证是否存在当前目录
			File fileByCacheSon = new File(fileByCache, PackageUtil.getAppPackageName());
			// File fileByCacheSon=new File(fileByCache,"AA");
			if (fileByCacheSon.exists()) {
				// System.out.println("HHHHHHHHHHHHHHHHHHHHHHH");
				// System.out.println("图片的网络连接：："+netUrlorPath);
				String md5Url = EncryptUtil.md5(netUrlorPath);
				// 以包名为文件夹的对象存在的时候，通过将文件对象和图片的名称的拼接构建文件对象
				File imageFile = new File(fileByCacheSon, URLEncoder.encode(md5Url));
				if (imageFile.exists()) {
					// 图片文件对象存在的时候获取当前的图片对象对应的路径
					savedPath = imageFile.getAbsolutePath();
					// System.out.println("手机中没有SD卡"+savedPath);
				} else {
					// System.out.println("IIIIIIIIIIIIIIIIIIIIIIIII");
					return null;
				}
			} else {
				// System.out.println("JJJJJJJJJJJJJJJJJJJJJJJJJ");
				return null;
			}
		}

		return savedPath;

	}

	/**
	 * 得到希望保存的位置的路径
	 *
	 * @param netUrlorPath
	 * @return
	 */
	private static String getHopeSavePath(String netUrlorPath) {

		String saveHopePath = null;
		/**
		 * 将网络获取到的图片保存到本机的缓存中（机身内存、SD卡）
		 */
		if (StorageUtil.isPhoneHaveSD() && StorageUtil.getUsefulSizeOfSD() > 10 * 1024 * 1024) {
			// saveBitmapPath=StorageUtil.getPathBySD()+netUrl;
			// saveBitmapPath=new File(new File(StorageUtil.getPathBySD()),
			// URLEncoder.encode(netUrl)).getAbsolutePath();

			// 以SD卡目录为路径创建File对象
			File fileBySD = new File(StorageUtil.getPathBySD());
			// 在SD卡根目录中创建以当前应用包名为名称的文件夹
			File fileBySDSon = new File(fileBySD, PackageUtil.getAppPackageName());
			// File fileBySDSon=new File(fileBySD, "AA");
			if (!fileBySDSon.exists()) {
				fileBySDSon.mkdir();
			}
			String md5Url = EncryptUtil.md5(netUrlorPath);
			// 以图片的网络连接作为对应的文件名称，进行保存图片
			saveHopePath = new File(fileBySDSon, URLEncoder.encode(md5Url)).getAbsolutePath();

		} else {
			// System.out.println("存储一开始没有SD卡");
			// saveBitmapPath=StorageUtil.getPathBycache(context)+netUrl;
			// saveBitmapPath=new File(new
			// File(StorageUtil.getPathBycache(context)),
			// URLEncoder.encode(netUrl)).getAbsolutePath();

			// 以当前应用的缓存目录根目录创建File对象
			File fileByCache = new File(StorageUtil.getPathBycache());
			// 在缓存目录下以当前应用的包名为名称创建一个文件夹
			File fileByCacheSon = new File(fileByCache, PackageUtil.getAppPackageName());
			// File fileByCacheSon=new File(fileByCache,"AA");
			if (!fileByCacheSon.exists()) {
				fileByCacheSon.mkdir();
			}
			String md5Url = EncryptUtil.md5(netUrlorPath);
			// 以图片的网络连接作为对应的文件名称，进行保存图片
			saveHopePath = new File(fileByCacheSon, URLEncoder.encode(md5Url)).getAbsolutePath();
			// System.out.println("存储一开始没有SD卡"+saveHopePath);
		}
		return saveHopePath;
	}

}
