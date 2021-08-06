package com.android.intdv.movie.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
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
                    var onMovieSelected: (MovieDetails) -> Unit,
                    val onFavourite : (MovieDetails) -> Unit,
                    val onUnFavourite : (MovieDetails) -> Unit) :
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
     * Updates/Refreshes the data to load new records
     *
     * @param newMovieList - List of new movie objects
     */
    fun refreshData(newMovieList : ArrayList<MovieDetails>) {
        movieList = newMovieList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: MovieDetails) = with(itemView) {
            item.posterPath?.let {
                ivPoster.loadFromUrl(Util.getOriginalImageUrl(it))
            }
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

            if(item.isFavourite) {
                setFavourite(ivFavourite)
            } else {
                setUnFavourite(ivFavourite)
            }

            ivFavourite.setOnClickListener {
                val tag = ivFavourite.tag as String
                if(tag == itemView.context.getString(R.string.favourite)) {
                    onUnFavourite(item)
                    setUnFavourite(ivFavourite)
                    item.isFavourite = false
                } else {
                    onFavourite(item)
                    setFavourite(ivFavourite)
                    item.isFavourite = true
                }
            }
        }

        private fun setFavourite(ivFavourite : ImageView) {
            ivFavourite.tag = itemView.context.getString(R.string.favourite)
            ivFavourite.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_fav_selected)
        }

        private fun setUnFavourite(ivFavourite : ImageView) {
            ivFavourite.tag = itemView.context.getString(R.string.un_favourite)
            ivFavourite.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_fav_unselected)
        }
    }
}