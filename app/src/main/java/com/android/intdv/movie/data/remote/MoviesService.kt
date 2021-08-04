package com.android.intdv.movie.data.remote

import retrofit2.Retrofit

/**
 * Service class to call the API
 */
class MoviesService
constructor(retrofit: Retrofit) : MoviesApi {

    private val api by lazy { retrofit.create(MoviesApi::class.java) }

    override fun getCurrentlyPlayingMovies() = api.getCurrentlyPlayingMovies()
}