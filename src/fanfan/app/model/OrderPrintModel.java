package fanfan.app.model;

import java.util.List;

public class OrderPrintModel {

	/**
	 * 订单号
	 */
	private String orderNum;

	/**
	 * 总金额
	 */
	private float orderTotal;

	/**
	 * 支付金额
	 */
	private float orderPay;

	/**
	 * 桌号
	 */
	private String orderDeskNum;

	/**
	 * 编号
	 */
	private String orderDateNum;

	/**
	 * 备注
	 */
	private String orderRemark;

	/**
	 * 下单时间
	 */
	private String orderTime;

	/**
	 * 商品数量
	 */
	private Integer commoditySize;

	/**
	 * 支付类型文本
	 */
	private String orderPayTypeText;

	/**
	 * 打包/膛吃
	 */
	private String orderTypeText;

	public String getOrderPayTypeText() {
		return orderPayTypeText;
	}

	public void setOrderPayTypeText(String orderPayTypeText) {
		this.orderPayTypeText = orderPayTypeText;
	}

	public String getOrderTypeText() {
		return orderTypeText;
	}

	public void setOrderTypeText(String orderTypeText) {
		this.orderTypeText = orderTypeText;
	}

	/**
	 * 订单详情
	 */
	private List<OrderDetailPrintModel> details;

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public float getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(float orderTotal) {
		this.orderTotal = orderTotal;
	}

	public float getOrderPay() {
		return orderPay;
	}

	public void setOrderPay(float orderPay) {
		this.orderPay = orderPay;
	}

	public String getOrderDeskNum() {
		return orderDeskNum;
	}

	public void setOrderDeskNum(String orderDeskNum) {
		this.orderDeskNum = orderDeskNum;
	}

	public String getOrderDateNum() {
		return orderDateNum;
	}

	public void setOrderDateNum(String orderDateNum) {
		this.orderDateNum = orderDateNum;
	}

	public String getOrderRemark() {
		return orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public Integer getCommoditySize() {
		return commoditySize;
	}

	public void setCommoditySize(Integer commoditySize) {
		this.commoditySize = commoditySize;
	}

	public List<OrderDetailPrintModel> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetailPrintModel> details) {
		this.details = details;
	}
}
