package fanfan.app.model;

public class OrderDetailPrintModel {

	public final static Integer TYPE_COMMODITY = 1;
	public final static Integer TYPE_COMMODITY_NORMS = 5;
	public final static Integer TYPE_PACKAGE = 6;

	public String getOutTitle() {
		return outTitle;
	}

	public void setOutTitle(String outTitle) {
		this.outTitle = outTitle;
	}

	public float getOutPrice() {
		return outPrice;
	}

	public void setOutPrice(float outPrice) {
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

	private float outPrice;

	// 商品数量
	private Integer outSize;

	private Integer outType;
}
