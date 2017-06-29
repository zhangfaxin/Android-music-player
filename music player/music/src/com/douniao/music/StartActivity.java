package com.douniao.music;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Window;

import com.douniao.music.Utils.DBHelper;
import com.douniao.music.Utils.DataUtils;
import com.douniao.music.Utils.MyApplication;
import com.huazi.Mp3Player.R;
import com.loginregister.LoginAct;

public class StartActivity extends Activity {
	
	public static Context context;
	private ContentResolver resolver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.start_layout);
		context = this;
		resolver = getContentResolver();
		createshortCut();
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				SQLiteDatabase db = new DBHelper(context, "myData").getWritableDatabase();
				db.execSQL("create table if not exists all_music (id integer primary key autoincrement not null, name varchar(50) , path varchar(100) , artist varchar(50))");
				db.execSQL("create table if not exists play_music (id integer primary key autoincrement not null, name varchar(50) , path varchar(100) , artist varchar(50))");
				db.execSQL("create table if not exists my_happy_music (id integer primary key autoincrement not null, name varchar(50) , path varchar(100) , artist varchar(50))");
				db.execSQL("create table if not exists my_quiet_music (id integer primary key autoincrement not null, name varchar(50) , path varchar(100) , artist varchar(50))");		
				db.execSQL("create table if not exists my_sad_music (id integer primary key autoincrement not null, name varchar(50) , path varchar(100) , artist varchar(50))");		
				db.close();

				DataUtils.update_AllMusicData(resolver,context);
//				DataUtils.get_AllListsFD();
				DataUtils.get_AllMusicListFD(context);
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				startActivity(new Intent(context, LoginAct.class));
				finish();
				super.onPostExecute(result);
			}
			
		}.execute();
	}
	private void createshortCut() {
		 final SharedPreferences setting = getSharedPreferences("silent.preferences", 0);  
		 //�ж��Ƿ��ǵ�һ������,Ĭ��Ϊtrue
		 boolean isFirstStart=setting.getBoolean("FIRST_START", true);
		 if(isFirstStart){
			 

//				AlertDialog.Builder builder=new Builder(this);
//				builder.setTitle("���������ݷ�ʽ");
//				builder.setMessage("�Ƿ񴴽������ݷ�ʽ�����-��-����");
//				builder.setPositiveButton("ȷ��", new OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
						
						 Intent shortcutintent = new Intent( "com.android.launcher.action.INSTALL_SHORTCUT");  
					        shortcutintent.putExtra("duplicate", false);   // �������ظ����� 
					        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
					        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.launcher);//���ͼƬ
					        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
					        //�����ݽ���������
					        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext() , StartActivity.class));
					        sendBroadcast(shortcutintent);
					        
					        //��һ��������isFirstStart����Ϊfalse
					        Editor editor=setting.edit();
					        editor.putBoolean("FIRST_START", false); 
					        editor.commit();
						
						
//						dialog.dismiss();
//					}
//				}).show();
//				builder.setNegativeButton("ȡ��", new OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						
//					}
//				}).show();
				
			
			 
			
		 }
		
	}
}
