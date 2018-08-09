package fanfan.app.view.webview;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import fanfan.app.constant.SPConstant;
import fanfan.app.constant.UrlConstant;
import fanfan.app.manager.BlueToothManager;
import fanfan.app.manager.OkHttpManager;
import fanfan.app.model.APIResponse;
import fanfan.app.model.Response;
import fanfan.app.util.PhotoUtil;
import fanfan.app.util.SPUtils;
import fanfan.app.util.Utils;
import fanfan.app.view.WebViewActivity;

public class JavaScriptImpl implements JavaScriptAPI {

	X5WebView webView;
	
	WebViewActivity webViewActivity;
	
	private String choiseCallbackKey,loadingKey="loading";
	
	private static JavaScriptImpl instrance;
	
	/**
	 * 获取当前实例
	 * @return
	 */
	public static JavaScriptImpl getInstrance() {
		return instrance;
	}
	
	public JavaScriptImpl(X5WebView webView,WebViewActivity webViewActivity) {
		this.webView = webView;
		this.webViewActivity = webViewActivity;
		instrance = this;
	}
	
	//get提交
	@Override
	@JavascriptInterface
	public void ajaxGet(final String url,final String jsonParams,final String callBackKey) { 
		final Map<String,Object> params = JSONObject.parseObject(jsonParams, Map.class);
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
	public void ajaxPost(final String url,final String jsonParams,final String callBackKey) {
		final Map<String,Object> params = JSONObject.parseObject(jsonParams, Map.class);
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
	 * 获取固定的key
	 */
	@JavascriptInterface
	public void setKeyVal(final String key,final String val,final String callBackKey) { 
		new Handler().post(new Runnable() { 
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SPUtils.getInstance().put(key, val);
				webViewCallBack(SPUtils.getInstance().getString(key),callBackKey);
			}
		}); 
	}
	
	/**
	 * 蓝牙操作
	 */
	@Override
	@JavascriptInterface
	public void blueTooth(final boolean start,final String callBackKey) {
		
		// TODO Auto-generated method stub
		new Handler().post(new Runnable() {
			public void run() {
				if(start) {
					BlueToothManager.getInstrance().startScaneBlue(webViewActivity, new Response<Object>() {
						
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
	
	@Override
	//回调js方法
	public void webViewCallBack(final String data,final String callBackKey) {
		// TODO Auto-generated method stub 
		webView.post(new Runnable() {
			@Override
			public void run() {
				Log.d("webview", data);
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

	/**
	 * 打开相册
	 */
	@Override
	@JavascriptInterface
	public void choiceImg(final int takePhoto, final String callBackKey) {
		choiseCallbackKey=callBackKey;
		// TODO Auto-generated method stub
				new Handler().post(new Runnable() {
					public void run() {
						switch(takePhoto) {
						case 1:
							//拍照
							PhotoUtil.takePhoto(webViewActivity);
							break;
						case 2:
							//打开相册
							PhotoUtil.openPhoto(webViewActivity);
							break;
						}
					}
				});
	}

	/**
	 * 上传文件
	 */
	@Override
	public void uploadFile(File file) {
		if(file==null) {
			return;
		}
		
		// TODO Auto-generated method stub
		final Map<String,Object> params = new HashMap<>();
    	params.put("content-type", "uploadFile");
    	params.put("content", PhotoUtil.getFile(true));
    	OkHttpManager.getInstrance().post(UrlConstant.uploadFile, params, new Response<String>() {

			@Override
			public void callBack(APIResponse<String> response) {
				// TODO Auto-generated method stub
				if(response.isSuccess()) {
					webViewCallBack(JSON.toJSONString(response), choiseCallbackKey);
				}
			}
		});
    	//通知前端 加载进度条
    	webViewCallBack("",loadingKey);
	}

	@Override
	@JavascriptInterface
	public void bindUser(String customerId, String callBackKey) {
		SPUtils.getInstance().put(SPConstant.customerId,Integer.parseInt(customerId));
		// TODO Auto-generated method stub
		initXG(customerId,callBackKey);
	}
	
	@Override
	@JavascriptInterface
	public void loginOut(String userId,String callBackKey) {
		XGPushManager.delAccount(Utils.getApp(),"*");
		webViewCallBack("", callBackKey);
	}
	
	@SuppressLint("NewApi")
	@Override
	@JavascriptInterface
	public void exitApp() {
		webViewActivity.finishAffinity();
		System.exit(0);
	}
	
	
	/**
	 * 加载信鸽
	 */
	private void initXG(String customerId,final String callBackKey) {
		// 注册接口
        XGPushConfig.enableDebug(Utils.getApp(),true);
    	XGPushManager.bindAccount(Utils.getApp(), customerId,new XGIOperateCallback() {
	        	@Override
	          public void onSuccess(Object data, int flag) {
	           //token在设备卸载重装的时候有可能会变
	        		webViewCallBack("绑定成功",callBackKey);
	           }
	           @Override
	           public void onFail(Object data, int errCode, String msg) {
	        	   webViewCallBack("绑定失败"+msg,callBackKey);
	           }
		});
	}
}
