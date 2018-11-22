package fanfan.app.util;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Build;

public class BlueOldUtils {

	static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	private BluetoothAdapter bluetoothAdapter;

	BluetoothSocket localBluetoothSocket = null;

	boolean hasConnect = false;

	public BlueOldUtils(Context context) {

		if (Build.VERSION.SDK_INT >= 18) {
			this.bluetoothAdapter = ((BluetoothManager) context.getSystemService("bluetooth")).getAdapter();
		}

		if (this.bluetoothAdapter == null) {
			this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		}
	}

	/**
	 * 获取PN码
	 * 
	 * @param bluetoothDevice
	 * @return
	 */
	public String getCode(BluetoothDevice bluetoothDevice) {
		String str = "";
		if (isQPrinter(bluetoothDevice)) {
			return "0000";
		}

		if (isGPrinter(bluetoothDevice)) {
			return "1234";
		}

		if (otherPrinter(bluetoothDevice)) {
			if (isGPrinter(bluetoothDevice)) {
				return "1234";
			}
		}

		return "0000";
	}

	/**
	 * 获取链接的设备
	 * 
	 * @param address
	 * @return
	 */
	public BluetoothDevice getDevice(String address) {

		try {
			BluetoothDevice localBluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
			return localBluetoothDevice;
		} catch (Exception paramString) {

		}
		return null;
	}

	/**
	 * 绑定设备
	 * 
	 * @param bluetoothDevice
	 * @return
	 * @throws Exception
	 */
	public boolean createBond(BluetoothDevice bluetoothDevice) throws Exception {
		return ((Boolean) bluetoothDevice.getClass().getMethod("createBond", new Class[0]).invoke(bluetoothDevice,
				new Object[0])).booleanValue();
	}

	/**
	 * 删除绑定
	 * 
	 * @param bluetoothDevice
	 * @return
	 * @throws Exception
	 */
	public boolean removeBond(BluetoothDevice bluetoothDevice) throws Exception {
		if (bluetoothDevice == null) {
			return false;
		}
		return ((Boolean) bluetoothDevice.getClass().getMethod("removeBond", new Class[0]).invoke(bluetoothDevice,
				new Object[0])).booleanValue();
	}

	/**
	 * 取消正在绑定
	 * 
	 * @param bluetoothDevice
	 * @return
	 * @throws Exception
	 */
	public boolean cancelBondProcess(BluetoothDevice bluetoothDevice) throws Exception {
		if (bluetoothDevice == null) {
			return false;
		}
		return ((Boolean) bluetoothDevice.getClass().getMethod("cancelBondProcess", new Class[0])
				.invoke(bluetoothDevice, new Object[0])).booleanValue();
	}

	/**
	 * 设置PinCode
	 * 
	 * @param bluetoothDevice
	 * @param pinCode
	 * @return
	 * @throws Exception
	 */
	public boolean setPinCode(BluetoothDevice bluetoothDevice, String pinCode) throws Exception {
		if (bluetoothDevice == null) {
			return false;
		}
		return ((Boolean) bluetoothDevice.getClass().getMethod("setPin", new Class[] { byte[].class })
				.invoke(bluetoothDevice, new Object[] { pinCode.getBytes() })).booleanValue();
	}

	/**
	 * 是否链接到蓝牙
	 * 
	 * @return
	 */
	public boolean hasConnect() {
		if (localBluetoothSocket != null) {
			return hasConnect && localBluetoothSocket.isConnected();
		}
		return false;
	}

	/**
	 * 链接蓝牙
	 * 
	 * @param paramBluetoothDevice
	 * @return
	 * @throws Exception
	 */
	public void connet(BluetoothDevice paramBluetoothDevice) throws Exception {
		try {
			cancelBondProcess(paramBluetoothDevice);
			if (paramBluetoothDevice.getBondState() == 12) {
				localBluetoothSocket = paramBluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
				// localObject = localBluetoothSocket;
			}
		} catch (Exception localException1) {
			hasConnect = false;
		}

		try {
			if (localBluetoothSocket != null) {
				localBluetoothSocket.connect();
				hasConnect = true;
			}
		} catch (Exception localException2) {
			hasConnect = false;
		}

		try {
			if (localBluetoothSocket != null) {
				localBluetoothSocket.close();
			}

			removeBond(paramBluetoothDevice);
			cancelBondProcess(paramBluetoothDevice);
			Thread.sleep(500L);
			localBluetoothSocket = paramBluetoothDevice.createRfcommSocketToServiceRecord(uuid);
			localBluetoothSocket.connect();
			hasConnect = true;
		} catch (Exception ex) {
			hasConnect = false;
		}
	}

	private static boolean isQPrinter(BluetoothDevice bluetoothDevice) {
		if (bluetoothDevice == null) {
			return false;
		}
		return "Qsprinter".equalsIgnoreCase(bluetoothDevice.getName());
	}

	public static boolean isGPrinter(BluetoothDevice bluetoothDevice) {
		if (bluetoothDevice == null) {
			return false;
		}
		return "Gprinter".equalsIgnoreCase(bluetoothDevice.getName());
	}

	public static boolean otherPrinter(BluetoothDevice bluetoothDevice) {
		String str = "";
		if (bluetoothDevice != null) {
			str = bluetoothDevice.getName();
		}
		return (!StringUtils.isEmpty(str)) && (str.toLowerCase().startsWith("Gprinter".toLowerCase()));
	}

}
