package com.example.moviesapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.data.model.MovieDetails
import com.example.moviesapp.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>> = _popularMovies

    private val _nowPlayingMovies = MutableLiveData<List<Movie>>()
    val nowPlayingMovies: LiveData<List<Movie>> = _nowPlayingMovies

    private val _upcomingMovies = MutableLiveData<List<Movie>>()
    val upcomingMovies: LiveData<List<Movie>> = _upcomingMovies

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchPopularMovies(page: Int = 1) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.getPopularMovies(page)
                if (response.isSuccessful) {
                    _popularMovies.value = response.body()?.movies
                } else {
                    _error.value = "Error fetching popular movies"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchNowPlayingMovies(page: Int = 1) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.getNowPlayingMovies(page)
                if (response.isSuccessful) {
                    _nowPlayingMovies.value = response.body()?.movies
                } else {
                    _error.value = "Error fetching now playing movies"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchUpcomingMovies(page: Int = 1) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.getUpcomingMovies(page)
                if (response.isSuccessful) {
                    _upcomingMovies.value = response.body()?.movies
                } else {
                    _error.value = "Error fetching upcoming movies"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.getMovieDetails(movieId)
                if (response.isSuccessful) {
                    _movieDetails.value = response.body()
                } else {
                    _error.value = "Error fetching movie details"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchMovies(query: String, page: Int = 1) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.searchMovies(query, page)
                if (response.isSuccessful) {
                    _searchResults.value = response.body()?.movies
                } else {
                    _error.value = "Error searching movies"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 