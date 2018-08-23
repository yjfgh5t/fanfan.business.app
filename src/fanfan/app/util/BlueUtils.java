package fanfan.app.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BlueUtils {

	private Context mContext;
	private static BlueToothUtils mInstance;
	private Set<BluetoothDevice> pairedDevices;
	private BluetoothDevice mCurDevice;
	private BluetoothSocket connectSocket;
	// 通用串口UUID
	public static UUID cannelUUID = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
	
	//通知接口
	BTUtilListener utilListener;

	/**
	 * 获取单列
	 * 
	 * @return
	 */
	public static synchronized BlueToothUtils getInstance() {
		if (mInstance == null) {
			mInstance = new BlueToothUtils();
		}
		return mInstance;
	}

	/**
	 * 初始加载
	 * 
	 * @param context
	 */
	public BlueToothUtils init(Context context) {
		this.mContext = context;

		BluetoothAdapter bTAdatper = BluetoothAdapter.getDefaultAdapter();

		// 已经绑定的设备
		this.pairedDevices = bTAdatper.getBondedDevices();
		if (pairedDevices == null) {
			pairedDevices = new HashSet<>();
		}

		final BroadcastReceiver mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				if (utilListener == null) {
					return;
				}

				String action = intent.getAction();
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					// 设备名称
					String deviceName = intent.getParcelableExtra(BluetoothDevice.EXTRA_NAME);
					// 避免重复添加已经绑定过的设备
					if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
						pairedDevices.add(device);
					}
					utilListener.onLeScanDevices(device, 0, deviceName);
				} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
					utilListener.onLeScanStart();
				} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
					utilListener.onLeScanStop();
				}
			}
		};

		return mInstance;
	}

	/**
	 * 打开蓝牙设备
	 */
	public boolean open(boolean enableScan) {
		BluetoothAdapter bTAdatper = BluetoothAdapter.getDefaultAdapter();
		if (bTAdatper == null) {
			Toast.makeText(mContext, "当前设备不支持蓝牙功能", Toast.LENGTH_SHORT).show();
			return false;
		}

		// 判断蓝牙是否开启
		if (!bTAdatper.isEnabled()) {
			// 开启蓝牙
			bTAdatper.enable();
		}
		// 开启被其它蓝牙设备发现的功能
		if (enableScan && bTAdatper.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			// 设置为一直开启
			i.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
			mContext.startActivity(i);
		}

		return true;
	}

	/**
	 * 搜索蓝牙设备
	 * 
	 * @param i
	 */
	public void startScan(int i) {

		// 是否开启蓝牙
		if (!open(false)) {
			return;
		}

		BluetoothAdapter bTAdatper = BluetoothAdapter.getDefaultAdapter();

		// 开始搜索蓝牙设备
		bTAdatper.startDiscovery();
	}

	/**
	 * 停止搜索设备
	 * 
	 * @param i
	 */
	public void stopScan(int i) {
		BluetoothAdapter bTAdatper = BluetoothAdapter.getDefaultAdapter();
		// 开始搜索蓝牙设备
		bTAdatper.cancelDiscovery();
	}

	public void connect(String address) {

		// 查询设备
		for (BluetoothDevice device : pairedDevices) {
			if (device.getAddress().equals(address)) {
				mCurDevice = device;
				break;
			}
		}

		if (mCurDevice == null) {
			Toast.makeText(mContext, "没有可连接的设备", Toast.LENGTH_SHORT);
			return;
		}
		
		//设备是否匹配
		if(mCurDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
			Toast.makeText(mContext, "设备没有配对", Toast.LENGTH_SHORT);
		}
		
		// 创建Socket
		//final BluetoothSocket socket = mCurDevice.createRfcommSocketToServiceRecord(cannelUUID);
		// 启动连接线程
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					 Method m = mCurDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
					 connectSocket = (BluetoothSocket)m.invoke(mCurDevice, 1);
					// 连接
					 connectSocket.connect();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(mContext, "无法连接设备", Toast.LENGTH_SHORT);
					Log.e("蓝牙连接失败", "蓝牙连接失败", e);
				}
			}
		});

		thread.start();
	}
	
	/**
	 * 取消配对
	 */
	public void disBond() {
		
		if(mCurDevice==null || mCurDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
			return;
		}
		
		 Method m;
		try {
			m = mCurDevice.getClass().getMethod("removeBond", (Class[]) null);
			m.invoke(mCurDevice, (Object[]) null);
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public interface BTUtilListener {
		void onLeScanStart(); // 扫描开始

		void onLeScanStop(); // 扫描停止

		void onLeScanDevices(BluetoothDevice blueToothModel, Integer index, String deviceName); // 扫描得到的设备

		void onConnected(BluetoothDevice mCurDevice); // 设备的连接

		void onDisConnected(BluetoothDevice mCurDevice); // 设备断开连接

		void onConnecting(BluetoothDevice mCurDevice); // 设备连接中

		void onDisConnecting(BluetoothDevice mCurDevice); // 设备连接失败
	}
	
}
