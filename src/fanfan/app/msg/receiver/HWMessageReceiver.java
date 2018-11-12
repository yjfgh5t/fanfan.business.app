package fanfan.app.msg.receiver;

import java.io.UnsupportedEncodingException;
 

import android.content.Context;
import android.os.Bundle;
import fanfan.app.model.MessageModel;
import fanfan.app.msg.IMessageReceive;
import fanfan.app.msg.MessageReceiveImpl;

public class HWMessageReceiver {

	IMessageReceive messageReceive = new MessageReceiveImpl();
	
//	/**
//	 * 穿透消息
//	 */
//	@Override
//    public boolean onPushMsg(Context context, byte[] msg, Bundle arg2) {
//		
//		try {
//			String msgContent = new String(msg, "UTF-8");
//			messageReceive.onTextMessage(context, new MessageModel(IMessageReceive.MESSAGE_FROM_HW, msgContent));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        return super.onPushMsg(context, msg, arg2);
//    }
//	
//	/**
//	 * 打开事件
//	 */
//	@Override
//	public void onEvent(Context context, Event event, Bundle extras) {
//	    String msgContent = extras.getString(BOUND_KEY.pushMsgKey);  
//	    
//	    if(Event.NOTIFICATION_CLICK_BTN.equals(event)) {
//	    	messageReceive.onNotifactionClicked(context, new MessageModel(IMessageReceive.MESSAGE_FROM_HW, msgContent));
//	    }else if(Event.NOTIFICATION_OPENED.equals(event)) {
//	    	messageReceive.onNotifactionShow(context, new MessageModel(IMessageReceive.MESSAGE_FROM_HW, msgContent));
//	    }
//	    
//	    super.onEvent(context, event, extras);  
//	}
	
}
