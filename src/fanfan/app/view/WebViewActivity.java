package fanfan.app.view;

import com.tencent.android.tpush.XGPushConfig;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import fanfan.app.constant.CodeConstant;
import fanfan.app.constant.SPConstant;
import fanfan.app.constant.UrlConstant;
import fanfan.app.manager.BlueToothManager;
import fanfan.app.receiver.ForegroundReceiver;
import fanfan.app.util.SPUtils;
import fanfan.app.view.permission.PermissionHelper;
import fanfan.app.view.permission.PermissionInterface;
import fanfan.app.view.webview.JavaScriptAPI;
import fanfan.app.view.webview.JavaScriptImpl;
import fanfan.app.view.webview.X5WebView;
import fanfan.business.app.R;

public class WebViewActivity extends Activity implements PermissionInterface {

	X5WebView webView;

	JavaScriptAPI javaScriptAPI;

	private static String tempFile = "temp_photo.jpg";

	public static int activity_result_scan = 2;

	private ForegroundReceiver foregroundReceiver;
	
	private PermissionHelper mPermissionHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		
		//初始化并发起权限申请
        mPermissionHelper = new PermissionHelper(this, this);
        mPermissionHelper.requestPermissions();
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
	
	/**
	 * 销毁
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(foregroundReceiver);
		super.onDestroy();
	}
	
	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(mPermissionHelper.requestPermissionsResult(requestCode, permissions, grantResults)){
			//权限请求结果，并已经处理了该回调
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
	
	@Override
	public int getPermissionsRequestCode() {
		// TODO Auto-generated method stub
		return 999;
	}

	@Override
	public String[] getPermissions() {
		// TODO Auto-generated method stub
		return new String[]{
				// 创建文件夹权限
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                // 拨打电话权限
                Manifest.permission.READ_PHONE_STATE,
                // 地理位置权限
                Manifest.permission.ACCESS_FINE_LOCATION,
                // 相机权限
                Manifest.permission.CAMERA
        };
	}

	@Override
	public void requestPermissionsSuccess() {
		// TODO Auto-generated method stub
		initView();
	}

	@Override
	public void requestPermissionsFail() {
		// TODO Auto-generated method stub
		finish();
		System.exit(0);
	}
	
	/**
	 * 视图初始化
	 */
	private void initView() {
		webView = (X5WebView) findViewById(R.id.full_web_webview);

		// 设置信鸽初始化信息
		initXG();

		// WebView初始化信息
		initWebView();

		// 蓝牙初始化信息
		BlueToothManager.getInstrance().init(this);
	}

	@SuppressWarnings("static-access")
	@SuppressLint("NewApi")
	private void initWebView() {
		// 加载Url地址
		webView.loadUrl(UrlConstant.indexUrl);

		getWindow().setFormat(PixelFormat.TRANSLUCENT);

		webView.setWebContentsDebuggingEnabled(true);

		webView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);

		// 曝光js接口
		javaScriptAPI = new JavaScriptImpl(webView, this);

		webView.addJavascriptInterface(javaScriptAPI, "android");
		
		webView.setWebChromeClient(new WebChromeClient() {
			@Override 
			public void onReceivedIcon(WebView view, Bitmap icon) { 
				super.onReceivedIcon(view, icon);
			}
			
			@Override 
			public void onGeolocationPermissionsShowPrompt(String origin,GeolocationPermissionsCallback callback) { 
				callback.invoke(origin, true, false); 
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}
		});

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
