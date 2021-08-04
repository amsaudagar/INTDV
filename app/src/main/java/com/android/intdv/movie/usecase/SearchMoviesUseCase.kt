package com.android.intdv.movie.usecase

import com.android.intdv.core.exception.Failure
import com.android.intdv.core.functional.Either
import com.android.intdv.core.interactor.UseCase
import com.android.intdv.movie.data.MoviesRepository
import com.android.intdv.movie.data.remote.response.MovieListResponse

/**
 * Use case responsible to search movies through repository
 */
class SearchMoviesUseCase
constructor(private val repository: MoviesRepository) :
        UseCase<MovieListResponse, SearchMoviesUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, MovieListResponse> {
        return repository.searchMovies(params.language, params.page, params.apiKey, params.query)
    }

    data class Params(val language: String, val page: Int, val apiKey: String, val query: String)
}