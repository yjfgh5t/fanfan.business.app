package fanfan.app.util;

import java.io.File;
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
	
	 static{
		 mFileTemp =  new File(Environment.getExternalStorageDirectory(), "temp-img.jpg");
		 mFileCut =  new File(Environment.getExternalStorageDirectory(), "temp-cut-img.jpg");
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
	 * 获取文件
	 * @param isCutFile
	 * @return
	 */
	public static File getFile(boolean isCutFile) {
			return isCutFile? mFileCut: mFileTemp;
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
