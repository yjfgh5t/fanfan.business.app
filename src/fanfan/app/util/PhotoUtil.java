package fanfan.app.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import fanfan.app.constant.CodeConstant;
import fanfan.app.view.cutimg.ClipImageActivity;

public class PhotoUtil {
	
	 private static File mFileTemp;
	 private static File mFileCut;
	 private static File mFileCompress;
	
	 static{
		 mFileTemp =  new File(Environment.getExternalStorageDirectory(), "temp-img.jpg");
		 mFileCut =  new File(Environment.getExternalStorageDirectory(), "temp-cut-img.jpg");
		 mFileCompress = new File(Environment.getExternalStorageDirectory(), "temp-compress-img.jpg");
	 }
	 
	/**
	 * 打开相册
	 */
	public static void openPhoto(Activity activity) {
		 Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
         photoPickerIntent.setType("image/*");
         activity.startActivityForResult(photoPickerIntent, CodeConstant.Code_Choise_Img);
	}
	
	/**
	 * 拍照
	 */
	public static void takePhoto(Activity activity) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
        	Uri mImageCaptureUri =  Uri.fromFile(mFileTemp);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            activity.startActivityForResult(intent, CodeConstant.Code_Take_Photo);
        } catch (ActivityNotFoundException e) {
            Log.d("PhotoUtil", "cannot take picture", e);
        }
	}
	
	/**
	 * 获取选择相册返回的数据
	 */
	public static void setPhotoData(Uri data) {
		try {
			InputStream inputStream = Utils.getApp().getContentResolver().openInputStream(data);
	         FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
	         copyStream(inputStream, fileOutputStream);
	         fileOutputStream.close();
	         inputStream.close(); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	} 
	
	/**
	 * 开始剪切图片
	 * @param activity
	 */
	public static void startCutImg(Activity activity) {
			try {
		    	//裁剪图片
		    	 ClipImageActivity.prepare()
		         .aspectX(3).aspectY(2)
		         .inputPath(mFileTemp.getPath())
		         .outputPath(mFileCut.getPath())
		         .startForResult(activity, CodeConstant.Code_Cut_Back);
		}catch(Exception ex) {
			Log.d("启动出错", ex.getMessage());
		}
	}
	 
	
	/**
	 * 给图片添加水印
	 * @param gText
	 * @param isCutFile
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException
	 */
	public static void wartermarkImg(String gText){  
		try {
            float fontSize = 8 * 10;  
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(mFileTemp));  
              
            //bitmap = scaleWithWH(bitmap, 300*scale, 300*scale);
             
            android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();   
             
            // set default bitmap config if none  
            if(bitmapConfig == null) {  
              bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;  
            }  
            // resource bitmaps are imutable,   
            // so we need to convert it to mutable one  
            bitmap = bitmap.copy(bitmapConfig, true);  
              
            Canvas canvas = new Canvas(bitmap);  
            // new antialised Paint  
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);  
            // text color - #3D3D3D  
            paint.setColor(Color.argb(200, 255, 255, 255));    
            paint.setTextSize(fontSize);              
             paint.setDither(true); //获取跟清晰的图像采样  
             paint.setFilterBitmap(true);//过滤一些  
            Rect bounds = new Rect();  
            paint.getTextBounds(gText, 0, gText.length(), bounds);
            //宽度百分比
            float percentX = bitmap.getWidth()*0.5f;
            //高度百分比
            float percentY = bitmap.getHeight()*0.5f;
            //文字长度
            float textWidth = fontSize * gText.length()/2; 
            canvas.drawText(gText, bitmap.getWidth()-percentX-textWidth, bitmap.getHeight()-percentY, paint);  
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mFileTemp)); 
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();  
            bos.close();
		}catch(IOException ex) {
			Log.e("给图片加水印失败", ex.getMessage());
		}
	}
	
	private static Bitmap scaleWithWH(Bitmap src, double w, double h) {
        if (w == 0 || h == 0 || src == null) {
            return src;
        } else {
            // 记录src的宽高
            int width = src.getWidth();
            int height = src.getHeight();
            // 创建一个matrix容器
            Matrix matrix = new Matrix();
            // 计算缩放比例
            float scaleWidth = (float) (w / width);
            float scaleHeight = (float) (h / height);
            // 开始缩放
            matrix.postScale(scaleWidth, scaleHeight);
            // 创建缩放后的图片
            return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
        }
    }
	
	/**
	 * 获取文件
	 * @param isCutFile
	 * @return
	 */
	public static File getFile(boolean isCutFile) {
			return isCutFile? mFileCut: mFileTemp;
	}
	
	/**
	 * 压缩图片
	 */
	public static File compressImg(boolean isCutFile) {
		
		// 获取图片
		File imgFile = isCutFile? mFileCut: mFileTemp;

		InputStream input = null;
		OutputStream compressStream = null;
		// 压缩图片文件
		try {
			input = new FileInputStream(imgFile);

			Bitmap bitmap = BitmapFactory.decodeStream(input);

			int width = (int) (bitmap.getWidth() * 0.4);
			int height = (int) (bitmap.getHeight() * 0.4);

			// 压缩后的bitmap
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT); 

			compressStream = new FileOutputStream(mFileCompress);
			// 将bitmap源文输出
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, compressStream);
			compressStream.flush();
			// 压缩文件
			return mFileCompress;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if (compressStream != null) {
					compressStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
		
	}
	
	
	private static void copyStream(InputStream input, OutputStream output)
	            throws IOException {

	        byte[] buffer = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = input.read(buffer)) != -1) {
	            output.write(buffer, 0, bytesRead);
	        }
	    }
	
}
