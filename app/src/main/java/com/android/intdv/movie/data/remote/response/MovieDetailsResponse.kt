package com.android.intdv.movie.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(

    @SerializedName("id")
    @Expose
    var id: Long = 0,

    @SerializedName("runtime")
    @Expose
    var runtime: Long = 0,

    @SerializedName("poster_path")
    @Expose
    var posterPath: String = "",

    @SerializedName("original_title")
    @Expose
    var title: String = "",

    @SerializedName("overview")
    @Expose
    var overview: String = "",

    @SerializedName("release_date")
    @Expose
    var releaseDate: String = "",

    @SerializedName("genres")
    @Expose
    val genres: ArrayList<Genres> = arrayListOf()) {

    companion object {
        fun empty() = MovieDetailsResponse(0, 0, "", "", "","", arrayListOf())
    }
}

data class Genres(

    @SerializedName("id")
    @Expose
    var id: Int = 0,

    @SerializedName("name")
    @Expose
    var name: String = "")