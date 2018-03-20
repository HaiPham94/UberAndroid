package com.example.haipham.uberandroid.common;

import com.example.haipham.uberandroid.remote.IGoogleApi;
import com.example.haipham.uberandroid.remote.RetrofitClient;

/**
 * Created by phamhai on 3/19/18.
 */

public class Common {
    public static String baseURL = "https://maps.googleapis.com";
    public static IGoogleApi getGoogleApi(){
        return RetrofitClient.getClient(baseURL).create(IGoogleApi.class);
    }
}
