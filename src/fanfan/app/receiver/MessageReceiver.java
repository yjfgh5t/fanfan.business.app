package fanfan.app.receiver;

import java.util.Map;

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
import fanfan.app.util.SPUtils;
import fanfan.app.util.StringUtils;
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
			JavaScriptImpl.getInstrance().webViewCallBack("", CodeConstant.Notify_Msg_CallKey+".xg-click");
		}
	}

	@Override
	public void onNotifactionShowedResult(Context arg0, XGPushShowedResult arg1) {
		// TODO Auto-generated method stub
		if(JavaScriptImpl.getInstrance()!=null) {
			JavaScriptImpl.getInstrance().webViewCallBack("", CodeConstant.Notify_Msg_CallKey+".xg-show");
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
	public void onTextMessage(Context context, XGPushTextMessage textMessage) {
		// TODO Auto-generated method stub
		if(JavaScriptImpl.getInstrance()!=null) {
			JavaScriptImpl.getInstrance().webViewCallBack("", CodeConstant.Notify_Msg_CallKey+".xg-msg");
		}
		
		String customText = textMessage.getCustomContent();
		
		if(!StringUtils.isEmpty(customText)) {
			Map<String,Object> params = JSONObject.parseObject(customText,Map.class);
			switch(params.get("msgType").toString()) {
				case "payOrder" :
					//播放新订单语言
					MediaManager.getInstrance().playNewOrder();
					//是否自动打印
					if("true".equals(SPUtils.getInstance().getString(SPConstant.autoPrint,"true"))){
						OrderPrintModel printModel = JSONObject.parseObject(params.get("data").toString(), OrderPrintModel.class);
						PrintManager.getInstrance().printOrder(printModel);
					}
					break;
			}
		}
		
	}

	@Override
	public void onUnregisterResult(Context arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
