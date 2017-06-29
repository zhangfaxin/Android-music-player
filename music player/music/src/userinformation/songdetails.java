package userinformation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;
import com.huazi.Mp3Player.R;
public class songdetails extends Activity {
	private ExpandableListView expendView;
	private int []group_click=new int[5];
	private long mExitTime=0;
	private TextView settime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.song_details);
		Intent intent = getIntent();
		String path = intent.getStringExtra("path");
		String name = intent.getStringExtra("name");
		String artist = intent.getStringExtra("artist");
		TextView nameshow = (TextView) findViewById(R.id.tv_song_title);
		TextView artistshow = (TextView) findViewById(R.id.tv_song_artist);
		TextView pathshow = (TextView) findViewById(R.id.tv_song_filepath);
		pathshow.setText(path);
		nameshow.setText(name);
		artistshow.setText(artist);

		}
}
	