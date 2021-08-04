package com.android.intdv.movie.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.intdv.R
import com.android.intdv.core.extension.loadFromUrl
import com.android.intdv.core.util.Util
import com.android.intdv.movie.data.remote.response.MovieDetails
import kotlinx.android.synthetic.main.movie_item.view.*
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

    /**
     * Updates the data to load more records
     * @param newMovieList - List of new movie objects
     */
    fun updateData(newMovieList : ArrayList<MovieDetails>) {
        movieList.addAll(newMovieList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: MovieDetails) = with(itemView) {
            ivPoster.loadFromUrl(Util.getOriginalImageUrl(item.posterPath))
            title.text = item.title
            releaseDate.text = Util.getFormattedDate(item.releaseDate)
            itemView.setOnClickListener {
                onMovieSelected(item)
            }

            if(item.rating < 50f) {
                ratingView.setDetails(item.rating, context.getColor(R.color.darkYellow),
                    context.getColor(R.color.lightYellow))
            } else {
                ratingView.setDetails(item.rating, context.getColor(R.color.darkGreen),
                    context.getColor(R.color.lightGreen))
            }
        }
    }
}