package com.douniao.music.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.widget.BaseAdapter;

import android.app.Application;

public class MyApplication extends Application {

	private static List<HashMap<String, Object>> playMusicList = new ArrayList<HashMap<String, Object>>();
	private static List<HashMap<String, Object>> allMusicList = new ArrayList<HashMap<String, Object>>();
	private static List<HashMap<String, Object>> happyMusicList = new ArrayList<HashMap<String, Object>>();
	private static List<HashMap<String, Object>> quietMusicList = new ArrayList<HashMap<String, Object>>();
	private static List<HashMap<String, Object>> sadMusicList = new ArrayList<HashMap<String, Object>>();
	public static int playStatus ;
	public static int musicPosition;

	
	@Override
	public void onCreate() {

		super.onCreate();
	}

	public static List<HashMap<String, Object>> getPlayMusicList() {
		return playMusicList;
	}

	public static List<HashMap<String, Object>> getAllMusicList() {
		return allMusicList;
	}

	public static List<HashMap<String, Object>> getQuietMusicList() {
		return quietMusicList;
	}

	public static List<HashMap<String, Object>> getSadMusicList() {
		return sadMusicList;
	}

	public static List<HashMap<String, Object>> getHappyMusicList() {
		return happyMusicList;
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}
}
