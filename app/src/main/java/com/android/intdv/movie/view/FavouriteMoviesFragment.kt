package com.android.intdv.movie.view

import android.os.Bundle
import android.view.View
import com.android.intdv.R
import com.android.intdv.core.extension.gone
import com.android.intdv.core.extension.toArrayList
import com.android.intdv.core.extension.visible
import com.android.intdv.core.platform.BaseActivity
import com.android.intdv.core.platform.BaseFragment
import com.android.intdv.core.util.Constant
import com.android.intdv.movie.data.remote.response.MovieDetails
import com.android.intdv.movie.view.dialogs.SortMoviesDialog
import kotlinx.android.synthetic.main.favourite_movie_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * Represents the fragment for favourite movies
 */
class FavouriteMoviesFragment : BaseFragment(), SortMoviesDialog.IOnMovieSortListener {

    private lateinit var moviesAdapter : MoviesAdapter

    private var movieList = ArrayList<MovieDetails>()
    private var sortType = SortMoviesDialog.SortType.NEWEST

    override fun layoutId() = R.layout.favourite_movie_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivBack.setOnClickListener {
            onBackPressed()
        }

        rootView.setOnTouchListener { _, _ -> true }
        rootView.setOnClickListener { }

        CoroutineScope(Main).launch {
            movieList = getFavouriteMovies().toArrayList()

            if(movieList.isNullOrEmpty()) {
                tvNoFavouriteMovie.visible()
                recyclerView.gone()
            } else {
                movieList.forEach {
                    it.isFavourite = true
                }
                movieList = getSortedList(SortMoviesDialog.SortType.NEWEST, movieList)
                moviesAdapter = MoviesAdapter(movieList,
                    ::onMovieSelected,
                    ::onFavourite,
                    ::onUnFavourite)
                recyclerView.adapter = moviesAdapter
            }
        }

        ivSort.setOnClickListener {
            val sortDialog = SortMoviesDialog(sortType)
            sortDialog.setListener(this)
            sortDialog.show(parentFragmentManager, "SortMoviesDialog")
        }
    }

    override fun onSorted(sortType: SortMoviesDialog.SortType) {
        this.sortType = sortType
        movieList = getSortedList(sortType, movieList)
        if(::moviesAdapter.isInitialized) {
            moviesAdapter.refreshData(movieList)
        }
    }

    private suspend fun getFavouriteMovies(): List<MovieDetails> {
        val job = CoroutineScope(IO).async {
            favouriteMovieDAO.findAll()
        }
        return job.await()
    }

    private fun onMovieSelected(movieDetails : MovieDetails) {
        val fragment = MovieDetailsFragment()
        val bundle = Bundle()
        bundle.putLong(Constant.MOVIE_ID, movieDetails.id)
        bundle.putBoolean(Constant.IS_FAVOURITE_MOVIE, movieDetails.isFavourite)
        fragment.arguments = bundle
        (activity as BaseActivity).addFragmentInFlow(fragment)
    }
}