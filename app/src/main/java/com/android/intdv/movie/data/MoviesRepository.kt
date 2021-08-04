package com.android.intdv.movie.data

import com.android.intdv.core.exception.Failure
import com.android.intdv.core.functional.Either
import com.android.intdv.core.platform.BaseRepository
import com.android.intdv.core.platform.NetworkHandler
import com.android.intdv.movie.data.remote.MoviesService
import com.android.intdv.movie.data.remote.response.MovieDetailsResponse
import com.android.intdv.movie.data.remote.response.MovieListResponse

/**
 * Represents the repository responsible to make the REST API call using service class
 */
interface MoviesRepository {
    fun getCurrentPlayingMovies(language : String, page : Int, apiKey : String): Either<Failure, MovieListResponse>
    fun getPopularMovies(language : String, page : Int, apiKey : String): Either<Failure, MovieListResponse>
    fun searchMovies(language : String, page : Int, apiKey : String, query : String): Either<Failure, MovieListResponse>
    fun getMovieDetails(movieId : Long, language : String, apiKey : String): Either<Failure, MovieDetailsResponse>

    class MoviesRepositoryImp
    constructor(private val networkHandler: NetworkHandler,
                private val service: MoviesService) : MoviesRepository, BaseRepository() {

        override fun getCurrentPlayingMovies(language : String, page : Int, apiKey : String): Either<Failure, MovieListResponse> {
            return when (networkHandler.isConnected) {
                true -> {
                    request(service.getCurrentlyPlayingMovies(language, page, apiKey), {
                        it
                    }, MovieListResponse.empty())
                }
                false, null -> {
                    Either.Left(Failure.NetworkConnection)
                }
            }
        }

        override fun getPopularMovies(language : String, page : Int, apiKey : String): Either<Failure, MovieListResponse> {
            return when (networkHandler.isConnected) {
                true -> {
                    request(service.getPopularMovies(language, page, apiKey), {
                        it
                    }, MovieListResponse.empty())
                }
                false, null -> {
                    Either.Left(Failure.NetworkConnection)
                }
            }
        }

        override fun searchMovies(language : String, page : Int, apiKey : String, query : String): Either<Failure, MovieListResponse> {
            return when (networkHandler.isConnected) {
                true -> {
                    request(service.searchMovies(language, page, apiKey, query), {
                        it
                    }, MovieListResponse.empty())
                }
                false, null -> {
                    Either.Left(Failure.NetworkConnection)
                }
            }
        }

        override fun getMovieDetails(movieId : Long, language : String, apiKey : String): Either<Failure, MovieDetailsResponse> {
            return when (networkHandler.isConnected) {
                true -> {
                    request(service.getMovieDetails(movieId, language, apiKey), {
                        it
                    }, MovieDetailsResponse.empty())
                }
                false, null -> {
                    Either.Left(Failure.NetworkConnection)
                }
            }
        }
    }
}