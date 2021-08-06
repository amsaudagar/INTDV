package com.android.intdv.core.platform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.intdv.R
import com.android.intdv.core.exception.Failure
import com.android.intdv.core.extension.toArrayList
import com.android.intdv.movie.data.local.FavouriteMovieDAO
import com.android.intdv.movie.data.remote.response.MovieDetails
import com.android.intdv.movie.view.dialogs.SortMoviesDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * Represents the base fragment which will provide the common features to all child fragments
 */
abstract class BaseFragment : Fragment() {

    val favouriteMovieDAO: FavouriteMovieDAO by inject()

    /**
     * Returns the resource id for the layout to render in the fragment
     */
    abstract fun layoutId(): Int

    /**
     * Handles the specific feature failure
     */
    open fun handleFeatureFailure() { }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(layoutId(), container, false)
    }

    /**
     * Handles the back press event
     */
    open fun onBackPressed() {
        val count: Int = parentFragmentManager.backStackEntryCount
        if (count == 0) {
            activity?.onBackPressed()
        } else {
            parentFragmentManager.popBackStack()
        }
    }

    /**
     * Displays the progress dialog
     */
    fun showProgressDialog() {
        activity?.let {
            if (!it.isFinishing && !it.isDestroyed) {
                (activity as BaseActivity).showProgress()
            }
        }
    }

    /**
     * Hides the progress dialog
     */
    fun hideProgressDialog() {
        (activity as BaseActivity).hideProgress()
    }

    /**
     * Handles the API failure
     */
    fun handleFailure(failure: Failure?) {
        hideProgressDialog()

        when (failure) {
            is Failure.NetworkConnection -> renderFailure(getString(R.string.connection_failure))
            is Failure.ServerError -> renderFailure(getString(R.string.server_failure))
            else -> {
                handleFeatureFailure()
            }
        }
    }

    /**
     * Displays the error message if API fails
     */
    open fun renderFailure(message: String) {
        //To Show generic failure message
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        hideProgressDialog()
        super.onDestroy()
    }

    /**
     * Inserts favourite movie in data base
     *
     * @param movieDetails details for favourite movie
     */
    fun onFavourite(movieDetails: MovieDetails) {
        CoroutineScope(Dispatchers.IO).launch {
            favouriteMovieDAO.insertMovie(movieDetails)
        }
    }

    /**
     * Deletes the favourite movie from data base
     *
     * @param movieDetails details for favourite movie to remove
     */
    fun onUnFavourite(movieDetails: MovieDetails) {
        CoroutineScope(Dispatchers.IO).launch {
            favouriteMovieDAO.deleteMovieById(movieDetails.id)
        }
    }

    suspend fun getFavouriteMovieIds(): List<Long> {
        val job = CoroutineScope(Dispatchers.IO).async {
            favouriteMovieDAO.findAllIds()
        }
        return job.await()
    }

    fun getSortedList(sortType: SortMoviesDialog.SortType,
                      movieList : ArrayList<MovieDetails>) : ArrayList<MovieDetails> {
        var tempMovieList = ArrayList<MovieDetails>()
        when(sortType) {
            SortMoviesDialog.SortType.NEWEST -> {
                tempMovieList = movieList.sortedByDescending {
                    it.releaseDate
                }.toArrayList()
            }
            SortMoviesDialog.SortType.OLDEST -> {
                tempMovieList = movieList.sortedByDescending {
                    it.releaseDate
                }.toArrayList()
                tempMovieList = tempMovieList.asReversed().toArrayList()
            }
            SortMoviesDialog.SortType.HIGH_TO_LOW_RATING -> {
                tempMovieList = movieList.sortedBy {
                    it.rating
                }.toArrayList()
                tempMovieList = tempMovieList.asReversed().toArrayList()
            }
            SortMoviesDialog.SortType.LOW_TO_HIGH_LOW_RATING -> {
                tempMovieList = movieList.sortedBy {
                    it.rating
                }.toArrayList()
            }
        }
        return tempMovieList
    }
}