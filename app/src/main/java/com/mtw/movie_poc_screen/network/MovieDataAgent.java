package com.mtw.movie_poc_screen.network;

import android.content.Context;

/**
 * Created by Aspire-V5 on 12/6/2017.
 */

public interface MovieDataAgent {
    void loadPopularMovies(int page, String accessToken, Context context);
}
