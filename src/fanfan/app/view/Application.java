package fanfan.app.view;
 
import fanfan.app.manager.VersionManager; 

public class Application extends android.app.Application {
 

	@Override
	public void onCreate() {
		//刷新Html版本
		VersionManager.getInstrance().refshHtmlVersion();
    }
}
