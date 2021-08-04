package com.android.intdv.movie.data.remote

import com.android.intdv.movie.data.remote.response.MovieDetailsResponse
import com.android.intdv.movie.data.remote.response.MovieListResponse
import retrofit2.Call
import retrofit2.http.*

internal interface MoviesApi {

    companion object {
        private const val GET_CURRENTLY_PLAYING_MOVIES = "movie/now_playing"
        private const val GET_POPULAR_MOVIES = "movie/popular"
        private const val SEARCH_MOVIES = "search/movie"
        private const val GET_MOVIE_DETAILS = "movie/"
    }

    @GET(GET_CURRENTLY_PLAYING_MOVIES)
    fun getCurrentlyPlayingMovies(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieListResponse>

    @GET(GET_POPULAR_MOVIES)
    fun getPopularMovies(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieListResponse>

    @GET(SEARCH_MOVIES)
    fun searchMovies(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<MovieListResponse>

    @GET("$GET_MOVIE_DETAILS{MOVIE_ID}")
    fun getMovieDetails(
        @Path("MOVIE_ID") movieId: Long,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): Call<MovieDetailsResponse>

}