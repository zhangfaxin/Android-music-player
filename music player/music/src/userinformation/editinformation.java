package userinformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.loginregister.LoginAct;
import com.loginregister.RegisterAct;

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
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;

public class editinformation extends Activity {

	private EditText name;
	private EditText pwd;
	private EditText sexedit;
	private EditText telephone_edit;
	private EditText mail_edit;
	private EditText age_edit;
	private String username;
	private String password;
	private String email = null, sex = null, mobilephone = null, age = null;
	private String str;
	private String postStr;
	private String serverIp = "172.24.61.97";
	private String serverPort = "8080";
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edituserinformation);
		Button save = (Button) findViewById(R.id.save_edit);
		sexedit = (EditText) findViewById(R.id.sexedit);
		telephone_edit = (EditText) findViewById(R.id.telephone_edit);
		mail_edit = (EditText) findViewById(R.id.mail_edit);
		age_edit = (EditText) findViewById(R.id.local_edit);
		sex = sexedit.getText().toString();
		mobilephone = telephone_edit.getText().toString();
		email = mail_edit.getText().toString();
		age = age_edit.getText().toString();
		Intent intent = getIntent();
		String sexr = intent.getStringExtra("sex");
		String emailr = intent.getStringExtra("email");
		String mobilephoner = intent.getStringExtra("mobilephone");
		String addressr = intent.getStringExtra("address");
		sexedit.setText(sexr);
		telephone_edit.setText(mobilephoner);
		mail_edit.setText(emailr);
		age_edit.setText(addressr);
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (legal()) {
					LoginAct usera = new LoginAct();
					String username = usera.showname;
					String password = usera.showpwd;
					str = sex + "," + email + "," + mobilephone + "," + age;
					String httpstr = "http://";
					postStr = httpstr + serverIp + ":" + serverPort + "/RegisterAndLogin/UpdateBasicInfo";
					try {
						// 设置连接超时
						HttpParams httpParameters = new BasicHttpParams();
						int timeoutConnection = 3000;
						HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
						DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
						HttpPost httpPost = new HttpPost(postStr);
						List<NameValuePair> nvps = new ArrayList<NameValuePair>();
						nvps.add(new BasicNameValuePair("Name", username));
						nvps.add(new BasicNameValuePair("userinfo", str));
						// 防止中文乱码
						httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
						HttpResponse response = httpclient.execute(httpPost);
						// 得到服务器响应
						HttpEntity entity = response.getEntity();
						InputStream is = entity.getContent();
						String isUser = ConvertStreamToString(is);
						if ("true".equals(isUser)) {
							AlertDialog.Builder builder = new Builder(editinformation.this);
							builder.setMessage("修改成功");
							builder.setTitle("提示");
							builder.setPositiveButton("确认", new OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									Intent intent = new Intent();
									intent.setClass(editinformation.this, baseinformation.class);
									startActivity(intent);
									finish();
								}
							}).show();
						} else if ("false".equals(isUser)) {
							AlertDialog.Builder builder = new Builder(editinformation.this);
							builder.setMessage("修改失败");
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
			}

			// private void finish() {
			// // TODO Auto-generated method stub
			//
			// }
		});

	}

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

	private boolean legal() {
		sexedit = (EditText) findViewById(R.id.sexedit);
		telephone_edit = (EditText) findViewById(R.id.telephone_edit);
		mail_edit = (EditText) findViewById(R.id.mail_edit);
		age_edit = (EditText) findViewById(R.id.local_edit);
		sex = sexedit.getText().toString();
		mobilephone = telephone_edit.getText().toString();
		email = mail_edit.getText().toString();
		age = age_edit.getText().toString();
		if (!("男".equals(sex) || "女".equals(sex))) {
			Toast.makeText(editinformation.this, "性别只能输入男或女！", Toast.LENGTH_SHORT).show();
			return false;
		} else if (!isNumeric(mobilephone)){
			Toast.makeText(editinformation.this, "手机号只能包含数字！", Toast.LENGTH_SHORT).show();
			return false;
		}else if (!isNumeric(age)) {
			// 密码不能为空
			Toast.makeText(editinformation.this, "年龄只能由数字组成!", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static boolean checkletter(String fstrData) {
		char c = fstrData.charAt(0);
		if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
			return true;
		} else {
			return false;
		}
	}
}
