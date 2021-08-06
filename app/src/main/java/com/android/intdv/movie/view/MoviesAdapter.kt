package com.android.intdv.movie.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
     * Updates the data to load more records
     * @param newMovieList - List of new movie objects
     */
    fun updateData(newMovieList : ArrayList<MovieDetails>) {
        movieList.addAll(newMovieList)
        notifyDataSetChanged()
    }


    /**
     * Updates the data to load more records
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
                setFavourite(tvFavourite)
            } else {
                setUnFavourite(tvFavourite)
            }

            tvFavourite.setOnClickListener {
                val tag = tvFavourite.tag as String
                if(tag == itemView.context.getString(R.string.favourite)) {
                    onUnFavourite(item)
                    setUnFavourite(tvFavourite)
                    item.isFavourite = false
                } else {
                    onFavourite(item)
                    setFavourite(tvFavourite)
                    item.isFavourite = true
                }
            }
        }

        private fun setFavourite(tvFavourite : TextView) {
            tvFavourite.text = ""//itemView.context.getString(R.string.favourite)
            tvFavourite.tag = itemView.context.getString(R.string.favourite)
            val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_fav_selected)
            itemView.tvFavourite.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
        }

        private fun setUnFavourite(tvFavourite : TextView) {
            tvFavourite.text = ""//itemView.context.getString(R.string.un_favourite)
            tvFavourite.tag = itemView.context.getString(R.string.un_favourite)
            val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_fav_unselected)
            itemView.tvFavourite.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
        }
    }
}