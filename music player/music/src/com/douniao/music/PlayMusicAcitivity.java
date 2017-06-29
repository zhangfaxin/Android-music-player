package com.douniao.music;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;

import com.douniao.music.Utils.LyricView;
import com.douniao.music.Utils.MyApplication;
import com.douniao.music.Utils.PlayUtils;
import com.douniao.music.Utils.SongLyric;
import com.huazi.Mp3Player.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import userinformation.SensorManagerHelper;
import userinformation.SensorManagerHelper.OnShakeListener;

public class PlayMusicAcitivity extends Activity {
	public static int repeat=0;
	public static int shuffle=0;
	public static int state=0;
	static TextView musicTextView;
	private Button lastButton;
	public static Button playButton;
	private Button nextButton;
	private Button mRepeat;
	private Button mShuffle;
	static Context context;
	public static SeekBar seekBar;
	static ImageView musicImage;

	
	static String name;
	static String path;
	static String artist;

	static Handler handler;
	public static Runnable runnable;
	static boolean isChangeSeekbarP;

	ContentResolver resolver;
	static String SDRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

	private static SongLyric lrc;
	private static LyricView lrcView;
	private static long time = 0;
	private static LinearLayout lrcLayout;
	private static String lrcPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.playmusic_layout);
		

		
		context = this;
		resolver = getContentResolver();

		musicTextView = (TextView) findViewById(R.id.text_musicName);
		lastButton = (Button) findViewById(R.id.button_last);
		playButton = (Button) findViewById(R.id.button_play);
		nextButton = (Button) findViewById(R.id.button_next);
        mRepeat = (Button)findViewById(R.id.audio_player_repeat);
        mShuffle = (Button)findViewById(R.id.audio_player_shuffle);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		musicImage = (ImageView) findViewById(R.id.image_pic);
		lrcLayout = (LinearLayout) findViewById(R.id.lrc_layout);
		lrcView = new LyricView(lrcLayout.getContext());
		lrcLayout.addView(lrcView);
        SensorManagerHelper sensorHelper = new SensorManagerHelper(this);
		
		lastButton.setOnClickListener(new ButtonListener());
		playButton.setOnClickListener(new ButtonListener());
		nextButton.setOnClickListener(new ButtonListener());
		seekBar.setOnSeekBarChangeListener(new onSeekBarListener());
		handler = new Handler();
		
		runnable = new Runnable() {

			
			public void run() {
				System.out.println(PlayService.player.getCurrentPosition());
				if (MyApplication.playStatus == 1) {
					seekBar.setMax(PlayService.player.getDuration());
					seekBar.setProgress(PlayService.player.getCurrentPosition());
					musicImage.setImageResource(R.drawable.pic);

					if (new File(lrcPath).exists()) {
						lrc = new SongLyric(lrcPath);
						lrc.setMaxTime(PlayService.player.getDuration());
						lrcView.setLyric(lrc);
						time = PlayService.player.getCurrentPosition();
						lrcView.setTime(time);
						lrcView.postInvalidate();
						setTitle(SongLyric.longToString((int) time) + "/" + SongLyric.longToString((int) lrc.getMaxTime()));
					}
				}
				if (!name.equals(PlayService.name)) {
					name = PlayService.name;
					path = PlayService.path;
					artist = PlayService.artist;
					musicTextView.setText(name);
					musicImage.setImageResource(R.drawable.pic);
					new imageTask().execute();
					lrcPath = path.substring(0, path.length()-4) + ".lrc";
				}
				handler.postDelayed(this, 50);
			}
		};
		
        mRepeat.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	if(repeat==0){
            	mRepeat.setBackgroundDrawable(getResources().getDrawable(R.drawable.apollo_holo_light_repeat_one));
            	mShuffle.setBackgroundDrawable(getResources().getDrawable(R.drawable.apollo_holo_light_shuffle_normal));
            	shuffle=0;
            	repeat=1;
            	}else{
                	mRepeat.setBackgroundDrawable(getResources().getDrawable(R.drawable.apollo_holo_light_repeat_normal));
                	repeat=0;
            	}
            }
        });
        
        mShuffle.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	if(shuffle==0){
            	mShuffle.setBackgroundDrawable(getResources().getDrawable(R.drawable.apollo_holo_light_shuffle_on));
            	mRepeat.setBackgroundDrawable(getResources().getDrawable(R.drawable.apollo_holo_light_repeat_normal));
            	repeat=0;
            	shuffle=1;
            	}else{
                	mShuffle.setBackgroundDrawable(getResources().getDrawable(R.drawable.apollo_holo_light_shuffle_normal));
                	shuffle=0;
            	}
            }
        });
        
    	sensorHelper.setOnShakeListener(new OnShakeListener() {
    		// TODO Auto-generated method stub

			public void onShake() {
				// TODO Auto-generated method stub
				if(MyApplication.getPlayMusicList().size() < 1){
				Toast.makeText(context,"没有歌曲", Toast.LENGTH_SHORT).show();
				}else{
					handler.removeCallbacks(runnable);
					PlayService.stop();
					musicImage.setImageResource(R.drawable.pic);
					next();
				}
			}
    	});
//        private void cycleRepeat() {
////            if (MusicUtils.mService == null) {
////                return;
////            }
//            try {
//                int mode = MusicUtils.mService.getRepeatMode();
//                if (mode == ApolloService.REPEAT_NONE) {
//                    MusicUtils.mService.setRepeatMode(ApolloService.REPEAT_ALL);
//                    ApolloUtils.showToast(R.string.repeat_all, mToast, getActivity());
//                } else if (mode == ApolloService.REPEAT_ALL) {
//                    MusicUtils.mService.setRepeatMode(ApolloService.REPEAT_CURRENT);
//                    if (MusicUtils.mService.getShuffleMode() != ApolloService.SHUFFLE_NONE) {
//                        MusicUtils.mService.setShuffleMode(ApolloService.SHUFFLE_NONE);
//                        setShuffleButtonImage();
//                    }
//                    ApolloUtils.showToast(R.string.repeat_one, mToast, getActivity());
//                } else {
//                    MusicUtils.mService.setRepeatMode(ApolloService.REPEAT_NONE);
//                    ApolloUtils.showToast(R.string.repeat_off, mToast, getActivity());
//                }
//                setRepeatButtonImage();
//            } catch (RemoteException ex) {
//                ex.printStackTrace();
//            }
//
//        }
        

		if (PlayService.path==null) {
			name = null;
			path = null;
		} else {
			if (MyApplication.playStatus != 0) {
				name = PlayService.name;
				path = PlayService.path;
				artist = PlayService.artist;
				System.out.println(path);
				lrcPath = path.substring(0, path.length()-4) + ".lrc";
				System.out.println(lrcPath);
				musicTextView.setText(name);
				new imageTask().execute();
				
				
				if (MyApplication.playStatus == 1) {
					handler.post(runnable);
					playButton.setBackgroundResource(R.drawable.pause_select);
				}
				if (MyApplication.playStatus == 2) {
					playButton.setBackgroundResource(R.drawable.play_select);
				}
				for (int i = 0; i < MyApplication.getPlayMusicList().size(); i++) {
					if (name.equals(MyApplication.getPlayMusicList().get(i).get("name"))) {
						MyApplication.musicPosition = i;
						break;
					}
				}
			} else {
				HashMap<String, Object> map = MyApplication.getPlayMusicList().get(0);
				name = (String) map.get("name");
				path = (String) map.get("path");
				artist = (String) map.get("artist");
				lrcPath = path.substring(0, path.length()-4) + ".lrc";
			}
		}
    	if(state==0){
    	state=1;
    	}else if(state==1){
        	if(repeat==0){
        	mRepeat.setBackgroundDrawable(getResources().getDrawable(R.drawable.apollo_holo_light_repeat_normal));
        	}else{
            	mRepeat.setBackgroundDrawable(getResources().getDrawable(R.drawable.apollo_holo_light_repeat_one));
        	}
        	if(shuffle==0){
        	mShuffle.setBackgroundDrawable(getResources().getDrawable(R.drawable.apollo_holo_light_shuffle_normal));
        	}else{
            	mShuffle.setBackgroundDrawable(getResources().getDrawable(R.drawable.apollo_holo_light_shuffle_on));
        	}
    		
    	}
		
	}

	public static class imageTask extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {

			File file = new File(SDRoot + "Mp3Player" + File.separator + artist + ".jpg");
			System.out.println(file);
			if (!file.exists()) {
				System.out.println("-------->下载图片！！�?<----------");
				InputStream inputStream = PlayUtils.getInputstreamByKeyWord(artist);
				PlayUtils.write2SDFromInput("Mp3Player", artist + ".jpg", inputStream);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			Bitmap image = BitmapFactory.decodeFile(SDRoot + "Mp3Player" + File.separator + artist + ".jpg", null);
			musicImage.setImageBitmap(image);
			super.onPostExecute(result);
		}

	}

	@Override
	protected void onResume() {
		if (MyApplication.playStatus == 1) {
			handler.post(runnable);
		}
		super.onResume();
	}

	@Override
	protected void onStop() {
		handler.removeCallbacks(runnable);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		handler.removeCallbacks(runnable);
		super.onDestroy();
	}

	public static void getMusicInfo(int position) {
		HashMap<String, Object> map = MyApplication.getPlayMusicList().get(position);
		name = (String) map.get("name");
		path = (String) map.get("path");
		artist = (String) map.get("artist");
		lrcPath = path.substring(0, path.length()-4) + ".lrc";
		System.out.println("------------lrcPath>" + lrcPath);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "停止");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			PlayService.stop();
			handler.removeCallbacks(runnable);
			seekBar.setProgress(0);
			musicImage.setImageResource(R.drawable.pic);
			playButton.setBackgroundResource(R.drawable.play_select);
			MainActivity.footer.setText("正在播放的歌�?");
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	public static void resetMusic(){
		PlayService.stop();
		handler.removeCallbacks(runnable);
		seekBar.setProgress(0);
		musicImage.setImageResource(R.drawable.pic);
		playButton.setBackgroundResource(R.drawable.play_select);
		MainActivity.footer.setText("正在播放的歌曲");
	}
	private class ButtonListener implements OnClickListener {

		
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_last:

				try {
					Thread.sleep(80);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (MyApplication.getPlayMusicList().size() > 1) {
					handler.removeCallbacks(runnable);
					PlayService.stop();
					musicImage.setImageResource(R.drawable.pic);
					MyApplication.musicPosition = MyApplication.musicPosition - 1 < 0 ? MyApplication.getPlayMusicList().size() - 1 : MyApplication.musicPosition - 1;
					getMusicInfo(MyApplication.musicPosition);
					playMusic();
				} else {
					Toast.makeText(context, "没有上一首", 0).show();
				}
				break;
			case R.id.button_play:
				if (MyApplication.playStatus == 0) {
					if (name == null) {
						Toast.makeText(context, "没有歌曲", 0).show();
					} else {
						playMusic();
					}
				} else if (MyApplication.playStatus == 1) {
					handler.removeCallbacks(runnable);
					PlayService.pause();
					playButton.setBackgroundResource(R.drawable.play_select);
				} else if (MyApplication.playStatus == 2) {
					PlayService.goon();
					handler.post(runnable);
					playButton.setBackgroundResource(R.drawable.pause_select);
				}
				break;
			case R.id.button_next:

				try {
					Thread.sleep(80);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				if (MyApplication.getPlayMusicList().size() > 1) {
					handler.removeCallbacks(runnable);
					PlayService.stop();
					musicImage.setImageResource(R.drawable.pic);
					next();
				} else {
					Toast.makeText(context, "没有下一首", 0).show();
				}
				break;
			default:

				break;
			}
		}

	}

	public static void playMusic() {
		Intent intent = new Intent(context, PlayService.class);
		intent.putExtra("name", name);
		intent.putExtra("path", path);
		intent.putExtra("artist", artist);
		context.startService(intent);
		
		playButton.setBackgroundResource(R.drawable.pause_select);
		musicTextView.setText(name);
		TextView footer = (TextView) MainActivity.footer;
		footer.setText(name);
		
		handler.post(runnable);
		new imageTask().execute();
	}

	public static void next() {
		MyApplication.musicPosition = (MyApplication.musicPosition + 1) % MyApplication.getPlayMusicList().size();
		getMusicInfo(MyApplication.musicPosition);
		playMusic();
	}

	// PlayService中自动播放下一首歌曲用到的
	public static HashMap<String, Object> getNextInfo() {
		MyApplication.musicPosition = (MyApplication.musicPosition + 1) % MyApplication.getPlayMusicList().size();
		HashMap<String, Object> map = MyApplication.getPlayMusicList().get(MyApplication.musicPosition);
		return map;
	}

	private class onSeekBarListener implements OnSeekBarChangeListener {

	
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

		}

		
		public void onStartTrackingTouch(SeekBar seekBar) {
			if (MyApplication.playStatus != 0) {
				handler.removeCallbacks(runnable);
				isChangeSeekbarP = true;
			}
		}

	
		public void onStopTrackingTouch(SeekBar seekBar) {
			if (isChangeSeekbarP) {
				PlayService.player.seekTo(seekBar.getProgress());
				handler.post(runnable);
				isChangeSeekbarP = false;
			}
		}

	}

}
