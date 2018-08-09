package fanfan.app.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderPrintModel {

	/**
	 * 订单号
	 */
	 private String orderNum;
	 
	 public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public BigDecimal getOrderPay() {
		return orderPay;
	}

	public void setOrderPay(BigDecimal orderPay) {
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

	/**
	  * 支付金额
	  */
	 private BigDecimal orderPay;
	 
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
	 private List<OrderDetailPrintModel> details;
	 
	 class OrderDetailPrintModel{
		 
		    public String getOutTitle() {
			return outTitle;
		}

		public void setOutTitle(String outTitle) {
			this.outTitle = outTitle;
		}

		public BigDecimal getOutPrice() {
			return outPrice;
		}

		public void setOutPrice(BigDecimal outPrice) {
			this.outPrice = outPrice;
		}

		public Integer getOutSize() {
			return outSize;
		}

		public void setOutSize(Integer outSize) {
			this.outSize = outSize;
		}

		public Integer getOutType() {
			return outType;
		}

		public void setOutType(Integer outType) {
			this.outType = outType;
		}

			private String outTitle;

		    private BigDecimal outPrice;

		    //商品数量
		    private Integer outSize;

		    private Integer outType;
	 }
}
