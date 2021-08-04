package com.android.intdv.core.platform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.intdv.R
import com.android.intdv.core.exception.Failure

/**
 * Represents the base fragment which will provide the common features to all child fragments
 */
abstract class BaseFragment : Fragment() {

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

}