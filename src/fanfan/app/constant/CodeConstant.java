package fanfan.app.constant;

public class CodeConstant {
	/**
	 * 开发环境
	 */
	public static final boolean Is_Dev=true;
	
	/**
	 * 拍照
	 */
	public static final int Code_Take_Photo=0x2;
	
	/**
	 * 选择相册
	 */
	public static final int Code_Choise_Img=0x3;
	
	/**
	 * 截图返回
	 */
	public static final int Code_Cut_Back=0x4;
	
	/**
	 * 扫码返回
	 */
	public static final int Code_San_QRCode=0x5;
	
	/**
	 * 通知消息key
	 */
	public static final String Notify_Msg_CallKey = "notify_msg";
	
	/**
	 * 蓝牙通知事件
	 */
	public static final String Notify_Blue_ToothKey ="local_notify_blue_booth_event";
	
	/**
	 * 点击前置通知广播
	 */
	public static final String Notify_Click_Action = "fanfan.business.app.foregound.click.action";
	
	public static final String Notify_Alarm_Action = "fanfan.business.app.foregound.alarm.action";
	
}
