package fanfan.app.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inuker.bluetooth.library.BluetoothClient;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import fanfan.app.model.APIResponse;
import fanfan.app.model.BlueToothModel;
import fanfan.app.model.Response;
import fanfan.app.util.BlueOldUtils;
import fanfan.app.util.BlueUtils;
import fanfan.app.util.BlueUtils.BTUtilListener;
import fanfan.app.util.Utils;

public class BlueToothManager {

	private static BlueToothManager blueToothManager;

	private Response<Object> callResponse;

	BluetoothClient bluetoothClient;

	BTUtilListener utilListener;

	BlueOldUtils blueOldUtils;

	private List<String> cacheDevices;

	String MAC;

	/**
	 * 获取单列
	 * 
	 * @return
	 */
	public static BlueToothManager getInstrance() {
		if (blueToothManager == null) {
			blueToothManager = new BlueToothManager();
		}
		return blueToothManager;
	}

	public BlueToothManager() {
		utilListener = new BTUtilListenerImp();
		BlueUtils.getInstance().setBTUtilListener(new BTUtilListenerImp());
		blueOldUtils = new BlueOldUtils(Utils.getApp());
		// 设置处理类

		// BlueToothUtils.getInstance().setBTUtilListener(new BTUtilListenerImp());
		cacheDevices = new ArrayList();
	}

	/**
	 * 开始扫描蓝牙
	 * 
	 * @param context
	 */
	public void startScaneBlue(Context context, Response<Object> call) {

		callResponse = call;

		BlueUtils.getInstance().startScan();

		if (blueOldUtils == null) {
			blueOldUtils = new BlueOldUtils(context);
		}
	}

	/**
	 * 链接蓝牙
	 */
	public void connectBlue(String address, Response<Object> call) {

		try {
			callResponse = call;
			// 开始链接蓝牙
			// BlueUtils.getInstance().connect(address);

			BluetoothDevice device = blueOldUtils.getDevice(address);

			if (device.getBondState() != device.BOND_BONDED) {
				String code = blueOldUtils.getCode(device);

				blueOldUtils.setPinCode(device, code);
				boolean success = blueOldUtils.createBond(device);
				System.out.println("绑定" + success);
			} else {
				blueOldUtils.connet(device);
				System.out.println("链接：" + (blueOldUtils.hasConnect() ? "false" : "true"));
			}

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	/**
	 * 停止扫描蓝牙
	 */
	public void stopScaneBlue() {
		BlueUtils.getInstance().stopScan();
	}

	/**
	 * 蓝牙状态
	 */
	public void connectState() {
		BlueUtils.getInstance().connectState();
	}

	class BTUtilListenerImp implements BTUtilListener {

		@Override
		public void onLeScanStart() {
			// TODO Auto-generated method stub
			if (callResponse != null) {
				Map<String, String> state = new HashMap<>();
				state.put("event", "start");
				callResponse.callBack(new APIResponse<Object>().success().setData(state));
			}
		}

		@Override
		public void onLeScanStop() {
			// TODO Auto-generated method stub
			if (callResponse != null) {
				Map<String, String> state = new HashMap<>();
				state.put("event", "stop");
				callResponse.callBack(new APIResponse<Object>().success().setData(state));
			}
		}

		@Override
		public void onLeScanDevices(BluetoothDevice device, String deviceName) {
			// TODO Auto-generated method stub
			if (callResponse != null) {
				Map<String, Object> state = new HashMap<>();
				state.put("event", "device");
				BlueToothModel toothModel = new BlueToothModel();
				toothModel.setAddress(device.getAddress());
				toothModel.setName(deviceName);
				state.put("model", toothModel);
				callResponse.callBack(new APIResponse<Object>().success().setData(state));
			}
		}

		@Override
		public void onConnected(BluetoothDevice mCurDevice) {
			// TODO Auto-generated method stub
			if (callResponse != null) {
				// 链接成功
				Map<String, String> state = new HashMap<>();
				state.put("event", "connected");
				callResponse.callBack(new APIResponse<Object>().success().setData(state));
			}
		}

		@Override
		public void onDisConnected(BluetoothDevice mCurDevice) {
			// TODO Auto-generated method stub
			if (callResponse != null) {
				// 设备断开链接
				Map<String, String> state = new HashMap<>();
				state.put("event", "disConnected");
				callResponse.callBack(new APIResponse<Object>().success().setData(state));
			}
		}

		@Override
		public void onConnecting(BluetoothDevice mCurDevice) {
			// TODO Auto-generated method stub
			if (callResponse != null) {
				// 链接中
				Map<String, String> state = new HashMap<>();
				state.put("event", "connecting");
				callResponse.callBack(new APIResponse<Object>().success().setData(state));
			}
		}

		@Override
		public void onDisConnecting(BluetoothDevice mCurDevice) {
			// TODO Auto-generated method stub
			if (callResponse != null) {
				// 链接失败
				Map<String, String> state = new HashMap<>();
				state.put("event", "disConnecting");
				callResponse.callBack(new APIResponse<Object>().success().setData(state));
			}
		}

		@Override
		public void onCancelBound(BluetoothDevice mCurDevice) {
			// TODO Auto-generated method stub
			if (callResponse != null) {
				// 链接失败
				Map<String, String> state = new HashMap<>();
				state.put("event", "cancelBound");
				callResponse.callBack(new APIResponse<Object>().success().setData(state));
			}
		}

	}

}
