package com.example.moviesapp.data.repository

import com.example.moviesapp.data.api.MoviesApiService
import com.example.moviesapp.data.model.MovieResponse
import com.example.moviesapp.data.model.MovieDetails
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val api: MoviesApiService
) {
    suspend fun getPopularMovies(page: Int = 1): Response<MovieResponse> {
        return api.getPopularMovies(page = page)
    }

    suspend fun getNowPlayingMovies(page: Int = 1): Response<MovieResponse> {
        return api.getNowPlayingMovies(page = page)
    }

    suspend fun getUpcomingMovies(page: Int = 1): Response<MovieResponse> {
        return api.getUpcomingMovies(page = page)
    }

    suspend fun getMovieDetails(movieId: Int): Response<MovieDetails> {
        return api.getMovieDetails(movieId = movieId)
    }

    suspend fun searchMovies(query: String, page: Int = 1): Response<MovieResponse> {
        return api.searchMovies(query = query, page = page)
    }
} 