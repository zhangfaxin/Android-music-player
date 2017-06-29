package userinformation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;

import userinformation.cleancache;

import com.douniao.music.MainActivity;
import com.douniao.music.StartActivity;
import com.huazi.Mp3Player.R;
import com.loginregister.LoginAct;
public class setting extends Activity {
	private ExpandableListView expendView;
	private int []group_click=new int[5];
	private long mExitTime=0;
	private LinearLayout update;
	private LinearLayout exit;
	private LinearLayout shortcut;
	private LinearLayout clean;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);
		
		context = this;
		update = (LinearLayout) this.findViewById(R.id.more_code);
		exit = (LinearLayout) this.findViewById(R.id.exit_code);
		shortcut= (LinearLayout) this.findViewById(R.id.shortcut);
		clean= (LinearLayout) this.findViewById(R.id.cleancache);
		
		update.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// name.setText("");
				// pwd.setText("");
				Intent intent =new Intent();
				intent.setClass(setting.this,updatepassword.class);
				startActivity(intent);}
		});
		exit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(setting.this,LoginAct.class);
//				startActivity(intent);
//				finish();
				Intent logoutIntent = new Intent(setting.this,LoginAct.class);
				logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(logoutIntent);
			}
		});
		shortcut.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent shortcutintent = new Intent( "com.android.launcher.action.INSTALL_SHORTCUT");  
		        shortcutintent.putExtra("duplicate", false);   // 不允许重复创建 
		        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
		        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.launcher);//快捷图片
		        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		        //点击快捷进入程序入口
		        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext() , StartActivity.class));
		        sendBroadcast(shortcutintent);
			}
		});
		clean.setOnClickListener(new View.OnClickListener() {
			


			public void onClick(View v) {
				// TODO Auto-generated method stub
				cleancache.clearAllCache(getApplicationContext());
				Toast.makeText(context, "清除缓存成功！", Toast.LENGTH_SHORT).show();
			}
		});
		
	}
}
	