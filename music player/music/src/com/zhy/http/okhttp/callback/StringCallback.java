package com.zhy.http.okhttp.callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class StringCallback extends Callback<String>
{
    public String parseNetworkResponse(Response response) throws IOException
    {
        return response.body().string();
    }

}
