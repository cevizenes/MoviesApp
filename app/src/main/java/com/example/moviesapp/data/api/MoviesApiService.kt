package com.example.moviesapp.data.api

import com.example.moviesapp.data.model.MovieResponse
import com.example.moviesapp.data.model.MovieDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApiService {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val API_KEY = "YOUR_API_KEY"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
    }

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MovieDetails>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): Response<MovieResponse>
} 