package fanfan.app.manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import android.util.Log;
import fanfan.app.constant.CodeConstant;
import fanfan.app.constant.SPConstant;
import fanfan.app.constant.UrlConstant;
import fanfan.app.model.APIResponse;
import fanfan.app.model.DownLoadModel;
import fanfan.app.model.Response;
import fanfan.app.model.VersionModel;
import fanfan.app.util.FileIOUtils;
import fanfan.app.util.FileUtils;
import fanfan.app.util.ResourceUtils;
import fanfan.app.util.SPUtils;
import fanfan.app.util.ZipUtils;
import fanfan.app.view.webview.JavaScriptImpl;

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
			FileUtils.createOrExistsDir(SPConstant.sdCardPath+"/temp/www");
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
		
		//未安装 /文件被删除
		if(getCurrentHtmlVersion().equals("0.0.0") || !FileUtils.isDir(SPConstant.sdCardWWWPath)) {
			//copy assets至SDCard
			Boolean success = ResourceUtils.copyFileFromAssets(assetsPath, SPConstant.sdCardWWWPath+"/www.zip");
			if(success) {
				//解压文件
				unHtmlZip();
				//设置版本
				SPUtils.getInstance().put(SPConstant.htmlVersion, SPConstant.htmlInstallVersion);
				isRefeash=false;
				//刷新是否需要更新版本
				refshHtmlVersion();
			}
		//检查新版本
		}else{
			OkHttpManager.getInstrance().get(UrlConstant.version, new Response<JSONObject>(){
				@Override
				public void callBack(APIResponse<JSONObject> response) {
					//当前版本和线上版本不匹配
					if(response.isSuccess()) {
						VersionModel version = response.getData().toJavaObject(VersionModel.class);
						//或者有新版本是 下载文件
						if(!version.getHtmlVersion().equals(getCurrentHtmlVersion())) {
							downLoadHtmlZip(version);
						}
					}
				}
			});
		}
		
		isRefeash=false;
	}
	
	/**
	 * 下载最新Html版本
	 */
	private  void downLoadHtmlZip(final VersionModel version) {
		
		Map<String, Object> params = new HashMap<>(); 
		params.put("content-type", "file");
		
		//下载文件
		OkHttpManager.getInstrance().get(UrlConstant.htmlDownload,params, new Response<DownLoadModel>() {

			@Override
			public void callBack(APIResponse<DownLoadModel> response) {
				// TODO Auto-generated method stub
				if(response.isSuccess()) {
					//删除老的www.zip文件
					FileUtils.deleteFile(SPConstant.sdCardPath+"/temp/www.zip");
					//写入至SDCard
					boolean writeSuccess = FileIOUtils.writeFileFromIS(SPConstant.sdCardPath+"/temp/www.zip", response.getData().getInputStream());
					
					if(writeSuccess && FileUtils.isFile(SPConstant.sdCardPath+"/temp/www.zip")){
						//解压文件
						if(unHtmlZip()) {
							//删除old文件
							if(FileUtils.deleteAllInDir(SPConstant.sdCardWWWPath)) {
								//coup新文件
								if(FileUtils.copyDir(SPConstant.sdCardPath+"/temp/www/", SPConstant.sdCardWWWPath+"/")) {
									//设置版本
									SPUtils.getInstance().put(SPConstant.htmlVersion, version.getHtmlVersion());
									SPUtils.getInstance().put(SPConstant.downLoadAPKVersion, version.getAndroidVersion());
									//发送更新版本消息
									if(JavaScriptImpl.getInstrance()!=null) {
										JavaScriptImpl.getInstrance().webViewCallBack("", CodeConstant.Notify_Msg_CallKey+".new-version");
									}
								}
							}
						}
					}
				}
			}
		});
	}
	
	private boolean unHtmlZip() {
		try {
		 //删除 temp/www 文件
		 FileUtils.deleteAllInDir(SPConstant.sdCardPath+"/temp/www");
		 List<File> files  = ZipUtils.unzipFile(SPConstant.sdCardPath+"/temp/www.zip", SPConstant.sdCardPath+"/temp/www/");
		 for(File file:files) {
			 Log.d("解压文件", file.getName());
		 }
		 return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
}
