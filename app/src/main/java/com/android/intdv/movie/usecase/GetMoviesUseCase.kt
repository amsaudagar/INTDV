package com.android.intdv.movie.usecase

import com.android.intdv.core.exception.Failure
import com.android.intdv.core.functional.Either
import com.android.intdv.core.interactor.UseCase
import com.android.intdv.movie.data.MovieType
import com.android.intdv.movie.data.MoviesRepository
import com.android.intdv.movie.data.remote.response.MovieListResponse

/**
 * Use case responsible to fetch movies through repository
 */
class GetMoviesUseCase
constructor(private val repository: MoviesRepository) :
        UseCase<MovieListResponse, GetMoviesUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, MovieListResponse> {
        return when(params.movieType){
            MovieType.CURRENTLY_PLAYING -> repository.getCurrentPlayingMovies(params.language, params.page, params.apiKey)
            MovieType.POPULAR -> repository.getPopularMovies(params.language, params.page, params.apiKey)
        }
    }

    data class Params(val movieType : MovieType, val language: String, val page: Int, val apiKey: String)
}