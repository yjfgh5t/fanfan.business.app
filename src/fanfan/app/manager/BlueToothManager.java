package fanfan.app.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.receiver.listener.BluetoothBondListener;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.provider.SyncStateContract.Constants;
import fanfan.app.model.APIResponse;
import fanfan.app.model.BlueToothModel;
import fanfan.app.model.Response;
import fanfan.app.util.BlueToothUtils;
import fanfan.app.util.BlueUtils;
import fanfan.app.util.BlueUtils.BTUtilListener;

public class BlueToothManager {

	private static BlueToothManager blueToothManager;

	private  Response<Object> callResponse;
	
	private Response<Object> connetCallResponse;
	
	BluetoothClient bluetoothClient;
	
	BTUtilListener utilListener;
	
	private List<String> cacheDevices;
	
	String MAC;
	
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
	
	public BlueToothManager() {
		utilListener = new BTUtilListenerImp();
		BlueUtils.getInstance().setBTUtilListener(new BTUtilListenerImp());
		//设置处理类
	
		//BlueToothUtils.getInstance().setBTUtilListener(new BTUtilListenerImp());
		cacheDevices = new ArrayList();
	}
	
	/**
	 * 开始扫描蓝牙
	 * @param context
	 */
	public void startScaneBlue(Context context, Response<Object> call) {
		
		callResponse=call;
		
		BlueUtils.getInstance().startScan();
		
		//设置上下文对象
//		BlueToothUtils.getInstance().setContext(context);
//		BlueToothUtils.getInstance().init();
//		//开始搜索
//		//BlueToothUtils.getInstance().scanLeDevice(true); 
//		//BlueUtils.getInstance().startScan();
//		
		if(bluetoothClient==null) {
			bluetoothClient = new BluetoothClient(context);
		}
//		
//		if(!bluetoothClient.isBluetoothOpened()) {
//			bluetoothClient.openBluetooth();
//		}
	
//		cacheDevices.clear();
		
		 
//		SearchRequest request = new SearchRequest.Builder()
//		        .searchBluetoothLeDevice(3000, 2)   // 先扫BLE设备3次，每次3s
//		        .searchBluetoothClassicDevice(3000) // 再扫经典蓝牙5s
//		        .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
//		        .build();
//		bluetoothClient.search(request, new SearchResponse() {
//		    @Override
//		    public void onSearchStarted() {
//		    	utilListener.onLeScanStart();
//		    }
//
//		    @Override
//		    public void onDeviceFounded(SearchResult device) {
//		    	if(!cacheDevices.contains(device.getAddress())) {
//			    	cacheDevices.add(device.getAddress());
//			    	utilListener.onLeScanDevices(device.device, device.getName());
//		    	}
//		    }
//
//		    @Override
//		    public void onSearchStopped() {
//		    	utilListener.onLeScanStop();
//		    }
//
//		    @Override
//		    public void onSearchCanceled() {
//		     
//		    }
// 
//		});
	}
	
	/**
	 * 链接蓝牙
	 */
	public void connectBlue(String address,Response<Object> call) {
		
		try {
		connetCallResponse = call;
		//开始链接蓝牙
		BlueUtils.getInstance().connect(address);
		//BlueToothUtils.getInstance().connectLeDevice(address);
//			bluetoothClient.connect(address,  new BleConnectResponse() {
//			    @Override
//			    public void onResponse(int code, BleGattProfile profile) {
//			        if (code == com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS) {
//			        	utilListener.onConnected(null);
//			        }else {
//			        	utilListener.onDisConnected(null);
//			        }
//			    }
//			});
			
			//bluetoothClient.disconnect(address);
			
//			bluetoothClient.registerBluetoothBondListener(new BluetoothBondListener() {
//			    @Override
//			    public void onBondStateChanged(String mac, int bondState) {
//			        // bondState = Constants.BOND_NONE, BOND_BONDING, BOND_BONDED
//			    	if(bondState==com.inuker.bluetooth.library.Constants.BOND_BONDED) {
//			    		MAC = mac;
//			    	}
//			    }
//			});
			
		}catch(Exception ex) {
			System.out.println(ex);
		}
	}
	
	/**
	 * 停止扫描蓝牙
	 */
	public void stopScaneBlue() {
		//BlueToothUtils.getInstance().scanLeDevice(false);
		BlueUtils.getInstance().stopScan();
		//bluetoothClient.stopSearch();
		//utilListener.onLeScanStop();
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
		public void onLeScanDevices(BluetoothDevice device,String deviceName) {
			// TODO Auto-generated method stub
			Map<String,Object> state =new  HashMap<>();
			state.put("event", "device"); 
			BlueToothModel  toothModel  = new BlueToothModel();
			toothModel.setAddress(device.getAddress());
			toothModel.setName(deviceName);
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
 
//		@Override
//		public void onStrength(int strength) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void onModel(int model) {
//			// TODO Auto-generated method stub
//			
//		} 
		
	}
	
}
