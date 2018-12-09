package fanfan.app.model;

import fanfan.app.constant.SPConstant;
import fanfan.app.util.SPUtils;

public class HeadModel {

	private String clientType;

	private Integer userId;

	private Integer customerId;

	public HeadModel() {
		this.clientType = "Android";
		this.userId = -1;
	}

	@Override
	public String toString() {
		this.customerId = SPUtils.getInstance().getInt(SPConstant.customerId, -1);

		return String.format("{\"clientType\":\"%s\",\"userId\":%d,\"customerId\":%d}", this.clientType, this.userId,
				this.customerId);
	}

}
