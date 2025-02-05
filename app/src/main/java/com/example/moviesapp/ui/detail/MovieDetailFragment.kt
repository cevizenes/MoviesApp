package com.example.moviesapp.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.data.api.MoviesApiService
import com.example.moviesapp.databinding.FragmentMovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel.fetchMovieDetails(args.movieId)
        
        viewModel.movieDetails.observe(viewLifecycleOwner) { movieDetails ->
            movieDetails?.let {
                binding.apply {
                    movieTitleTextView.text = it.title
                    movieOverviewTextView.text = it.overview
                    movieRatingTextView.text = String.format("%.1f", it.voteAverage)
                    movieReleaseDateTextView.text = it.releaseDate
                    movieRuntimeTextView.text = "${it.runtime} min"
                    movieGenresTextView.text = it.genres.joinToString(", ") { genre -> genre.name }
                    
                    it.backdropPath?.let { backdropPath ->
                        val imageUrl = "${MoviesApiService.IMAGE_BASE_URL}$backdropPath"
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(movieBackdropImageView)
                    }
                    
                    it.posterPath?.let { posterPath ->
                        val imageUrl = "${MoviesApiService.IMAGE_BASE_URL}$posterPath"
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(moviePosterImageView)
                    }
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

//        viewModel.error.observe(viewLifecycleOwner) { error ->
//            if (error != null) {
//                // Show error message
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 