package com.loginregister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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

import com.huazi.Mp3Player.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterAct extends Activity {
	private Button reset;
	private Button register;
	// 用户名
	private EditText uNameEditText;
	// 密码
	private EditText pwdEditText1;
	// 重复密码
	private EditText pwdEditText2;
	private String userName;
	private String password1;
	private String password2;
	private String serverIp = "172.24.61.97";
	private String serverPort = "8080";
	private String httpStr;
	private String postStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		reset = (Button) findViewById(R.id.reset);
		register = (Button) findViewById(R.id.register2);

		register.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (registerIsSuccsee()) {
					register(); // 注册
				}

			}

		});
		// 重置输入框
		reset.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				uNameEditText = (EditText) findViewById(R.id.username);
				pwdEditText1 = (EditText) findViewById(R.id.password1);
				pwdEditText2 = (EditText) findViewById(R.id.password2);
				uNameEditText.setText("");
				pwdEditText1.setText("");
				pwdEditText2.setText("");
			}
		});

	}

	// 判断输入是否规范

	private boolean registerIsSuccsee() {
		EditText name = (EditText) findViewById(R.id.username);
		EditText pwd1 = (EditText) findViewById(R.id.password1);
		EditText pwd2 = (EditText) findViewById(R.id.password2);
		// 获取用户输入的信息
		String userName = name.getText().toString();
		String password1 = pwd1.getText().toString();
		String password2 = pwd2.getText().toString();
		if ("".equals(userName)) {
			// 用户输入用户名为空
			Toast.makeText(RegisterAct.this, "用户名不能为空!", Toast.LENGTH_SHORT).show();
			return false;
		} else if ("".equals(password1)) {
			// 密码不能为空
			Toast.makeText(RegisterAct.this, "密码不能为空!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (!password1.equals(password2)) {
			Toast.makeText(RegisterAct.this, "两次输入密码不一致!", Toast.LENGTH_SHORT).show();
			return false;
		} else if ((password1.indexOf(',') != -1) || (password1.indexOf('@') != -1) || (password1.indexOf('#') != -1)
				|| (password1.indexOf('~') != -1)) {
			// 密码不能为空
			Toast.makeText(RegisterAct.this, "密码只能由数字字母组成!", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	// 注册

	private void register() {
		uNameEditText = (EditText) findViewById(R.id.username);
		pwdEditText1 = (EditText) findViewById(R.id.password1);
		pwdEditText2 = (EditText) findViewById(R.id.password2);
		userName = uNameEditText.getText().toString();
		password1 = pwdEditText1.getText().toString();
		password2 = pwdEditText2.getText().toString();
		httpStr = "http://";
		postStr = httpStr + serverIp + ":" + serverPort + "/RegisterAndLogin/register";
		try {
			HttpParams httpParams = new BasicHttpParams();
			// 设置连接超时
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpPost httpPost = new HttpPost(postStr);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("username", userName));
			nvps.add(new BasicNameValuePair("password1", password1));
			nvps.add(new BasicNameValuePair("password2", password1));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			String isUser = ConvertStreamToString(is);
			if ("success".equals(isUser)) {
				// 表示用户注册成功
				AlertDialog.Builder builder = new Builder(RegisterAct.this);
				builder.setMessage("恭喜你！注册成功");
				builder.setTitle("提示");
				builder.setPositiveButton("确认", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(RegisterAct.this, LoginAct.class);
						dialog.dismiss();
						startActivity(i);
					}
				}).show();
			} else if ("userExist".equals(isUser)) {
				// 用户已经被注册
				AlertDialog.Builder builder = new Builder(RegisterAct.this);
				builder.setMessage("抱歉！该用户已被注册！");
				builder.setTitle("提示");
				builder.setPositiveButton("确认", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
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

		System.out.println(result);
		return result;
	}
}
