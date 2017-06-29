package userinformation;



import com.douniao.music.user;
import com.huazi.Mp3Player.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
/**搜索歌曲的活动单元组件**/
public class search extends Activity {
	private AutoCompleteTextView actv;
	
	private Button search;
	public static Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	  	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏显示
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.search);
		search = (Button) findViewById(R.id.search);
		actv = (AutoCompleteTextView) findViewById(R.id.actv);
		context=this;
		search.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String select = actv.getText().toString();
				if(select.equals("")){
					Toast.makeText(context, "请输入要查找的歌曲名", 0).show();
				}else{
				Intent intent =new Intent();
				intent.putExtra("input", select);
				intent.setClass(search.this,localsearch.class);
				startActivity(intent);
				finish();
				}

				
			}
		});
	}

}
