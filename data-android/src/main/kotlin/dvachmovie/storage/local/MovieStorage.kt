package dvachmovie.storage.local

import androidx.lifecycle.MutableLiveData
import dvachmovie.db.data.Movie

data class MovieStorage(
        val movieList: MutableLiveData<List<Movie>> = MutableLiveData(),
        val currentMovie: MutableLiveData<Movie> = MutableLiveData())