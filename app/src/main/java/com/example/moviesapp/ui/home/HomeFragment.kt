package com.example.moviesapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.R
import com.example.moviesapp.databinding.FragmentHomeBinding
import com.example.moviesapp.ui.adapter.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: HomeViewModel by viewModels()
    private var popularPage = 1
    private var categoryPage = 1
    private var isLoadingPopular = false
    private var isLoadingCategory = false

    private val popularMoviesAdapter by lazy {
        MoviesAdapter(
            onMovieClick = { movie ->
                viewModel.fetchMovieDetails(movie.id)
            },
            onLastItemReached = {
                if (!isLoadingPopular) {
                    popularPage++
                    loadPopularMovies()
                }
            },
            isHorizontal = true
        )
    }

    private val categoryMoviesAdapter by lazy {
        MoviesAdapter(
            onMovieClick = { movie ->
                viewModel.fetchMovieDetails(movie.id)
            },
            onLastItemReached = {
                if (!isLoadingCategory) {
                    categoryPage++
                    when (currentCategory) {
                        Category.NOW_PLAYING -> loadNowPlayingMovies()
                        Category.UPCOMING -> loadUpcomingMovies()
                    }
                }
            },
            isHorizontal = false
        )
    }

    private enum class Category {
        NOW_PLAYING,
        UPCOMING
    }

    private var currentCategory = Category.NOW_PLAYING

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        setupObservers()
        setupClickListeners()
        updateCategoryButtonsUI()
        loadInitialData()
    }

    private fun setupRecyclerViews() {
        binding.homeMoviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMoviesAdapter
        }

        binding.categoryMoviesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = categoryMoviesAdapter
        }
    }

    private fun setupObservers() {
        viewModel.popularMovies.observe(viewLifecycleOwner) { movies ->
            movies?.let {
                val currentList = popularMoviesAdapter.currentList.toMutableList()
                currentList.addAll(it)
                popularMoviesAdapter.submitList(currentList)
                isLoadingPopular = false
            }
        }

        viewModel.nowPlayingMovies.observe(viewLifecycleOwner) { movies ->
            if (currentCategory == Category.NOW_PLAYING) {
                movies?.let {
                    val currentList = categoryMoviesAdapter.currentList.toMutableList()
                    currentList.addAll(it)
                    categoryMoviesAdapter.submitList(currentList)
                }
            }
            isLoadingCategory = false
        }

        viewModel.upcomingMovies.observe(viewLifecycleOwner) { movies ->
            if (currentCategory == Category.UPCOMING) {
                movies?.let {
                    val currentList = categoryMoviesAdapter.currentList.toMutableList()
                    currentList.addAll(it)
                    categoryMoviesAdapter.submitList(currentList)
                }
            }
            isLoadingCategory = false
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                isLoadingPopular = false
                isLoadingCategory = false
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnNowPlaying.setOnClickListener {
            if (currentCategory != Category.NOW_PLAYING) {
                currentCategory = Category.NOW_PLAYING
                resetCategoryAndLoad { loadNowPlayingMovies() }
            }
        }

        binding.btnUpcoming.setOnClickListener {
            if (currentCategory != Category.UPCOMING) {
                currentCategory = Category.UPCOMING
                resetCategoryAndLoad { loadUpcomingMovies() }
            }
        }
    }

    private fun resetCategoryAndLoad(loadFunction: () -> Unit) {
        categoryPage = 1
        categoryMoviesAdapter.submitList(emptyList())
        updateCategoryButtonsUI()
        loadFunction()
    }

    private fun updateCategoryButtonsUI() {
        binding.btnNowPlaying.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (currentCategory == Category.NOW_PLAYING) R.color.selected_text else R.color.white
            )
        )
        binding.btnUpcoming.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (currentCategory == Category.UPCOMING) R.color.selected_text else R.color.white
            )
        )
    }

    private fun loadInitialData() {
        loadPopularMovies()
        loadNowPlayingMovies()
    }

    private fun loadPopularMovies() {
        if (!isLoadingPopular) {
            isLoadingPopular = true
            viewModel.fetchPopularMovies(popularPage)
        }
    }

    private fun loadNowPlayingMovies() {
        if (!isLoadingCategory) {
            isLoadingCategory = true
            viewModel.fetchNowPlayingMovies(categoryPage)
        }
    }

    private fun loadUpcomingMovies() {
        if (!isLoadingCategory) {
            isLoadingCategory = true
            viewModel.fetchUpcomingMovies(categoryPage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}