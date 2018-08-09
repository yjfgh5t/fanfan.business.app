package fanfan.app.constant;

import android.os.Environment;

public class SPConstant {

	/**
	 * 用户Id
	 */
	public static final String customerId ="sp_customer_id";
	
	 
	/**
	 * html版本
	 */
	public static final String htmlVersion ="sp_html_version";
	
	/**
	 * html当前安装自带版本
	 */
	public static final String htmlInstallVersion="1.0.0";
	
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
