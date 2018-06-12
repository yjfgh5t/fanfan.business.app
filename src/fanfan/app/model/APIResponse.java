package fanfan.app.model;

public class APIResponse<T> {
 
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	 
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public T getData() {
		return data;
	}

	public APIResponse<T> setData(T data) {
		this.data = data;
		return this;
	}
	
	public APIResponse<T> success() {
		 this.setCode(0);
		 this.setMsg("success");
		
		return this;
	}

	private int code;
	
	private boolean success;
	
	private String msg;
 	
	private T data; 
}
