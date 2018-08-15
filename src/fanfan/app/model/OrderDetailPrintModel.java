package fanfan.app.model;

import java.math.BigDecimal;

public class OrderDetailPrintModel {
	
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
