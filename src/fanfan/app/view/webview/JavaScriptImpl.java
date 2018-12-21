package fanfan.app.view.webview;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import fanfan.app.constant.CodeConstant;
import fanfan.app.constant.SPConstant;
import fanfan.app.constant.UrlConstant;
import fanfan.app.manager.BlueToothManager;
import fanfan.app.manager.OkHttpManager;
import fanfan.app.manager.PrintManager;
import fanfan.app.manager.VersionManager;
import fanfan.app.model.APIResponse;
import fanfan.app.model.OrderPrintModel;
import fanfan.app.model.PictureOptionModel;
import fanfan.app.model.Response;
import fanfan.app.util.EncryptUtils;
import fanfan.app.util.FileUtils;
import fanfan.app.util.PhotoUtil;
import fanfan.app.util.SPUtils;
import fanfan.app.util.StringUtils;
import fanfan.app.util.Utils;
import fanfan.app.view.ScanActivity;
import fanfan.app.view.WebViewActivity;

public class JavaScriptImpl implements JavaScriptAPI {

	X5WebView webView;

	WebViewActivity webViewActivity;

	private String choiseCallbackKey, sacnQRCodeCallbackKey, loadingKey = "loading";

	private static JavaScriptImpl instrance;

	private boolean hasBindXG = false;

	private Response<Object> blueResponse;

	private PictureOptionModel optionModel;

	/**
	 * 获取当前实例
	 * 
	 * @return
	 */
	public static JavaScriptImpl getInstrance() {
		return instrance;
	}

	public JavaScriptImpl(X5WebView webView, WebViewActivity webViewActivity) {
		this.webView = webView;
		this.webViewActivity = webViewActivity;
		instrance = this;

		// 初始化蓝牙操作的默认返回
		blueResponse = new Response<Object>() {
			@Override
			public void callBack(APIResponse<Object> response) {
				// TODO Auto-generated method stub
				webViewCallBack(JSON.toJSONString(response), CodeConstant.Notify_Blue_ToothKey);
			}
		};
	}

	// get提交
	@Override
	@JavascriptInterface
	public void ajaxGet(final String url, final String jsonParams, final String callBackKey) {
		final Map<String, Object> params = JSONObject.parseObject(jsonParams, Map.class);
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				OkHttpManager.getInstrance().get(UrlConstant.domain + url, params, new Response<Object>() {
					@Override
					public void callBack(final APIResponse<Object> response) {
						webViewCallBack(JSON.toJSONString(response), callBackKey);
					}
				});
			}

		});
	}

	// post提交
	@Override
	@JavascriptInterface
	public void ajaxPost(final String url, final String jsonParams, final String callBackKey) {
		final Map<String, Object> params = JSONObject.parseObject(jsonParams, Map.class);
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				OkHttpManager.getInstrance().post(UrlConstant.domain + url, params, new Response<Object>() {
					@Override
					public void callBack(final APIResponse<Object> response) {
						// TODO Auto-generated method stub
						webViewCallBack(JSON.toJSONString(response), callBackKey);
					}
				});
			}

		});
	}

	/**
	 * 获取固定的key
	 */
	@JavascriptInterface
	public void getKeyVal(final String key, final String callBackKey) {
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				webViewCallBack(SPUtils.getInstance().getString(key), callBackKey);
			}
		});
	}

	/**
	 * 获取固定的key
	 */
	@JavascriptInterface
	public void setKeyVal(final String key, final String val, final String callBackKey) {
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SPUtils.getInstance().put(key, val);
				webViewCallBack(SPUtils.getInstance().getString(key), callBackKey);
			}
		});
	}

	/**
	 * 蓝牙操作
	 */
	@Override
	@JavascriptInterface
	public void blueTooth(final String event, final String callBackKey) {
		// TODO Auto-generated method stub
		new Handler().post(new Runnable() {
			public void run() {
				switch (event) {
				case "start":
					BlueToothManager.getInstrance().startScaneBlue(webViewActivity, blueResponse);
					break;
				case "stop":
					BlueToothManager.getInstrance().stopScaneBlue();
					break;
				case "state":
					BlueToothManager.getInstrance().connectState();
					break;
				}
			}
		});
	}

	/**
	 * 连接蓝牙
	 */
	@Override
	@JavascriptInterface
	public void blueToothConnect(final String address, final String callBackKey) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			public void run() {
				BlueToothManager.getInstrance().connectBlue(address, blueResponse);
			}
		}).start();
	}

	/**
	 * 扫码
	 */
	@Override
	@JavascriptInterface
	public void scanQRCode(String callBackKey) {
		sacnQRCodeCallbackKey = callBackKey;
		// TODO Auto-generated method stub
		new Handler().post(new Runnable() {
			public void run() {
				webViewActivity.startActivityForResult(new Intent(webViewActivity, ScanActivity.class),
						CodeConstant.Code_San_QRCode);
			}
		});
	}

	/**
	 * 打印
	 */
	@Override
	@JavascriptInterface
	public void print(final String jsonString,final String printType, final String callBackKey) {
		new Handler().post(new Runnable() {
			public void run() {
				boolean success = false;
				switch(printType) {
					// 打印订单
					case "order":
						OrderPrintModel printModel = JSONObject.parseObject(jsonString, OrderPrintModel.class);
						success = PrintManager.getInstrance().printOrder(printModel);
						break;
					// 打印图片
					case "img":
						byte[] imgData = Base64.decode(jsonString, Base64.DEFAULT);
						success = PrintManager.getInstrance().printImg(imgData);
						break;
				}
				webViewCallBack(success ? "true" : "false", callBackKey);
			}
		});
	}

	@Override
	// 回调js方法
	public void webViewCallBack(final String data, final String callBackKey) {
		// TODO Auto-generated method stub
		webView.post(new Runnable() {
			@Override
			public void run() {
				String result = "";
				if (data != null) {
					result = data;
				}
				Log.d("webview", result);
				if (result.indexOf("{") == 0) {
					// 回调js方法
					webView.loadUrl((String.format("javascript:window.callback(%s,\"%s\")", result, callBackKey)));
				} else {
					// 回调js方法
					webView.loadUrl((String.format("javascript:window.callback(\"%s\",\"%s\")", result, callBackKey)));
				}
			}
		});
	}

	/**
	 * 打开相册
	 */
	@Override
	@JavascriptInterface
	public void choiceImg(final String pictureOption, final String callBackKey) {
		optionModel = JSONObject.parseObject(pictureOption, PictureOptionModel.class);
		choiseCallbackKey = callBackKey;
		// TODO Auto-generated method stub
		new Handler().post(new Runnable() {
			public void run() {
				switch (optionModel.getOpenType()) {
				case 1:
					// 拍照
					PhotoUtil.takePhoto(webViewActivity);
					break;
				case 2:
					// 打开相册
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
		if (file == null) {
			return;
		}
		// TODO Auto-generated method stub
		final Map<String, Object> params = new HashMap<>();
		params.put("content-type", "uploadFile");
		params.put("content", file);
		OkHttpManager.getInstrance().post(UrlConstant.uploadFile, params, new Response<String>() {

			@Override
			public void callBack(APIResponse<String> response) {
				// TODO Auto-generated method stub
				if (response.isSuccess()) {
					webViewCallBack(JSON.toJSONString(response), choiseCallbackKey);
				}
			}
		});
		// 通知前端 加载进度条
		webViewCallBack("", loadingKey);
	}

	@Override
	@JavascriptInterface
	public void bindUser(String customerId, String callBackKey) {
		SPUtils.getInstance().put(SPConstant.customerId, Integer.parseInt(customerId));
		// TODO Auto-generated method stub
		bindXG(callBackKey);
	}

	@Override
	@JavascriptInterface
	public void loginOut(String userId, String callBackKey) {
		// XGPushManager.delAccount(Utils.getApp(),"*");
		webViewCallBack("", callBackKey);
	}

	@SuppressLint("NewApi")
	@Override
	@JavascriptInterface
	public void exitApp() {
		webViewActivity.finishAffinity();
		System.exit(0);
	}

	@SuppressLint("NewApi")
	@Override
	@JavascriptInterface
	public void checkOrInstallAPK(int checkOrInstall, String callBackKey) {
		// TODO Auto-generated method stub
		// 检测是否有新apk安装
		if (checkOrInstall == 1) {
			if (FileUtils.isFile(SPConstant.sdCardWWWPath + "/fanfan.apk")) {
				boolean hasInstall = false;
				// 当前安装apk和服务apk版本是否一致
				String downLoadVersion = SPUtils.getInstance().getString(SPConstant.downLoadAPKVersion);
				if (!StringUtils.isTrimEmpty(downLoadVersion)) {
					hasInstall = !downLoadVersion.equals(Utils.getAppVersion());
				}
				// 没有新版本安装时 检查服务是否有新版本安装
				if (!hasInstall) {
					// 刷新Html版本
					VersionManager.getInstrance().refshHtmlVersion();
				}
				webViewCallBack(hasInstall ? "true" : "false", callBackKey);
			}
		} else {
			// 安装apk
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://" + SPConstant.sdCardWWWPath + "/fanfan.apk"),
					"application/vnd.android.package-archive");
			webViewCallBack("true", callBackKey);
			webViewActivity.startActivity(intent);
		}
	}
	


	@SuppressLint("NewApi")
	@Override
	@JavascriptInterface
	public void saveImage(String imgData, String name, String callBackKey) {
		// TODO Auto-generated method stub
		byte[] data = Base64.decode(imgData, Base64.DEFAULT);
		Bitmap bitmap = null;
		bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		PhotoUtil.saveBmp2Gallery(bitmap, name, webViewActivity);
		webViewCallBack("true", callBackKey);
	}

	/**
	 * 加载信鸽
	 */
	@SuppressLint("NewApi")
	@Override
	public void bindXG(final String callBackKey) {
		// 注册接口
		// XGPushConfig.enableDebug(Utils.getApp(),true);
		int customerId = SPUtils.getInstance().getInt(SPConstant.customerId, 0);
		if (customerId != 0 && !hasBindXG) {
			// 防止重复绑定
			hasBindXG = true;

			XGPushManager.bindAccount(Utils.getApp(), customerId + "", new XGIOperateCallback() {
				@Override
				public void onSuccess(Object data, int flag) {
					hasBindXG = true;
					// token在设备卸载重装的时候有可能会变
					if (!StringUtils.isEmpty(callBackKey)) {
						webViewCallBack("绑定成功", callBackKey);
					}
				}

				@Override
				public void onFail(Object data, int errCode, String msg) {
					hasBindXG = false;
					if (!StringUtils.isEmpty(callBackKey)) {
						webViewCallBack("绑定失败" + msg, callBackKey);
					}
				}
			});
		} else {
			if (!StringUtils.isEmpty(callBackKey)) {
				webViewCallBack("绑定成功", callBackKey);
			}
		}
	}

	/**
	 * 打开App
	 */
	@SuppressLint("NewApi")
	@Override
	@JavascriptInterface
	public void openApp(String url, String callBackKey) {

		if (StringUtils.isEmpty(url)) {
			webViewCallBack("false", callBackKey);
			return;
		}

		//拨打电话
		if(url.indexOf("tel://")!=-1) {
			String mobile = url.substring(url.lastIndexOf("://") + 3);
			Uri uri = Uri.parse("tel:"+mobile);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			webViewActivity.startActivity(intent);
			webViewCallBack("true", callBackKey);
		}else {
			//打开其它App
			Uri uri = Uri.parse(url);
			String host = uri.getHost();
			String scheme = uri.getScheme();
			// host 和 scheme 都不能为null
			if (!StringUtils.isEmpty(host) && !StringUtils.isEmpty(scheme)) {
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				if (isInstall(intent)) {
					webViewActivity.startActivity(intent);
					webViewCallBack("true", callBackKey);
				}else {
					webViewCallBack("false", callBackKey);
				}
			}
		}
	}

	/**
	 * 获取登录的Code
	 */
	@SuppressLint("NewApi")
	@Override
	@JavascriptInterface
	public void getLoginCode(final String timeSpan, final String callBackKey) {
		// TODO Auto-generated method stub
		String userStr = SPUtils.getInstance().getString(SPConstant.userInfo, "");
		String code = "";
		if (!StringUtils.isEmpty(userStr)) {
			JSONObject userObject = JSONObject.parseObject(userStr);
			Integer userId = userObject.getInteger("userId");
			String pwd = userObject.getString("code");
			String emptyStr = String.format("%d%s%s%s", userId, "!@#QWE", timeSpan, pwd);
			code = EncryptUtils.simpleMD5Encrypt(emptyStr);
		}
		webViewCallBack(code, callBackKey);
	}

	@Override
	public void refreshView() {

		webView.loadUrl(UrlConstant.indexUrl);
	}

	// 判断app是否安装
	private boolean isInstall(Intent intent) {
		return Utils.getApp().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
				.size() > 0;
	}

	/**
	 * 操作返回
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data, Activity activity) {
		if (resultCode != webViewActivity.RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case CodeConstant.Code_Choise_Img:
			PhotoUtil.setPhotoData(data.getData());

			// 是否给图片水印
			if (optionModel.getHasWatermark() == 1) {
				PhotoUtil.wartermarkImg(optionModel.getWatermark());
			}

			// 执行截图
			if (optionModel.getHasCutImage() == 1) {
				PhotoUtil.startCutImg(activity);
			} else {
				File imgFile = PhotoUtil.compressImg(false);
				// 压缩文件
				if (imgFile != null) {
					this.uploadFile(imgFile);
				}
			}

			break;
		case CodeConstant.Code_Take_Photo:
			// 是否给图片水印
			if (optionModel.getHasWatermark() == 1) {
				PhotoUtil.wartermarkImg(optionModel.getWatermark());
			}

			// 执行截图
			if (optionModel.getHasCutImage() == 1) {
				PhotoUtil.startCutImg(activity);
			} else {
				File imgFile = PhotoUtil.compressImg(false);
				// 压缩文件
				if (imgFile != null) {
					this.uploadFile(imgFile);
				}
			}
			break;
		case CodeConstant.Code_Cut_Back:
			// 获取图片
			File imgFile = PhotoUtil.compressImg(true);
			// 压缩文件
			if (imgFile != null) {
				this.uploadFile(imgFile);
			}
			break;
		case CodeConstant.Code_San_QRCode:
			if (data != null && data.hasExtra("result")) {
				webViewCallBack(data.getStringExtra("result"), sacnQRCodeCallbackKey);
			}
			break;
		}
	}

}
