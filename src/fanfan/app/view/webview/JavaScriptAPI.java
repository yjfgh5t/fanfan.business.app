package fanfan.app.view.webview;

import java.io.File;

public interface JavaScriptAPI {
	/**
	 * 调用ajax方法
	 * @param method
	 * @param url
	 * @param params
	 * @param callBackKey
	 */
	void ajaxGet(String url,String jsonParams,String callBackKey); 
	
	/**
	 * 调用ajax方法
	 * @param method
	 * @param url
	 * @param params
	 * @param callBackKey
	 */
	void ajaxPost(String url,String jsonParams,String callBackKey); 
	
	/**
	 * 获取key val 值
	 * @param key
	 */
	void getKeyVal(String key,final String callBackKey);
	
	/**
	 * 设置key val 值
	 * @param key
	 * @param val
	 * @param callBackKey
	 */
	 void setKeyVal(final String key,final String val,final String callBackKey);
	 
	 /**
	  * 绑定用户
	  * @param customerId
	  * @param callBackKey
	  */
	 void bindUser(final String customerId,final String callBackKey);
	
	/**
	 * 蓝牙操作
	 * @param start 开启、停止
	 * @param callBackKey
	 */
	void blueTooth(boolean start,final String callBackKey);
	
	/**
	 * 选择照片
	 * @param takePhoto 1:拍照 2:打开相册
	 * @param callBackKey
	 */
	void choiceImg(final int takePhoto,String callBackKey);
	
	/**
	 * 退出app
	 */
	void exitApp();
	
	/**
	 * 调用webView 方法
	 * @param data
	 * @param callBackKey
	 */
	void webViewCallBack(final String data,final String callBackKey);
	
	/**
	 * 执行上传文件
	 * @param file
	 */
	void uploadFile(final File file);
	
	/**
	 * 退出登录
	 * @param userId
	 * @param callBackKey
	 */
	void loginOut(String userId,String callBackKey);
}
