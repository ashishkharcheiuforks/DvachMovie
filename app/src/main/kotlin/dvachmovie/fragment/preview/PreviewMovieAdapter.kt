package dvachmovie.fragment.preview

import android.databinding.DataBindingUtil
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import dvachmovie.R
import dvachmovie.databinding.ItemPreviewMoviesBinding
import dvachmovie.repository.local.Movie

class PreviewMovieAdapter :
        ListAdapter<Movie, PreviewMovieAdapter.ViewHolder>
        (PreviewMovieDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_preview_movies, parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { movie ->
            with(holder) {
                itemView.tag = movie
                bind(movie)
            }

        }
    }

    class ViewHolder(
            private val binding: ItemPreviewMoviesBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            with(binding) {
                viewModel = PreviewItemViewModel(movie)
                executePendingBindings()
            }
        }
    }
}