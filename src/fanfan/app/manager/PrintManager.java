package fanfan.app.manager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.util.Log;
import fanfan.app.constant.CodeConstant;
import fanfan.app.constant.SPConstant;
import fanfan.app.model.OrderDetailPrintModel;
import fanfan.app.model.OrderPrintModel;
import fanfan.app.model.Response; 
import fanfan.app.util.BlueToothUtils;
import fanfan.app.util.PrintUtils;
import fanfan.app.util.SPUtils;
import fanfan.app.view.webview.X5WebView;

/**
 * 打印
 * @author jy
 *
 */
public class PrintManager {
	
	static PrintManager instance; 
	
	static int newOrderPlayCount=0;
	
	public static final SimpleDateFormat formatTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	public static PrintManager getInstrance() {
		if (instance == null) {
			instance = new PrintManager();
		}
		return instance;
	}
	
	/**
	 * 打印订单
	 */
	public void printOrder(final OrderPrintModel printModel) {
		//开启线程打印
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
					blueToothPrint(printModel);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		thread.run();
	}

	
	/**
	 * 执行WebView打印
	 * @param context
	 * @param webView
	 */
	@SuppressLint("NewApi")
	public void blueToothPrint(OrderPrintModel printModel) {
		BluetoothDevice device = BlueToothUtils.getInstance().getCurrentDevice();
		try {
			BluetoothSocket socket =  device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
			socket.connect();
			PrintUtils.setOutputStream(socket.getOutputStream());
			
			PrintUtils.selectCommand(PrintUtils.RESET);
			PrintUtils.selectCommand(PrintUtils.LINE_SPACING_DEFAULT);
			PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
			PrintUtils.printText(SPUtils.getInstance().getString(SPConstant.shopName,"饭饭点餐") +"\n\n");
			PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
			PrintUtils.printText("#"+printModel.getOrderDateNum()+" 桌号："+printModel.getOrderDeskNum()+"\n\n");
			PrintUtils.selectCommand(PrintUtils.NORMAL);
			PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
			PrintUtils.printText(PrintUtils.printTwoData("订单编号",printModel.getOrderNum()+"\n"));
			PrintUtils.printText(PrintUtils.printTwoData("下单时间",formatTimestamp.format(printModel.getOrderTime())+"\n"));

			PrintUtils.printText("--------------------------------\n");
			PrintUtils.selectCommand(PrintUtils.BOLD);
			PrintUtils.printText(PrintUtils.printThreeData("菜品", "数量", "金额\n"));
			PrintUtils.printText("--------------------------------\n");
			PrintUtils.selectCommand(PrintUtils.BOLD_CANCEL);
			List<OrderDetailPrintModel> listDetails =  printModel.getDetails();
			if(listDetails!=null) {
				for(OrderDetailPrintModel detail : listDetails) {
					PrintUtils.printText(PrintUtils.printThreeData(detail.getOutTitle(), detail.getOutSize()+"",detail.getOutPrice().toString()+"\n"));
				}
			}
			//PrintUtils.printText(PrintUtils.printThreeData("牛肉面啊啊啊牛肉面啊啊啊", "888", "98886.00\n"));

			PrintUtils.printText("--------------------------------\n");
			PrintUtils.printText(PrintUtils.printTwoData("合计",printModel.getOrderTotal()+"\n"));
			PrintUtils.printText("--------------------------------\n");
			PrintUtils.printText(PrintUtils.printTwoData("支付", printModel.getOrderPay()+"\n"));
			PrintUtils.printText("--------------------------------\n");

			PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
			PrintUtils.printText("备注："+printModel.getOrderRemark());
			PrintUtils.printText("\n\n\n\n\n");
			socket.getOutputStream().close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
