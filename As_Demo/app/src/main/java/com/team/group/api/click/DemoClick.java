package com.team.group.api.click;

import com.team.group.api.response.FilmDetail;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


public interface DemoClick {


    @GET("v2/movie/subject/{id}")
    Observable<FilmDetail> getFilmDetail(@Path("id") String id);
}
