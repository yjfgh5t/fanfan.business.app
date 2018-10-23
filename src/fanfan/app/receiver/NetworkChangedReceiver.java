package fanfan.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import fanfan.app.util.NetworkUtils;
import fanfan.app.view.webview.JavaScriptImpl;

public class NetworkChangedReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		//网络是否可用
		if(NetworkUtils.isConnected()) {
			//绑定信鸽
			JavaScriptImpl.getInstrance().bindXG("");
		}
	}

}
