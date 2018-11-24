package fanfan.app.util.standard;

import android.bluetooth.BluetoothDevice;

public interface BTUtilListener {

	/**
	 * 描开始
	 */
	void onLeScanStart();

	/**
	 * 停止扫描
	 */
	void onLeScanStop();

	/**
	 * 找到设备
	 * 
	 * @param blueToothModel
	 * @param deviceName
	 */
	void onLeScanDevices(BluetoothDevice blueToothModel, String deviceName);

	/**
	 * 链接到设备
	 * 
	 * @param mCurDevice
	 */
	void onConnected(BluetoothDevice mCurDevice); // 设备的连接

	/**
	 * 设备断开链接
	 * 
	 * @param mCurDevice
	 */
	void onDisConnected(BluetoothDevice mCurDevice); // 设备断开连接

	/**
	 * 设备链接中
	 * 
	 * @param mCurDevice
	 */
	void onConnecting(BluetoothDevice mCurDevice); // 设备连接中

	/**
	 * 设备链接失效
	 * 
	 * @param mCurDevice
	 */
	void onDisConnecting(BluetoothDevice mCurDevice); // 设备连接失败

	/**
	 * 设备取消绑定
	 * 
	 * @param mCurDevice
	 */
	void onCancelBound(BluetoothDevice mCurDevice);

	/**
	 * 绑定到设备
	 * 
	 * @param mCurDevice
	 */
	void onBound(BluetoothDevice mCurDevice);

	/**
	 * 打开蓝牙
	 */
	void onOpen();
}
