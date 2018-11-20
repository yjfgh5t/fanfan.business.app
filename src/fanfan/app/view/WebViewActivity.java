package fanfan.app.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import fanfan.app.constant.CodeConstant;
import fanfan.app.constant.SPConstant;
import fanfan.app.constant.UrlConstant;
import fanfan.app.manager.MediaManager;
import fanfan.app.manager.PrintManager;
import fanfan.app.manager.VersionManager;
import fanfan.app.util.BlueUtils;
import fanfan.app.util.PhotoUtil;
import fanfan.app.util.SPUtils;
import fanfan.app.util.ZipUtils;
import fanfan.app.view.webview.JavaScriptAPI;
import fanfan.app.view.webview.JavaScriptImpl;
import fanfan.app.view.webview.X5WebView;
import fanfan.business.app.R;

public class WebViewActivity extends Activity {

	X5WebView webView;

	JavaScriptAPI javaScriptAPI;

	private static String tempFile = "temp_photo.jpg";

	public static int activity_result_scan = 2; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		webView = (X5WebView) findViewById(R.id.full_web_webview);
		
		//设置信鸽初始化信息
		initXG();
		
		//WebView初始化信息
		initWebView();
		
		//蓝牙初始化信息
		BlueUtils.getInstance().init(this);
	}

	/**
	 * 操作返回
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		javaScriptAPI.onActivityResult(requestCode, resultCode, data, this);
	}

	/**
	 * Activity重新显示
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		javaScriptAPI.webViewCallBack("", CodeConstant.Notify_Msg_CallKey + ".notify-click");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//发送广播
		
		super.onResume();
	}

	/**
	 * 回退键
	 */
	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		javaScriptAPI.webViewCallBack("回退", CodeConstant.Notify_Msg_CallKey + ".back-key");
	}

	@SuppressWarnings("static-access")
	@SuppressLint("NewApi")
	private void initWebView() {
		// 加载Url地址
		if (CodeConstant.Is_Dev) {
			webView.loadUrl(UrlConstant.test_domain);
		} else {
			webView.loadUrl(VersionManager.getInstrance().getIndexPath());
		}

		getWindow().setFormat(PixelFormat.TRANSLUCENT);

		webView.setWebContentsDebuggingEnabled(true);

		webView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);

		// 曝光js接口
		javaScriptAPI = new JavaScriptImpl(webView, this);

		webView.addJavascriptInterface(javaScriptAPI, "android");
		
		//绑定信鸽
		javaScriptAPI.bindXG("");
		
		//设置http地址 给h5使用
		SPUtils.getInstance().put(SPConstant.httpPath, UrlConstant.domain);
	}
	
	private void initXG() {
		//信鸽开启厂商推送
		XGPushConfig.enableOtherPush(getApplicationContext(), true);
		
		//启用华为推送调试
		XGPushConfig.setHuaweiDebug(true);
	}
}
