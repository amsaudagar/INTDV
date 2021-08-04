package com.android.intdv.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import com.android.intdv.movie.usecase.CurrentMoviesUseCase
import com.android.intdv.core.interactor.UseCase
import com.android.intdv.core.platform.BaseResponse
import com.android.intdv.core.platform.BaseViewModel

/**
 * View model class to provide the data to view through rest API
 */
class MoviesViewModel
constructor(private val currentMoviesUseCase : CurrentMoviesUseCase) : BaseViewModel() {

    var currentlyPlayingMovies: MutableLiveData<BaseResponse> = MutableLiveData()

    /**
     * Fetches the currently playing movies
     */
    fun getCurrentPlayingMovies()
            = currentMoviesUseCase(UseCase.None()) {
        it.either(::handleFailure, ::handleCurrentMovieResponse)
    }

    private fun handleCurrentMovieResponse(baseResponse: BaseResponse?) {
        currentlyPlayingMovies.value = baseResponse
    }
}