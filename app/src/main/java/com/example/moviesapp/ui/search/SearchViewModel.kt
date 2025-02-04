package com.example.moviesapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> = _searchResults

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadInitialMovies()
    }

    private fun loadInitialMovies() {
        viewModelScope.launch {
            try {
                val response = repository.getPopularMovies(1)
                if (response.isSuccessful) {
                    _searchResults.value = response.body()?.movies ?: emptyList()
                } else {
                    _error.value = "Error loading movies"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            }
        }
    }

    fun searchMovies(query: String, page: Int = 1) {
        viewModelScope.launch {
            try {
                if (query.isBlank()) {
                    loadInitialMovies()
                    return@launch
                }
                val response = repository.searchMovies(query, page)
                if (response.isSuccessful) {
                    _searchResults.value = response.body()?.movies ?: emptyList()
                } else {
                    _error.value = "Error searching movies"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            }
        }
    }

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                repository.getMovieDetails(movieId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
} 