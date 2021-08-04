package com.android.intdv.movie.usecase

import com.android.intdv.core.exception.Failure
import com.android.intdv.core.functional.Either
import com.android.intdv.core.interactor.UseCase
import com.android.intdv.movie.data.MoviesRepository
import com.android.intdv.movie.data.remote.response.MovieDetailsResponse

/**
 * Use case responsible to fetch movies details
 */
class GetMovieDetailsUseCase
constructor(private val repository: MoviesRepository) :
        UseCase<MovieDetailsResponse, GetMovieDetailsUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, MovieDetailsResponse> =
        repository.getMovieDetails(params.movieId, params.language, params.apiKey)

    data class Params(val movieId : Long, val language: String, val apiKey: String)
}