package fanfan.app.msg;

import android.content.Context;
import fanfan.app.model.MessageModel;

public interface IMessageReceive {

	public final String MESSAGE_FROM_XG="XG";
	public final String MESSAGE_FROM_HW="HW";
	public final String MESSAGE_FROM_XM="XM";
	
	/**
	 * 通知点击
	 * @param context
	 * @param message
	 */
	void onNotifactionClicked(Context context,MessageModel message);
	
	/**
	 * 通知展示
	 * @param context
	 * @param message
	 */
	void onNotifactionShow(Context context,MessageModel message);
	
	/**
	 * 穿透消息
	 * @param context
	 * @param message
	 */
	void onTextMessage(Context context,MessageModel message);
}
