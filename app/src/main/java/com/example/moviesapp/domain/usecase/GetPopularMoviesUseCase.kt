package com.example.moviesapp.domain.usecase

import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(page: Int): Result<List<Movie>> {
        return try {
            val response = repository.getPopularMovies(page)
            if (response.isSuccessful) {
                Result.success(response.body()?.movies ?: emptyList())
            } else {
                Result.failure(Exception("Error fetching popular movies"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 