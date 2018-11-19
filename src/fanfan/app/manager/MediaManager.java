package fanfan.app.manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import fanfan.app.constant.SPConstant;
import fanfan.app.model.menum.MediaType;
import fanfan.app.util.StringUtils;
import fanfan.app.util.Utils;

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
	private static String newOrderMediaPath = SPConstant.sdCardWWWPath+"/static/media/new_order.mp3";
	
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
	private static LinkedBlockingQueue<MediaPlayer> linkedMedia;
	
	/**
	 * 上一次播放地址
	 */
	private static String prePlayPath="";
	
	/**
	 * 音量管理器
	 */
	 private AudioManager audioMgr = null;
	
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
	}
	
	// 播放
	public synchronized void playMedia(MediaType mediaType) {
		
		try {
			MediaPlayer player = new MediaPlayer();
			player.setOnCompletionListener(new OnCompletionListener() { 
				/**
				 * 播放完成后 播放新的文件
				 */
				public void onCompletion(MediaPlayer mp) {
					mediaPlayer = linkedMedia.poll();
					if(mediaPlayer!=null) {
						mediaPlayer.start();
					}
				}
			});
			
			//放入播放队列
			switch(mediaType) {
				//新订单
				case newOrderMedia:
					player.setDataSource(newOrderMediaPath);
					break;
				//蓝牙打印失败
				case printFailByBlueMedia:
					player.setDataSource(printFailByBlueMediaPath);
					break;
			}
			
			player.prepare();
			//如果当前没有播放
			if(mediaPlayer==null || !mediaPlayer.isPlaying()) {
				initPlayWork();
				mediaPlayer = player;
				mediaPlayer.start();
			}else {
				//添加至队列
				linkedMedia.add(player);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initPlayWork() {
		try {
		 audioMgr = (AudioManager) Utils.getApp().getSystemService(Utils.getApp().AUDIO_SERVICE);
		 int maxVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		 audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume,AudioManager.FLAG_PLAY_SOUND);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
