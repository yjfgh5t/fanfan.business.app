package fanfan.app.manager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;

import fanfan.app.model.APIResponse;
import fanfan.app.model.Response;
import fanfan.app.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpManager {

	static OkHttpClient okHttpClient = null;
	static OkHttpManager instance;

	public static OkHttpManager getInstrance() {
		if (instance != null) {
			return instance;
		}

		instance = new OkHttpManager();

		OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS);

		okHttpClient = new OkHttpClient();

		// 设置连接时间
		okHttpClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(180, TimeUnit.SECONDS)
				.writeTimeout(180, TimeUnit.SECONDS);

		return instance;
	}

	/**
	 * 异步 get
	 * 
	 * @param url
	 * @return
	 */
	public <T> void get(String url, Response<T> response) {
		get(url, null, response);
	}

	/**
	 * 异步 get
	 * 
	 * @param url
	 * @return
	 */
	public <T> void get(String url, Map<String, Object> params, Response<T> response) {

		Request request = buildRequest(url, "GET", params);
		if(request==null) {
			return;
		}
		okHttpClient.newCall(request).enqueue(response);
	}

	/**
	 * 异步获取
	 * 
	 * @param url
	 * @return
	 */
	public <T> void post(String url, Response<T> response) {
		post(url, null, response);
	}

	/**
	 * 异步获取
	 * 
	 * @param url
	 * @return
	 */
	public <T> void post(String url, Map<String, Object> params, Response<T> response) {
		Request request = buildRequest(url, "POST", params);
		if(request==null) {
			return;
		}
		okHttpClient.newCall(request).enqueue(response);
	}

	/**
	 * 构建Reques对象
	 * 
	 * @param url
	 * @param method
	 * @return
	 */
	private Request buildRequest(String url, String method, Map<String, Object> params) {

		//验证是否有网咯
		if(!NetworkUtils.isConnected()) {
			
		}
		
		Request.Builder requestBuilder = new Request.Builder();

		RequestBody formBody = null;

		// Get 或者 Post
		boolean isGet = method.equals("GET");

		if (params != null) {

			String contentType = "form";

			// json
			if (params.containsKey("content-type")) {
				// 获取操作类型
				contentType = params.get("content-type").toString();

				switch (contentType) {
				case "json":
					formBody = RequestBody.create(MediaType.parse("application/json"),
							params.get("content").toString());
					break;
				case "file":
					//formBody = RequestBody.create(MediaType.parse("application/octet-stream"),"");
					break;
				}
				
				//移除 防止拼接至参数
				params.remove("content-type");
			}

			Iterator<Entry<String, Object>> iterator = params.entrySet().iterator();

			FormBody.Builder formBuilder = new FormBody.Builder();

			StringBuilder strBuilder = new StringBuilder();

			while (iterator.hasNext()) {

				Entry<String, Object> item = iterator.next();
				// 设置Header参数
				if (item.getKey().indexOf("head.") == 0) {
					requestBuilder.addHeader(item.getKey().substring(5, item.getKey().length()),
							item.getValue().toString());
				} else {

					if (isGet) {
						strBuilder.append(item.getKey() + "=" + item.getValue() + "&");
					} else {
						formBuilder.addEncoded(item.getKey(), item.getValue().toString());
					}
				}
			}

			// 判断Get Post
			if (isGet) {
				if (strBuilder.length() > 0) {
					if (url.indexOf("?") == -1) {
						url += "?" + strBuilder.toString();
					} else {
						url += "&" + strBuilder.toString();
					}
					// 去重最后一个&
					url = url.substring(0, url.length() - 1);
				}
			} else {
				// form 请求
				if (contentType.equals("form")) {
					formBody = formBuilder.build();
				}
			}
		}

		requestBuilder.url(url);
		requestBuilder.method(method, formBody);
		return requestBuilder.build();
	}
}
