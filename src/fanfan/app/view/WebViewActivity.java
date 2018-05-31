package fanfan.app.view;
 
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import fanfan.app.manager.VersionManager;
import fanfan.business.app.R; 

public class WebViewActivity extends Activity {

	X5WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		webView = (X5WebView) findViewById(R.id.full_web_webview);
		
		initWebView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	@SuppressLint("NewApi")
	private void initWebView() {
		
		//加载Url地址
		webView.loadUrl(VersionManager.getInstrance().getIndexPath());
		 
		getWindow().setFormat(PixelFormat.TRANSLUCENT);

		webView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
		
		webView.addJavascriptInterface(new JavaScriptAPI() {

			@Override
			public void onJsFunctionCalled(String tag) {
				// TODO Auto-generated method stub 
			}
			
			@JavascriptInterface
			public void onButtonCLick() {
				
			}
			
		} , "fanfan.android"); 
	}
	
	
	public interface JavaScriptAPI{
		
		void onJsFunctionCalled(String tag); 
	}
}
