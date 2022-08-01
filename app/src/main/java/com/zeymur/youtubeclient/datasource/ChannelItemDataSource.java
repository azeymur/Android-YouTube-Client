package com.zeymur.youtubeclient.datasource;

import com.zeymur.youtubeclient.R;
import com.zeymur.youtubeclient.RequestState;
import com.zeymur.youtubeclient.model.RequestError;
import com.zeymur.youtubeclient.model.Item;
import com.zeymur.youtubeclient.model.YTubeResponse;

import com.google.gson.Gson;

import android.content.Context;
import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelItemDataSource extends ItemDataSource {
    private String channelId;
    private String playlistId;

    public ChannelItemDataSource(Context context, String channelId, String key) {
        super(context, key);
        this.channelId = channelId;
        this.key = key;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull final LoadInitialCallback<String, Item> callback) {
        requestState.postValue(new RequestState(RequestState.STATE.RUNNING));

        service.getChannel(channelId, "contentDetails", key)
                .enqueue(new Callback<YTubeResponse>() {
                    @Override
                    public void onResponse(Call<YTubeResponse> call, Response<YTubeResponse> response) {
                        if (response.isSuccessful() && response.body().getItems().size() != 0) {
                            playlistId = response.body().getItems().get(0).getContentDetails().getRelatedPlaylists().getUploads();

                            service.getPlaylistItems(playlistId, "snippet,contentDetails", key, null, PAGE_SIZE)
                                    .enqueue(new Callback<YTubeResponse>() {
                                        @Override
                                        public void onResponse(Call<YTubeResponse> call, Response<YTubeResponse> response) {
                                            if (response.isSuccessful()) {
                                                requestState.postValue(new RequestState(RequestState.STATE.SUCCESS));
                                                callback.onResult(response.body().getItems(), null, response.body().getNextPageToken());
                                            } else {
                                                RequestError requestError;
                                                try {
                                                    requestError = new Gson().fromJson(response.errorBody().string(), RequestError.class);
                                                } catch (Exception exp) {
                                                    requestError = new RequestError(context.getString(R.string.error_invalid_request));
                                                }

                                                requestState.postValue(new RequestState(RequestState.STATE.FAILED, requestError.getMessage(), () -> loadInitial(params, callback)));
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<YTubeResponse> call, Throwable t) {
                                            requestState.postValue(new RequestState(RequestState.STATE.FAILED, t.getMessage(), () -> loadInitial(params, callback)));

                                        }
                                    });
                        } else {
                            RequestError requestError;
                            try {
                                requestError = new Gson().fromJson(response.errorBody().string(), RequestError.class);
                            } catch (Exception exp) {
                                requestError = new RequestError(context.getString(R.string.error_invalid_request));
                            }

                            requestState.postValue(new RequestState(RequestState.STATE.FAILED, requestError.getMessage(), () -> loadInitial(params, callback)));
                        }
                    }

                    @Override
                    public void onFailure(Call<YTubeResponse> call, Throwable t) {
                        requestState.postValue(new RequestState(RequestState.STATE.FAILED, t.getMessage(), () -> loadInitial(params, callback)));

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<String> params, @NonNull final LoadCallback<String, Item> callback) {
        //This is not necessary in our case as our data doesn't change.
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<String> params, @NonNull final LoadCallback<String, Item> callback) {
        requestState.postValue(new RequestState(RequestState.STATE.RUNNING));

        service.getPlaylistItems(playlistId, "snippet,contentDetails", key, params.key, PAGE_SIZE)
                .enqueue(new Callback<YTubeResponse>() {
                    @Override
                    public void onResponse(Call<YTubeResponse> call, Response<YTubeResponse> response) {
                        if (response.isSuccessful()) {
                            requestState.postValue(new RequestState(RequestState.STATE.SUCCESS));
                            callback.onResult(response.body().getItems(), response.body().getNextPageToken());
                        } else {
                            RequestError requestError;
                            try {
                                requestError = new Gson().fromJson(response.errorBody().string(), RequestError.class);
                            } catch (Exception exp) {
                                requestError = new RequestError(context.getString(R.string.error_invalid_request));
                            }

                            requestState.postValue(new RequestState(RequestState.STATE.FAILED, requestError.getMessage(), () -> loadAfter(params, callback)));
                        }
                    }

                    @Override
                    public void onFailure(Call<YTubeResponse> call, Throwable t) {
                        requestState.postValue(new RequestState(RequestState.STATE.FAILED, t.getMessage(), () -> loadAfter(params, callback)));
                    }
                });
    }
}
