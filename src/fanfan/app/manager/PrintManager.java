package fanfan.app.manager;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import fanfan.app.constant.SPConstant;
import fanfan.app.model.OrderDetailPrintModel;
import fanfan.app.model.OrderPrintModel;
import fanfan.app.model.menum.MediaType;
import fanfan.app.util.BlueOldUtils;
import fanfan.app.util.PicFromPrintUtils;
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
		boolean printState = blueToothOrderPrint(printModel);

		if (!printState) {
			MediaManager.getInstrance().playMedia(MediaType.printFailByBlueMedia);
		}

		return printState;
	}

	public boolean printImg(final Bitmap bit) {
		// 蓝牙打印
		boolean printState = blueToothImgPrint(bit);

		if (!printState) {
			MediaManager.getInstrance().playMedia(MediaType.printFailByBlueMedia);
		}

		return printState;
	}

	private boolean blueToothImgPrint(final Bitmap bit) {
		try {
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
			// 打印图片指令
			PrintUtils.selectCommand(PrintUtils.IMAGE);
			// 转二进制
			byte[] imgData = PicFromPrintUtils.draw2PxPoint(bit);
			// 开始打印
			PrintUtils.selectCommand(imgData);
			// 走纸
			PrintUtils.selectCommand(PrintUtils.END);
			// 切纸
			PrintUtils.selectCommand(PrintUtils.CUTE_PAPER);

			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 蓝牙订单打印
	 * 
	 * @param context
	 * @param webView
	 */
	@SuppressLint("NewApi")
	private boolean blueToothOrderPrint(OrderPrintModel printModel) {
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
			PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT);
			PrintUtils.printText(SPUtils.getInstance().getString(SPConstant.shopName, "饭饭点餐") + "\n");
			PrintUtils.printText(
					PrintUtils.printTwoData("排队号：#" + printModel.getOrderDateNum(), printModel.getOrderDeskNum())
							+ "\n\n");
			PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
			PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
			PrintUtils
					.printText("备注：" + printModel.getOrderRemark() + " 【" + printModel.getOrderTypeText() + "】" + "\n");
			PrintUtils.selectCommand(PrintUtils.NORMAL);
			PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT);
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
						totalSize += detail.getOutSize();
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
			PrintUtils.printText(PrintUtils.printTwoData("收货人:", "江洋（15821243531）"));
			PrintUtils.printText("上海市松江区城鸿路222弄8号楼801室");
			PrintUtils.printText("\n\n\n\n\n");
			PrintUtils.selectCommand(PrintUtils.CUTE_PAPER);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 图片打印
	 * 
	 * @return
	 */
	private boolean blueToothImagePrint() {

		return false;
	}

}
