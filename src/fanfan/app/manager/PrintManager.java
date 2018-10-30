package fanfan.app.manager;

import java.io.IOException;
import java.io.OutputStream;
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
import fanfan.app.model.menum.MediaType;
import fanfan.app.util.BlueUtils;
import fanfan.app.util.BluetoothConnector.BluetoothSocketWrapper;
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
	public boolean printOrder(final OrderPrintModel printModel) {
		//蓝牙打印
		boolean printState = blueToothPrint(printModel);
		
		if(!printState) {
			MediaManager.getInstrance().playMedia(MediaType.printFailByBlueMedia);
		}
		
		return printState;
	}

	
	/**
	 * 执行WebView打印
	 * @param context
	 * @param webView
	 */
	@SuppressLint("NewApi")
	public boolean blueToothPrint(OrderPrintModel printModel) {
		try {
			//打印失败
			if(!PrintUtils.selectCommand(PrintUtils.RESET)) {
				return false;
			}
			PrintUtils.selectCommand(PrintUtils.LINE_SPACING_DEFAULT);
			PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
			PrintUtils.selectCommand(PrintUtils.BOLD);
			PrintUtils.printText(SPUtils.getInstance().getString(SPConstant.shopName,"饭饭点餐") +"\n\n"); 
			PrintUtils.printText(PrintUtils.printTwoData("排队号：#"+printModel.getOrderDateNum(), "桌号："+printModel.getOrderDeskNum())+"\n\n");
			PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
			PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
			PrintUtils.printText("备注："+printModel.getOrderRemark()+" 【"+printModel.getOrderTypeText()+"】"+"\n");
			PrintUtils.selectCommand(PrintUtils.NORMAL);
			PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
			PrintUtils.printText("--------------------------------\n");
			PrintUtils.selectCommand(PrintUtils.BOLD);
			PrintUtils.printText(PrintUtils.printThreeData("菜品", "数量", "金额\n"));
			PrintUtils.printText("--------------------------------\n");
			PrintUtils.selectCommand(PrintUtils.BOLD_CANCEL);
			List<OrderDetailPrintModel> listDetails =  printModel.getDetails();
			if(listDetails!=null) {
				for(OrderDetailPrintModel detail : listDetails) {
					// outType=6时未餐盒 不需要打印数量
					String outSize = detail.getOutType().equals(6)?"":detail.getOutSize()+"";
					PrintUtils.printText(PrintUtils.printThreeData(detail.getOutTitle(), outSize,detail.getOutPrice()+"\n"));
				}
			}
			PrintUtils.printText("--------------------------------\n");
			PrintUtils.printText(PrintUtils.printTwoData("合计",printModel.getOrderTotal()+"\n"));
			PrintUtils.printText("--------------------------------\n");
			PrintUtils.printText(PrintUtils.printTwoData("支付", printModel.getOrderPay()+"\n"));
			PrintUtils.printText("--------------------------------\n");
			PrintUtils.printText(PrintUtils.printTwoData("订单编号",printModel.getOrderNum()+"\n"));
			PrintUtils.printText(PrintUtils.printTwoData("下单时间",printModel.getOrderTime()+"\n"));
			PrintUtils.printText("\n\n\n\n\n");
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
}
