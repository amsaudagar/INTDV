package com.android.intdv.core.extension

import android.view.View
import android.widget.ImageView
import com.android.intdv.core.platform.GlideApp
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * Indicates whether the view is visible or not, true if visible false otherwise
 */
fun View.isVisible() = this.visibility == View.VISIBLE

/**
 * Sets the visibility of View as visible
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

/**
 * Sets the visibility of View as gone
 */
fun View.gone() {
    this.visibility = View.GONE
}

/**
 * Loads the image in ImageView
 */
fun ImageView.loadFromUrl(url: String) =
    GlideApp.with(this.context.applicationContext)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .into(this)

/**
 * Loads the image in ImageView
 */
fun ImageView.loadFromUrl(url: String, placeholder: Int, errorPlaceholder: Int) =
    GlideApp.with(this.context.applicationContext)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .placeholder(placeholder)
        .error(errorPlaceholder)
        .into(this)
