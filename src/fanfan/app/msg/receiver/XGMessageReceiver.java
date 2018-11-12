package fanfan.app.msg.receiver;
 
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import android.content.Context;
import fanfan.app.model.MessageModel;
import fanfan.app.msg.IMessageReceive;
import fanfan.app.msg.MessageReceiveImpl;

public class XGMessageReceiver extends XGPushBaseReceiver {
	
	IMessageReceive messageReceive = new MessageReceiveImpl();
	
	@Override
	public void onDeleteTagResult(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onNotifactionClickedResult(Context context, XGPushClickedResult result) {
		// TODO Auto-generated method stub
		String customText = result.getCustomContent();
		messageReceive.onNotifactionClicked(context, new MessageModel(IMessageReceive.MESSAGE_FROM_XG, customText));
	}

	@Override
	public void onNotifactionShowedResult(Context context, XGPushShowedResult result) {
		// TODO Auto-generated method stub
		String customText = result.getCustomContent();
		messageReceive.onNotifactionShow(context, new MessageModel(IMessageReceive.MESSAGE_FROM_XG, customText));
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
	public void onTextMessage(Context context, XGPushTextMessage result) {
		// TODO Auto-generated method stub
		String customText = result.getCustomContent();
		messageReceive.onNotifactionShow(context, new MessageModel(IMessageReceive.MESSAGE_FROM_XG, customText));
	}

	@Override
	public void onUnregisterResult(Context context, int arg1) {
		// TODO Auto-generated method stub
	}
}
 