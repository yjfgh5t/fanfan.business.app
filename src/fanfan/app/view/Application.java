package fanfan.app.view;
 
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushReceiver;
import com.tencent.smtt.sdk.QbSdk;

import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import fanfan.app.constant.SPConstant;
import fanfan.app.manager.VersionManager;
import fanfan.app.receiver.ForegroundReceiver;
import fanfan.app.service.ForegroundService;
import fanfan.app.util.SPUtils;

public class Application extends android.app.Application {
 

	@Override
	public void onCreate() {
		try {
		//加载选择X5内核
		initTBS();
		
		//创建服务
		initService();
		
		//初始设置数据
		intiData();
		
		//刷新Html版本
		VersionManager.getInstrance().refshHtmlVersion(); 
		
		}catch(Exception ex) {
			Log.e("应用启动异常", ex.getLocalizedMessage());
		}
    }
	
	
	/**
	 * 加载选择X5内核
	 */
	private void initTBS() {
		
		//搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。 
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() { 
			@Override
			public void onViewInitFinished(boolean arg0) {
				// TODO Auto-generated method stub
				//x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
				Log.d("app", " onViewInitFinished is " + arg0);
			}
			
			@Override
			public void onCoreInitFinished() {
				// TODO Auto-generated method stub
			}
		};
		//x5内核初始化接口
		QbSdk.initX5Environment(getApplicationContext(),  cb); 
	}
	
	/**
	 * 加载服务
	 */
	private void initService() {
		//启动前台通知服务
		Intent  intent = new Intent(this,ForegroundService.class); 
		startService(intent);
	}
	
	/**
	 * 默认自动打印
	 */
	private void intiData() {
		//初始设置自动打印
		if(SPUtils.getInstance().getString(SPConstant.autoPrint).equals("")) {
			SPUtils.getInstance().put(SPConstant.autoPrint, "true");
		}
		
	}
}
