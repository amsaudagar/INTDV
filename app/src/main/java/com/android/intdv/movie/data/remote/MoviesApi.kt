package com.android.intdv.movie.data.remote

import com.android.intdv.core.platform.BaseResponse
import retrofit2.Call
import retrofit2.http.GET

internal interface MoviesApi {

    companion object {
        private const val GET_CURRENTLY_PLAYING_MOVIES = "movie/now_playing?language=en-US&page=undefined&api_key=55957fcf3ba81b137f8fc01ac5a31fb5"
    }

    @GET(GET_CURRENTLY_PLAYING_MOVIES)
    fun getCurrentlyPlayingMovies(): Call<BaseResponse>
}