package fanfan.app.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import fanfan.app.model.APIResponse;
import fanfan.app.model.BlueToothModel;
import fanfan.app.model.Response;
import fanfan.app.util.BlueToothUtils;
import fanfan.app.util.StringUtils;
import fanfan.app.util.Utils;
import fanfan.app.util.BlueToothUtils.BTUtilListener;

public class BlueToothManager {

	private static BlueToothManager blueToothManager;

	private  Response<Object> callResponse;
	
	private Response<Object> connetCallResponse;
	
	/**
	 * 获取单列
	 * @return
	 */
	public static BlueToothManager getInstrance() {
		
		if(blueToothManager==null) {
			blueToothManager = new BlueToothManager();
		} 
		return blueToothManager;
	}
	
	/**
	 * 开始扫描蓝牙
	 * @param context
	 */
	public void startScaneBlue(Context context, Response<Object> call) {
		
		callResponse=call;
		
		//设置上下文对象
		BlueToothUtils.getInstance().setContext(context);
		
		//设置处理类
		BlueToothUtils.getInstance().setBTUtilListener(new BTUtilListenerImp());
		
		//开始搜索
		BlueToothUtils.getInstance().scanLeDevice(true);
	}
	
	/**
	 * 链接蓝牙
	 */
	public void connectBlue(String address,Response<Object> call) {
		
		try {
		connetCallResponse = call;
		
		//开始链接蓝牙
		BlueToothUtils.getInstance().connectLeDevice(address);
		}catch(Exception ex) {
			System.out.println(ex);
		}
	}
	
	/**
	 * 停止扫描蓝牙
	 */
	public void stopScaneBlue() {
		BlueToothUtils.getInstance().scanLeDevice(false);
	}
	
	class BTUtilListenerImp implements BTUtilListener{

		@Override
		public void onLeScanStart() {
			// TODO Auto-generated method stub
			Map<String,String> state =new  HashMap<>();
			state.put("event", "start");
			callResponse.callBack(new APIResponse<Object>().success().setData(state));
		}

		@Override
		public void onLeScanStop() {
			// TODO Auto-generated method stub
			Map<String,String> state =new  HashMap<>();
			state.put("event", "stop");
			callResponse.callBack(new APIResponse<Object>().success().setData(state));
		}

		@Override
		public void onLeScanDevices(BluetoothDevice device,Integer index,String deviceName) {
			// TODO Auto-generated method stub
			Map<String,Object> state =new  HashMap<>();
			state.put("event", "device"); 
			BlueToothModel  toothModel  = new BlueToothModel();
			toothModel.setAddress(device.getAddress());
			toothModel.setName(deviceName);
			toothModel.setIndex(index);
			state.put("model", toothModel);
			callResponse.callBack(new APIResponse<Object>().success().setData(state));
		}

		@Override
		public void onConnected(BluetoothDevice mCurDevice) {
			// TODO Auto-generated method stub
			//链接成功
			Map<String,String> state =new  HashMap<>();
			state.put("event", "connected");
			connetCallResponse.callBack(new APIResponse<Object>().success().setData(state));
		}

		@Override
		public void onDisConnected(BluetoothDevice mCurDevice) {
			// TODO Auto-generated method stub
			//设备断开链接 
			Map<String,String> state =new  HashMap<>();
			state.put("event", "disConnected");
			connetCallResponse.callBack(new APIResponse<Object>().success().setData(state));
		}

		@Override
		public void onConnecting(BluetoothDevice mCurDevice) {
			// TODO Auto-generated method stub
			//链接中
			Map<String,String> state =new  HashMap<>();
			state.put("event", "connecting");
			connetCallResponse.callBack(new APIResponse<Object>().success().setData(state));
		}

		@Override
		public void onDisConnecting(BluetoothDevice mCurDevice) {
			// TODO Auto-generated method stub
			//链接失败 
			Map<String,String> state =new  HashMap<>();
			state.put("event", "disConnecting");
			connetCallResponse.callBack(new APIResponse<Object>().success().setData(state));
		}

		@Override
		public void onStrength(int strength) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onModel(int model) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
