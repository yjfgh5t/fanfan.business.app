package fanfan.app.view;
 
import fanfan.app.constant.UrlConstant;
import fanfan.app.manager.BlueToothManager;
import fanfan.app.manager.OkHttpManager;
import fanfan.app.manager.VersionManager;
import fanfan.app.model.APIResponse;
import fanfan.app.model.Response;
import fanfan.app.util.ActivityUtils;
import fanfan.app.util.StringUtils;
import fanfan.app.util.ToastUtils;
import fanfan.app.util.Utils;
import fanfan.business.app.R;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText; 

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

	//登录按钮
	Button viewLogin=null;
	EditText viewUserName=null;
	EditText viewUserPwd=null;
			
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		init(); 
		//刷新版本
		//VersionManager.getInstrance().refshHtmlVersion();
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
	 
	
	/**
	 * 初始加载
	 */
	private void init() {
		 
		 viewLogin = (Button)findViewById(R.id.login);
		 viewUserName = (EditText) findViewById(R.id.username);
		 viewUserPwd = (EditText)findViewById(R.id.userpwd); 
		 
		 viewUserName.setText("admin");
		 viewUserPwd.setText("111111");
		 
		 //点击事件
		 viewLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//验证输入值
				if(!checkEditVal()) {
					return;
				}
			}
		});
	}
	
	/**
	 * 验证输入的值
	 * @return
	 */
	private boolean checkEditVal() { 
		
		BlueToothManager.getInstrance().connectBlue("57:4C:54:15:0C:7E", new Response<Object>() {
			@Override
			public void callBack(APIResponse<Object> response) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
			}
			
		});
		
		
		
		if(StringUtils.isEmpty(viewUserName.getText().toString())) {
			ToastUtils.showShort(R.string.empty_username);
			return false;
		}
		
		if(StringUtils.isEmpty(viewUserPwd.getText().toString())) {
			ToastUtils.showShort(R.string.empty_userpwd);
			return false;
		}
		
		return true;
	}
	
	
	
}
