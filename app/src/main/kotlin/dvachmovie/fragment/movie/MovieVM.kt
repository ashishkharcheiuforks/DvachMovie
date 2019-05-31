package dvachmovie.fragment.movie

import android.net.Uri
import android.view.View
import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dvachmovie.PresenterModel
import dvachmovie.db.data.Movie
import dvachmovie.pipe.CookieModel
import dvachmovie.pipe.ErrorModel
import dvachmovie.pipe.ReportModel
import dvachmovie.pipe.ReportPipe
import dvachmovie.pipe.ShuffledMoviesModel
import dvachmovie.pipe.settingsStorage.GetIsAllowGesturePipe
import dvachmovie.pipe.settingsStorage.GetIsListBtnVisiblePipe
import dvachmovie.pipe.settingsStorage.GetIsReportBtnVisiblePipe
import dvachmovie.pipe.utils.ShuffleMoviesPipe
import dvachmovie.usecase.moviestorage.GetCurrentMovieUseCase
import dvachmovie.usecase.moviestorage.GetMovieListUseCase
import dvachmovie.usecase.real.GetCookieUseCase
import dvachmovie.usecase.real.ReportUseCase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MovieVM @Inject constructor(
        private val broadcastChannel: BroadcastChannel<PresenterModel>,
        getCookieUseCase: GetCookieUseCase,
        getMovieListUseCase: GetMovieListUseCase,
        getCurrentMovieUseCase: GetCurrentMovieUseCase,
        private val getIsReportBtnVisiblePipe: GetIsReportBtnVisiblePipe,
        private val getIsListBtnVisiblePipe: GetIsListBtnVisiblePipe,
        private val getIsAllowGesturePipe: GetIsAllowGesturePipe,
        shuffleMoviesPipe: ShuffleMoviesPipe,
        reportPipe: ReportPipe) : ViewModel() {

    init {
        viewModelScope.launch {
            broadcastChannel.asFlow().collect {
                render(it)
            }
        }
        refreshVM()
    }

    fun refreshVM() {
        isReportBtnVisible.value = getIsReportBtnVisiblePipe.execute(Unit)

        isListBtnVisible.value = getIsListBtnVisiblePipe.execute(Unit)

        isGestureEnabled.value = getIsAllowGesturePipe.execute(Unit)
    }

    lateinit var downloadBtnClicked: () -> Unit
    lateinit var downloadTask: (download: String, cookie: String) -> Unit
    lateinit var routeToSettingsTask: () -> Unit
    lateinit var copyURLTask: (movieUrl: String) -> Unit
    lateinit var routeToPreviewTask: () -> Unit
    lateinit var showMessageTask: (message: String) -> Unit

    val onBtnDownloadClicked = View.OnClickListener { downloadBtnClicked() }
    val onBtnShuffleClicked = View.OnClickListener {
        viewModelScope.launch {
            shuffleMoviesPipe.execute(movieList.value ?: listOf())
        }
    }
    val onBtnSettingsClicked = View.OnClickListener { routeToSettingsTask() }

    val onBtnCopyURLClicked = View.OnClickListener {
        copyURLTask(currentMovie.value?.movieUrl ?: "")
    }
    val onBtnListVideosClicked = View.OnClickListener { routeToPreviewTask() }

    val onBtnReportClicked = View.OnClickListener {
        currentMovie.value?.let {
            ReportUseCase.Params(it.board, it.thread, it.post)
        }?.let { model ->
            viewModelScope.launch {
                reportPipe.execute(model)
            }
        }
    }

    val movieList by lazy { runBlocking { getMovieListUseCase.execute(Unit) } }
    val currentMovie by lazy { runBlocking { getCurrentMovieUseCase.execute(Unit) } }

    val currentPos: MutableLiveData<Pair<Int, Long>> by lazy {
        MutableLiveData<Pair<Int, Long>>(Pair(0, 0L))
    }

    val cookie: MutableLiveData<String> by lazy {
        runBlocking {
            MutableLiveData<String>(getCookieUseCase.execute(Unit).toString())
        }
    }

    val isPlayerControlVisibility = MutableLiveData<Boolean>(true)


    private val function = Function<List<Movie>, LiveData<List<Uri>>> { values ->
        val urlVideo: List<Uri> = values.map { value -> Uri.parse(value.movieUrl) }
        if (urlVideo.isNotEmpty()) {
            runBlocking {
                cookie.value = getCookieUseCase.execute(Unit).toString()
            }
        }
        MutableLiveData<List<Uri>>(urlVideo)
    }

    val uriMovies: MutableLiveData<List<Uri>> =
            Transformations.switchMap(movieList, function)
                    as MutableLiveData<List<Uri>>

    val isReportBtnVisible = MutableLiveData<Boolean>()

    val isListBtnVisible = MutableLiveData<Boolean>()

    val isGestureEnabled = MutableLiveData<Boolean>()

    private fun render(model: PresenterModel) {
        when (model) {
            is CookieModel -> {
                downloadTask(currentMovie.value?.movieUrl ?: "", model.cookie.toString())
            }

            is ShuffledMoviesModel -> movieList.postValue(model.movies)

            is ReportModel -> {
                showMessageTask("Report submitted!")
            }

            is ErrorModel -> {
                showMessageTask(model.throwable.message ?: "Something went wrong. Please try again")
            }
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}
