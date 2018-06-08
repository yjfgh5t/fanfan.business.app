package fanfan.app.model;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.alibaba.fastjson.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;

public abstract class Response<T> implements Callback{ 
	
	public abstract void callBack(APIResponse<T> response); 
	
	@Override
	public void onFailure(Call call, IOException e) {
		
		APIResponse response = new APIResponse<T>();
		response.setCode(-1);
		response.setMsg(e.getMessage());
		response.setSuccess(false);
		callBack(response);
	}
	
	@Override
	public void onResponse(Call call, okhttp3.Response httpResponse){

		APIResponse response =  new APIResponse<T>();
		
		if(httpResponse.isSuccessful()&& httpResponse.code()==200) { 
			 
			try {
				//获取泛型类型
				ParameterizedType parameterizedType  = (ParameterizedType)getClass().getGenericSuperclass();
				
				if(httpResponse.request()!=null && httpResponse.request().header("is-file")!=null) {
					//下载文件 
					DownLoadModel fileModel = new DownLoadModel();
					fileModel.setContentLength(httpResponse.body().contentLength());
					//fileModel.setFileName(httpResponse.request().url().);
					fileModel.setInputStream(httpResponse.body().byteStream());
					response.setData(fileModel);   
				}else {
					String responseBody = httpResponse.body().string();
					//转换
					if(responseBody.indexOf("{")==0) {
						response =  JSONObject.parseObject(responseBody, APIResponse.class);
					}else {
						throw new IOException("请求服务出错");
					}
				}
				response.setSuccess(true); 
			} catch (IOException e) {
				response.setMsg(e.getMessage());
				response.setSuccess(false);
				response.setCode(-1); 
				e.printStackTrace();
			}
			
		}else { 
			response.setCode(-1);
			response.setSuccess(false);
			response.setMsg("请求服务出错");
		}
		
		//回调
		callBack(response);
		
		//关闭
		httpResponse.close();
	}
	
}
