package com.android.keche.baidutieba.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public final class ImageDealUtil {

    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    /**
     * string转成bitmap
     *
     * @param st
     */
    public static Bitmap convertStringToIcon(String st)
    {
        // OutputStream out;
        Bitmap bitmap = null;
        try
        {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        }
        catch (Exception e)
        {
            return null;
        }
    }


    /**
     * View 转 Bitmap
     * @param view
     * @return
     */
    public static Bitmap getBitmapFromView(View view)
    {
        // Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        // Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        // Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            // has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            // does not have background drawable, then draw white background on
            // the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        // return the bitmap
        return returnedBitmap;
    }

    /**
     * View 转 Bitmap
     * @param view
     * @return
     */
    public static Bitmap viewToBitmap(View view)
    {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bm = view.getDrawingCache();
        return bm;
    }


    /**
     * 字节byte转Bitmap
     * @param b 字节数组
     * @return 位图
     */
    public static Bitmap Bytes2Bitmap(byte[] b)
    {
        if (b.length != 0)
        {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }


    /**
     * Bitmap转字节数组
     * @param bm 位图
     * @return 字节数组
     *
     */
    public static byte[] Bitmap2Bytes(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }



    /**
     * 字节byte转Drawable
     * @param b 字节数组
     * @return Drawable
     */
    public Drawable Bytes2Drawable(byte[] b)
    {
        Bitmap bitmap = this.Bytes2Bitmap(b);
        return this.bitmap2Drawable(bitmap);
    }


    /**
     * Drawable转byte
     * @param d drawable
     * @return 字节数组
     */
    public byte[] Drawable2Bytes(Drawable d)
    {
        Bitmap bitmap = this.drawable2Bitmap(d);
        return this.Bitmap2Bytes(bitmap);
    }


    /**
     * Drawable转Bitmap
     * @param drawable
     * @return 位图
     */
    public static Bitmap drawable2Bitmap(Drawable drawable)
    {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * Bitmap转Drawable
     * @param bitmap
     * @return
     */
    public static Drawable bitmap2Drawable(Bitmap bitmap)
    {
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        Drawable d = (Drawable) bd;
        return d;
    }


    /**
     * 图片转文件
     * @param bmp
     * @return
     */
    public static boolean saveBitmap2file(Bitmap bmp)
    {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
        int quality = 100;
        OutputStream stream = null;
        try
        {
            // 判断SDcard状态
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            {
                // 错误提示
                return false;
            }

            // 检查SDcard空间
            File SDCardRoot = Environment.getExternalStorageDirectory();
            if (SDCardRoot.getFreeSpace() < 10000)
            {
                // 弹出对话框提示用户空间不够
                Log.e("Utils", "存储空间不够");
                return false;
            }

            // 在SDcard创建文件夹及文件
            File bitmapFile = new File(SDCardRoot.getPath()
                    + Environment.getExternalStorageDirectory().getPath()); //FILE_PATH
            bitmapFile.getParentFile().mkdirs();// 创建文件夹
            stream = new FileOutputStream(SDCardRoot.getPath()
                    + Environment.getExternalStorageDirectory().getPath());// "/sdcard/"
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return bmp.compress(format, quality, stream);
    }


    /**
     * 图片转文件
     * @param bmp
     * @param filename
     * @return
     */
    public static boolean saveBitmap2file(Bitmap bmp, String filename)
    {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try
        {
            stream = new FileOutputStream("/sdcard/" + filename);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }


    /**
     * 下载图片（Bitmap）
     * @param params
     * @return
     */
    public static Bitmap loadImage(String... params)
    {
        InputStream is = null;
        Bitmap bitmap = null;
        try
        {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            if (HttpURLConnection.HTTP_OK != conn.getResponseCode())
            {
                // 网络连接异常
                Log.e("", "loadImage连接异常");
                return null;
            }

            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != is)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }




}
