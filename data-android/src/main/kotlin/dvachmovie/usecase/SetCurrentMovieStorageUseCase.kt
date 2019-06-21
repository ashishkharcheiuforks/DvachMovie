package dvachmovie.usecase

import dvachmovie.db.data.Movie
import dvachmovie.storage.MovieStorage
import dvachmovie.usecase.base.UseCase
import javax.inject.Inject

open class SetCurrentMovieStorageUseCase @Inject constructor(
        private val movieStorage: MovieStorage) : UseCase<Movie, Unit>() {

    override fun execute(input: Movie) {
        movieStorage.setCurrentMovieAndUpdate(input)
    }
}
