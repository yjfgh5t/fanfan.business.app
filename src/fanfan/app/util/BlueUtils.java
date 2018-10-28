package fanfan.app.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;
import fanfan.app.constant.SPConstant;
import fanfan.app.manager.BlueToothManager;

@SuppressLint("NewApi")
public class BlueUtils {

	private Context context;
	private static BlueUtils mInstance;
	private Set<BluetoothDevice> pairedDevices;
	private BluetoothDevice mCurDevice;
	private BluetoothGattCallback mGattCallback;
	private BluetoothGatt toothGatt;
	private BroadcastReceiver mFoundReceiver;
	private BroadcastReceiver mStateReceiver;
	private BluetoothGattService gattService;
	private List<BluetoothGattService> gattServices;
	private List<BluetoothGattCharacteristic> gattCharacteristic;
	private int characteristicIndex=0;
	/**
	 * 蓝牙链接失败 自动链接次数
	 */
	private int autoConnectCount=0;
	/**
	 * 蓝牙配对成功后 自动绑定
	 */
	private boolean hasConnect=false;
	
	
	// 通用串口UUID
	public static UUID cannelUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
	
	//通知接口
	BTUtilListener utilListener;

	/**
	 * 获取单列
	 * 
	 * @return
	 */
	public static synchronized BlueUtils getInstance() {
		if (mInstance == null) {
			mInstance = new BlueUtils();
		}
		return mInstance;
	}

	/**
	 * 初始加载
	 * 
	 * @param context
	 */
	public BlueUtils init(Context context) {
		
		if(this.context!=null) {
			return this;
		}
		
		this.context = context;

		BluetoothAdapter bTAdatper = BluetoothAdapter.getDefaultAdapter();


		gattCharacteristic = new ArrayList<>();
		// 已经绑定的设备
		//this.pairedDevices = bTAdatper.getBondedDevices();
		if (pairedDevices == null) {
			pairedDevices = new HashSet<>();
		}
		
		//找到设备广播
		mFoundReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				if (utilListener == null) {
					return;
				}
				String action = intent.getAction(); 
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); 
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					if(!pairedDevices.contains(device)) {
						pairedDevices.add(device);
						utilListener.onLeScanDevices(device,device.getName());
					}
				} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
					utilListener.onLeScanStart();
				} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
					utilListener.onLeScanStop();
					//注销广播
					context.unregisterReceiver(mFoundReceiver);
				}
			}
		};
		
		//蓝牙状态更改广播
		mStateReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				if (utilListener == null) {
					return;
				}
				String action = intent.getAction(); 
				if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
				    switch (state) {
				        case BluetoothAdapter.STATE_OFF:
				            Log.d("aaa", "STATE_OFF 手机蓝牙关闭");
				            closeToothGatt(toothGatt);
				            utilListener.onDisConnected(null);
				            break;
				        case BluetoothAdapter.STATE_TURNING_OFF:
				            Log.d("aaa", "STATE_TURNING_OFF 手机蓝牙正在关闭");
				            break;
				        case BluetoothAdapter.STATE_ON:
				            if(mCurDevice!=null) {
				            	//重新链接
				            	connect(mCurDevice.getAddress());
				            	//清空特性数据
				            	gattCharacteristic.clear();
				            	//清空gttSevice
				            	gattServices.clear();
				            }
				            break;
				        case BluetoothAdapter.STATE_TURNING_ON:
				            Log.d("aaa", "STATE_TURNING_ON 手机蓝牙正在开启");
				            break;
				    }
				}
			}
		};
		
		 //蓝牙操作事件
	    mGattCallback = new BluetoothGattCallback() {
				@Override
				public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
					super.onConnectionStateChange(gatt, status, newState);
					switch (newState) {
					//蓝牙链接成功
					case BluetoothProfile.STATE_CONNECTED:
						//autoConnectCount=0;
						utilListener.onConnected(mCurDevice);
						//搜索蓝牙服务
						gatt.discoverServices();
						//是否链接
						hasConnect=true;
						break;
					//蓝牙链接失败 蓝牙禁止链接
					//case BluetoothProfile.STATE_DISCONNECTING:
					case BluetoothProfile.STATE_DISCONNECTED:
						if(hasConnect) {
							//设置链接失败
							hasConnect=false;
							//关闭链接
							closeToothGatt(gatt);
							//重新链接
							if(mCurDevice!=null) {
								connect(mCurDevice.getAddress());
							}
						}else { 
							utilListener.onDisConnected(mCurDevice);
							closeToothGatt(gatt);
						}
						break;
					//蓝牙链接中
					case BluetoothProfile.STATE_CONNECTING:
						utilListener.onConnecting(mCurDevice); 
						break;
					}
				}
				
				@Override
				public void onServicesDiscovered(BluetoothGatt gatt, int status){
					if(status == BluetoothGatt.GATT_SUCCESS) {
						toothGatt = gatt;
						gattServices = gatt.getServices(); 
						Log.e("BlueUtils", "发现服务");
					}
				}
				
				@Override
				public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
					if(status == BluetoothGatt.GATT_SUCCESS) {
						 //Log.e("BlueUtils", "写入成功");
					}
				}
		};
		
		
		//注册监听广播
		IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		context.registerReceiver(mStateReceiver, filter);
		
		return mInstance;
	}

	/**
	 * 打开蓝牙设备
	 */
	public boolean open(boolean enableScan) {
		BluetoothAdapter bTAdatper = BluetoothAdapter.getDefaultAdapter();
		if (bTAdatper == null) {
			Toast.makeText(context, "当前设备不支持蓝牙功能", Toast.LENGTH_SHORT).show();
			return false;
		}

		// 判断蓝牙是否开启
		if (!bTAdatper.isEnabled()) {
			// 开启蓝牙
			if(!bTAdatper.enable()) {
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				context.startActivity(enableBtIntent); 
			}
			//蓝牙启动 需要时间 线程等待5s
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 开启被其它蓝牙设备发现的功能
		if (enableScan && bTAdatper.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			// 设置为一直开启
			i.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
			context.startActivity(i);
		}

		return true;
	}

	public boolean write(String data) {
		try {
			return write(data.getBytes("gbk"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	 
	public boolean write(byte[] data) {
		
		//蓝牙未连接
		if(!hasConnect) {
			return false;
		}
		
		//return startWrite(data);
		boolean writeState=false;
		if(data.length>20) {
			byte[] writeData = new byte[20];  
			int i=0;
			for(byte b : data) {
				if(i==20) {
					writeState = startWrite(writeData);
					 writeData = new byte[20];
					i=0;
				}
				writeData[i]=b;
				i++;
			}
			if(i>0) {
			 startWrite(writeData);
			}
		}else {
			writeState = startWrite(data);
		}
		return writeState;
	}
	
	private boolean startWrite(byte[] data) {
		
		getCharacteristic();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(gattCharacteristic==null || gattCharacteristic.size()==characteristicIndex) {
			Log.e("BlueUtils", "未能获取到Characteristic");
			gattCharacteristic.clear();
			characteristicIndex=0;
			return false;
		}
		
		BluetoothGattCharacteristic gattCharaModel= gattCharacteristic.get(characteristicIndex);
		
		if(!gattCharaModel.setValue(data)) {
			Log.e("BlueUtils", "设置数据失败"+(characteristicIndex++));
			return false;
		}
		
		if(!toothGatt.writeCharacteristic(gattCharaModel)) {
			//写入数据失败
			Log.e("BlueUtils", "写入数据失败"+(characteristicIndex++));
			return false;
		}
		return true;
	}
	
	/**
	 * 关闭Gatt
	 * @throws InterruptedException 
	 */
	private void closeToothGatt(BluetoothGatt gatt){
		if(gatt!=null) {
			gatt.disconnect();
			gatt.close();
			gatt=null;
		}
		
		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 搜索蓝牙设备
	 * 
	 * @param i
	 */
	public void startScan() {

		// 是否开启蓝牙
		if (!open(false)) {
			return;
		}
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		context.registerReceiver(mFoundReceiver, filter);
		
		BluetoothAdapter bTAdatper = BluetoothAdapter.getDefaultAdapter();

		//清空数据
		pairedDevices.clear();
		
		// 开始搜索蓝牙设备
		bTAdatper.startDiscovery();
	}

	/**
	 * 停止搜索设备
	 * 
	 * @param i
	 */
	public void stopScan() {
		BluetoothAdapter bTAdatper = BluetoothAdapter.getDefaultAdapter();
		if(bTAdatper.isDiscovering()) {
			// 停止搜索蓝牙设备
			bTAdatper.cancelDiscovery();
			//注销广播
			context.unregisterReceiver(mFoundReceiver);
		}
		utilListener.onLeScanStop();
	}

	/**
	 * 链接状态
	 */
	public void connectState() {
		if(mCurDevice!=null && toothGatt!=null && hasConnect) {
			utilListener.onConnected(mCurDevice);
		}else {
			utilListener.onDisConnected(null);
		}
	}
	
	/**
	 * 链接蓝牙设备
	 * @param address
	 */
	public void connect(String address) {

		// 是否开启蓝牙
		if (!open(false)) {
			return;
		}
		
		stopScan();
		
		// 获得设备 
		mCurDevice =  BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
		
		if (mCurDevice == null) {
			Toast.makeText(context, "没有可连接的设备", Toast.LENGTH_SHORT);
			return;
		}
		
		//关闭Gatt
		closeToothGatt(toothGatt);
		
		try {
		//判断是否是6.0版本以上
		if(Build.VERSION.SDK_INT>22) {
			toothGatt = mCurDevice.connectGatt(context, false, mGattCallback,BluetoothDevice.TRANSPORT_LE);
		}else {
			toothGatt = mCurDevice.connectGatt(context, false, mGattCallback);
		}
		}catch(Exception ex) {
			utilListener.onDisConnecting(null);
		}
	}
	
	/**
	 * 获取特性
	 */
	private List<BluetoothGattCharacteristic> getCharacteristic() {
		
		if(gattServices==null || gattServices.size()==0) {
			return null;
		}
		
		if(gattCharacteristic!=null && gattCharacteristic.size()>0) {
			return gattCharacteristic;
		}
		
		for(BluetoothGattService gattService : gattServices) {
			for(BluetoothGattCharacteristic characteristic: gattService.getCharacteristics()) {
				if(characteristic.getWriteType()==BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE) {
					Log.e("BlueUtils", "S-UUID:"+gattService.getUuid()+" S-Type"+gattService.getType()+" writeType:"+characteristic.getWriteType()+" C-UUID:"+characteristic.getUuid());
					gattCharacteristic.add(characteristic);
				}
			}
		}
		
		return gattCharacteristic;
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
	
	/**
	 * 设置处理结果
	 * @param listener
	 */
	public void setBTUtilListener(BTUtilListener listener) {
		utilListener = listener;
	}
	
	
	public interface BTUtilListener {
		void onLeScanStart(); // 扫描开始

		void onLeScanStop(); // 扫描停止

		void onLeScanDevices(BluetoothDevice blueToothModel,String deviceName); // 扫描得到的设备

		void onConnected(BluetoothDevice mCurDevice); // 设备的连接

		void onDisConnected(BluetoothDevice mCurDevice); // 设备断开连接

		void onConnecting(BluetoothDevice mCurDevice); // 设备连接中

		void onDisConnecting(BluetoothDevice mCurDevice); // 设备连接失败
	}
	
}
