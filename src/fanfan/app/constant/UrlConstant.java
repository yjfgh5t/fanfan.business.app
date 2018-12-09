package fanfan.app.constant;

import fanfan.app.manager.VersionManager;

public class UrlConstant {

	public static final String domain;

	public static final String indexUrl;

	/**
	 * 登录地址
	 */
	public static final String loginUrl;

	/**
	 * html版本
	 */
	public static final String version;

	/**
	 * 下载html zip
	 */
	public static final String htmlDownload;

	public static final String apkDownload;

	/**
	 * 上传文件地址
	 */
	public static final String uploadFile;

	static {
		if (CodeConstant.Is_Dev) {
			domain = "http://192.168.1.10:8081/api/";
			indexUrl = "http://192.168.1.10:7061";
			// 下载地址
			htmlDownload = domain + "/info/www.zip";
			apkDownload = domain + "/info/fanfan.apk";
		} else {
			domain = "http://www.wxcard.com.cn/api/";
			indexUrl = VersionManager.getInstrance().getIndexPath();
			// 下载地址
			htmlDownload = domain + "http://static.wxcard.com.cn/www.zip";
			apkDownload = domain + "http://static.wxcard.com.cn/fanfan.apk";
		}

		loginUrl = domain + "user/login";
		version = domain + "/info/version";
		uploadFile = domain + "/info/upload";
	}
}
