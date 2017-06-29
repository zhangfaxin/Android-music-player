package com.douniao.music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.douniao.music.AllMusicListActivity.MyListAdapter;
import com.douniao.music.Utils.DBHelper;
import com.douniao.music.Utils.DataUtils;
import com.douniao.music.Utils.MusicDemo;
import com.douniao.music.Utils.MyApplication;
import com.huazi.Mp3Player.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends TabActivity {

	public static Context context;
	private LocalActivityManager manager;
	private TabHost tabHost;
	private ViewPager viewPager;
	public static TextView footer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		context = MainActivity.this;
		footer = (TextView) findViewById(R.id.text_footet_playingMusic);

		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);

		this.loadTabHost();
		this.loadViewPager();

		tabHost.setCurrentTab(0);

		footer.setOnClickListener(new OnClickListener() {

			
			public void onClick(View v) {
				MainActivity.context.startActivity(new Intent(MainActivity.this, PlayMusicAcitivity.class));
			}
		});

	}

	@Override
	protected void onResume() {
		if (MyApplication.playStatus != 0) {
			footer.setText(PlayService.name);
		}
		super.onResume();
	}

	public View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	public void loadTabHost() {
		tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("myMusic").setIndicator("我的音乐", getResources().getDrawable(R.drawable.xin_big)).setContent(new Intent(context, MyMusicListActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("allMusic").setIndicator("所有歌曲", getResources().getDrawable(R.drawable.all_tab)).setContent(new Intent(context, AllMusicListActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("playingMusic").setIndicator("播放列表", getResources().getDrawable(R.drawable.play_tab)).setContent(new Intent(context, PlayMusicListActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("user").setIndicator("用户中心", getResources().getDrawable(R.drawable.user_tab)).setContent(new Intent(context, user.class)));

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			
			public void onTabChanged(String tabId) {
				if (tabId.equals("myMusic")) {
					viewPager.setCurrentItem(0);
				}
				if (tabId.equals("allMusic")) {
					ContentResolver resolver = getContentResolver();
					DataUtils.update_AllMusicData(resolver,context);
					viewPager.setCurrentItem(1);
				}
				if (tabId.equals("playingMusic")) {
					ContentResolver resolver = getContentResolver();
					DataUtils.update_AllMusicData(resolver,context);
					viewPager.setCurrentItem(2);
				}
				if (tabId.equals("user")) {
					viewPager.setCurrentItem(3);
				}
			}

		});
	}
	
	
	
	public void backDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("确认退出吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				((Activity) context).finish();
				new AsyncTask<Void, Void, Void>(){

					@Override
					protected Void doInBackground(Void... params) {
						if (MyApplication.playStatus != 0) {
							PlayService.stop();
//							PlayService.player.release();
						}
						DataUtils.upData_allLists();
						return null;
					}
					
				}.execute();
				
			}
		});
		
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});

		builder.create().show();
	}
	
	
	
	
	
	
	
	
	
	public void loadViewPager() {

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		final ArrayList<View> list = new ArrayList<View>();
		list.add(getView("myMusic", new Intent(context, MyMusicListActivity.class)));
		list.add(getView("allMusic", new Intent(context, AllMusicListActivity.class)));
		list.add(getView("playingMusic", new Intent(context, PlayMusicListActivity.class)));
		list.add(getView("user", new Intent(context, user.class)));
		viewPager.setAdapter(new PagerAdapter() {
			
			@Override
			public void destroyItem(View arg0, int arg1, Object arg2) {
				viewPager.removeView(list.get(arg1));
			}
			
			@Override
			public Object instantiateItem(View arg0, int arg1) {
				((ViewPager) arg0).addView(list.get(arg1));
				return list.get(arg1);
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return list.size();
			}

			@Override
			public void finishUpdate(View arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void restoreState(Parcelable arg0, ClassLoader arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Parcelable saveState() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void startUpdate(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			
			public void onPageSelected(int arg0) {
				tabHost.setCurrentTab(arg0);
			}

			
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}
}
