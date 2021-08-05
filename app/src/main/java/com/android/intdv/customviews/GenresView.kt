package com.android.intdv.customviews

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.android.intdv.R
import kotlinx.android.synthetic.main.genres_item.view.*

/**
 * @author Abdul Mateen
 *
 * Indicates the custom view to show the genres of the movie
 */
class GenresView(context: Context, text: String) : LinearLayout(context) {
    init {
        val v = View.inflate(context, R.layout.genres_item, this)
        txtGenres.text = text
    }
}