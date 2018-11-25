package fanfan.app.manager;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import fanfan.app.constant.SPConstant;
import fanfan.app.model.OrderDetailPrintModel;
import fanfan.app.model.OrderPrintModel;
import fanfan.app.model.menum.MediaType;
import fanfan.app.util.BlueOldUtils;
import fanfan.app.util.PrintUtils;
import fanfan.app.util.SPUtils;

/**
 * 打印
 * 
 * @author jy
 *
 */
public class PrintManager {

	static PrintManager instance;

	static int newOrderPlayCount = 0;

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
		// 蓝牙打印
		boolean printState = blueToothPrint(printModel);

		if (!printState) {
			MediaManager.getInstrance().playMedia(MediaType.printFailByBlueMedia);
		}

		return printState;
	}

	/**
	 * 执行WebView打印
	 * 
	 * @param context
	 * @param webView
	 */
	@SuppressLint("NewApi")
	public boolean blueToothPrint(OrderPrintModel printModel) {
		try {
			// 获取Socket
			BluetoothSocket socket = BlueOldUtils.getInstance().getConnectSocket();
			if (socket != null) {
				PrintUtils.setOutputStream(socket.getOutputStream());
			} else {
				return false;
			}
			// 打印失败
			if (!PrintUtils.selectCommand(PrintUtils.RESET)) {
				return false;
			}
			PrintUtils.selectCommand(PrintUtils.LINE_SPACING_DEFAULT);
			PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
			PrintUtils.selectCommand(PrintUtils.BOLD);
			PrintUtils.printText(SPUtils.getInstance().getString(SPConstant.shopName, "饭饭点餐") + "\n");
			PrintUtils.printText(PrintUtils.printTwoData("排队号：#" + printModel.getOrderDateNum(),
					"桌号：" + printModel.getOrderDeskNum()) + "\n\n");
			PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
			PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
			PrintUtils
					.printText("备注：" + printModel.getOrderRemark() + " 【" + printModel.getOrderTypeText() + "】" + "\n");
			PrintUtils.selectCommand(PrintUtils.NORMAL);
			PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
			PrintUtils.printText("--------------------------------\n");
			PrintUtils.selectCommand(PrintUtils.BOLD);
			PrintUtils.printText(PrintUtils.printThreeData("菜品", "数量", "金额\n"));
			PrintUtils.selectCommand(PrintUtils.BOLD_CANCEL);
			List<OrderDetailPrintModel> listDetails = printModel.getDetails();
			int totalSize = 0;
			if (listDetails != null) {
				for (OrderDetailPrintModel detail : listDetails) {
					// 商品总数量
					if (detail.getOutType() == OrderDetailPrintModel.TYPE_COMMODITY
							|| detail.getOutType().equals(OrderDetailPrintModel.TYPE_COMMODITY_NORMS)) {
						totalSize++;
					}
					// outType=6时未餐盒 不需要打印数量
					String outSize = detail.getOutType().equals(OrderDetailPrintModel.TYPE_PACKAGE) ? ""
							: detail.getOutSize() + "";

					PrintUtils.printText(
							PrintUtils.printThreeData(detail.getOutTitle(), outSize, detail.getOutPrice() + "\n"));
				}
			}

			PrintUtils.printText(PrintUtils.printThreeData("合计: ", totalSize + "", printModel.getOrderTotal() + "\n"));
			PrintUtils.printText("--------------------------------\n");
			PrintUtils.printText(PrintUtils.printTwoData("付款方式: " + printModel.getOrderPayTypeText(),
					"付款: " + printModel.getOrderPay()));
			PrintUtils.printText("--------------------------------\n");
			PrintUtils.printText(PrintUtils.printTwoData("订单编号:", printModel.getOrderNum() + "\n"));
			PrintUtils.printText(PrintUtils.printTwoData("下单时间:", printModel.getOrderTime() + "\n"));
			PrintUtils.printText("\n\n\n\n\n");
			PrintUtils.selectCommand(PrintUtils.CUTE_PAPER);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

}
