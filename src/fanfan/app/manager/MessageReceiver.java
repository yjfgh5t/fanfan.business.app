package fanfan.app.manager;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import android.content.Context;
import fanfan.app.view.webview.JavaScriptImpl;

public class MessageReceiver extends XGPushBaseReceiver {

	@Override
	public void onDeleteTagResult(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
		 
	}

	@Override
	public void onNotifactionClickedResult(Context arg0, XGPushClickedResult arg1) {
		// TODO Auto-generated method stub
		if(JavaScriptImpl.getInstrance()!=null) {
			JavaScriptImpl.getInstrance().webViewCallBack("click", "xg_msg");
		}
	}

	@Override
	public void onNotifactionShowedResult(Context arg0, XGPushShowedResult arg1) {
		// TODO Auto-generated method stub
		if(JavaScriptImpl.getInstrance()!=null) {
			JavaScriptImpl.getInstrance().webViewCallBack("show", "xg_msg");
		}
	}

	@Override
	public void onRegisterResult(Context arg0, int arg1, XGPushRegisterResult arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSetTagResult(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextMessage(Context arg0, XGPushTextMessage arg1) {
		// TODO Auto-generated method stub
		if(JavaScriptImpl.getInstrance()!=null) {
			JavaScriptImpl.getInstrance().webViewCallBack("msg", "xg_msg");
		}
	}

	@Override
	public void onUnregisterResult(Context arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
