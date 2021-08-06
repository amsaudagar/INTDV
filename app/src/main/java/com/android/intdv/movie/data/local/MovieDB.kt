package com.android.intdv.movie.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.intdv.movie.data.remote.response.MovieDetails

@Database(entities = [MovieDetails::class], version = 1, exportSchema = false)
abstract class MovieDB : RoomDatabase() {
    abstract fun favouriteMovieDao(): FavouriteMovieDAO
}