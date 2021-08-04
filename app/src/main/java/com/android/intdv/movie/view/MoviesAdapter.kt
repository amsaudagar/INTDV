package com.android.intdv.movie.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.intdv.R
import com.android.intdv.movie.data.remote.response.MovieDetails
import java.util.*

/**
 * Adapter class to list the movie items in scrolling view
 */
class MoviesAdapter(var movieList: ArrayList<MovieDetails>,
                    var onMovieSelected: (MovieDetails) -> Unit) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.movie_item,
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

        }
    }
}