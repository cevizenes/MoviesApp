package com.example.moviesapp.domain.repository

import com.example.moviesapp.data.model.MovieDetails
import com.example.moviesapp.data.model.MovieResponse
import retrofit2.Response

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Response<MovieResponse>
    suspend fun getNowPlayingMovies(page: Int): Response<MovieResponse>
    suspend fun getUpcomingMovies(page: Int): Response<MovieResponse>
    suspend fun getMovieDetails(movieId: Int): Response<MovieDetails>
    suspend fun searchMovies(query: String, page: Int): Response<MovieResponse>
}
