package fanfan.app.manager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.util.Log;
import fanfan.app.constant.CodeConstant;
import fanfan.app.constant.SPConstant;
import fanfan.app.view.webview.X5WebView;

/**
 * 打印
 * @author jy
 *
 */
public class PrintManager {
	
	static PrintManager instance; 
	
	static MediaPlayer newOrderMediaPlay;
	
	static int newOrderPlayCount=0;
	
	static String printPath= "http://192.168.1.10:8080";//SPConstant.sdCardIndexPath+"/#/print";
	
	public static PrintManager getInstrance() {
		if (instance == null) {
			instance = new PrintManager();
		}
		return instance;
	}
	
	/**
	 * 打印订单
	 */
	public void printOrder(final Context context, String printJsonModel) {
		
		final X5WebView webView = new X5WebView(context);
		
		webView.loadUrl(printPath);
		//参数
		webViewCallBack(webView,printJsonModel,CodeConstant.Notify_Msg_CallKey+".print");
		
		//开启线程打印
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
					createWebPrintJob(context,webView);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		thread.run();
	}

	//回调js方法
	private void webViewCallBack(final X5WebView webView,final String data,final String callBackKey) {
		// TODO Auto-generated method stub 
		webView.post(new Runnable() {
			@Override
			public void run() {
				Log.d("webview", data);
				if(data.indexOf("{")==0) {
					// 回调js方法
					webView.loadUrl((String.format("javascript:window.callback(%s,\"%s\")",data,callBackKey)));
				}else {
					// 回调js方法
					webView.loadUrl((String.format("javascript:window.callback(\"%s\",\"%s\")",data,callBackKey)));
				}
			}
		});
	}
	
	/**
	 * 执行WebView打印
	 * @param context
	 * @param webView
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressLint("NewApi")
	public void createWebPrintJob(Context context, X5WebView webView) {

	    // Get a PrintManager instance
	    android.print.PrintManager printManager = (android.print.PrintManager)context.getSystemService(Context.PRINT_SERVICE);

	    // Get a print adapter instance
	    PrintDocumentAdapter printAdapter = (PrintDocumentAdapter) webView.createPrintDocumentAdapter("打印内容");
	    
	    printManager.print(printPath, printAdapter,null);
	    // Save the job object for later status checking
	    //mPrintJobs.add(printJob);
	}
	
}
