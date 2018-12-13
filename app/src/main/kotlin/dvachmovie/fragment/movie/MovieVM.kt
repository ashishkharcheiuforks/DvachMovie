package dvachmovie.fragment.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dvachmovie.db.data.MovieEntity
import dvachmovie.repository.local.MovieRepository
import javax.inject.Inject

class MovieVM @Inject constructor(movieRepository: MovieRepository) : ViewModel() {

    private val uriMovies: MutableLiveData<MutableList<MovieEntity>> = movieRepository.getMovies()
    val currentPos: MutableLiveData<Int> = MutableLiveData()

    fun getUrlList() = uriMovies
}