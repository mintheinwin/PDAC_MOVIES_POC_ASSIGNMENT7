package com.mtw.movie_poc_screen.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import com.mtw.movie_poc_screen.events.RestApiEvents;
import com.mtw.movie_poc_screen.network.responses.GetMovieResponse;
import com.mtw.movie_poc_screen.utils.APIConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Aspire-V5 on 12/6/2017.
 */

public class MovieDataAgentImpl implements MovieDataAgent {
    private static MovieDataAgentImpl objectInstance;

    private MovieAPI movieAPI;

    private MovieDataAgentImpl() {
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        // time 60 sec is optimal.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(okHttpClient)
                .build();

        movieAPI = retrofit.create(MovieAPI.class);
    }

    public static MovieDataAgentImpl getObjectInstance() {
        if (objectInstance == null) {
            objectInstance = new MovieDataAgentImpl();
        }
        return objectInstance;
    }


    @Override
    public void loadPopularMovies(int page, String accessToken, final Context context) {
        Call<GetMovieResponse> loadPopularMoviesCall = movieAPI.loadPopularMovies(page, accessToken);
        loadPopularMoviesCall.enqueue(new Callback<GetMovieResponse>() {
            @Override
            public void onResponse(Call<GetMovieResponse> call, Response<GetMovieResponse> response) {
                GetMovieResponse getMovieResponse = response.body();
                Log.e("status ", getMovieResponse.getCode() + "");
                if (getMovieResponse != null
                        && getMovieResponse.getPopularMovies().size() > 0) {
                    RestApiEvents.MoviesDataLoadedEvent moviesDataLoadedEvent = new RestApiEvents.MoviesDataLoadedEvent(getMovieResponse.getPage(), getMovieResponse.getPopularMovies(),context);
                    EventBus.getDefault().post(moviesDataLoadedEvent);

                } else {
                    RestApiEvents.ErrorInvokingAPIEvent errorInvokingAPIEvent
                            = new RestApiEvents.ErrorInvokingAPIEvent("No data could be load for now. Please try again later.");
                    EventBus.getDefault().post(errorInvokingAPIEvent);
                }
            }

            @Override
            public void onFailure(Call<GetMovieResponse> call, Throwable t) {
                RestApiEvents.ErrorInvokingAPIEvent errorInvokingAPIEvent
                        = new RestApiEvents.ErrorInvokingAPIEvent(t.getMessage());
                EventBus.getDefault().post(errorInvokingAPIEvent);
            }
        });

    }
}
