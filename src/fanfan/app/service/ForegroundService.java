package fanfan.app.service;
 
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import fanfan.app.constant.SPConstant;
import fanfan.app.receiver.ForegroundReceiver;
import fanfan.app.util.SPUtils;
import fanfan.app.view.WebViewActivity;
import fanfan.business.app.R;

public class ForegroundService extends Service {

	/**
     * id不可设置为0,否则不能设置为前台service
     */
    private static final int NOTIFICATION_DOWNLOAD_PROGRESS_ID = 0x0001;
    
    private boolean IS_CREATE=false;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		
		if(!IS_CREATE) {
			//创建通知栏
			createNotification(intent);
			//设置已经创建
			IS_CREATE = true;
		}
		 return super.onStartCommand(intent, flags, startId);
	}
	
	
	/**
	 * 创建通知消息
	 */
	private void createNotification(Intent intent) {
		//使用兼容版本
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		//设置状态栏的通知图标
		builder.setSmallIcon(R.drawable.ic_launcher);
		 //设置通知栏横条的图标
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher));
        //禁止用户点击删除按钮删除
        builder.setAutoCancel(false);
        //禁止滑动删除
        builder.setOngoing(true);
        //优先级越高 越靠前
        builder.setPriority(NotificationCompat.PRIORITY_MAX); 
        //设置通知栏的标题内容
        builder.setContentTitle(SPUtils.getInstance().getString(SPConstant.notifyContentTitle, "饭饭商户版正在运行"));
        builder.setContentText(SPUtils.getInstance().getString(SPConstant.notifyContentText, "为保证正常接收订单消息、请勿关闭"));
        
        Intent activeIntent = new Intent(this,ForegroundReceiver.class);
        //点击
        PendingIntent clickIntent = PendingIntent.getBroadcast(this, 0, activeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(clickIntent);
        //创建通知
        Notification notification = builder.build();
        //设置不被清楚
        notification.flags = Notification.FLAG_NO_CLEAR;
        //设置为前台服务
        startForeground(NOTIFICATION_DOWNLOAD_PROGRESS_ID,notification);
	}
	
}
