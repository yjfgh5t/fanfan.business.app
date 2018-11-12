package fanfan.app.msg;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import android.content.Context;
import fanfan.app.constant.CodeConstant;
import fanfan.app.constant.SPConstant;
import fanfan.app.manager.MediaManager;
import fanfan.app.manager.PrintManager;
import fanfan.app.model.MessageModel;
import fanfan.app.model.OrderPrintModel;
import fanfan.app.model.menum.MediaType;
import fanfan.app.util.SPUtils;
import fanfan.app.util.StringUtils;
import fanfan.app.view.webview.JavaScriptImpl;

public class MessageReceiveImpl implements IMessageReceive {

	@Override
	public void onNotifactionClicked(Context context, MessageModel message) {
		// TODO Auto-generated method stub
		executeMsg(message.getMsgContent(),"xg-click");
	}

	@Override
	public void onNotifactionShow(Context context, MessageModel message) {
		// TODO Auto-generated method stub
		executeMsg(message.getMsgContent(),"xg-show");
	}

	@Override
	public void onTextMessage(Context context, MessageModel message) {
		// TODO Auto-generated method stub
		executeMsg(message.getMsgContent(),"xg-msg");		
	}

	
	/**
	 * 处理消息
	 */
	private void executeMsg(String msgContent, String msgType) {
		if(!StringUtils.isEmpty(msgContent)) {
			Map<String,Object> params = JSONObject.parseObject(msgContent,Map.class);
			
			//内部处理
			switch(params.get("msgType").toString()) {
				//支付订单
				case "payOrder" :
					//播放新订单语言
					MediaManager.getInstrance().playMedia(MediaType.newOrderMedia);
					//是否自动打印   
					if("true".equals(SPUtils.getInstance().getString(SPConstant.autoPrint,"false"))){
						try {
							OrderPrintModel printModel =JSON.parseObject(params.get("data").toString(),OrderPrintModel.class);
							PrintManager.getInstrance().printOrder(printModel);
						}catch(Exception ex) {
							System.out.println(ex);
						}
					}
					break;
			}
		}

		// 前端处理
		if(JavaScriptImpl.getInstrance()!=null) {
			JavaScriptImpl.getInstrance().webViewCallBack(msgContent, CodeConstant.Notify_Msg_CallKey+"."+msgType);
		}
	}
}
