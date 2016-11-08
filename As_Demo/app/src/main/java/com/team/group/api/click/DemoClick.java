package com.team.group.api.click;

import com.team.group.api.response.DemoResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface DemoClick {

    @GET("train/s")
    Observable<DemoResponse> demoClick(@Query("name") String name, @Query("key") String key);

}
