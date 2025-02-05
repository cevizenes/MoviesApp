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

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val popularMoviesCache = mutableMapOf<Int, List<Movie>>()
    private val nowPlayingMoviesCache = mutableMapOf<Int, List<Movie>>()
    private val upcomingMoviesCache = mutableMapOf<Int, List<Movie>>()

    fun fetchPopularMovies(page: Int) {
        if (popularMoviesCache.containsKey(page)) {
            _popularMovies.value = popularMoviesCache[page]
            return
        }

        viewModelScope.launch {
            try {
                val response = repository.getPopularMovies(page)
                if (response.isSuccessful) {
                    response.body()?.movies?.let { movies ->
                        popularMoviesCache[page] = movies
                        _popularMovies.value = movies
                    }
                } else {
                    _error.value = "Error fetching popular movies"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            }
        }
    }

    fun fetchNowPlayingMovies(page: Int) {
        if (nowPlayingMoviesCache.containsKey(page)) {
            _nowPlayingMovies.value = nowPlayingMoviesCache[page]
            return
        }

        viewModelScope.launch {
            try {
                val response = repository.getNowPlayingMovies(page)
                if (response.isSuccessful) {
                    response.body()?.movies?.let { movies ->
                        nowPlayingMoviesCache[page] = movies
                        _nowPlayingMovies.value = movies
                    }
                } else {
                    _error.value = "Error fetching now playing movies"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            }
        }
    }

    fun fetchUpcomingMovies(page: Int) {
        if (upcomingMoviesCache.containsKey(page)) {
            _upcomingMovies.value = upcomingMoviesCache[page]
            return
        }

        viewModelScope.launch {
            try {
                val response = repository.getUpcomingMovies(page)
                if (response.isSuccessful) {
                    response.body()?.movies?.let { movies ->
                        upcomingMoviesCache[page] = movies
                        _upcomingMovies.value = movies
                    }
                } else {
                    _error.value = "Error fetching upcoming movies"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            }
        }
    }
}
