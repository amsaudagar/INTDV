package com.android.intdv.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import com.android.intdv.core.di.API_KEY
import com.android.intdv.core.di.LANGUAGE
import com.android.intdv.core.platform.BaseViewModel
import com.android.intdv.movie.data.MovieType
import com.android.intdv.movie.data.remote.response.MovieDetailsResponse
import com.android.intdv.movie.data.remote.response.MovieListResponse
import com.android.intdv.movie.usecase.GetMovieDetailsUseCase
import com.android.intdv.movie.usecase.GetMoviesUseCase
import com.android.intdv.movie.usecase.SearchMoviesUseCase

/**
 * View model class to provide the data to view through rest API
 */
class MoviesViewModel
constructor(
    private val currentMoviesUseCase: GetMoviesUseCase,
    private val movieDetailsUseCase: GetMovieDetailsUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
) : BaseViewModel() {

    var currentlyPlayingMovies: MutableLiveData<MovieListResponse> = MutableLiveData()
    var popularMovies: MutableLiveData<MovieListResponse> = MutableLiveData()
    var searchMovieList: MutableLiveData<MovieListResponse> = MutableLiveData()
    var movieDetails: MutableLiveData<MovieDetailsResponse> = MutableLiveData()

    /**
     * Fetches the currently playing movies
     */
    fun getCurrentPlayingMovies(page: Int) = currentMoviesUseCase(
        GetMoviesUseCase.Params(MovieType.CURRENTLY_PLAYING, LANGUAGE, page, API_KEY)
    ) {
        it.either(::handleFailure, ::handleCurrentMovieResponse)
    }

    private fun handleCurrentMovieResponse(movieListResponse: MovieListResponse?) {
        currentlyPlayingMovies.value = movieListResponse
    }

    /**
     * Fetches the popular movies
     */
    fun getPopularMovies(page: Int) = currentMoviesUseCase(
        GetMoviesUseCase.Params(
            MovieType.POPULAR, LANGUAGE, page, API_KEY
        )
    ) {
        it.either(::handleFailure, ::handlePopularMovieResponse)
    }

    private fun handlePopularMovieResponse(movieListResponse: MovieListResponse?) {
        popularMovies.value = movieListResponse
    }

    /**
     * Searches the movies
     */
    fun searchMovies(page: Int, query : String) = searchMoviesUseCase(
        SearchMoviesUseCase.Params(LANGUAGE, page, API_KEY, query)
    ) {
        it.either(::handleFailure, ::handleSearchMovieResponse)
    }

    private fun handleSearchMovieResponse(movieListResponse: MovieListResponse?) {
        searchMovieList.value = movieListResponse
    }

    /**
     * Fetches the movie details
     *
     * @param movieId - movie id to fetch movie details
     */
    fun getMovieDetails(movieId: Long) = movieDetailsUseCase(
        GetMovieDetailsUseCase.Params(
            movieId, LANGUAGE, API_KEY
        )
    ) {
        it.either(::handleFailure, ::handleMovieDetailsResponse)
    }

    private fun handleMovieDetailsResponse(movieDetailsResponse: MovieDetailsResponse?) {
        movieDetails.value = movieDetailsResponse
    }
}