package fanfan.app.receiver;

import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import android.content.Context;
import fanfan.app.constant.CodeConstant;
import fanfan.app.constant.SPConstant;
import fanfan.app.manager.MediaManager;
import fanfan.app.manager.PrintManager;
import fanfan.app.model.OrderPrintModel;
import fanfan.app.model.menum.MediaType;
import fanfan.app.util.SPUtils;
import fanfan.app.util.StringUtils;
import fanfan.app.view.webview.JavaScriptImpl;

public class MessageReceiver extends XGPushBaseReceiver {
	
	@Override
	public void onDeleteTagResult(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onNotifactionClickedResult(Context arg0, XGPushClickedResult result) {
		// TODO Auto-generated method stub
		String customText = result.getCustomContent();
		executeMsg(customText,"xg-click");
	}

	@Override
	public void onNotifactionShowedResult(Context arg0, XGPushShowedResult result) {
		// TODO Auto-generated method stub
		String customText = result.getCustomContent();
		executeMsg(customText,"xg-show");
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
	public void onTextMessage(Context context, XGPushTextMessage textMessage) {
		// TODO Auto-generated method stub
		String customText = textMessage.getCustomContent();
		executeMsg(customText,"xg-msg");		
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

	@Override
	public void onUnregisterResult(Context arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
