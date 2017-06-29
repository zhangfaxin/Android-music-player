package userinformation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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

import java.util.HashMap;

import com.douniao.music.PlayMusicListActivity;
import com.douniao.music.PlayService;
import com.douniao.music.Utils.MyApplication;
import com.douniao.music.Utils.PlayUtils;
import com.huazi.Mp3Player.R;
public class localsearch extends Activity {
	private ExpandableListView expendView;
	public static Context context;
//	private int []group_click=new int[5];
	private long mExitTime=0;
	public  int local=0;
	int count=0;
	int jishu=0;
	String []group_title;
	String []group_artist;
	String []group_name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.localsearch);
		context=this;
		Intent intent=getIntent();
		String input=intent.getStringExtra("input");
		for (int i = 0; i < MyApplication.getAllMusicList().size(); i++) {
			if (input.equals(MyApplication.getAllMusicList().get(i).get("name"))) {
				local=1;
//				group_title[2]=input;
//				group_title[0]=input;
				count++;
			}
		}
		if(local==1){
			group_title=new String[count];
			group_name=new String[count];
			group_artist=new String[count];

			for (int i = 0; i < MyApplication.getAllMusicList().size(); i++) {
				if (input.equals(MyApplication.getAllMusicList().get(i).get("name"))&&jishu<count) {
					group_title[jishu]=(String) MyApplication.getAllMusicList().get(i).get("name")+"  "+(String) MyApplication.getAllMusicList().get(i).get("artist");
					group_name[jishu]=(String) MyApplication.getAllMusicList().get(i).get("name");
					group_artist[jishu]=(String) MyApplication.getAllMusicList().get(i).get("artist");
					jishu++;
				}
			}
			new  AlertDialog.Builder(this)    
			.setTitle("标题" )  
			.setMessage("查询成功" )  
			.setPositiveButton("确定" ,  null )  
			.show();
		}else{
			new  AlertDialog.Builder(this)    
			.setTitle("标题" )  
			.setMessage("没有找到" )  
			.setPositiveButton("确定" ,  null )  
			.show();
			group_title=new String[]{"查询不到"};
		}
		
		final MyExpendAdapter adapter=new MyExpendAdapter();	
		expendView=(ExpandableListView) findViewById(R.id.list_search);
		expendView.setGroupIndicator(null);  //设置默认图标不显示
		expendView.setAdapter(adapter);
		//一级点击事件
		expendView.setOnGroupClickListener(new OnGroupClickListener() {

			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				int Position=0;
				// TODO Auto-generated method stub
//				group_click[groupPosition]+=1;
				
				for (int i = 0; i < MyApplication.getAllMusicList().size(); i++) {
					if(group_name[groupPosition].equals(MyApplication.getAllMusicList().get(i).get("name"))&&group_artist[groupPosition].equals(MyApplication.getAllMusicList().get(i).get("artist"))){
						Position=i;
						break;
					}
				}
				
				HashMap<String, Object> map = MyApplication.getAllMusicList().get(Position);
				if (map.get("name").equals(PlayService.name) && MyApplication.playStatus == 1&&map.get("path").equals(PlayService.path)) {
					Toast.makeText(context, "正在播放...", 0).show();
				}else {
					boolean b = PlayUtils.addMusicToList(context, map, MyApplication.getPlayMusicList());
					if (b) {
						PlayMusicListActivity.myListAdapter.notifyDataSetChanged();
						PlayUtils.turnToPlay(map, context);
					}
				}
				
//				if(groupPosition==0){
////					AlertDialog.Builder builder=new Builder(setting.this);
////					builder.setMessage("抱歉！该用户已被注册！");
////					builder.setTitle("提示");
////					builder.setPositiveButton("确认", new OnClickListener() {
////						
////						public void onClick(DialogInterface dialog, int which) {
////							dialog.dismiss();
////						}
////					}).show();
//				}
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
			convertView=getLayoutInflater().inflate(R.layout.localsearch_list, null);
			
//			ImageView icon=(ImageView) convertView.findViewById(R.id.icon_setting);
			TextView tv=(TextView) convertView.findViewById(R.id.iv_title_search);
			
			tv.setText(group_title[groupPosition]);
			
//			if(groupPosition==0){
//				icon.setImageResource(R.drawable.constants);
//			}else if(groupPosition==1){
//				icon.setImageResource(R.drawable.mailto);
//			}else if(groupPosition==2){
//				icon.setImageResource(R.drawable.mailbox);
//			}else if(groupPosition==3){
//				icon.setImageResource(R.drawable.setting);
//			}
						
			
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
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if(keyCode==KeyEvent.KEYCODE_BACK){
//			if((System.currentTimeMillis()-mExitTime)<2000){
//				android.os.Process.killProcess(android.os.Process.myPid());
//			}else{
//				Toast.makeText(setting.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//				mExitTime=System.currentTimeMillis();
//			}
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
}

