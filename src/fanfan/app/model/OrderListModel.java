package fanfan.app.model;

import java.math.BigDecimal;
import java.util.Date;

public class OrderListModel {

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public String getOrderStateText() {
		return orderStateText;
	}

	public void setOrderStateText(String orderStateText) {
		this.orderStateText = orderStateText;
	}

	public BigDecimal getOrderPay() {
		return orderPay;
	}

	public void setOrderPay(BigDecimal orderPay) {
		this.orderPay = orderPay;
	}

	public Integer getCommodityTotal() {
		return commodityTotal;
	}

	public void setCommodityTotal(Integer commodityTotal) {
		this.commodityTotal = commodityTotal;
	}

	public String getCommoditImg() {
		return commoditImg;
	}

	public void setCommoditImg(String commoditImg) {
		this.commoditImg = commoditImg;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getLastPayTime() {
		return lastPayTime;
	}

	public void setLastPayTime(Long lastPayTime) {
		this.lastPayTime = lastPayTime;
	}

	public String getAlipayOrderStr() {
		return alipayOrderStr;
	}

	public void setAlipayOrderStr(String alipayOrderStr) {
		this.alipayOrderStr = alipayOrderStr;
	}

		private Integer id;

	    private String orderNum;

	    private Integer orderState;

	    private String  orderStateText;

	    private BigDecimal orderPay;

	    private Integer commodityTotal;

	    private String commoditImg;

	    private String title;

	    private Date createTime;

	    //最后付款剩余秒数
	    private Long lastPayTime;

	    //支付宝支付串
	    private String alipayOrderStr;
}
