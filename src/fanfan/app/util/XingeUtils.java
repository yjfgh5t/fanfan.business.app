package fanfan.app.util;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import android.app.Application;
import android.util.Log;

public class XingeUtils {

	
	/**
	 * 绑定信鸽
	 * @param app
	 * @param userAccount
	 * @return
	 */
	public static void bindXinge(Application app,String userAccount) {
		
		//信鸽
		 XGPushConfig.enableDebug(app,true);
		 
		 //注册信鸽
		 XGPushManager.registerPush(app,new XGIOperateCallback() {
				 @Override
				 public void onSuccess(Object data, int flag) {
				 //token在设备卸载重装的时候有可能会变
					 Log.d("TPush", "注册成功，设备token为：" + data);
				 }
				 @Override
				 public void onFail(Object data, int errCode, String msg) {
					 Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
				 }
			 });
		
		 //绑定账号
		 XGPushManager.bindAccount(app, userAccount, new XGIOperateCallback() {
			
			@Override
			public void onSuccess(Object arg0, int arg1) {
				// TODO Auto-generated method stub
				 Log.d("TPush", "绑定账号成功，设备arg1为：" + arg1);
			}
			
			@Override
			public void onFail(Object arg0, int arg1, String arg2) {
				// TODO Auto-generated method stub
				 Log.d("TPush", "绑定账号失败，设备arg2为：" + arg2);
			}
		});
		 
	}
	
}
