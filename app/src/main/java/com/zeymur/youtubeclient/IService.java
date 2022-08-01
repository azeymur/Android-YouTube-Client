package com.zeymur.youtubeclient;

import com.zeymur.youtubeclient.model.YTubeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface IService {
    @Headers("Cache-Control: public, max-stale=30")
    @GET("playlistItems")
    Call<YTubeResponse> getPlaylistItems(@Query("playlistId") String playlistId,
                                         @Query("part") String part,
                                         @Query("key") String key,
                                         @Query("pageToken") String pageToken,
                                         @Query("maxResults") int maxResults);

    @GET("channels")
    Call<YTubeResponse> getChannel(@Query("id") String id,
                                   @Query("part") String part,
                                   @Query("key") String key);

}
