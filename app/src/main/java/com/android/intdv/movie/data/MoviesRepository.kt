package com.android.intdv.movie.data

import com.android.intdv.core.exception.Failure
import com.android.intdv.core.functional.Either
import com.android.intdv.core.platform.BaseResponse

/**
 * Represents the repository responsible to make the REST API call using service class
 */
interface MoviesRepository {
    fun getCurrentPlayingMovies(): Either<Failure, BaseResponse>
}