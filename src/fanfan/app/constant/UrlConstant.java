package fanfan.app.constant;

public class UrlConstant {

	public static final String domain;
	
	public static final String test_domain="http://192.168.2.68:7061";
	
	/**
	 * 登录地址
	 */
	public static final String loginUrl;
	
	/**
	 * html版本
	 */
	public static final String htmlVersion;
	
	/**
	 * 下载html zip
	 */
	public static final String htmlDownload;
	
	/**
	 * 上传文件地址
	 */
	public static final String uploadFile;
	
	static{
		if(CodeConstant.Is_Dev) {
			domain="http://192.168.2.68:8081/api/";
		}else {
			domain="http://www.wxcard.com.cn/api/";
		}
		loginUrl=domain+"user/login";
		htmlVersion=domain+"/info/version";
		htmlDownload=domain+"/info/www.zip";
		uploadFile=domain+"/info/upload";
	}
}
