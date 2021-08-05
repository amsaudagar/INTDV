package com.android.intdv.movie.view

import android.os.Bundle
import android.view.View
import com.android.intdv.R
import com.android.intdv.core.extension.failure
import com.android.intdv.core.extension.observe
import com.android.intdv.core.platform.BaseActivity
import com.android.intdv.core.platform.BaseFragment
import com.android.intdv.core.util.Constant
import com.android.intdv.movie.data.remote.response.MovieDetails
import com.android.intdv.movie.data.remote.response.MovieListResponse
import com.android.intdv.movie.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.movie_list_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Represents the fragment for movie list
 */
class MovieListFragment : BaseFragment() {

    private val moviesViewModel: MoviesViewModel by viewModel()
    private lateinit var moviesAdapter : MoviesAdapter

    private var pager = 1

    override fun layoutId() = R.layout.movie_list_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(moviesViewModel) {
            observe(currentlyPlayingMovies, ::handleMovieListSuccess)
            observe(popularMovies, ::handlePopularMovieListSuccess)
            failure(failure, ::handleFailure)
        }

        showProgressDialog()
        moviesViewModel.getCurrentPlayingMovies(1)
        moviesViewModel.getPopularMovies(pager)

        setClickListener()
    }

    override fun handleFeatureFailure() {

    }

    private fun setClickListener() {
        ivFavourite.setOnClickListener {
            val fragment = FavouriteMoviesFragment()
            (activity as BaseActivity).addFragmentInFlow(fragment)
        }

        ivSearch.setOnClickListener {
            val fragment = SearchMovieFragment()
            (activity as BaseActivity).addFragmentInFlow(fragment)
        }
    }

    private fun handleMovieListSuccess(movieListResponse: MovieListResponse?) {
        hideProgressDialog()
        rvMoviePoster.adapter = PosterAdapter(movieListResponse?.movieList?: arrayListOf(),
            ::onMovieSelected)
    }

    private fun handlePopularMovieListSuccess(movieListResponse: MovieListResponse?) {
        hideProgressDialog()
        moviesAdapter = MoviesAdapter(movieListResponse?.movieList?: arrayListOf(), ::onMovieSelected)
        recyclerView.adapter = moviesAdapter
    }

    private fun onMovieSelected(movieDetails : MovieDetails) {
        val fragment = MovieDetailsFragment()
        val bundle = Bundle()
        bundle.putLong(Constant.MOVIE_ID, movieDetails.id)
        fragment.arguments = bundle
        (activity as BaseActivity).addFragmentInFlow(fragment)
    }
}