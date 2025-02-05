package com.example.moviesapp.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.databinding.FragmentSearchBinding
import com.example.moviesapp.ui.adapter.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: SearchViewModel by viewModels()
    private var searchPage = 1
    private var isLoadingSearch = false
    private var searchJob: Job? = null

    private val searchMoviesAdapter by lazy {
        MoviesAdapter(
            onMovieClick = { movie ->
                findNavController().navigate(
                    SearchFragmentDirections.actionNavigationSearchToMovieDetailFragment(movie.id)
                )
            },
            onLastItemReached = {
                if (!isLoadingSearch) {
                    searchPage++
                    val query = binding.searchEditText.text.toString()
                    performSearch(query)
                }
            },
            isHorizontal = false,
            isSearch = true
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchBar()
        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchMoviesAdapter
            visibility = View.VISIBLE
        }
    }

    private fun setupSearchBar() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchJob?.cancel()
                searchJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(500)
                    s?.toString()?.let { query ->
                        searchPage = 1
                        performSearch(query)
                    }
                }
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.searchEditText.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = textView.text.toString()
                searchPage = 1
                performSearch(query)
                true
            } else {
                false
            }
        }
    }

    private fun setupObservers() {
        viewModel.searchResults.observe(viewLifecycleOwner) { movies ->
            movies?.let {
                if (it.isEmpty()) {
                    binding.noResultsLayout.visibility = View.VISIBLE
                    binding.searchRecyclerView.visibility = View.GONE
                    searchMoviesAdapter.submitList(emptyList())
                } else {
                    binding.noResultsLayout.visibility = View.GONE
                    binding.searchRecyclerView.visibility = View.VISIBLE
                    
                    if (searchPage == 1) {
                        searchMoviesAdapter.submitList(it)
                    } else {
                        val currentList = searchMoviesAdapter.currentList.toMutableList()
                        currentList.addAll(it)
                        searchMoviesAdapter.submitList(currentList)
                    }
                }
                isLoadingSearch = false
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                isLoadingSearch = false
            }
        }
    }

    private fun performSearch(query: String) {
        if (!isLoadingSearch) {
            isLoadingSearch = true
            viewModel.searchMovies(query, searchPage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchJob?.cancel()
        _binding = null
    }
} 