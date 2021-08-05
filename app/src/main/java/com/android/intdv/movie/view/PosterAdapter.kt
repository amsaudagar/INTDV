package com.android.intdv.movie.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.intdv.R
import com.android.intdv.core.extension.loadFromUrl
import com.android.intdv.core.util.Util
import com.android.intdv.movie.data.remote.response.MovieDetails
import kotlinx.android.synthetic.main.poster_item.view.*

/**
 * Adapter to list down the Movie posters for currently playing movies
 */
class PosterAdapter(var movieList: ArrayList<MovieDetails>,
                    var onMovieSelected: (MovieDetails) -> Unit) :
    RecyclerView.Adapter<PosterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.poster_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount() = movieList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: MovieDetails) = with(itemView) {
            ivPoster.loadFromUrl(Util.getOriginalImageUrl(item.posterPath))
            itemView.setOnClickListener {
                onMovieSelected(item)
            }
        }
    }
}