package fanfan.app.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Environment;
import android.util.Log;
import fanfan.app.constant.SPConstant;
import fanfan.app.constant.UrlConstant;
import fanfan.app.model.APIResponse;
import fanfan.app.model.DownLoadModel;
import fanfan.app.model.Response;
import fanfan.app.util.FileIOUtils;
import fanfan.app.util.FileUtils;
import fanfan.app.util.ResourceUtils;
import fanfan.app.util.SPUtils;
import fanfan.app.util.Utils;
import fanfan.app.util.ZipUtils;

public class VersionManager {
	
	//assets 路径
	static String assetsPath = "www.zip";
	
	static Boolean isRefeash=false;
	
	static VersionManager instance; 
	
	public static VersionManager getInstrance() {
		if (instance == null) {
			instance = new VersionManager();
			
			//创建文件夹 
			FileUtils.createOrExistsDir(SPConstant.sdCardWWWPath);  
		}
		return instance;
	}
	
	public String getIndexPath() {
		return "file://"+SPConstant.sdCardWWWPath+"/index.html";
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
		
		if(isRefeash) {
			return;
		}
		
		isRefeash=true;
		
		//未安装
		if(getCurrentHtmlVersion().equals("0.0.0")) {
			//copy assets至SDCard
			Boolean success = ResourceUtils.copyFileFromAssets(assetsPath, SPConstant.sdCardWWWPath+"/www.zip");
			if(success) {
				//解压文件
				unHtmlZip();
				//设置版本
				SPUtils.getInstance().put(SPConstant.htmlVersion, SPConstant.htmlInstallVersion);
			}
		//文件不存在 或被删除
		}else if(!FileUtils.isDir(SPConstant.sdCardWWWPath)){
			//下载文件
			downLoadHtmlZip(getCurrentHtmlVersion());
		}
		else{
			OkHttpManager.getInstrance().get(UrlConstant.htmlVersion, new Response<String>(){

				@Override
				public void callBack(APIResponse<String> response) {
					// TODO Auto-generated method stub
					//当前版本和线上版本不匹配
					if(response.isSuccess()&&!response.getData().equals(getCurrentHtmlVersion())) {
						//下载文件
						downLoadHtmlZip(response.getData());
					}
				}
				
			}); 
		}
		
		isRefeash=false;
	}
	
	/**
	 * 下载最新Html版本
	 */
	private  void downLoadHtmlZip(final String version) {
		
		Map<String, Object> params = new HashMap<>(); 
		params.put("content-type", "file");
		
		//下载文件
		OkHttpManager.getInstrance().get(UrlConstant.htmlDownload,params, new Response<DownLoadModel>() {

			@Override
			public void callBack(APIResponse<DownLoadModel> response) {
				// TODO Auto-generated method stub
				if(response.isSuccess()) {
					//删除old文件
					FileUtils.deleteAllInDir(SPConstant.sdCardWWWPath);
					
					//写入至SDCard
					boolean writeSuccess = FileIOUtils.writeFileFromIS(SPConstant.sdCardWWWPath+"/www.zip", response.getData().getInputStream());
					
					if(writeSuccess) {
						//解压文件
						unHtmlZip();
						//设置版本
						SPUtils.getInstance().put(SPConstant.htmlVersion, version);
					}
				}
			}
		});
	}
	
	private  void unHtmlZip() {
		try {
		 List<File> files  = ZipUtils.unzipFile(SPConstant.sdCardWWWPath+"/www.zip", SPConstant.sdCardWWWPath+"/");
		 for(File file:files) {
			 Log.d("解压文件", file.getName());
		 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
