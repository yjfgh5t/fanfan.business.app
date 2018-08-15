package fanfan.app.view;
 
import java.util.Map;

import com.tencent.smtt.sdk.QbSdk;

import android.app.PendingIntent;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import fanfan.app.constant.SPConstant;
import fanfan.app.manager.BlueToothManager;
import fanfan.app.manager.VersionManager;
import fanfan.app.model.APIResponse;
import fanfan.app.model.Response;
import fanfan.app.service.ForegroundService;
import fanfan.app.util.SPUtils;
import fanfan.app.util.StringUtils;
import fanfan.app.util.Utils; 

public class Application extends android.app.Application {
 

	@Override
	public void onCreate() {
		
		//加载选择X5内核
		initTBS();
		
		//创建服务
		initService();
		
		//刷新Html版本
		VersionManager.getInstrance().refshHtmlVersion(); 
		
		//链接蓝牙
		initBlueToothService();
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
		
		 IntentFilter filter = new IntentFilter("MY_ACTION");
		//启动前台通知服务
		Intent  intent = new Intent(this,ForegroundService.class); 
		startService(intent);
	}
	
	/**
	 * 加载蓝牙服务
	 */
	private void initBlueToothService() {
		String blueData = SPUtils.getInstance().getString(SPConstant.blueToothConnect);
		//获取数据
		if(!StringUtils.isEmpty(blueData)) {
			final String [] tempData = blueData.split(";");
			if(tempData.length>1) {
				BlueToothManager.getInstrance().startScaneBlue(getApplicationContext(), new Response<Object>() {
					@Override
					public void callBack(APIResponse<Object> response) {
						// TODO Auto-generated method stub
						Map<String,Object> params = (Map<String,Object>)response;
						if("stop".equals(params.get("event"))) {
							BlueToothManager.getInstrance().connectBlue(tempData[1], new Response<Object>() {
								@Override
								public void callBack(APIResponse<Object> response) {
									// TODO Auto-generated method stub
									System.out.println("链接成功");
								}});
						}
					}
				});
			}
		}
	}
}
