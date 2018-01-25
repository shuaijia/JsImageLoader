package com.jia.jsloader.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.view.WindowManager;

import com.jia.jsloader.base.JsApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by hm on 2016/1/15.
 *
 *  这个工具类完成的操作是关于图片相关的一系列的工具操作
 *  1、图片的压缩操作（a、给固定的宽高尺寸 b、给固定的压缩大小要求kb为单位）
 *  2、图片的圆角操作
 *  3、图片的虚化操作（设置头像的时候，其所在的大背景相应变化为和头像风格接近的效果）
 *  4、
 */
public class ImageUtil {





    /**
     * 这里定义的方法完成的操作是进行图片的按要求压缩，
     * 1、按照尺寸压缩
     * 2、按照大小压缩
     *
     * imageUrl与bitmap二者中有且仅有一个可以为null，不可同时为空，也不可同时为非空
     *
     * @param imageUrl   图片的本地缓存路径，不包含此参数时设置为null
     * @param bitmap     图片的Bitmap对象，不包含此参数时设置为null
     * @param width      期望的图片宽度，默认值为0，表示不按照width\height方式进行压缩
     * @param height     期望的图片高度，默认值为0，表示不按照width\height方式进行压缩
     * @param reuestKB   期望的图片大小，单位为kb，默认值为0，表示不按照质量压缩方式进行压缩
     * @return
     */
    public static Bitmap getCompressBitmap(String imageUrl, Bitmap bitmap, int width, int height, int reuestKB){

        //二者中只能存在一个不为空的对象
        if(!(null!=imageUrl&&null!=bitmap||(null==imageUrl&&null==bitmap))){

            if(null!=imageUrl){
                bitmap=getBitmapByNativePath(imageUrl);

            }

            if(width>0&&height>0&&reuestKB>0){

                bitmap=compressBmpByWH(bitmap,width,height);
                bitmap=compressBmpByByte(bitmap,reuestKB);

            }else if (width>0&&height>0){

                bitmap=compressBmpByWH(bitmap,width,height);

            }else if(reuestKB>0){

                bitmap=compressBmpByByte(bitmap,reuestKB);

            }else{

                return null;
            }

        }else{

            return null;
        }

        return bitmap;
    }


    /**
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 第一种缩放方式
     * 根据路径获得图片并压缩，返回bitmap用于显示
     *
     * @param filePath   图片的保存路径
     * @param width      期望的宽度
     * @param height     期望的高度
     * @return
     */
    public static Bitmap compressBmpByWH(String filePath, int width, int height) {
        BitmapFactory.Options options;
        try {
            options = new BitmapFactory.Options();
            // 先设置为TRUE不加载到内存中，但可以得到宽和高
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            // Calculate inSampleSize
            options.inSampleSize = mhcalculateInSampleSize(options, width, height);
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 第一种压缩的辅助方法
     * 计算图片的缩放值
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int mhcalculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 第二种缩放方式
     *
     * @param bitmap   传递进来的Bitmap对象
     * @param w        期望的宽度
     * @param h        期望的高度
     * @return
     */
    public static Bitmap compressBmpByWH(Bitmap bitmap, int w, int h) {

        // load the origial Bitmap
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        // calculate the scale
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);

        // make a Drawable from Bitmap to allow to set the Bitmap
        // to the ImageView, ImageButton or what ever
        return resizedBitmap;

    }

    /**
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 第三种缩放方式
     * 根据传递进来的Bitmap对象，并按照期望的图片文件大小要求进行图片压缩
     *
     * @param bitmap      传递的Bitmap对象
     * @param requestSize 期望的图片的大小单位KB
     * @return
     */
    public static Bitmap compressBmpByByte(Bitmap bitmap , int requestSize){


        // 1.按质量压缩：就是把他压缩的某一个大小呢
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        //循环判断如果压缩后图片是否大于requestSizekb,大于继续压缩
        while ( baos.toByteArray().length / 1024>requestSize) {
            baos.reset();//重置baos即清空baos
            options -= 1;//每次都减少1
            //这里压缩options%，把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);

        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //把ByteArrayInputStream数据生成图片
        Bitmap resultBitmap = BitmapFactory.decodeStream(isBm, null, null);
        return resultBitmap;

    }

    /**
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 第四种缩放方式
     * 根据图片的路径和当前手机的默认屏幕分辨率进行图片的压缩
     *
     * @param nativePath   保存到手机本地的图片路径
     * @return
     */
    public static Bitmap getCompressBitmapBYScreen(String nativePath){


        try {
            //1、得到图片的宽高信息
            // （在图片中头信息保存了关于图片大小、分辨率等的信息，利用下面API可以获取到相应的数据
            //   但是存在一些图片中不包括这个信息头部，例如一些图片软件操作后的图片）
            ExifInterface exifInterface=new ExifInterface(nativePath);
            int imageWidth=exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH,0);
            int imageHeight=exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH,0);
            //2、获取当前手机默认的分辨率，宽高
//            WindowManager wm= (WindowManager) BaseApplication.getMainContext().getSystemService(Context.WINDOW_SERVICE);
            WindowManager wm= (WindowManager) JsApplication.getMainContext().getSystemService(Context.WINDOW_SERVICE);
            int screenWidth=wm.getDefaultDisplay().getWidth();
            int screenHeight=wm.getDefaultDisplay().getHeight();

            int requestSize=(imageWidth/screenWidth)>(imageHeight/screenHeight)?(imageWidth/screenWidth):(imageHeight/screenHeight);

            //3、根据上面宽高比值，得到可以满足条件的采样率
            BitmapFactory.Options opts=new BitmapFactory.Options();
            opts.inSampleSize=requestSize;
            Bitmap compressBitmap= BitmapFactory.decodeFile(nativePath,opts);
            return compressBitmap;


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 当前方法完成的操作是根据传递进来的bitmap对象获取该对象对应的图片的大小
     * 返回的大小单位是byte
     *
     * @param bitmap   想要获取大小的图片的Bitmap对象
     * @return
     */
    public static long getBitmapTotalByte(Bitmap bitmap){

        if(null!=bitmap){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            int options = 100;
            return baos.toByteArray().length;
        }else{
            return 0;
        }

    }

    /**
     * 该方法完成的操作是根据保存到本地的图片路径获取对应的Bitmap对象
     *
     * @param nativePath
     * @return
     */
    public static Bitmap getBitmapByNativePath(String nativePath){
        if(null!=nativePath){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(nativePath, options);
            return bitmap;
        }else{
            return null;
        }

    }


    /**
     * 这里是个关于将图片进行虚化操作的工具方法
     * @param sentBitmap   需要虚化的Bitmap对象
     * @param radius       需要虚化到的级别（按照该类的头像背景的缩放级别该数值设置为30已经可以达到要求）
     * @return
     */
    public static Bitmap virtualBitmap(Bitmap sentBitmap, int radius) {

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    /**
     * 将矩形图片的角变成圆角类型
     *
     * @param bitmap  原Bitmap图片
     * @param pixels  图片圆角的弧度(单位:像素(px))
     * @return 带有圆角的图片(Bitmap 类型)
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


}
