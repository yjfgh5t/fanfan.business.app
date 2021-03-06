package fanfan.app.service;

import java.util.Calendar;

import com.tencent.android.tpush.XGPushManager;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import fanfan.app.constant.CodeConstant;
import fanfan.app.constant.SPConstant;
import fanfan.app.receiver.ForegroundReceiver;
import fanfan.app.util.SPUtils;
import fanfan.app.util.Utils;
import fanfan.business.app.R;

public class ForegroundService extends Service {

	/**
	 * id不可设置为0,否则不能设置为前台service
	 */
	public static final int NOTICE_ID = 100;

	private boolean IS_CREATE = false;

	private Long startTime = 0L;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		startTime = SystemClock.elapsedRealtime();
		// 创建通知栏
		createNotification();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// 设置通知
		createNotification();
		// 判断信鸽是否存活
		if (!Utils.isAppAlive(this, "fanfan.business.app:xg_service_v4")) {
			XGPushManager.startPushService(this);
		}

		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		// 读者可以修改此处的Minutes从而改变提醒间隔时间
		// 此处是设置每隔2分钟启动一次
		int Minutes = 1 * 60 * 1000;
		long triggerAtTime = SystemClock.elapsedRealtime() + Minutes;
		// 此处设置开启AlarmReceiver这个Service
		Intent recevierIntent = new Intent(this, ForegroundReceiver.class);
		recevierIntent.setAction(CodeConstant.Notify_Alarm_Action);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, recevierIntent, 0);
		// ELAPSED_REALTIME_WAKEUP表示让定时任务的出发时间从系统开机算起，并且会唤醒CPU。
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

		return super.onStartCommand(intent, START_STICKY, startId);
	}

	@Override
	public void onDestroy() {
		// 停止前台服务
		stopForeground(true);
		super.onDestroy();
	}

	/**
	 * 创建通知消息
	 */
	private void createNotification() {

		// 使用兼容版本
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		// 设置状态栏的通知图标
		builder.setSmallIcon(R.drawable.icon_msg);
		// 设置通知栏横条的图标
		builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_msg));
		// 禁止用户点击删除按钮删除
		builder.setAutoCancel(false);
		// 禁止滑动删除
		builder.setOngoing(true);
		// 值显示一个
		builder.setOnlyAlertOnce(true);
		// 优先级越高 越靠前
		builder.setPriority(NotificationCompat.PRIORITY_MAX);

		String now = addZero(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) + ":"
				+ addZero(Calendar.getInstance().get(Calendar.MINUTE));

		// 设置通知栏的标题内容
		builder.setContentTitle(SPUtils.getInstance().getString(SPConstant.notifyContentTitle, "饭饭商户版正在运行 " + now));
		// 内容
		builder.setContentText(SPUtils.getInstance().getString(SPConstant.notifyContentText, "为保证接收订单信息、请勿关闭"));

		Intent activeIntent = new Intent(this, ForegroundReceiver.class);
		activeIntent.setAction(CodeConstant.Notify_Click_Action);
		// 点击
		PendingIntent clickIntent = PendingIntent.getBroadcast(this, 0, activeIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(clickIntent);
		// 创建通知
		Notification notification = builder.build();
		// 设置不被清楚
		notification.flags = Notification.FLAG_NO_CLEAR;
		// 设置为前台服务
		startForeground(NOTICE_ID, notification);
	}

	/**
	 * 重启服务
	 */
	private void restartService() {
		NotificationManager mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mManager.cancel(NOTICE_ID);

		// 重启自己
		Intent intent = new Intent(getApplicationContext(), ForegroundService.class);
		startService(intent);
	}

	/**
	 * 补零
	 * 
	 * @param time
	 * @return
	 */
	private String addZero(Integer time) {
		if (time < 10) {
			return "0" + time;
		}
		return time + "";
	}
}
