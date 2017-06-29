package userinformation;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;

public abstract class MusicCallback extends Callback<Music>{
    @Override
    public Music parseNetworkResponse(Response response) throws IOException, JSONException
    {
    	////////////////////////////////////////////  	
        String string = response.body().string().trim();
        JSONObject data0 = new JSONObject(string.substring(string.indexOf("{"), string.lastIndexOf("}") + 1));
        String str0= data0.toString();
//   	String string ="{data:{song:{list:[{\"f\":\"ame0\",fsinger:0,fsong:Ë®ÊÖ,k:123},{\"f\":\"am\",fsinger:02,fsong:Ë®,k:123}]}}}";
    	///////////////////////////////////////////
        JSONObject data = new JSONObject(str0).getJSONObject("data");
    	String str= data.toString();
    	JSONObject data1 = new JSONObject(str).getJSONObject("song");
    	String str1= data1.toString();
        Music music = new Gson().fromJson(str1, Music.class);
    	///////////////////////////////////////////////////////////////////
//  	Music music = new Gson().fromJson(string, Music.class);
//      return  mGson.fromJson(jsonString, mClass);
        //////////////////////////////////////////////////////////////////       
//    	Music music = null;
//        try {
//        	JSONObject js0=new JSONObject(string);
//			Object obj0=js0.get("data");
//			String str0 = obj0.toString().trim();
//			JSONObject js1=new JSONObject(obj0);
//			Object obj1=js1.get("data");
//		     music = new Gson().fromJson(str0, Music.class);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        
       //////////////////////////////////////////////////////////////////
        return music;
    }
    

}
