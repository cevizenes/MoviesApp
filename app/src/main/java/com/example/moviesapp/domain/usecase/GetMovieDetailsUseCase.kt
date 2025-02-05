package com.example.moviesapp.domain.usecase

import com.example.moviesapp.data.model.MovieDetails
import com.example.moviesapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Result<MovieDetails> {
        return try {
            val response = repository.getMovieDetails(movieId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Movie details not found"))
            } else {
                Result.failure(Exception("Error fetching movie details"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 