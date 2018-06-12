package fanfan.app.manager;

import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import fanfan.app.model.APIResponse;
import fanfan.app.model.Response;
import fanfan.app.util.BlueToothUtils;
import fanfan.app.util.Utils;
import fanfan.app.util.BlueToothUtils.BTUtilListener;

public class BlueToothManager {

	private static BlueToothManager blueToothManager;

	private  Response<Object> callResponse;
	
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
	public void startScaneBlue(Response<Object> call) {
		
		callResponse=call;
		
		//设置上下文对象
		BlueToothUtils.getInstance().setContext(Utils.getApp());
		
		//设置处理类
		BlueToothUtils.getInstance().setBTUtilListener(new BTUtilListenerImp());
		
		//开始搜索
		BlueToothUtils.getInstance().scanLeDevice(true);
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
			
		}

		@Override
		public void onLeScanStop() {
			// TODO Auto-generated method stub
		}

		@Override
		public void onLeScanDevices(List<BluetoothDevice> listDevice) {
			// TODO Auto-generated method stub
			callResponse.callBack(new APIResponse<Object>().success().setData(listDevice));
		}

		@Override
		public void onConnected(BluetoothDevice mCurDevice) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDisConnected(BluetoothDevice mCurDevice) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onConnecting(BluetoothDevice mCurDevice) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDisConnecting(BluetoothDevice mCurDevice) {
			// TODO Auto-generated method stub
			
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
