package com.android.intdv.movie.view

import android.os.Bundle
import android.view.View
import com.android.intdv.R
import com.android.intdv.core.extension.failure
import com.android.intdv.core.extension.loadFromUrl
import com.android.intdv.core.extension.observe
import com.android.intdv.core.platform.BaseFragment
import com.android.intdv.core.util.Constant
import com.android.intdv.core.util.Util
import com.android.intdv.customviews.GenresView
import com.android.intdv.movie.data.remote.response.MovieDetailsResponse
import com.android.intdv.movie.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.movie_details_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Represents the fragment for movie details
 */
class MovieDetailsFragment : BaseFragment() {

    private val moviesViewModel: MoviesViewModel by viewModel()

    override fun layoutId() = R.layout.movie_details_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = arguments?.getLong(Constant.MOVIE_ID)
        movieId?.let {
            showProgressDialog()
            moviesViewModel.getMovieDetails(it)
        }

        with(moviesViewModel) {
            observe(movieDetails, ::handleMovieDetailsSuccess)
            failure(failure, ::handleFailure)
        }

        ivBack.setOnClickListener {
            onBackPressed()
        }
        rootView.setOnTouchListener { _, _ -> true }
        rootView.setOnClickListener { }
    }

    private fun handleMovieDetailsSuccess(movieDetailsResponse: MovieDetailsResponse?) {
        hideProgressDialog()
        movieDetailsResponse?.let {
            ivPoster.loadFromUrl(Util.getOriginalImageUrl(it.posterPath))
            title.text = it.title
            txtOverview.text = it.overview
            releaseDate.text =  "${Util.getFormattedDate(it.releaseDate)} - ${Util.getFormattedTime(it.runtime)}"

            it.genres.forEach {genres ->
                flexLayout.addView(GenresView(requireContext(), genres.name))
            }
        }
    }
}