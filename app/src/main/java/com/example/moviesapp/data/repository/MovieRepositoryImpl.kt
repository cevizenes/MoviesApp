package com.example.moviesapp.data.repository

import com.example.moviesapp.data.api.MoviesApiService
import com.example.moviesapp.data.model.MovieDetails
import com.example.moviesapp.data.model.MovieResponse
import com.example.moviesapp.domain.repository.MovieRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val api: MoviesApiService
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): Response<MovieResponse> {
        return api.getPopularMovies(page = page)
    }

    override suspend fun getNowPlayingMovies(page: Int): Response<MovieResponse> {
        return api.getNowPlayingMovies(page = page)
    }

    override suspend fun getUpcomingMovies(page: Int): Response<MovieResponse> {
        return api.getUpcomingMovies(page = page)
    }

    override suspend fun getMovieDetails(movieId: Int): Response<MovieDetails> {
        return api.getMovieDetails(movieId = movieId)
    }

    override suspend fun searchMovies(query: String, page: Int): Response<MovieResponse> {
        return api.searchMovies(query = query, page = page)
    }
} 