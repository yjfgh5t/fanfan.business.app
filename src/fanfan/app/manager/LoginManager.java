package fanfan.app.manager;

import java.util.HashMap;
import java.util.Map;

import fanfan.app.constant.SPConstant;
import fanfan.app.constant.UrlConstant;
import fanfan.app.model.APIResponse;
import fanfan.app.model.Response;
import fanfan.app.util.EncryptUtils;
import fanfan.app.util.SPUtils;
import fanfan.app.util.ToastUtils;

public class LoginManager {
	
	private static LoginManager loginManager;

	/**
	 * 获取单列
	 * @return
	 */
	public static LoginManager getInstrance() {
		
		if(loginManager==null) {
			loginManager = new LoginManager();
		} 
		return loginManager;
	}
	
	/**
	 * 登入
	 * @param username
	 * @param userpwd
	 */
	public void login( String username, String userpwd,final Response<Boolean> call) {
		
		//md5加密
		//userpwd = EncryptUtils.encryptMD5ToString(username+userpwd);
		 
		
		final Map<String,Object> params = new HashMap<>(); 
		params.put("userName", username);
		params.put("userPwd", userpwd);
		
		//调用接口
		OkHttpManager.getInstrance().post(UrlConstant.loginUrl,params, new Response<Boolean>() {

			@Override
			public void callBack(APIResponse<Boolean> response) {
				// 验证登入是否成功
				if(response.isSuccess() && response.getData()) {
					//保存用户信息
					SPUtils.getInstance().put(SPConstant.userName, params.get("userName").toString());
					SPUtils.getInstance().put(SPConstant.userPwd, params.get("userPwd").toString());
				}else {
					response.setData(false);
					ToastUtils.showShort("用户名或密码错误");
				} 
				 
				call.callBack(response);
			}
		});
	}
	
}
