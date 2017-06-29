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
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MyMusicListActivity extends ExpandableListActivity {

	private ExpandableListView myMusicListView;
	private ArrayList<HashMap<String, Object>> groupsList;
	private List<List<HashMap<String, Object>>> childsList;
	public static MyMusicListAdapter listAdapter;
	private static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymusic_list_layout);
		myMusicListView = getExpandableListView();
		context = this;

		groupsList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> group1 = new HashMap<String, Object>();
		group1.put("name", "开心");
		HashMap<String, Object> group2 = new HashMap<String, Object>();
		group2.put("name", "安静");
		HashMap<String, Object> group3 = new HashMap<String, Object>();
		group3.put("name", "忧伤");
		groupsList.add(group1);
		groupsList.add(group2);
		groupsList.add(group3);

		childsList = new ArrayList<List<HashMap<String, Object>>>();
		childsList.add(MyApplication.getHappyMusicList());
		childsList.add(MyApplication.getQuietMusicList());
		childsList.add(MyApplication.getSadMusicList());

		listAdapter = new MyMusicListAdapter(this, groupsList, childsList);
		myMusicListView.setAdapter(listAdapter);
	}
	
	public static Context getContext(){
		return context;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		HashMap<String, Object> map = childsList.get(groupPosition).get(childPosition);
		if (map.get("name").equals(PlayService.name) && MyApplication.playStatus == 1&&map.get("artist").equals(PlayService.artist)) {
			Toast.makeText(context, "正在播放...", 0).show();
		}else {
			boolean b = PlayUtils.addMusicToList(context, map, MyApplication.getPlayMusicList());
			if (b) {
				PlayMusicListActivity.myListAdapter.notifyDataSetChanged();
				PlayUtils.turnToPlay(map, context);
			}
		}
		
		return super.onChildClick(parent, v, groupPosition, childPosition, id);
	}

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			backDialog();
		}
		return false;
	};

	@Override
	protected void onDestroy() {
		DataUtils.upData_allLists();
		super.onDestroy();
	}

	public void backDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("确认退出吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {

			
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
		builder.setNegativeButton("取消", new OnClickListener() {

			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	class MyMusicListAdapter extends BaseExpandableListAdapter {

		private Context context;
		private List<HashMap<String, Object>> groups;
		private List<List<HashMap<String, Object>>> childs;
		private childViewsHolder childHolder;
		private GroupViewsHolder groupHolder;

		public MyMusicListAdapter(Context context, List<HashMap<String, Object>> groups, List<List<HashMap<String, Object>>> childs) {
			this.context = context;
			this.groups = groups;
			this.childs = childs;
		}

		private class childViewsHolder {
			TextView childText;
			ImageButton childButton;
		}

		
		public Object getChild(int arg0, int arg1) {
			return childs.get(arg0).get(arg1);
		}

		
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public void removeChildItem(int groupPosition, int childPosition) {
			childs.get(groupPosition).remove(childPosition);
			this.notifyDataSetChanged();
		}

		
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView != null) {
				childHolder = (childViewsHolder) convertView.getTag();
			} else {
				childHolder = new childViewsHolder();
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.mymusic_childs_layout, null);
				childHolder.childText = (TextView) convertView.findViewById(R.id.text_childItem);
				childHolder.childButton = (ImageButton) convertView.findViewById(R.id.button_deleteChildItem);
				convertView.setTag(childHolder);
			}

			HashMap<String, Object> childItem = childs.get(groupPosition).get(childPosition);
			if (childItem != null) {
				String name = (String) childItem.get("name");
				childHolder.childText.setText(name);
				childHolder.childButton.setOnClickListener(new childItemOnClick(groupPosition, childPosition));
			}
			return convertView;
		}

		public class childItemOnClick implements android.view.View.OnClickListener {

			private int groupPosition;
			private int childPosition;

			public childItemOnClick(int groupPosition, int childposition) {
				this.groupPosition = groupPosition;
				this.childPosition = childposition;
			}

		
			public void onClick(View v) {
				removeChildItem(groupPosition, childPosition);
			}

		}

		
		public int getChildrenCount(int groupPosition) {
			return childs.get(groupPosition).size();
		}

		
		public Object getGroup(int groupPosition) {
			return groups.get(groupPosition);

		}

		
		public int getGroupCount() {
			return groups.size();
		}

		
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		private class GroupViewsHolder {
			TextView groupText;
			ImageButton groupButton;
		}

	
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			if (convertView != null) {
				groupHolder = (GroupViewsHolder) convertView.getTag();
			} else {
				groupHolder = new GroupViewsHolder();
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.mymusic_groups_layout, null);
				groupHolder.groupText = (TextView) convertView.findViewById(R.id.text_groupItem);
				groupHolder.groupButton = (ImageButton) convertView.findViewById(R.id.button_playAll);
				convertView.setTag(groupHolder);
			}

			HashMap<String, Object> map = groups.get(groupPosition);
			if (map != null) {
				String name = (String) map.get("name");
				groupHolder.groupText.setText(name);
				groupHolder.groupButton.setOnClickListener(new GroupOnClick(groupPosition));
			}

			return convertView;
		}

		class GroupOnClick implements View.OnClickListener {

			private int position;

			public GroupOnClick(int position) {
				this.position = position;
			}

			
			public void onClick(View v) {
				switch (position) {
				case 0:
					PlayUtils.turnToPlay_List(context, MyApplication.getHappyMusicList());
					break;
				case 1:
					PlayUtils.turnToPlay_List(context, MyApplication.getQuietMusicList());
					break;
				case 2:
					PlayUtils.turnToPlay_List(context, MyApplication.getSadMusicList());
					break;
				default:
					break;
				}
			}
		}

		
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}

	}

}
