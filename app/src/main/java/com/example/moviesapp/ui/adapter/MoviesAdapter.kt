package com.example.moviesapp.ui.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.moviesapp.R
import com.example.moviesapp.data.api.MoviesApiService
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.databinding.ItemMovieGridBinding
import com.example.moviesapp.databinding.ItemMovieHorizontalBinding
import com.example.moviesapp.databinding.ItemMovieSearchBinding

class MoviesAdapter(
    private val onMovieClick: (Movie) -> Unit,
    private val onLastItemReached: () -> Unit,
    private val isHorizontal: Boolean = false,
    private val isSearch: Boolean = false
) : ListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when {
            isSearch -> {
                val binding = ItemMovieSearchBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SearchMovieViewHolder(binding)
            }
            isHorizontal -> {
                val binding = ItemMovieHorizontalBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HorizontalMovieViewHolder(binding)
            }
            else -> {
                val binding = ItemMovieGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GridMovieViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = getItem(position)
        when (holder) {
            is SearchMovieViewHolder -> holder.bind(movie)
            is HorizontalMovieViewHolder -> holder.bind(movie)
            is GridMovieViewHolder -> holder.bind(movie)
        }
        
        if (position == itemCount - 1) {
            onLastItemReached()
        }
    }

    inner class SearchMovieViewHolder(
        private val binding: ItemMovieSearchBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onMovieClick(getItem(position))
                }
            }
        }

        @SuppressLint("DefaultLocale")
        fun bind(movie: Movie) {
            binding.apply {
                movieTitleTextView.text = movie.title
                movieYearTextView.text = movie.releaseDate?.take(4) ?: ""
                movieRatingTextView.text = String.format("%.1f", movie.voteAverage)
                movieOverviewTextView.text = movie.overview
                posterProgressBar.visibility = View.VISIBLE

                movie.posterPath?.let { posterPath ->
                    val imageUrl = "${MoviesApiService.IMAGE_BASE_URL}$posterPath"
                    
                    Glide.with(root.context)
                        .load(imageUrl)
                        .error(R.drawable.ic_launcher_foreground)
                        .listener(object : RequestListener<Drawable> {
                            override fun onResourceReady(
                                resource: Drawable,
                                model: Any,
                                target: Target<Drawable>?,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                posterProgressBar.visibility = View.GONE
                                return false
                            }

                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>,
                                isFirstResource: Boolean
                            ): Boolean {
                                posterProgressBar.visibility = View.GONE
                                return false
                            }
                        })
                        .into(moviePosterImageView)
                } ?: run {
                    posterProgressBar.visibility = View.GONE
                    moviePosterImageView.setImageResource(R.drawable.ic_launcher_foreground)
                }
            }
        }
    }

    inner class HorizontalMovieViewHolder(
        private val binding: ItemMovieHorizontalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onMovieClick(getItem(position))
                }
            }
        }

        fun bind(movie: Movie) {
            binding.posterProgressBar.visibility = View.VISIBLE

            movie.posterPath?.let { posterPath ->
                val imageUrl = "${MoviesApiService.IMAGE_BASE_URL}$posterPath"
                
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .error(R.drawable.ic_launcher_foreground)
                    .listener(object : RequestListener<Drawable> {
                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.posterProgressBar.visibility = View.GONE
                            return false
                        }

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.posterProgressBar.visibility = View.GONE
                            return false
                        }
                    })
                    .into(binding.moviePosterImageView)
            } ?: run {
                binding.posterProgressBar.visibility = View.GONE
                binding.moviePosterImageView.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }
    }

    inner class GridMovieViewHolder(
        private val binding: ItemMovieGridBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onMovieClick(getItem(position))
                }
            }
        }

        fun bind(movie: Movie) {
            binding.posterProgressBar.visibility = View.VISIBLE

            movie.posterPath?.let { posterPath ->
                val imageUrl = "${MoviesApiService.IMAGE_BASE_URL}$posterPath"
                
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .error(R.drawable.ic_launcher_foreground)
                    .listener(object : RequestListener<Drawable> {
                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.posterProgressBar.visibility = View.GONE
                            return false
                        }

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.posterProgressBar.visibility = View.GONE
                            return false
                        }
                    })
                    .into(binding.moviePosterImageView)
            } ?: run {
                binding.posterProgressBar.visibility = View.GONE
                binding.moviePosterImageView.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }
    }
}

private class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
} 