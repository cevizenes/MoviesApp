package com.example.moviesapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.domain.usecase.GetPopularMoviesUseCase
import com.example.moviesapp.domain.usecase.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val searchCache = mutableMapOf<Pair<String, Int>, List<Movie>>()

    init {
        loadInitialMovies()
    }

    private fun loadInitialMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            getPopularMoviesUseCase(1)
                .onSuccess { movies ->
                    _searchResults.value = movies
                    _error.value = null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                    _searchResults.value = emptyList()
                }
            _isLoading.value = false
        }
    }

    fun searchMovies(query: String, page: Int = 1) {
        viewModelScope.launch {
            _isLoading.value = true
            if (query.isBlank()) {
                loadInitialMovies()
                return@launch
            }

            searchMoviesUseCase(query, page)
                .onSuccess { movies ->
                    searchCache[Pair(query, page)] = movies
                    _searchResults.value = movies
                    _error.value = null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                    if (page == 1) {
                        _searchResults.value = emptyList()
                    }
                }
            _isLoading.value = false
        }
    }

    fun clearSearch() {
        loadInitialMovies()
        searchCache.clear()
    }
}