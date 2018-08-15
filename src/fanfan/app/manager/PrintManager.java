package fanfan.app.manager;

import java.io.IOException;

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
import fanfan.app.model.OrderPrintModel;
import fanfan.app.util.BlueToothUtils;
import fanfan.app.util.PrintUtils;
import fanfan.app.view.webview.X5WebView;

/**
 * 打印
 * @author jy
 *
 */
public class PrintManager {
	
	static PrintManager instance; 
	
	static int newOrderPlayCount=0;
	
	public static PrintManager getInstrance() {
		if (instance == null) {
			instance = new PrintManager();
		}
		return instance;
	}
	
	/**
	 * 打印订单
	 */
	public void printOrder(final Context context, OrderPrintModel printModel) {
		//开启线程打印
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
					blueToothPrint(context);
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
	public void blueToothPrint(Context context) {
		BluetoothDevice device = BlueToothUtils.getInstance().getCurrentDevice();
		try {
			BluetoothSocket socket =  device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
			
			PrintUtils.setOutputStream(socket.getOutputStream());
			
			PrintUtils.selectCommand(PrintUtils.RESET);
			PrintUtils.selectCommand(PrintUtils.LINE_SPACING_DEFAULT);
			PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
			PrintUtils.printText("美食餐厅\n\n");
			PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
			PrintUtils.printText("桌号：1号桌\n\n");
			PrintUtils.selectCommand(PrintUtils.NORMAL);
			PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
			PrintUtils.printText(PrintUtils.printTwoData("订单编号", "201507161515\n"));
			PrintUtils.printText(PrintUtils.printTwoData("点菜时间", "2016-02-16 10:46\n"));
			PrintUtils.printText(PrintUtils.printTwoData("上菜时间", "2016-02-16 11:46\n"));
			PrintUtils.printText(PrintUtils.printTwoData("人数：2人", "收银员：张三\n"));

			PrintUtils.printText("--------------------------------\n");
			PrintUtils.selectCommand(PrintUtils.BOLD);
			PrintUtils.printText(PrintUtils.printThreeData("项目", "数量", "金额\n"));
			PrintUtils.printText("--------------------------------\n");
			PrintUtils.selectCommand(PrintUtils.BOLD_CANCEL);
			PrintUtils.printText(PrintUtils.printThreeData("面", "1", "0.00\n"));
			PrintUtils.printText(PrintUtils.printThreeData("米饭", "1", "6.00\n"));
			PrintUtils.printText(PrintUtils.printThreeData("铁板烧", "1", "26.00\n"));
			PrintUtils.printText(PrintUtils.printThreeData("一个测试", "1", "226.00\n"));
			PrintUtils.printText(PrintUtils.printThreeData("牛肉面啊啊", "1", "2226.00\n"));
			PrintUtils.printText(PrintUtils.printThreeData("牛肉面啊啊啊牛肉面啊啊啊", "888", "98886.00\n"));

			PrintUtils.printText("--------------------------------\n");
			PrintUtils.printText(PrintUtils.printTwoData("合计", "53.50\n"));
			PrintUtils.printText(PrintUtils.printTwoData("抹零", "3.50\n"));
			PrintUtils.printText("--------------------------------\n");
			PrintUtils.printText(PrintUtils.printTwoData("应收", "50.00\n"));
			PrintUtils.printText("--------------------------------\n");

			PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
			PrintUtils.printText("备注：不要辣、不要香菜");
			PrintUtils.printText("\n\n\n\n\n");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
