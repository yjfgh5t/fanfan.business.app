package fanfan.app.model;

public class PictureOptionModel {
	
	/**
	 * 打开类型 1:拍照   2:相册
	 */
	private Integer openType;
	
	/**
	 * 是否水印 
	 */
	private Integer hasWatermark;
	
	public Integer getHasWatermark() {
		return hasWatermark;
	}

	public void setHasWatermark(Integer hasWatermark) {
		this.hasWatermark = hasWatermark;
	}

	/**
	 * 是否裁剪图片
	 */
	private Integer hasCutImage;

	/**
	 * 水印文字
	 */
	private String watermark;
	
 

	public Integer getHasCutImage() {
		return hasCutImage;
	}

	public void setHasCutImage(Integer hasCutImage) {
		this.hasCutImage = hasCutImage;
	}


	public Integer getOpenType() {
		return openType;
	}

	public void setOpenType(Integer openType) {
		this.openType = openType;
	}

	public String getWatermark() {
		return watermark;
	}

	public void setWatermark(String watermark) {
		this.watermark = watermark;
	} 
 
	
	
	

}
