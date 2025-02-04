package com.example.moviesapp.data.model

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    val runtime: Int?,
    val status: String,
    val tagline: String?,
    @SerializedName("original_language")
    val originalLanguage: String,
    val budget: Long,
    val revenue: Long,
    val genres: List<Genre>
)

data class Genre(
    val id: Int,
    val name: String
) 