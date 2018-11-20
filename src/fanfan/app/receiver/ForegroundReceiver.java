package fanfan.app.receiver;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import fanfan.app.constant.CodeConstant;
import fanfan.app.service.ForegroundService;
import fanfan.app.util.Utils;
import fanfan.app.view.WebViewActivity;

@SuppressLint("NewApi")
public class ForegroundReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		//时钟广播
		if(CodeConstant.Notify_Alarm_Action.equals(intent.getAction()) || CodeConstant.Notify_Click_Action.equals(intent.getAction())) {
			 Intent serviceIntent = new Intent();  
			 serviceIntent.setClass(context, ForegroundService.class);  
            // 启动service
            context.startService(serviceIntent);
		}
		
		//点击通知消息广播
		if(CodeConstant.Notify_Click_Action.equals(intent.getAction())) {
			//打开Notify广播
			if(Utils.isAppAlive(context, "fanfan.business.app")) {
				Log.i("ForegroundReceiver", "the app process is alive");
				//如果存活的话，就直接启动WebViewActivity，但要考虑一种情况，就是app的进程虽然仍然在
	            //但Task栈已经空了，比如用户点击Back键退出应用，但进程还没有被系统回收，如果直接启动
	            //DetailActivity,再按Back键就不会返回MainActivity了。所以在启动
	            //DetailActivity前，要先启动MainActivity。
				Intent webViewIntent = new Intent(context, WebViewActivity.class); 
	            Log.i("ForegroundReceiver", "the app process is alive");
	            //将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
	            //如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
	            //如果Task栈不存在MainActivity实例，则在栈顶创建
	            webViewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	            Intent[] intents = {webViewIntent};
	            //启动activity
	            context.startActivities(intents);
			} else {
				//进程被kill 重新启动
				  Log.i("ForegroundReceiver", "the app process is dead");
				  Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("fanfan.business.app");
		          launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		          Bundle args = new Bundle();
		          launchIntent.putExtra("parasName", args);
		          context.startActivity(launchIntent);
			}
		}
	}

}
