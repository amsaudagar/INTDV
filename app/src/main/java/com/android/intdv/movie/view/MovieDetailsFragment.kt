package com.android.intdv.movie.view

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.android.intdv.R
import com.android.intdv.core.extension.failure
import com.android.intdv.core.extension.loadFromUrl
import com.android.intdv.core.extension.observe
import com.android.intdv.core.platform.BaseFragment
import com.android.intdv.core.util.Constant
import com.android.intdv.core.util.Util
import com.android.intdv.customviews.GenresView
import com.android.intdv.movie.data.remote.response.MovieDetails
import com.android.intdv.movie.data.remote.response.MovieDetailsResponse
import com.android.intdv.movie.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.movie_details_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Represents the fragment for movie details
 */
class MovieDetailsFragment : BaseFragment() {

    private val moviesViewModel: MoviesViewModel by viewModel()

    private var isFavourite = false
    private lateinit var movieItem : MovieDetails

    override fun layoutId() = R.layout.movie_details_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = arguments?.getLong(Constant.MOVIE_ID)
        isFavourite = arguments?.getBoolean(Constant.IS_FAVOURITE_MOVIE)?:false

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


        if (isFavourite) {
            setFavourite()
        } else {
            setUnFavourite()
        }

        ivFavourite.setOnClickListener {
            val tag = ivFavourite.tag as String
            if(::movieItem.isInitialized) {
                if (tag == getString(R.string.favourite)) {
                    onUnFavourite(movieItem)
                    setUnFavourite()
                } else {
                    onFavourite(movieItem)
                    setFavourite()
                }
            }
        }
    }

    private fun setFavourite() {
        ivFavourite.tag = getString(R.string.favourite)
        ivFavourite.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_fav_selected)
    }

    private fun setUnFavourite() {
        ivFavourite.tag = getString(R.string.un_favourite)
        ivFavourite.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_fav_unselected)
    }

    private fun handleMovieDetailsSuccess(movieDetailsResponse: MovieDetailsResponse?) {
        hideProgressDialog()
        movieDetailsResponse?.let {
            it.posterPath?.let{path -> ivPoster.loadFromUrl(Util.getOriginalImageUrl(path))}
            title.text = it.title
            txtOverview.text = it.overview
            releaseDate.text =  "${Util.getFormattedDate(it.releaseDate)} - ${Util.getFormattedTime(it.runtime)}"

            it.genres.forEach {genres ->
                flexLayout.addView(GenresView(requireContext(), genres.name))
            }

            movieItem = MovieDetails(it.id, it.posterPath, it.title,
                    it.overview, 0.0f, it.releaseDate)
        }
    }
}