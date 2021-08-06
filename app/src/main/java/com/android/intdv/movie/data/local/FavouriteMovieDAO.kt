/*
 * *
 *  * Created by Mohamed Nabil on 12/18/18 5:29 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 12/18/18 5:29 PM
 *
 */

package com.android.intdv.movie.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.android.intdv.movie.data.remote.response.MovieDetails

/**
 * This is the ROOM DAO class to handle favourite movies
 */

@Dao
interface FavouriteMovieDAO {

    /**
     * Inserts the movie
     *
     * @param movieDetails the movie detail
     */
    @Insert(onConflict = REPLACE)
    fun insertMovie(movieDetails: MovieDetails)

    /**
     * Deletes the movie
     *
     * @param movieId movie id to delete
     */
    @Query("Delete FROM movieDetails WHERE id = :movieId")
    fun deleteMovieById(movieId: Long)

    /**
     * Retrieve list of all favourite movies
     *
     * @return List<MovieDetails>
     */
    @Query("SELECT * FROM movieDetails")
    fun findAll(): List<MovieDetails>

    /**
     * Retrieve list of all favourite movie ids
     *
     * @return List<id>
     */
    @Query("SELECT id FROM movieDetails")
    fun findAllIds(): List<Long>
}