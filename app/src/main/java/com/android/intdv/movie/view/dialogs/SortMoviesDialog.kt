package com.android.intdv.movie.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.android.intdv.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.sort_dialog.*

class SortMoviesDialog(val sortType: SortMoviesDialog.SortType) : BottomSheetDialogFragment(), RadioGroup.OnCheckedChangeListener {

    private var movieSortListener : IOnMovieSortListener? = null

    interface IOnMovieSortListener {
        fun onSorted(sortType : SortType)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sort_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    fun setListener(movieSortListener : IOnMovieSortListener) {
        this.movieSortListener = movieSortListener
    }

    private fun initUI() {
        txtCancel.setOnClickListener {
            dismiss()
        }
        when(sortType) {
            SortType.NEWEST -> rbNewest.isChecked = true
            SortType.OLDEST -> rbOldest.isChecked = true
            SortType.HIGH_TO_LOW_RATING -> rbHightRating.isChecked = true
            SortType.LOW_TO_HIGH_LOW_RATING -> rbLowRating.isChecked = true
        }
        rgSort.setOnCheckedChangeListener(this@SortMoviesDialog)
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        dismiss()
        val sortType = when(checkedId) {
            R.id.rbNewest -> {
                SortType.NEWEST
            }
            R.id.rbOldest -> {
                SortType.OLDEST
            }
            R.id.rbHightRating -> {
                SortType.HIGH_TO_LOW_RATING
            }
            R.id.rbLowRating -> {
                SortType.LOW_TO_HIGH_LOW_RATING
            }
            else -> {
                SortType.NEWEST
            }
        }
        movieSortListener?.onSorted(sortType)
    }

    enum class SortType {
        NEWEST,
        OLDEST,
        HIGH_TO_LOW_RATING,
        LOW_TO_HIGH_LOW_RATING
    }
}