package com.loginregister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.douniao.music.MainActivity;
import com.huazi.Mp3Player.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginAct extends Activity {
	private EditText name;
	private EditText pwd;
	public static String showname;
	public static String showpwd;
	private Button login;
	private CheckBox checkbox;
	private Button register;
	private String questStr = "";
	private String postStr = "";
	private String serverIp = "172.24.61.97";
	private String serverPort = "8080";
	private Intent intent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		name = (EditText) findViewById(R.id.reserve);
		pwd = (EditText) findViewById(R.id.pwd);
		checkbox = (CheckBox) findViewById(R.id.checkBox);
		SharedPreferences setinfo = getPreferences(LoginAct.MODE_PRIVATE);
		String savename = setinfo.getString("USER", "");
		String savepwd = setinfo.getString("PWD", "");
		name.setText(savename);
		pwd.setText(savepwd);
		login = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);
		intent = new Intent(LoginAct.this, RegisterAct.class);
		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (LoginAct.this.loginIsSuccsee()) {
					// 用户输入正确
					store(); // 记住密码
					login(); // 登录
				}
			}
		});
		register.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(intent);
			}
		});
	}

	// 判断用户输入是否规范
	private boolean loginIsSuccsee() {
		EditText name = (EditText) findViewById(R.id.reserve);
		EditText pwd = (EditText) findViewById(R.id.pwd);
		// 获取用户输入的信息
		String userName = name.getText().toString();
		String password = pwd.getText().toString();
		if ("".equals(userName)) {
			// 用户输入用户名为空
			Toast.makeText(LoginAct.this, "用户名不能为空!", Toast.LENGTH_SHORT).show();
			return false;
		} else if ("".equals(password)) {
			// 密码不能为空
			Toast.makeText(LoginAct.this, "密码不能为空!", Toast.LENGTH_SHORT).show();
			return false;
		} else if ((password.indexOf(',') != -1) || (password.indexOf('@') != -1) || (password.indexOf('#') != -1)
				|| (password.indexOf('~') != -1)) {
			// 密码不能为空
			Toast.makeText(LoginAct.this, "密码只能由数字字母组成!", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	// 记住密码

	private void store() {
		SharedPreferences setinfo = getPreferences(LoginAct.MODE_PRIVATE);
		if (checkbox.isChecked()) {
			setinfo.edit().putString("USER", name.getText().toString()).putString("PWD", pwd.getText().toString())
					.commit();
		} else {
			setinfo.edit().putString("USER", null).putString("PWD", null).commit();

		}
	}

	private void login() {
		// 获取输入的用户名,密码
		EditText name = (EditText) findViewById(R.id.reserve);
		EditText pwd = (EditText) findViewById(R.id.pwd);
		String username = name.getText().toString();
		String password = pwd.getText().toString();
		String httpstr = "http://";
		postStr = httpstr + serverIp + ":" + serverPort + "/RegisterAndLogin/login";
		try {
			// 设置连接超时
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(postStr);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("username", username));
			nvps.add(new BasicNameValuePair("password", password));
			// 防止中文乱码
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httpPost);
			// 得到服务器响应
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			String isUser = ConvertStreamToString(is);
			if ("true".equals(isUser)) {
				showname=username;
				showpwd=password;
				Intent intent = new Intent(LoginAct.this, MainActivity.class);// 跳转页面
				startActivity(intent);
				this.finish();
			} else if ("false".equals(isUser)) {
				AlertDialog.Builder builder = new Builder(LoginAct.this);
				builder.setMessage("用户名或密码输入错误！请重新登录");
				builder.setTitle("提示");
				builder.setPositiveButton("确认", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 读取字符流
	public String ConvertStreamToString(InputStream is) {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String returnStr = "";
		try {
			while ((returnStr = br.readLine()) != null) {
				sb.append(returnStr);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		final String result = sb.toString();
		return result;
	}

}