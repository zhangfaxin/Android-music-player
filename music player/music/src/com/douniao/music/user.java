package com.douniao.music;


import java.util.HashMap;

import com.douniao.music.Utils.DBHelper;
import com.douniao.music.Utils.MyApplication;
import com.huazi.Mp3Player.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;
import userinformation.baseinformation;
import userinformation.onlinemusic;
import userinformation.search;
import userinformation.setting;

public class user extends Activity {
	private ExpandableListView expendView;
	private int []group_click=new int[5];
	private long mExitTime=0;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show);
		 final MyExpendAdapter adapter=new MyExpendAdapter();
			context = this;
		expendView=(ExpandableListView) findViewById(R.id.list1);
		expendView.setGroupIndicator(null);  //设置默认图标不显示
		expendView.setAdapter(adapter);
		
		//一级点击事件
		expendView.setOnGroupClickListener(new OnGroupClickListener() {

			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// TODO Auto-generated method stub
				group_click[groupPosition]+=1;
				if(groupPosition==2){
					
					Intent intent =new Intent();
					intent.setClass(user.this,search.class);
					startActivity(intent);
				}else if(groupPosition==1){
					
					Intent intent =new Intent();
					intent.setClass(user.this,onlinemusic.class);
					startActivity(intent);
				}else if(groupPosition==3){
					
					Intent intent =new Intent();
					intent.setClass(user.this,setting.class);
					startActivity(intent);
				}else if(groupPosition == 0){
					Intent intent =new Intent();
					intent.setClass(user.this,baseinformation.class);
					startActivity(intent);					
				}
				
				return false;
			}

		});
		
		
	}
	
	/**
	 * 添加联系人
	 */
	
	/**
	 * 适配器
	 * @author Administrator
	 *
	 */
	private class MyExpendAdapter extends BaseExpandableListAdapter{
		
		/**
		 * pic state
		 */
		//int []group_state=new int[]{R.drawable.group_right,R.drawable.group_down};
		
		/**
		 * group title
		 */
		String []group_title=new String[]{"我的资料","在线搜索","本地搜索","设置"};
		
        
		/**
		 * 获取一级标签内容
		 */
		public Object getGroup(int groupPosition) {
			return group_title[groupPosition];
		}
        
		/**
		 * 一级标签总数
		 */
		public int getGroupCount() {
			return group_title.length;
		}
        
		/**
		 * 一级标签ID
		 */
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}
		/**
		 * 对一级标签进行设置
		 */
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			convertView=getLayoutInflater().inflate(R.layout.user_groups_layout, null);
			
			ImageView icon=(ImageView) convertView.findViewById(R.id.icon);
			TextView tv=(TextView) convertView.findViewById(R.id.iv_title);
			
			tv.setText(group_title[groupPosition]);
			
			if(groupPosition==0){
				icon.setImageResource(R.drawable.constants);
			}else if(groupPosition==1){
				icon.setImageResource(R.drawable.mailto);
			}else if(groupPosition==2){
				icon.setImageResource(R.drawable.search);
			}else if(groupPosition==3){
				icon.setImageResource(R.drawable.setting);
			}
						
			
			return convertView;
		}
		/**
		 * 指定位置相应的组视图
		 */
		public boolean hasStableIds() {
			return true;
		}

		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return false;
		}
        
		/**
		 *  当选择子节点的时候，调用该方法
		 */
		
	}
	
	/**
	 * 返回退出系统
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if((System.currentTimeMillis()-mExitTime)<2000){
				android.os.Process.killProcess(android.os.Process.myPid());
			}else{
				Toast.makeText(user.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime=System.currentTimeMillis();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

