package userinformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.huazi.Mp3Player.R;
import com.loginregister.LoginAct;

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
import android.widget.TextView;
import android.widget.Toast;

public class updatepassword extends Activity {
//	private Bundle bundle = null;
	private Button btn;
	private EditText updatename;
	private EditText updatepwd;
	private EditText followingpwd;
	private String postStr = "";
	private String serverIp = "172.24.61.97";
	private String serverPort = "8080";
	private Intent intent;
	private Button exitButton;
//	private Button shangchuanButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// --;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.update_password);
//		TextView text = (TextView) findViewById(R.id.TextView);
//		text.setText("��½�ɹ�");
		btn = (Button) findViewById(R.id.ackupdate);
		followingpwd = (EditText) findViewById(R.id.Reserve);
		updatename = (EditText) findViewById(R.id.updateusername);
		updatepwd = (EditText) findViewById(R.id.updatepassword);
		// ��Ϣ����
		btn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View paramView) {
				if (updatepassword.this.InputIsSuccsee()) {
					try {
						test();
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void test() throws URISyntaxException {
		EditText Res = (EditText) findViewById(R.id.Reserve);
		EditText name = (EditText) findViewById(R.id.updateusername);
		EditText pwd = (EditText) findViewById(R.id.updatepassword);
		String Reserve = Res.getText().toString();
		String updateusername = name.getText().toString();
		String updatepassword = pwd.getText().toString();
		String httpstr = "http://";
		postStr = httpstr + serverIp + ":" + serverPort + "/RegisterAndLogin/loginerror"; // ����loginservelet
		try {
			// �������ӳ�ʱ
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
			String str = updatepassword + ',' + Reserve;
			HttpPost httpPost = new HttpPost(postStr);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("updateusername", updateusername));
			nvps.add(new BasicNameValuePair("str", str));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			String isUser = ConvertStreamToString(is);
			String tb = isUser.substring(isUser.length() - 4);
			if ("true".equals(tb)) {
				// �޸�����ɹ�
				AlertDialog.Builder builder = new Builder(updatepassword.this);
				builder.setMessage("�޸ĳɹ���");
				builder.setTitle("��ʾ");
				builder.setPositiveButton("ȷ��", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent usrintent = new Intent(updatepassword.this, LoginAct.class);
						startActivity(usrintent);
					}
				}).show();
			} else if ("false".equals(isUser)) {
				// �޸�ʧ��
				AlertDialog.Builder builder = new Builder(updatepassword.this);
				builder.setMessage("��������޸�ʧ�ܣ�");
				builder.setTitle("��ʾ");
				builder.setPositiveButton("ȷ��", new OnClickListener() {
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

		// ����
	}

	private boolean InputIsSuccsee() {
		EditText updatename = (EditText) findViewById(R.id.updateusername);
		EditText updatepwd = (EditText) findViewById(R.id.updatepassword);
		EditText Res = (EditText) findViewById(R.id.Reserve);
		// ��ȡ�û��������Ϣ
		String username = updatename.getText().toString();
		String password = updatepwd.getText().toString();
		String Reserve = Res.getText().toString();
		if ("".equals(username)) {
			// �û������û���Ϊ��
			Toast.makeText(updatepassword.this, "�û�������Ϊ��!", Toast.LENGTH_SHORT).show();
			return false;
		} else if ("".equals(password)) {
			// ���벻��Ϊ��
			Toast.makeText(updatepassword.this, "���벻��Ϊ��!", Toast.LENGTH_SHORT).show();
			return false;
		} else if ("".equals(Reserve)) {
			// ���벻��Ϊ��
			Toast.makeText(updatepassword.this, "�޸����벻��Ϊ��!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (password.equals(Reserve)) {
			// ���벻��Ϊ��
			Toast.makeText(updatepassword.this, "��������������ͬ!", Toast.LENGTH_SHORT).show();
			return false;
		} else if ((password.indexOf(',') != -1) || (password.indexOf('@') != -1) || (password.indexOf('#') != -1)
				|| (password.indexOf('~') != -1)) {
			// ���벻��Ϊ��
			Toast.makeText(updatepassword.this, "����ֻ����������ĸ���!", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	// ��ȡ�ַ���
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
