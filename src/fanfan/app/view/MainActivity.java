package fanfan.app.view;
 
import fanfan.business.app.R; 
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem; 

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		init(); 
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	
	private void init() {
		
		 XGPushConfig.enableDebug(this,true);
		 
		 XGPushManager.registerPush(this,new XGIOperateCallback() {
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
		
	}
	
}
