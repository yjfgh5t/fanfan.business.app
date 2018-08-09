package fanfan.app.manager;

import java.io.File;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import fanfan.app.constant.SPConstant;

/**
 * 播放管理
 * @author japper
 *
 */
@SuppressLint("NewApi")
public class MediaManager {
	
	static MediaManager instance;
	
	/**
	 * 新订单mp3地址
	 */
	static String newOrderMediaPath = SPConstant.sdCardWWWPath+"/static/media/new-order.mp3";
	
	static MediaPlayer newOrderMediaPlay;
	
	static int newOrderPlayCount=0;
	
	public static MediaManager getInstrance() {
		if (instance == null) {
			instance = new MediaManager();
		}
		return instance;
	}
	
	
	/**
	 * 播放新订单 只允许播放一个
	 */
	public synchronized void playNewOrder(){
		try {
			readFile(SPConstant.sdCardPath);
			if(newOrderMediaPlay==null) {
				newOrderMediaPlay = new MediaPlayer();
				newOrderMediaPlay.setDataSource(newOrderMediaPath);
				//准备
				newOrderMediaPlay.prepare();
				//播放完成事件
				newOrderMediaPlay.setOnCompletionListener(new OnCompletionListener() { 
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
						if(newOrderPlayCount>0) {
							newOrderPlayCount--;
							//继续播放
							newOrderMediaPlay.start();
						}
					}
				});
				
			}
			//设置下一个播放
			if(newOrderMediaPlay.isPlaying()) {
				newOrderPlayCount++;
			}else {
				//开始播放
				newOrderMediaPlay.start();
			}
		} catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void readFile(String filePath) {
		
		File path = new File(filePath);
		
		RecursionDeleteFile(path);
		
	}
	
	public void RecursionDeleteFile(File file){
	    if(file.isFile()){
	       System.out.println(file.getName());
	        return;
	    }
	    if(file.isDirectory()){
	        File[] childFile = file.listFiles();
	        if(childFile == null || childFile.length == 0){
	        	System.out.println(file.getName());
	            return;
	        }
	        for(File f : childFile){
	            RecursionDeleteFile(f);
	        }
	        //file.delete();
	    }
	}
	
}
