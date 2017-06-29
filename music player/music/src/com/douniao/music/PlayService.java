package com.douniao.music;

import java.io.IOException;
import java.util.HashMap;

import com.douniao.music.Utils.MyApplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.widget.Toast;

public class PlayService extends Service {

	PlayMusicAcitivity repeate=new PlayMusicAcitivity();
	public static MediaPlayer player;
	public static String name;
	public static String path;
	public static String artist;
	
	public static int restate = 0;
	public static Context context;
	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		System.out.println("Service,onCreate!");
		if (player == null) {
			player = new MediaPlayer();
		}
		super.onCreate();
		context=this;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		System.out.println("Service,onStart!");

		if (name == intent.getStringExtra("name")) {
			Toast.makeText(PlayMusicAcitivity.context, "正在播放", 0).show();

		} else {
			name = intent.getStringExtra("name");
			path = intent.getStringExtra("path");
			artist = intent.getStringExtra("artist");
			System.out.println(name+path+artist);
			
				try {
					play();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}

		player.setOnCompletionListener(new OnCompletionListener() {

			
			public void onCompletion(MediaPlayer mp) {
				if(restate == 0)
				{
				player.reset();
				MyApplication.playStatus = 0;
				if(repeate.repeat==0&&repeate.shuffle==0){
					Toast.makeText(context, "正常", 0).show();
				MyApplication.musicPosition = (MyApplication.musicPosition + 1) % MyApplication.getPlayMusicList().size();
				HashMap<String, Object> map = MyApplication.getPlayMusicList().get(MyApplication.musicPosition);
				path = (String) map.get("path");
				name = (String) map.get("name");
				artist = (String) map.get("artist");
				}else if(repeate.repeat==1){
					Toast.makeText(context, "单曲", 0).show();
					MyApplication.musicPosition = MyApplication.musicPosition;
					HashMap<String, Object> map = MyApplication.getPlayMusicList().get(MyApplication.musicPosition);
					path = (String) map.get("path");
					name = (String) map.get("name");
					artist = (String) map.get("artist");
				}else if(repeate.shuffle==1){
					Toast.makeText(context, "随机", 0).show();
					int  num=(int)(100*Math.random()); 
					MyApplication.musicPosition = (num)% MyApplication.getPlayMusicList().size();
					HashMap<String, Object> map = MyApplication.getPlayMusicList().get(MyApplication.musicPosition);
					path = (String) map.get("path");
					name = (String) map.get("name");
					artist = (String) map.get("artist");
				}				
				try {
					play();
				} catch (Exception e) {
					System.out.println("播放有错误！");
				}
				}
				else if(restate == 1){
					player.reset();
					MyApplication.playStatus = 0;
					PlayMusicAcitivity.resetMusic();
					restate = 0;
				}
				

			}
		});
	}

	void play() throws IllegalArgumentException, IllegalStateException, IOException {
		player.reset();
		player.setDataSource(path);
		player.prepareAsync();
		player.setOnPreparedListener(new OnPreparedListener() {

			
			public void onPrepared(MediaPlayer mp) {
				if (player != null) {
					player.start();
					MyApplication.playStatus = 1;
					for (int i = 0; i < MyApplication.getPlayMusicList().size(); i++) {
						if (name.equals(MyApplication.getPlayMusicList().get(i).get("name"))) {
							MyApplication.musicPosition = i;
							break;
						}
					}
					MainActivity.footer.setText(name);
					MyApplication.playStatus = 1;
				}
			}
		});
	}


	public static void pause() {
		if (MyApplication.playStatus == 1) {
			player.pause();
			MyApplication.playStatus = 2;
		}
	}

	public static void goon() {
		if (MyApplication.playStatus != 0) {
			player.start();
			MyApplication.playStatus = 1;
		}
	}

	public static void stop() {
		if (MyApplication.playStatus != 0) {
			player.stop();
			MyApplication.playStatus = 0;
		}
	}

}
