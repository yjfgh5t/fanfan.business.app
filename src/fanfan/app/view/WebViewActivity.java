package fanfan.app.view;
 

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
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import fanfan.app.constant.CodeConstant;
import fanfan.app.manager.MediaManager;
import fanfan.app.manager.PrintManager;
import fanfan.app.util.PhotoUtil;
import fanfan.app.view.webview.JavaScriptAPI;
import fanfan.app.view.webview.JavaScriptImpl;
import fanfan.app.view.webview.X5WebView;
import fanfan.business.app.R; 

public class WebViewActivity extends Activity {

	X5WebView webView;
	
	JavaScriptAPI javaScriptAPI;
	
	private static String tempFile="temp_photo.jpg";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		webView = (X5WebView) findViewById(R.id.full_web_webview);
		
		initWebView();
	}

	/**
	 * 操作返回
	 */
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
        if (resultCode != RESULT_OK) { 
            return;
        }

        switch (requestCode) {
            case CodeConstant.Code_Choise_Img:
            	 PhotoUtil.setPhotoData(data.getData());
            	 //执行截图
            	 PhotoUtil.startCutImg(this);
            	break;
            case CodeConstant.Code_Take_Photo:
            	 //执行截图
            	 PhotoUtil.startCutImg(this);
            	break;
            case CodeConstant.Code_Cut_Back:
            	//获取图片
            	javaScriptAPI.uploadFile(PhotoUtil.getFile(true));
            	break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
	
	/**
	 * Activity重新显示
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		javaScriptAPI.webViewCallBack("", CodeConstant.Notify_Msg_CallKey+".notify-click");
	}
	
	/**
	 * 回退键
	 */
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		javaScriptAPI.webViewCallBack("回退", CodeConstant.Notify_Msg_CallKey+".back-key");
	}
	
	
	@SuppressWarnings("static-access")
	@SuppressLint("NewApi")
	private void initWebView() {
		//加载Url地址
		//webView.loadUrl(VersionManager.getInstrance().getIndexPath());
		
		webView.loadUrl("http://192.168.1.10:8080");
		 
		getWindow().setFormat(PixelFormat.TRANSLUCENT);

		webView.setWebContentsDebuggingEnabled(true);
		
		webView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
		
		//曝光js接口
		javaScriptAPI = new JavaScriptImpl(webView, this);
		
		webView.addJavascriptInterface(javaScriptAPI, "android");
	}
}
