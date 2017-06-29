package com.zhy.http.okhttp.callback;

import okhttp3.Response;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class BitmapCallback extends Callback<Bitmap>
{
    public Bitmap parseNetworkResponse(Response response) throws Exception
    {
        return BitmapFactory.decodeStream(response.body().byteStream());
    }

}
