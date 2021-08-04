package com.android.intdv.movie.usecase

import com.android.intdv.core.exception.Failure
import com.android.intdv.core.functional.Either
import com.android.intdv.core.interactor.UseCase
import com.android.intdv.core.platform.BaseResponse
import com.android.intdv.movie.data.MoviesRepository

/**
 * Use case responsible to fetch currently playing movies through repository
 */
class CurrentMoviesUseCase
constructor(private val repository: MoviesRepository) :
        UseCase<BaseResponse, UseCase.None>() {

    override suspend fun run(params: None): Either<Failure, BaseResponse> = repository.getCurrentPlayingMovies()
}