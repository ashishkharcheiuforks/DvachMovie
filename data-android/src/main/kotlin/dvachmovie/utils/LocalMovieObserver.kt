package dvachmovie.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import dvachmovie.db.data.Movie
import dvachmovie.repository.MovieDBRepository
import dvachmovie.storage.SettingsStorage
import dvachmovie.storage.local.MovieStorage
import javax.inject.Inject

class LocalMovieObserver @Inject constructor(
        private val movieStorage: MovieStorage,
        private val movieDBRepository: MovieDBRepository,
        private val settingsStorage: SettingsStorage,
        private val movieUtils: MovieUtils
) : MovieObserver{
    override suspend fun observeDB(lifecycleOwner: LifecycleOwner) {
        movieDBRepository.getMoviesFromBoard(settingsStorage.getBoard())
                .observe(lifecycleOwner, Observer { dbMovies ->
                    val movieList = movieStorage.movieList.value ?: listOf()
                    val diffList = movieUtils.calculateDiff(movieList,
                            dbMovies)

                    if (diffList.isNotEmpty()) {
                        movieStorage.movieList.value = diffList + movieList
                    }
                })
    }

    override suspend fun observeDB(lifecycleOwner: LifecycleOwner,
                                   observer: Observer<List<Movie>>) {
        movieDBRepository
                .getMoviesFromBoard(settingsStorage.getBoard())
                .observe(lifecycleOwner, observer)
    }
}