package userinformation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.huazi.Mp3Player.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.douniao.music.MainActivity;
import com.douniao.music.PlayMusicAcitivity;
import com.douniao.music.PlayService;
import com.douniao.music.Utils.MyApplication;
//import com.example.ok.User;
//import com.example.ok.MainActivity.MyStringCallback;
//import com.example.ok.MainActivity.MyStringCallback;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

//import java.io.File;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import userinformation.Music.song;

public class onlinemusic extends Activity {
	
	///////////////////////////////////////////
	private static String BaseUrl = "http://tingapi.ting.baidu.com/v1/restserver/ting";
	private static final String METHOD_SEARCH_MUSIC = "baidu.ting.search.catalogSug";
	private static final String PARAM_METHOD = "method";
	private static final String PARAM_QUERY = "query";
	private static Context context;
	///////////////////////////////////////
	final String address ="http://ws.stream.qqmusic.qq.com/";
	
	/////////////////////////////////////
	private static final String TAG = "onlinemusic";
	private static ProgressBar mProgressBar;
	private static TextView mTv;
	///////////////////////////////////////////
	private ListView lv;
	
	
	private EditText et_search;
	private TextView tv_tip;
	private MyListView listView;
	private TextView tv_clear;
	private RecordSQLiteOpenHelper helper = new RecordSQLiteOpenHelper(this);;
	private SQLiteDatabase db;
	private BaseAdapter adapter;
	private ImageView comeback;
	private List<Music.song> MusicList = new ArrayList<Music.song>();
	private MediaPlayer player;
	//////////////////////////
  
    //////////////////////////
//////////////////////////////////////////////////////////////////////////////////
	public static class MyStringCallback extends StringCallback
    {

        @Override
        public void onBefore(Request request)
        {
            super.onBefore(request);
            //setTitle("loading...");
        }

        @Override
        public void onAfter()
        {
            super.onAfter();
            //setTitle("Sample-okHttp");
        }

        @Override
        public void onError(Call call, Exception e)
        {
            mTv.setText("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response)
        {
            mTv.setText("onResponse:" + response);
        }

        @Override
        public void inProgress(float progress)
        {
            //Log.e(TAG, "inProgress:" + progress);
            //mProgressBar.setProgress((int) (100 * progress));
        }
    }
/////////////////////////////////////////////////////////////////////////////////

    
    //////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.online);
		comeback = (ImageView) findViewById(R.id.back);
		initView();
		if (player == null) {
			player = new MediaPlayer();
		}
		
		context = this;
        lv = (ListView)findViewById(R.id.listView_show);
//        data = getData();

        final MyAdapter Listadapter = new MyAdapter(this);
        lv.setAdapter(Listadapter);
////////////////////////////////////////////////////////////////////////
//		mTv = (TextView) findViewById(R.id.textView1);
///////////////////////////////////////////////////////////////////////////////////
		tv_clear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				deleteData();
				queryData("");
			}
		});

		comeback.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		et_search.setOnKeyListener(new View.OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
					((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
							getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					boolean hasData = hasData(et_search.getText().toString().trim());
					if (!hasData) {
						insertData(et_search.getText().toString().trim());
						queryData("");
					}
					////////////////////////////////////////////////////////////////////////////////////////
					searchMusic(et_search.getText().toString().trim(),new HttpCallback<Music>() {
			            @Override
			            public void onSuccess(Music response) 
			            {
			            	//mTv.setText("onResponse:" + response);
			            	MusicList.clear();
			            	MusicList.addAll(response.getSong());
			            	Listadapter.refreshItem();;
			            	
			            }

						@Override
						public void onFail(Exception e) {
							// TODO Auto-generated method stub
							
						}});
					///////////////////////////////////////////////////////////////////////////////////////

				}
				return false;
			}
		});

		et_search.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			public void afterTextChanged(Editable s) {
				if (s.toString().trim().length() == 0) {
					tv_tip.setText("搜索历史");
				} else {
					tv_tip.setText("搜索结果");
				}
				String tempName = et_search.getText().toString();
				queryData(tempName);

			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView textView = (TextView) view.findViewById(android.R.id.text1);
				String name = textView.getText().toString();
				et_search.setText(name);
			}
		});
	}


	private void insertData(String tempName) {
		db = helper.getWritableDatabase();
		db.execSQL("insert into records(name) values('" + tempName + "')");
		db.close();
	}


	@SuppressLint("InlinedApi")
	private void queryData(String tempName) {
		Cursor cursor = helper.getReadableDatabase().rawQuery(
				"select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
		adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[] { "name" },
				new int[] { android.R.id.text1 }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private boolean hasData(String tempName) {
		Cursor cursor = helper.getReadableDatabase().rawQuery(
				"select id as _id,name from records where name =?", new String[]{tempName});
		return cursor.moveToNext();
	}


	private void deleteData() {
		db = helper.getWritableDatabase();
		db.execSQL("delete from records");
		db.close();
	}
	private void initView() {
		et_search = (EditText) findViewById(R.id.et_search);
		tv_tip = (TextView) findViewById(R.id.tv_tip);
		listView = (userinformation.MyListView) findViewById(R.id.listView_online);
		tv_clear = (TextView) findViewById(R.id.tv_clear);

		Drawable drawable = getResources().getDrawable(R.drawable.online);
		drawable.setBounds(0, 0, 60, 60);
		et_search.setCompoundDrawables(drawable, null, null, null);
		}
	
	   static class ViewHolder
	    {
//	        public ImageView img;
	        public TextView song;
	        public TextView singer;
	    }
		
		 public class MyAdapter extends BaseAdapter
		    {
			 
		        private LayoutInflater mInflater = null;
		        private MyAdapter(Context context)
		        {
		            //根据context上下文加载布局，这里的是Demo17Activity本身，即this
		            this.mInflater = LayoutInflater.from(context);
		        }

		        public int getCount() {
		            //How many items are in the data set represented by this Adapter.
		            //在此适配器中所代表的数据集中的条目数
		            return MusicList.size();
		        }

		        public Object getItem(int position) {
		            // Get the data item associated with the specified position in the data set.
		            //获取数据集中与指定索引对应的数据项
		            return position;
		        }

		        public long getItemId(int position) {
		            //Get the row id associated with the specified position in the list.
		            //获取在列表中与指定索引对应的行id
		            return position;
		        }
				public void refreshItem(){
					this.notifyDataSetChanged();
				}
		        //Get a View that displays the data at the specified position in the data set.
		        //获取一个在数据集中指定索引的视图来显示数据
		        public View getView(int position, View convertView, ViewGroup parent) {
		            ViewHolder holder = null;
		            //如果缓存convertView为空，则需要创建View
		            if(convertView == null)
		            {
		                holder = new ViewHolder();
		                //根据自定义的Item布局加载布局
		                convertView = mInflater.inflate(R.layout.online_list, null);
		                holder.song = (TextView)convertView.findViewById(R.id.search_music);
		                holder.singer = (TextView)convertView.findViewById(R.id.search_artist);
		                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
		                convertView.setTag(holder);
		            }else
		            {
		                holder = (ViewHolder)convertView.getTag();
		            }
		            song itemInfo =  MusicList.get(position);
					//Toast.makeText(context,"789",Toast.LENGTH_SHORT).show();
					if (itemInfo != null) {
						holder.song.setOnClickListener((OnClickListener) new shareOnClickListener(position));
						holder.singer.setOnClickListener(new shareOnClickListener(position));

					}
//		            holder.img.setBackgroundResource((Integer)data.get(position).get("img"));
		            holder.song.setText((String)MusicList.get(position).getSongname());
		            holder.singer.setText((String)MusicList.get(position).getSingername());	 
		            return convertView;
		        }
				class shareOnClickListener implements OnClickListener {

					int localposition;
					public shareOnClickListener(int position) {
						this.localposition = position;
					}				
					public void onClick(View v) {
						//player.reset();
						String songId = MusicList.get(localposition).getSongid();
						String url = address+songId+".m4a?fromtag=46";
						Intent intent = new Intent(context, PlayService.class);
						intent.putExtra("name",MusicList.get(localposition).getSongname());
						intent.putExtra("path",url);
						intent.putExtra("artist",MusicList.get(localposition).getSingername());
						PlayService.restate = 1;
						context.startService(intent);	
					}
				}
		        
		    }
	//////////////////////////////////////////////////////////////////////
	
	
    public static void searchMusic(String keyword, final HttpCallback<Music> callback) {
        
    	OkHttpUtils.get().url("http://s.music.qq.com/fcgi-bin/music_search_new_platform")
//    	OkHttpUtils.get().url("")
//    	.addParams("n", "5")
        .addParams("w", keyword)
     
        .build()
        .execute(

        		new MusicCallback()
                {
                    @Override
                    public void onError(Call call, Exception e)
                    {
                        //mTv.setText("onError:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Music response)
                    {    
                    	//mTv.setText("onResponse:Songid= " + response.Music123().getSongid() + "  Songname=  "+response.Music123().getSongname()  + "  getSingername=  "+response.Music123().getSingername());
                    	callback.onSuccess(response);
                    }                  
                }

//
            	);}   
//////////////////////////////////////////////////////////////////////
 
}
