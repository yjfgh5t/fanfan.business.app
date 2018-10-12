package fanfan.app.manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import android.annotation.SuppressLint;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import fanfan.app.constant.SPConstant;
import fanfan.app.model.menum.MediaType;
import fanfan.app.util.StringUtils;

/**
 * 播放管理
 * @author japper
 *
 */
@SuppressLint("NewApi")
public class MediaManager {
	
	private static MediaManager instance;
	
	/**
	 * 新订单mp3地址
	 */
	private static String newOrderMediaPath = SPConstant.sdCardWWWPath+"/static/media/new-order.mp3";
	
	/**
	 * 蓝牙打印失败
	 */
	private static String printFailByBlueMediaPath = SPConstant.sdCardWWWPath+"/static/media/print_fail_bule.mp3";
	
	/**
	 * 播放对象
	 */
	private static MediaPlayer mediaPlayer; 
	
	/**
	 * 待播放的队列
	 */
	private static LinkedBlockingQueue<String> linkedMedia;
	
	/**
	 * 上一次播放地址
	 */
	private static String prePlayPath="";
	
	/**
	 * 单列对象
	 * @return
	 */
	public static MediaManager getInstrance() {
		if (instance == null) {
			instance = new MediaManager();
		}
		return instance;
	}
	
	/**
	 * 构造函数
	 */
	public MediaManager() {
		linkedMedia = new LinkedBlockingQueue<>();
		
		mediaPlayer = new MediaPlayer(); 
		//播放完成事件
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() { 
			public void onCompletion(MediaPlayer mp) {
				playStart();
			}
		});
	}
	
	// 播放
	public synchronized void playMedia(MediaType mediaType) {
		
		try { 
			//放入播放队列
			switch(mediaType) {
				//新订单
				case newOrderMedia:
					linkedMedia.put(newOrderMediaPath);
					break;
				//蓝牙打印失败
				case printFailByBlueMedia:
					linkedMedia.put(printFailByBlueMediaPath);
					break;
			}
			
			//开始播放
			playStart();
		
		} catch (InterruptedException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 执行播放
	 */
	private void playStart() {
		try {
			
			if(!mediaPlayer.isPlaying()) {
			
				String mediaPath = linkedMedia.poll();
				//队列不为空
				if(null !=mediaPath) {
					if(!prePlayPath.equals(mediaPath)) {
						//设置播放路径
						mediaPlayer.setDataSource(mediaPath);
						//准备
						mediaPlayer.prepare();
						//记录上一次播放
						prePlayPath = mediaPath;
					}
					//开始播放
					mediaPlayer.start();
				}
			}
		}catch(IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
			e.printStackTrace();
		}
	}
	
}
