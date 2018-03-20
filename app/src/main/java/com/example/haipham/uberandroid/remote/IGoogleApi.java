package com.example.haipham.uberandroid.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by phamhai on 3/19/18.
 */

public interface IGoogleApi {
    @GET
    Call<String> getPath(@Url String url);
}
