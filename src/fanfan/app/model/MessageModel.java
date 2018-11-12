package fanfan.app.model;

public class MessageModel {
	
	/**
	 * 消息来源 信鸽、小米、华为、魅族
	 */
	private String msgFrom;
	 
	
	/**
	 * 消息内容
	 */
	private String msgContent;


	public MessageModel(String msgFrom,String msgContent) {
		this.msgFrom = msgFrom;
		this.msgContent = msgContent;
	}
	
	public String getMsgFrom() {
		return msgFrom;
	}


	public void setMsgFrom(String msgFrom) {
		this.msgFrom = msgFrom;
	}


	public String getMsgContent() {
		return msgContent;
	}


	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	
	
}
