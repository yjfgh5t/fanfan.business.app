package fanfan.app.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

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
	 private Date orderTime;
	 
	 /**
	  * 商品数量
	  */
	 private Integer commoditySize;
	 
	 /**
	  * 订单详情
	  */
	 @JSONField(name="detailList")
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

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
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
