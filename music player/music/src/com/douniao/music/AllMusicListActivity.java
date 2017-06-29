package com.douniao.music;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import userinformation.songdetails;

import com.douniao.music.Utils.DBHelper;
import com.douniao.music.Utils.DataUtils;
import com.douniao.music.Utils.MusicDemo;
import com.douniao.music.Utils.MyApplication;
import com.douniao.music.Utils.PlayUtils;
import com.huazi.Mp3Player.R;
import com.loginregister.LoginAct;

public class AllMusicListActivity extends ListActivity {

	private ListView listView;
	private static MyListAdapter listAdapter;
	public static Context context;
	private ContentResolver resolver;
	private Intent intent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allmusic_list_layout);
		getWindow().setBackgroundDrawable(null);
		listView = (ListView) findViewById(android.R.id.list);
		context = this;
		resolver = getContentResolver();
			
		listAdapter = new MyListAdapter(this, MyApplication.getAllMusicList());
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				HashMap<String, Object> map = MyApplication.getAllMusicList().get(position);
				
				if (map.get("name").equals(PlayService.name) && MyApplication.playStatus == 1&&map.get("artist").equals(PlayService.artist)) {
					Toast.makeText(context, "正在播放...", 0).show();
				}else {
					boolean b = PlayUtils.addMusicToList(context, map, MyApplication.getPlayMusicList());
					if (b) {
						PlayMusicListActivity.myListAdapter.notifyDataSetChanged();
						PlayUtils.turnToPlay(map, context);
					}
				}
			}
			
		});
		ItemOnLongClick();
	}
	private void ItemOnLongClick(){
		
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){
			public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			    // set context menu title
			    menu.setHeaderTitle("文件操作");
			    // add context menu item
//			    menu.add(0,1,0,"设置铃声");
			    menu.add(0,1,0, "删除");
			    menu.add(0,2,0,"歌曲详细信息");
			}
		});
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    // 得到当前被选中的item信息
	    AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo(); 
		int position = menuInfo.position;
		HashMap<String, Object> map = MyApplication.getAllMusicList().get(position);
		switch(item.getItemId()) {
//		case 1:
//			  String name = (String) map.get("path");
//			  setMyRingtone(name);
//
//			break;
		case 1:
			resolver = getContentResolver();
			DataUtils.update_AllMusicData(resolver,context);
			String path = (String) map.get("path");
			Toast.makeText(context, path, 0).show();
			File file = new File(path);
			if(file.exists())
				file.delete();
			AllMusicListActivity.listAdapter.removeItem(position);
			for(int i=0;i<MyApplication.getPlayMusicList().size();i++){
				if(map.get("name").equals(MyApplication.getPlayMusicList().get(i).get("name"))){
					PlayMusicListActivity.myListAdapter.removeItem(i);
					PlayMusicListActivity.myListAdapter.notifyDataSetChanged();
					PlayService.player.reset();
					break;
				}
			}
			break;
		case 2:
			String showpath = (String) map.get("path");
			String showname = (String) map.get("name");
			String showartist = (String) map.get("artist");
			Intent intent = new Intent(AllMusicListActivity.this, songdetails.class);// 跳转页面
			intent.putExtra("path", showpath);
			intent.putExtra("name", showname);
			intent.putExtra("artist", showartist);
			startActivity(intent);
			
			break;
	    default:
	        return super.onContextItemSelected(item);
	    }
	    return true;
	}
	public void setMyRingtone(String path){
		File sdfile = new File(path);
//		ContentProvider s = null ;
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA,sdfile.getAbsolutePath());
		values.put(MediaStore.MediaColumns.TITLE,sdfile.getName());
		values.put(MediaStore.MediaColumns.MIME_TYPE,"audio/*");
		values.put(MediaStore.Audio.Media.IS_RINGTONE,false);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION,false);
		values.put(MediaStore.Audio.Media.IS_MUSIC,false);
	    values.put(MediaStore.Audio.Media.IS_ALARM,true);
		Uri uri=MediaStore.Audio.Media.getContentUriForPath(sdfile.getAbsolutePath());
		Toast.makeText(context,"1111:"+uri,Toast.LENGTH_SHORT).show();
//		this.getContentResolver().delete(uri,null,null);
		final Uri newUri =this.getContentResolver().insert(uri,values);
		//s.update(uri,values,null,null);
		Toast.makeText(context,"2222:"+values,Toast.LENGTH_SHORT).show();
		//Toast.makeText(context,"3333:"+newUri,Toast.LENGTH_SHORT).show();
		RingtoneManager.setActualDefaultRingtoneUri(this,RingtoneManager.TYPE_ALARM,newUri);
		Toast.makeText(context,"4444:"+path,Toast.LENGTH_SHORT).show();
		System.out.println("setMyRingtone()-----铃声");
		resolver=getContentResolver();
		DataUtils.update_AllMusicData(resolver,StartActivity.context);
	}

	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			backDialog();
		}
		return false;
	};

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

	public class MyListAdapter extends BaseAdapter {
		private class buttonViewHolder {
			TextView musicName;
			ImageButton add;
			ImageButton collect;
			ImageButton share;
		}

		private ArrayList<HashMap<String, Object>> musicList;
		private LayoutInflater mInflater;
		private Context mContext;
		private buttonViewHolder holder;

		public MyListAdapter(Context context, List<HashMap<String, Object>> appList) {
			musicList = (ArrayList<HashMap<String, Object>>) appList;
			mContext = context;
			mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		public int getCount() {
			return musicList.size();
		}

		public Object getItem(int position) {
			return musicList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}
		public void refreshItem(){
			this.notifyDataSetChanged();
		}
		public void removeItem(int position) {
			musicList.remove(position);
			this.notifyDataSetChanged();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView != null) {
				holder = (buttonViewHolder) convertView.getTag();
			} else {
				convertView = mInflater.inflate(R.layout.allmusic_list_item_layout, null);
				holder = new buttonViewHolder();
				holder.musicName = (TextView) convertView.findViewById(R.id.text_musicName);
				holder.add = (ImageButton) convertView.findViewById(R.id.button_add);
				holder.collect = (ImageButton) convertView.findViewById(R.id.button_collect);
				holder.share  = (ImageButton) convertView.findViewById(R.id.share);
				convertView.setTag(holder);
			}

			HashMap<String, Object> itemInfo = musicList.get(position);

			if (itemInfo != null) {
				String aname = (String) itemInfo.get("name");
				String aartist = (String) itemInfo.get("artist");
				String combine=aname+"   "+aartist;
				holder.musicName.setText(aname);
				holder.add.setOnClickListener(new addOnClickListener(position));
				holder.collect.setOnClickListener(new collectOnClickListener(position));
				holder.share.setOnClickListener(new shareOnClickListener(combine));
			}
			return convertView;
		}

		class collectOnClickListener implements OnClickListener {

			int position;

			public collectOnClickListener(int position) {
				this.position = position;
			}

			
			public void onClick(View v) {
				Builder builder = new AlertDialog.Builder(context);
				builder.setIcon(R.drawable.collect);
				builder.setTitle("　请选择收藏列表");
				builder.setItems(new String[] { "开心", "安静", "忧伤" }, new DialogInterface.OnClickListener() {

					
					public void onClick(DialogInterface dialog, int which) {
						HashMap<String, Object> map = MyApplication.getAllMusicList().get(position);
						boolean b;
						switch (which) {
						case 0:
							b = PlayUtils.addMusicToList(context,map, MyApplication.getHappyMusicList());
							break;
						case 1:
							b = PlayUtils.addMusicToList(context, map, MyApplication.getQuietMusicList());
							break;
						case 2:
							b = PlayUtils.addMusicToList(context, map, MyApplication.getSadMusicList());
							break;
						default:
							b = false;
							break;
						}
						if (b) MyMusicListActivity.listAdapter.notifyDataSetChanged();
					}
				});
				builder.create().show();
			}
		}
		class shareOnClickListener implements OnClickListener {

			String artist;

			public shareOnClickListener(String position) {
				this.artist = position;
			}

			
			public void onClick(View v) {
				
				intent=new Intent(Intent.ACTION_SEND);
				 intent.setType("text/plain"); // 分享发送的数据类型
		         intent.putExtra(Intent.EXTRA_SUBJECT, "音乐盒"); // 分享的主题
		         intent.putExtra(Intent.EXTRA_TEXT, artist); // 分享的内容
		         AllMusicListActivity.this.startActivity(Intent.createChooser(intent, "分享"));
			}
		}
		class addOnClickListener implements OnClickListener {

			private int position;

			public addOnClickListener(int position) {
				this.position = position;
			}

			
			public void onClick(View v) {
				HashMap<String, Object> map = MyApplication.getAllMusicList().get(position);
				boolean b = PlayUtils.addMusicToList(context, map, MyApplication.getPlayMusicList());
				if (b) PlayMusicListActivity.myListAdapter.notifyDataSetChanged();
			}
		}

	}

	public class AllMusic_List_asyncTask extends AsyncTask<String, Void, Void> {

		private ProgressDialog progressDialog;
		private MyListAdapter listAdapter;

		public AllMusic_List_asyncTask(MyListAdapter listAdapter) {
			this.listAdapter = listAdapter;
			progressDialog = new ProgressDialog(context);
		}

		@Override
		protected void onPreExecute() {
			progressDialog.setMessage("扫描所有音乐...");
			progressDialog.setCancelable(false);
			progressDialog.setIndeterminate(false);
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			DataUtils.update_AllMusicData(resolver,context);
			DataUtils.get_AllMusicListFD(context);
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(Void result) {
			listAdapter.notifyDataSetChanged();
			progressDialog.dismiss();
		}
	}
}
