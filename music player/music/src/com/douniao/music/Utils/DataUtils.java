package com.douniao.music.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.douniao.music.MainActivity;
import com.douniao.music.StartActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.widget.Toast;

public class DataUtils {

	private static List<MusicDemo> musicList = new ArrayList<MusicDemo>();

	public static void update_AllMusicData(ContentResolver resolver,Context context) {
		musicList.clear();
		Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		while (cursor.moveToNext()) {
			// 歌曲的名称 
			String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
			// 歌曲的歌手名
			String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
			// 歌曲文件的路径 
			String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
			musicList.add(new MusicDemo(name, path, artist));
		}
		DBHelper dbHelper = new DBHelper(context, "myData");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("delete from all_music");
		if (!musicList.isEmpty()) {
			for (Iterator<MusicDemo> iterator = musicList.iterator(); iterator.hasNext();) {
				MusicDemo demo = (MusicDemo) iterator.next();
				ContentValues values = new ContentValues();
				values.put("name", demo.getName());
				values.put("path", demo.getPath());
				values.put("artist", demo.getArtist());
				db.insert("all_music", null, values);
			}
		}
		db.close();
	}

	public static void get_AllMusicListFD(Context context) {
//		if (!MyApplication.getAllMusicList().isEmpty()) {
//			MyApplication.getAllMusicList().clear();
//		}
		MyApplication.getAllMusicList().clear();
		DBHelper dbHelper = new DBHelper(context, "myData");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select name,path,artist from all_music", null);
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String path = cursor.getString(cursor.getColumnIndex("path"));
			String artist = cursor.getString(cursor.getColumnIndex("artist"));
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("path", path);
			map.put("artist", artist);
//			Toast.makeText(context, (CharSequence) map.get("path"), 0).show();
			MyApplication.getAllMusicList().add(map);
		}
		cursor.close();
		db.close();
	}

	public static void get_PlayMusicListFD() {
		if (!MyApplication.getPlayMusicList().isEmpty()) {
			MyApplication.getPlayMusicList().clear();
		}
		DBHelper dbHelper = new DBHelper(StartActivity.context, "myData");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("Select name,path,artist from play_music", null);
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String path = cursor.getString(cursor.getColumnIndex("path"));
			String artist = cursor.getString(cursor.getColumnIndex("artist"));
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("path", path);
			map.put("artist", artist);
			MyApplication.getPlayMusicList().add(map);
			System.out.println(name + path + artist);
		}
		db.close();
	}

	public static void get_HappyMusicListFD() {
		if (!MyApplication.getHappyMusicList().isEmpty()) {
			MyApplication.getHappyMusicList().clear();
		}
		DBHelper dbHelper = new DBHelper(StartActivity.context, "myData");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("Select name,path,artist from my_happy_music", null);
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String path = cursor.getString(cursor.getColumnIndex("path"));
			String artist = cursor.getString(cursor.getColumnIndex("artist"));
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("path", path);
			map.put("artist", artist);
			MyApplication.getHappyMusicList().add(map);
		}
		db.close();
	}

	public static void get_QuietMusicListFD() {
		if (!MyApplication.getQuietMusicList().isEmpty()) {
			MyApplication.getQuietMusicList().clear();
		}
		DBHelper dbHelper = new DBHelper(StartActivity.context, "myData");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("Select name,path,artist from my_quiet_music", null);
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String path = cursor.getString(cursor.getColumnIndex("path"));
			String artist = cursor.getString(cursor.getColumnIndex("artist"));
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("path", path);
			map.put("artist", artist);
			MyApplication.getQuietMusicList().add(map);
		}
		db.close();
	}

	public static void get_SadMusicListFD() {
		if (!MyApplication.getSadMusicList().isEmpty()) {
			MyApplication.getSadMusicList().clear();
		}
		DBHelper dbHelper = new DBHelper(StartActivity.context, "myData");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("Select name,path,artist from my_sad_music", null);
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String path = cursor.getString(cursor.getColumnIndex("path"));
			String artist = cursor.getString(cursor.getColumnIndex("artist"));
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("path", path);
			map.put("artist", artist);
			MyApplication.getSadMusicList().add(map);
		}
		db.close();
	}

	public static void get_AllListsFD() {
		get_AllMusicListFD(StartActivity.context);
		get_HappyMusicListFD();
		get_PlayMusicListFD();
		get_QuietMusicListFD();
		get_SadMusicListFD();
	}

	public static void upData_Lists(Context context,String table_name) {
		List<HashMap<String, Object>> musicList = new ArrayList<HashMap<String, Object>>();
		if (table_name.equals("play_music")) {
			musicList = MyApplication.getPlayMusicList();
		}
		else if (table_name.equals("my_happy_music")) {
			musicList = MyApplication.getHappyMusicList();
		}
		else if (table_name.equals("my_quiet_music")) {
			musicList = MyApplication.getQuietMusicList();
		}
		else if (table_name.equals("my_sad_music")) {
			musicList = MyApplication.getSadMusicList();
		} else if (table_name.equals("all_music")) {
			musicList = MyApplication.getAllMusicList();
		}
		else{
			System.out.println("table name error!");
		}
		SQLiteDatabase db = new DBHelper(context, "myData").getWritableDatabase();
		db.execSQL("delete from " + table_name);
		db.execSQL("update sqlite_sequence SET seq = 0 where name = '"+ table_name+"'");
		for (Iterator<HashMap<String, Object>> iterator = musicList.iterator(); iterator.hasNext();) {
			HashMap<String, Object> hashMap = (HashMap<String, Object>) iterator.next();
			ContentValues values = new ContentValues();
			values.put("name", (String) hashMap.get("name"));
			values.put("path", (String) hashMap.get("path"));
			values.put("artist", (String) hashMap.get("artist"));
			db.insert(table_name, null, values);
		}
		db.close();
	}

	public static void upData_allLists() {
		upData_Lists(MainActivity.context,"play_music");
		upData_Lists(MainActivity.context,"my_happy_music");
		upData_Lists(MainActivity.context,"my_quiet_music");
		upData_Lists(MainActivity.context,"my_sad_music");
		upData_Lists(MainActivity.context,"all_music");
	}
}
