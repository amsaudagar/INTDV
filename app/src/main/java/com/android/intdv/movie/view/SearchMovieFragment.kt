package com.android.intdv.movie.view

import android.content.Context
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
import com.android.intdv.movie.view.dialogs.SortMoviesDialog
import com.android.intdv.movie.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.search_movie_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Represents the fragment for movie details
 */
class SearchMovieFragment : BaseFragment(), SortMoviesDialog.IOnMovieSortListener {

    private val moviesViewModel: MoviesViewModel by viewModel()

    private lateinit var moviesAdapter : MoviesAdapter

    private var movieList = ArrayList<MovieDetails>()
    private var sortType = SortMoviesDialog.SortType.NEWEST

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
            ivSearch.hideKeyboard()
            val query = etSearch.text.toString().trim()
            if(query.isNotEmpty()) {
                showProgressDialog()
                moviesViewModel.searchMovies(1, query)
            }
        }

        ivSort.setOnClickListener {
            val sortDialog = SortMoviesDialog(sortType)
            sortDialog.setListener(this)
            sortDialog.show(parentFragmentManager, "SortMoviesDialog")
        }

        rootView.setOnTouchListener { _, _ -> true }
        rootView.setOnClickListener { }
    }

    override fun onSorted(sortType: SortMoviesDialog.SortType) {
        this.sortType = sortType
        movieList = getSortedList(sortType, movieList)
        if(::moviesAdapter.isInitialized) {
            moviesAdapter.refreshData(movieList)
        }
    }

    private fun handleSearchResult(movieListResponse: MovieListResponse?) {
        hideProgressDialog()
        movieList = movieListResponse?.movieList?: arrayListOf()
        CoroutineScope(Dispatchers.Main).launch {
            val ids = getFavouriteMovieIds()
            movieList.forEach {
                it.isFavourite = ids.contains(it.id)
            }
        }
        movieList = getSortedList(SortMoviesDialog.SortType.NEWEST,movieList)
        moviesAdapter = MoviesAdapter(movieList,
            ::onMovieSelected,
            ::onFavourite,
            ::onUnFavourite)
        recyclerView.adapter = moviesAdapter
    }

    private fun onMovieSelected(movieDetails : MovieDetails) {
        val fragment = MovieDetailsFragment()
        val bundle = Bundle()
        bundle.putLong(Constant.MOVIE_ID, movieDetails.id)
        bundle.putBoolean(Constant.IS_FAVOURITE_MOVIE, movieDetails.isFavourite)
        fragment.arguments = bundle
        (activity as BaseActivity).addFragmentInFlow(fragment)
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}