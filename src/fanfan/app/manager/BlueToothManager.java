package fanfan.app.manager;

import java.util.HashMap;
import java.util.Map;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import fanfan.app.model.APIResponse;
import fanfan.app.model.BlueToothModel;
import fanfan.app.model.Response;
import fanfan.app.util.BlueOldUtils;
import fanfan.app.util.BlueUtils;
import fanfan.app.util.standard.BTUtilListener;

public class BlueToothManager {

	private static BlueToothManager blueToothManager;

	private Response<Object> callResponse;

	BTUtilListener utilListener;

	Context context;

	BluetoothDevice currDevice;

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

	/**
	 * 初始化加载
	 * 
	 * @param context
	 */
	public void init(Context context) {
		this.context = context;

		utilListener = new BTUtilListenerImp();

		// 监听蓝牙广播
		initReceive();

		// 初始化Ble蓝牙信息
		BlueUtils.getInstance().init(context);
		// 初始化经典蓝牙信息
		BlueOldUtils.getInstance().init(context);

		BlueUtils.getInstance().setBTUtilListener(utilListener);
	}

	/**
	 * 开始扫描蓝牙
	 * 
	 * @param context
	 */
	public void startScaneBlue(Context context, Response<Object> call) {

		callResponse = call;

		BlueUtils.getInstance().startScan();
	}

	/**
	 * 链接蓝牙
	 */
	public void connectBlue(String address, Response<Object> call) {

		try {
			callResponse = call;
			currDevice = BlueOldUtils.getInstance().getDevice(address);

			if (BlueUtils.getInstance().open(false)) {
				// 如果未链接
				if (!BlueOldUtils.getInstance().hasConnect()) {
					BlueOldUtils.getInstance().connet(currDevice);
				} else {
					utilListener.onConnected(currDevice);
				}
			}
		} catch (Exception ex) {
			System.out.println(ex);
			utilListener.onDisConnected(currDevice);
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
		if (BlueOldUtils.getInstance().hasConnect()) {
			utilListener.onConnected(currDevice);
		} else {
			utilListener.onDisConnected(currDevice);
		}
	}

	/**
	 * 蓝牙广播
	 */
	public void initReceive() {

		IntentFilter integer = new IntentFilter();
		// 蓝牙开启关闭状态
		integer.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		// 蓝牙绑定广播
		integer.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		// 蓝牙链接广播
		integer.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		// 蓝牙断开链接广播
		integer.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		// 输入PIN码广播
		integer.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);

		// 广播
		BroadcastReceiver blueReceive = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String action = intent.getAction();

				int state = -1;

				BluetoothDevice device = null;

				switch (action) {
				// 蓝牙开启关闭
				case BluetoothAdapter.ACTION_STATE_CHANGED:
					state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
					switch (state) {
					// 蓝牙关闭
					case BluetoothAdapter.STATE_OFF:
						utilListener.onDisConnected(currDevice);
						break;
					// 蓝牙开启
					case BluetoothAdapter.STATE_ON:
						utilListener.onOpen();
						break;
					}
					break;
				// 蓝牙绑定
				case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
					device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if (device.getAddress().equals(currDevice.getAddress())) {
						state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
						switch (state) {
						// 取消绑定
						case BluetoothDevice.BOND_NONE:
							utilListener.onCancelBound(currDevice);
							break;
						// 已经绑定
						case BluetoothDevice.BOND_BONDED:
							utilListener.onBound(currDevice);
							break;
						}
					}
					break;
				// 蓝牙绑定输入PIN码
				case BluetoothDevice.ACTION_PAIRING_REQUEST:
					String code = BlueOldUtils.getInstance().getCode(currDevice);
					try {
						BlueOldUtils.getInstance().setPinCode(currDevice, code);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				// 蓝牙链接
				case BluetoothDevice.ACTION_ACL_CONNECTED:
					device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if (device.getAddress().equals(currDevice.getAddress())) {
						utilListener.onConnected(currDevice);
					}
					break;
				// 蓝牙取消链接
				case BluetoothDevice.ACTION_ACL_DISCONNECTED:
					device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if (device.getAddress().equals(currDevice.getAddress())) {
						utilListener.onDisConnected(currDevice);
					}
					break;
				}
			}
		};

		context.registerReceiver(blueReceive, integer);
	}

	/**
	 * 事件
	 * 
	 * @author Administrator
	 *
	 */
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
			BlueOldUtils.getInstance().setConnect(true);
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
			BlueOldUtils.getInstance().setConnect(false);
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

		@Override
		public void onBound(BluetoothDevice mCurDevice) {
			// TODO Auto-generated method stub
			if (callResponse != null) {
				// 链接失败
				Map<String, String> state = new HashMap<>();
				state.put("event", "onBound");
				callResponse.callBack(new APIResponse<Object>().success().setData(state));
			}
		}

		@Override
		public void onOpen() {
			// TODO Auto-generated method stub
			if (callResponse != null) {
				// 链接失败
				Map<String, String> state = new HashMap<>();
				state.put("event", "onOpen");
				callResponse.callBack(new APIResponse<Object>().success().setData(state));
			}
		}

	}

}
