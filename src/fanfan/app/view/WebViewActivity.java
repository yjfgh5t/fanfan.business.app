package fanfan.app.view;

import com.tencent.android.tpush.XGPushConfig;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import fanfan.app.constant.CodeConstant;
import fanfan.app.constant.SPConstant;
import fanfan.app.constant.UrlConstant;
import fanfan.app.manager.BlueToothManager;
import fanfan.app.manager.VersionManager;
import fanfan.app.receiver.ForegroundReceiver;
import fanfan.app.util.SPUtils;
import fanfan.app.view.webview.JavaScriptAPI;
import fanfan.app.view.webview.JavaScriptImpl;
import fanfan.app.view.webview.X5WebView;
import fanfan.business.app.R;

public class WebViewActivity extends Activity {

	X5WebView webView;

	JavaScriptAPI javaScriptAPI;

	private static String tempFile = "temp_photo.jpg";

	public static int activity_result_scan = 2;

	private ForegroundReceiver foregroundReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		webView = (X5WebView) findViewById(R.id.full_web_webview);

		// 设置信鸽初始化信息
		initXG();

		// WebView初始化信息
		initWebView();

		// 蓝牙初始化信息
		BlueToothManager.getInstrance().init(this);
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

	/**
	 * 打开界面
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// 发送广播
		Intent intent = new Intent();
		intent.setAction(CodeConstant.Notify_Alarm_Action);
		sendBroadcast(intent);
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(foregroundReceiver);
		super.onDestroy();
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

		// 绑定信鸽
		javaScriptAPI.bindXG("");

		// 设置http地址 给h5使用
		SPUtils.getInstance().put(SPConstant.httpPath, UrlConstant.domain);
	}

	private void initXG() {
		// 信鸽开启厂商推送
		XGPushConfig.enableOtherPush(getApplicationContext(), CodeConstant.Is_Dev);

		// 启用华为推送调试
		XGPushConfig.setHuaweiDebug(CodeConstant.Is_Dev);

		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);

		// 注册屏幕广播事件
		foregroundReceiver = new ForegroundReceiver();
		registerReceiver(foregroundReceiver, filter);
	}
}
