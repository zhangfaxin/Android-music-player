package com.douniao.music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.douniao.music.Utils.DataUtils;
import com.douniao.music.Utils.MyApplication;
import com.douniao.music.Utils.PlayUtils;
import com.huazi.Mp3Player.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PlayMusicListActivity extends Activity {

	public static ListView playMusicList;
	public static MyListAdapter myListAdapter;
	public static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playmusic_list_layout);
		context = this;
		
		playMusicList = (ListView) findViewById(R.id.playMusicList);

		myListAdapter = new MyListAdapter(this, MyApplication.getPlayMusicList(), new int[] { R.id.text_playMusicName, R.id.button_delete });

		playMusicList.setAdapter(myListAdapter);

		playMusicList.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				HashMap<String, Object> map = MyApplication.getPlayMusicList().get(position);
				if (map.get("name").equals(PlayService.name) && MyApplication.playStatus == 1&&map.get("artist").equals(PlayService.artist) ) {
					Toast.makeText(context, "正在播放...", 0).show();
				}else {
					PlayUtils.turnToPlay(map, context);
				}
				
			}
		});
	}
	
	@Override
	protected void onRestart() {
		myListAdapter.notifyDataSetChanged();
		super.onRestart();
	}
	
	@Override
	protected void onResume() {
		myListAdapter.notifyDataSetChanged();
		myListAdapter.notifyDataSetInvalidated();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, R.string.text_deletePlayListMusics);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {
			new emptyPlayMusicListTask(myListAdapter).execute();
		}
		return super.onOptionsItemSelected(item);
	}

	public class emptyPlayMusicListTask extends AsyncTask<Void, Void, Void> {

		MyListAdapter myListAdapter;

		public emptyPlayMusicListTask(MyListAdapter myListAdapter) {
			this.myListAdapter = myListAdapter;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			MyApplication.getPlayMusicList().clear();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			myListAdapter.notifyDataSetChanged();
			PlayService.stop();
			MainActivity.footer.setText("正在播放的歌曲");
			super.onPostExecute(result);
		}

	}

	@Override
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
			ImageButton delete;
		}

		private ArrayList<HashMap<String, Object>> musicList;
		private LayoutInflater mInflater;
		private Context mContext;
		private int[] valueViewID;
		private buttonViewHolder holder;

		public MyListAdapter(Context context, List<HashMap<String, Object>> appList, int[] to) {
			musicList = (ArrayList<HashMap<String, Object>>) appList;
			mContext = context;
			mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			valueViewID = to;
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
 
		public void removeItem(int position) {
			if (musicList.get(position).get("name").equals(PlayService.name)) {
				PlayService.stop();
				TextView footer = (TextView) MainActivity.footer;
				footer.setText("正在播放的歌曲");
			}
			musicList.remove(position);
			this.notifyDataSetChanged();
		}

		
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView != null) {
				holder = (buttonViewHolder) convertView.getTag();
			} else {
				convertView = mInflater.inflate(R.layout.playmusic_list_item_layout, null);
				holder = new buttonViewHolder();
				holder.musicName = (TextView) convertView.findViewById(valueViewID[0]);
				holder.delete = (ImageButton) convertView.findViewById(valueViewID[1]);
				convertView.setTag(holder);
			}

			HashMap<String, Object> itemInfo = MyApplication.getPlayMusicList().get(position);

			if (itemInfo != null) {
				String aname = (String) itemInfo.get("name");
				holder.musicName.setText(aname);
				holder.delete.setOnClickListener(new deleOnClickListener(position));
			}
			return convertView;
		}

		class deleOnClickListener implements OnClickListener {

			private int position;

			public deleOnClickListener(int position) {
				this.position = position;
			}

		
			public void onClick(View v) {

				removeItem(position);
			}
		}

	}
}
