package com.android.intdv.movie.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieListResponse(

    @SerializedName("page")
    @Expose
    var page: Int = 0,

    @SerializedName("total_pages")
    @Expose
    var totalPages: Int = 0,

    @SerializedName("total_results")
    @Expose
    var totalResults: Int = 0,

    @SerializedName("results")
    @Expose
    val movieList: ArrayList<MovieDetails> = arrayListOf()
) {
    companion object {
        fun empty() = MovieListResponse(0, 0, 0, arrayListOf())
    }
}

data class MovieDetails(

    @SerializedName("id")
    @Expose
    var id: Long = 0,

    @SerializedName("poster_path")
    @Expose
    var posterPath: String = "",

    @SerializedName("original_title")
    @Expose
    var title: String = "",

    @SerializedName("overview")
    @Expose
    var overview: String = "",

    @SerializedName("vote_average")
    @Expose
    var voteAverage: Float = 0.0f,

    @SerializedName("release_date")
    @Expose
    var releaseDate: String = "") {

    val rating: Float get() {return (voteAverage*10)}

    var runtime = "0h 0m"
}