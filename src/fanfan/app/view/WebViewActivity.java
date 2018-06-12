package fanfan.app.view;
 

import java.util.Map;

import com.alibaba.fastjson.JSON;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import fanfan.app.constant.UrlConstant;
import fanfan.app.manager.BlueToothManager;
import fanfan.app.manager.OkHttpManager;
import fanfan.app.manager.VersionManager;
import fanfan.app.model.APIResponse;
import fanfan.app.model.Response;
import fanfan.app.util.BlueToothUtils;
import fanfan.app.util.ResourceUtils;
import fanfan.app.util.SPUtils;
import fanfan.business.app.R; 

public class WebViewActivity extends Activity {

	X5WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		webView = (X5WebView) findViewById(R.id.full_web_webview);
		
		initWebView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	@SuppressWarnings("static-access")
	@SuppressLint("NewApi")
	private void initWebView() { 
		//加载Url地址
		//webView.loadUrl(VersionManager.getInstrance().getIndexPath());
		
		webView.loadUrl("http://192.168.2.68:8080");
		 
		getWindow().setFormat(PixelFormat.TRANSLUCENT);

		webView.setWebContentsDebuggingEnabled(true);
		
		webView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
		
		webView.addJavascriptInterface(new JavaScriptAPI() {

			//get提交
			@Override
			@JavascriptInterface
			public void ajaxGet(final String url,final Map<String,Object> params,final String callBackKey) { 
				new Handler().post(new Runnable() { 
					@Override
					public void run() {
						// TODO Auto-generated method stub
						OkHttpManager.getInstrance().get(UrlConstant.domain+url, params, new Response<Object>() { 
							@Override
							public void callBack(final APIResponse<Object> response) {
								webViewCallBack(JSON.toJSONString(response),callBackKey);
							}
						});
					}
					
				});
			}
			
			//post提交
			@Override
			@JavascriptInterface
			public void ajaxPost(final String url,final Map<String,Object> params,final String callBackKey) {
				new Handler().post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						OkHttpManager.getInstrance().post(UrlConstant.domain+url, params, new Response<Object>() { 
							@Override
							public void callBack(final APIResponse<Object>  response) { 
								// TODO Auto-generated method stub 
									webViewCallBack(JSON.toJSONString(response),callBackKey);  
							}
						});
					}
					
				}); 
			}
			
			/**
			 * 获取固定的key
			 */
			@JavascriptInterface
			public void getKeyVal(final String key,final String callBackKey) { 
				new Handler().post(new Runnable() { 
					@Override
					public void run() {
						// TODO Auto-generated method stub
						webViewCallBack(SPUtils.getInstance().getString(key),callBackKey);
					}
				}); 
			}
			
			/**
			 * 蓝牙操作
			 */
			@Override
			@JavascriptInterface
			public void blueTooth(final Boolean start,final String callBackKey) {
				// TODO Auto-generated method stub
				new Handler().post(new Runnable() {
					public void run() {
						if(start) {
							BlueToothManager.getInstrance().startScaneBlue(new Response<Object>() {
								
								@Override
								public void callBack(APIResponse<Object> response) {
									// TODO Auto-generated method stub
									webViewCallBack(JSON.toJSONString(response), callBackKey);
								}
							});
						}else {
							BlueToothManager.getInstrance().stopScaneBlue();
						}
					}
				});
			}
			
			//回调js方法
			private void webViewCallBack(final String data,final String callBackKey) {
				// TODO Auto-generated method stub 
				webView.post(new Runnable() {
					@Override
					public void run() {
						if(data.indexOf("{")==0) {
							// 回调js方法
							webView.loadUrl((String.format("javascript:window.callback(%s,\"%s\")",data,callBackKey)));
						}else {
							// 回调js方法
							webView.loadUrl((String.format("javascript:window.callback(\"%s\",\"%s\")",data,callBackKey)));
						}
					}
				});
			}

		
			
		} , "android");  
	}
	
	
	public interface JavaScriptAPI{
		
		/**
		 * 调用ajax方法
		 * @param method
		 * @param url
		 * @param params
		 * @param callBackKey
		 */
		void ajaxGet(String url,Map<String,Object> params,String callBackKey); 
		
		/**
		 * 调用ajax方法
		 * @param method
		 * @param url
		 * @param params
		 * @param callBackKey
		 */
		void ajaxPost(String url,Map<String,Object> params,String callBackKey); 
		
		/**
		 * 获取key val 值
		 * @param key
		 */
		void getKeyVal(String key,final String callBackKey);
		
		/**
		 * 蓝牙操作
		 * @param start 开启、停止
		 * @param callBackKey
		 */
		void blueTooth(Boolean start,final String callBackKey);
	}
}
