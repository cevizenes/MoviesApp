package com.example.moviesapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.domain.usecase.GetPopularMoviesUseCase
import com.example.moviesapp.domain.usecase.GetNowPlayingMoviesUseCase
import com.example.moviesapp.domain.usecase.GetUpcomingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase
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
        viewModelScope.launch {
            getPopularMoviesUseCase(page)
                .onSuccess { movies ->
                    popularMoviesCache[page] = movies
                    _popularMovies.value = movies
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
        }
    }

    fun fetchNowPlayingMovies(page: Int) {
        viewModelScope.launch {
            getNowPlayingMoviesUseCase(page)
                .onSuccess { movies ->
                    nowPlayingMoviesCache[page] = movies
                    _nowPlayingMovies.value = movies
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
        }
    }

    fun fetchUpcomingMovies(page: Int) {
        viewModelScope.launch {
            getUpcomingMoviesUseCase(page)
                .onSuccess { movies ->
                    upcomingMoviesCache[page] = movies
                    _upcomingMovies.value = movies
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
        }
    }
}
