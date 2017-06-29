package userinformation;

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

import com.huazi.Mp3Player.R;
import com.loginregister.LoginAct;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
public class baseinformation extends Activity {
	private String email= null,sex= null,mobilephone = null,age= null;
	private String questStr = "";
	private String postStr = "";
	private String serverIp = "172.24.61.97";
	private String serverPort = "8080";
	//String requestUrl = "http://172.24.61.97:8080/RegisterAndLoginDemo/ShowBasicInfo";
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.active_user);
		//****************************有问题
		LoginAct user = new LoginAct();
		String username = user.showname;
		String password = user.showpwd;
    	//zToast.makeText(context,username,Toast.LENGTH_SHORT).show();
		String httpstr = "http://";
		postStr = httpstr + serverIp + ":" + serverPort + "/RegisterAndLogin/ShowBasicInfo";
		try {
			// 设置连接超时
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(postStr);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("ShowName", username));
			nvps.add(new BasicNameValuePair("ShowPwdWord", password));
			// 防止中文乱码
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httpPost);
			// 得到服务器响应
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			String isUser = ConvertStreamToString(is);
			String []strs = isUser.split(",");
			sex = strs[0];
		    email = strs[1];
		    mobilephone = strs[2];
		    age = strs[3];
			if("nan".equals(strs[0])){
				sex="男";
			}
			if("nv".equals(strs[0])){
				sex="女";
			}
			TextView photo = (TextView) findViewById(R.id.photo);
			TextView sex_setting = (TextView) findViewById(R.id.sex_setting);
			TextView telephone_setting = (TextView) findViewById(R.id.telephone_setting);
			TextView mailsetting = (TextView) findViewById(R.id.mail_setting);
			TextView local_setting = (TextView) findViewById(R.id.local_setting);
			sex_setting.setText(sex);
		    local_setting.setText(age);
			telephone_setting.setText(mobilephone);
			mailsetting.setText(email);
			if(username != null){
				photo.setText(username);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Button change = (Button) findViewById(R.id.xiugai_setting);
		change.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(baseinformation.this, editinformation.class);
				intent.putExtra("sex", sex);
				intent.putExtra("email", email);
				intent.putExtra("mobilephone", mobilephone);
				intent.putExtra("address", age);
				startActivity(intent);
				finish();
			}
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
}
	