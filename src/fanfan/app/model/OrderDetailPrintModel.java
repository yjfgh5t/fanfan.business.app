package fanfan.app.model;

import java.math.BigDecimal;

public class OrderDetailPrintModel {
	
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

		    //商品数量
		    private Integer outSize;

		    private Integer outType;
}
