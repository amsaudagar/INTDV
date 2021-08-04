package com.android.intdv.movie.data.remote

import retrofit2.Retrofit

/**
 * Service class to call the API
 */
class MoviesService
constructor(retrofit: Retrofit) : MoviesApi {

    private val api by lazy { retrofit.create(MoviesApi::class.java) }

    override fun getCurrentlyPlayingMovies(language: String, page: Int, apiKey: String)
            = api.getCurrentlyPlayingMovies(language, page, apiKey)

    override fun getPopularMovies(language: String, page: Int, apiKey: String)
            = api.getPopularMovies(language, page, apiKey)

    override fun searchMovies(language: String, page: Int, apiKey: String, query: String)
            = api.searchMovies(language, page, apiKey, query)

    override fun getMovieDetails(movieId: Long, language: String, apiKey: String)
            = api.getMovieDetails(movieId, language, apiKey)
}