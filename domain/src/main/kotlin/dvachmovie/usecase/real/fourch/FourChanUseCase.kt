package dvachmovie.usecase.real.fourch

import dvachmovie.AppConfig
import dvachmovie.api.FileItem
import dvachmovie.architecture.ScopeProvider
import dvachmovie.db.data.Movie
import dvachmovie.db.data.Thread
import dvachmovie.usecase.base.ExecutorResult
import dvachmovie.usecase.base.UseCase
import dvachmovie.usecase.real.DvachAmountRequestsUseCaseModel
import dvachmovie.usecase.real.DvachCountRequestUseCaseModel
import dvachmovie.usecase.real.DvachUseCaseModel
import dvachmovie.usecase.real.dvach.DvachUseCase
import dvachmovie.utils.MovieConverter
import dvachmovie.utils.MovieUtils
import dvachmovie.utils.ThreadConverter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class FourChanUseCase @Inject constructor(private val getThreadUseCase: GetThreadsFromFourchUseCase,
                                               private val getLinkFilesFromThreadsUseCase:
                                               GetLinkFilesFromThreadsFourchUseCase,
                                               private val movieUtils: MovieUtils,
                                               private val movieConverter: MovieConverter,
                                               private val scopeProvider: ScopeProvider,
                                               private val threadConverter: ThreadConverter) :
        UseCase<DvachUseCase.Params, Unit>() {

    private lateinit var board: String
    private lateinit var executorResult: ExecutorResult

    private var count = 0

    private val movies = mutableListOf<Movie>()
    private val threads = mutableListOf<Thread>()

    private lateinit var networkJob: Job
    private var returnJob: Job? = null

    suspend fun forceStart() {
        returnJob?.cancel()
        networkJob.cancel()

        if (movies.isNotEmpty()) {
            returnMovies()
        } else {
            executorResult.onFailure(RuntimeException("Current request is not containing movies"))
        }
    }

    override suspend fun executeAsync(input: DvachUseCase.Params) {
        returnJob?.cancel()
        var listThreadSize: Int
        board = input.board
        executorResult = input.executorResult!!
        val inputModel = GetThreadsFromFourchUseCase.Params(input.board)

        networkJob = scopeProvider.ioScope.async(Job()) {
            try {
                val useCaseModel = getThreadUseCase.executeAsync(inputModel)
                listThreadSize = useCaseModel.listThreads.size

                executorResult.onSuccess(DvachAmountRequestsUseCaseModel(listThreadSize))

                var isCancellationException = false
                withContext(Dispatchers.IO) {
                    for (threadParam in useCaseModel.listThreads) {
                        if (!isCancellationException) {
                            launch {
                                try {
                                    val list = executeLinkFilesUseCase(threadParam.first.toString(),
                                            threadParam.second)
                                    val webmItems =
                                            movieUtils.filterFileItemOnlyAsMovie(list)

                                    movies.addAll(movieConverter.convertFileItemToMovie(
                                            webmItems, board, AppConfig.FOURCHAN_URL))
                                    threads.addAll(threadConverter.convertFileItemToThread(
                                            webmItems, AppConfig.FOURCHAN_URL))
                                } catch (e: Exception) {
                                    if (e is CancellationException) {
                                        isCancellationException = true
                                    } else {
                                        executorResult.onFailure(e)
                                    }
                                }
                            }
                        }
                    }
                }

                if (networkJob.isActive) {
                    returnMovies()
                }
            } catch (e: Exception) {
                executorResult.onFailure(e)
            }
        }
        networkJob.join()
    }

    private suspend fun executeLinkFilesUseCase(num: String, nameThread: String): List<FileItem> {
        return try {
            val inputModel = GetLinkFilesFromThreadsFourchUseCase.Params(board, num, nameThread)
            val useCaseLinkFilesModel = getLinkFilesFromThreadsUseCase
                    .executeAsync(inputModel)
            useCaseLinkFilesModel.fileItems
        } catch (e: Exception) {
            throw e
        } finally {
            count++
            executorResult.onSuccess(DvachCountRequestUseCaseModel(count))
        }
    }

    private suspend fun returnMovies() {
        returnJob = scopeProvider.ioScope.launch(Job()) {
            try {
                if (movies.isEmpty()) {
                    executorResult.onFailure(RuntimeException("This is a private board or internet problem"))
                } else {
                    executorResult.onSuccess(DvachUseCaseModel(movies, threads))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                executorResult.onFailure(e)
            }
        }
        returnJob!!.join()
    }
}
