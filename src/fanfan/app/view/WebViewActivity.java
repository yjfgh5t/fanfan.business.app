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
		initWebView();
		BlueUtils.getInstance().init(this);
	}

	/**
	 * 操作返回
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case CodeConstant.Code_Choise_Img:
			PhotoUtil.setPhotoData(data.getData());
			// 执行截图
			PhotoUtil.startCutImg(this);
			break;
		case CodeConstant.Code_Take_Photo:
			// 执行截图
			PhotoUtil.startCutImg(this);
			break;
		case CodeConstant.Code_Cut_Back:
			// 获取图片
			File imgFile = PhotoUtil.getFile(true);

			InputStream input = null;
			OutputStream compressStream = null;
			// 压缩图片文件
			try {
				input = new FileInputStream(imgFile);

				Bitmap bitmap = BitmapFactory.decodeStream(input);

				int width = (int) (bitmap.getWidth() * 0.4);
				int height = (int) (bitmap.getHeight() * 0.4);

				// 压缩后的bitmap
				bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

				File outputFile = new File(Environment.getExternalStorageDirectory(), "compress-img.jpg");

				compressStream = new FileOutputStream(outputFile);
				// 将bitmap源文输出
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, compressStream);
				compressStream.flush();
				// 压缩文件
				javaScriptAPI.uploadFile(outputFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (input != null) {
						input.close();
					}
					if (compressStream != null) {
						compressStream.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			break;
		case CodeConstant.Code_San_QRCode:
			if (data != null && data.hasExtra("result")) {
				javaScriptAPI.resultScanQRCode(data.getStringExtra("result"));
			}
			break;
		}
	}

	/**
	 * Activity重新显示
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		javaScriptAPI.webViewCallBack("", CodeConstant.Notify_Msg_CallKey + ".notify-click");
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
}
