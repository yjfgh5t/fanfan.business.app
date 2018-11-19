package fanfan.app.constant;

import android.os.Environment;

public class SPConstant {

	/**
	 * 用户Id
	 */
	public static final String customerId ="sp_customer_id";
	
	/**
	 * 用户信息
	 */
	public static final String userInfo = "sp_user_info";
	 
	/**
	 * html版本
	 */
	public static final String htmlVersion ="sp_html_version";
	
	/**
	 * html当前安装包自带版本
	 */
	public static final String htmlInstallVersion="1.3.2";
	
	/**
	 * 下载apk安装包版本
	 */
	public static final String downLoadAPKVersion="sp_download_apk_version";
	
	/**
	 * 前台通知标题
	 */
	public static final String notifyContentTitle = "sp_notify_content_title";
	
	/**
	 * 前台通知内容
	 */
	public static final String notifyContentText = "sp_notify_content_text";
	
	/**
	 * 自动打印
	 */
	public static final String autoPrint ="sp_auto_print";
	
	/**
	 * 店铺名称
	 */
	public static final String shopName ="sp_shop_name";
	
	/**
	 * 链接的蓝牙
	 */
	public static final String blueToothConnect="sp_blue_tooth_connect";
	
	/**
	 * domain地址
	 */
	public static final String httpPath = "sp_http_path";
	
	/**
	 * SDCard路径
	 */
	public static final String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/fanfan.business.app";
	
	/**
	 * www解压包路径
	 */
	public static final String sdCardWWWPath = sdCardPath+"/www";
	
	/**
	 * 首页路径
	 */
	public static final String sdCardIndexPath = sdCardWWWPath+"/index.html";
	
	
	 
}
