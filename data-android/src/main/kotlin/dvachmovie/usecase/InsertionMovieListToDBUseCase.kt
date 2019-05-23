package dvachmovie.usecase

import dvachmovie.db.data.Movie
import dvachmovie.repository.MovieDBRepository
import dvachmovie.usecase.base.UseCase
import javax.inject.Inject

class InsertionMovieListToDBUseCase @Inject constructor(
        private val movieDBRepository: MovieDBRepository) : UseCase<List<Movie>, Unit>() {

    override suspend fun execute(input: List<Movie>) {
        movieDBRepository.insertAll(input)
    }
}