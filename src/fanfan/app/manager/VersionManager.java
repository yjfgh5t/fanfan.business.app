package fanfan.app.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.os.Environment;
import fanfan.app.constant.SPConstant;
import fanfan.app.constant.UrlConstant;
import fanfan.app.model.APIResponse;
import fanfan.app.model.DownLoadModel;
import fanfan.app.model.Response;
import fanfan.app.util.FileIOUtils;
import fanfan.app.util.FileUtils;
import fanfan.app.util.ResourceUtils;
import fanfan.app.util.SPUtils;
import fanfan.app.util.ZipUtils;

public class VersionManager {

	//SDCard 路径 
	static String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/www/";
	//assets 路径
	static String assetsPath = "file:///android_asset/www.zip";
	
	static VersionManager instance;
	
	public static VersionManager getInstrance() {
		if (instance == null) {
			instance = new VersionManager();
		}

		return instance;
	}
	
	/**
	 * 获取当前Html版本
	 * @return 0.0.0 未安装html  最低版本未 1.0.0
	 */
	public  String getCurrentHtmlVersion() {
		return SPUtils.getInstance().getString(SPConstant.htmlVersion,"0.0.0"); 
	}
	
	
	/**
	 * 更新html 版本
	 * @param version
	 */
	public  void refshHtmlVersion() {
		
		//未安装
		if(getCurrentHtmlVersion().equals("0.0.0")) {
			//copy assets至SDCard
			ResourceUtils.copyFileFromAssets(assetsPath, sdCardPath);
			//解压文件
			unHtmlZip();
		}else {
			 
			OkHttpManager.getInstrance().get(UrlConstant.htmlVersion, new Response<String>(){

				@Override
				public void callBack(APIResponse<String> response) {
					// TODO Auto-generated method stub
					//当前版本和线上版本不匹配
					if(response.isSuccess()&&!response.getData().equals(getCurrentHtmlVersion())) {
						//下载文件
						downLoadHtmlZip();
					}
				}
				
			});
			
		} 
	}
	
	/**
	 * 下载最新Html版本
	 */
	private  void downLoadHtmlZip() {
		
		
		//下载文件
		OkHttpManager.getInstrance().get(UrlConstant.htmlDownload, new Response<DownLoadModel>() {

			@Override
			public void callBack(APIResponse<DownLoadModel> response) {
				// TODO Auto-generated method stub
				if(response.isSuccess()) {
					
					byte _byte[] = new byte [response.getData().getContentLength()>1024?1024:response.getData().getContentLength().intValue()];
					
					//删除old的压缩包
					FileUtils.deleteFile(sdCardPath+"www.zip");
					
					//写入至SDCard
					FileIOUtils.writeFileFromIS(sdCardPath+"www.zip", response.getData().getInputStream());
					
					//解压文件
					unHtmlZip();
				}
			}
		});
	}
	
	private  void unHtmlZip() {
		
		try {
			ZipUtils.unzipFile(sdCardPath+"www.zip", sdCardPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
