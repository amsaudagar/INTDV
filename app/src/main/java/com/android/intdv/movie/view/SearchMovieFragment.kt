package com.android.intdv.movie.view

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import com.android.intdv.R
import com.android.intdv.core.extension.failure
import com.android.intdv.core.extension.observe
import com.android.intdv.core.platform.BaseActivity
import com.android.intdv.core.platform.BaseFragment
import com.android.intdv.core.util.Constant
import com.android.intdv.movie.data.remote.response.MovieDetails
import com.android.intdv.movie.data.remote.response.MovieListResponse
import com.android.intdv.movie.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.search_movie_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Represents the fragment for movie details
 */
class SearchMovieFragment : BaseFragment() {

    private val moviesViewModel: MoviesViewModel by viewModel()

    override fun layoutId() = R.layout.search_movie_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(moviesViewModel) {
            observe(searchMovieList, ::handleSearchResult)
            failure(failure, ::handleFailure)
        }

        ivBack.setOnClickListener {
            onBackPressed()
        }

        etSearch.requestFocus()
        (activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(etSearch, SHOW_IMPLICIT)

        etSearch.setSelection(0)

        ivSearch.setOnClickListener {
            val query = etSearch.text.toString().trim()
            if(query.isNotEmpty()) {
                showProgressDialog()
                moviesViewModel.searchMovies(1, query)
            }
        }

        rootView.setOnTouchListener { _, _ -> true }
        rootView.setOnClickListener { }
    }

    private fun handleSearchResult(movieListResponse: MovieListResponse?) {
        hideProgressDialog()
        val moviesAdapter = MoviesAdapter(movieListResponse?.movieList?: arrayListOf(), ::onMovieSelected)
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