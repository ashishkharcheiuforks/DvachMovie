package dvachmovie.usecase.real.neochan

import dvachmovie.TestException
import dvachmovie.architecture.ScopeProvider
import dvachmovie.db.data.NullMovie
import dvachmovie.usecase.base.ExecutorResult
import dvachmovie.usecase.base.UseCaseModel
import dvachmovie.usecase.real.DvachAmountRequestsUseCaseModel
import dvachmovie.usecase.real.DvachUseCaseModel
import dvachmovie.usecase.real.GetThreadsFromNeoChanUseCaseModel
import dvachmovie.usecase.real.dvach.DvachUseCase
import dvachmovie.utils.MovieConverter
import dvachmovie.utils.MovieUtils
import dvachmovie.utils.ThreadConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class NeoChanUseCaseTest {
    @InjectMocks
    lateinit var neoChanUseCase: NeoChanUseCase

    @Mock
    lateinit var getThreadsFromNeoChanUseCase: GetThreadsFromNeoChanUseCase

    @Mock
    lateinit var getLinkFilesFromThreadsNeoChanUseCase: GetLinkFilesFromThreadsNeoChanUseCase

    @Mock
    lateinit var movieUtils: MovieUtils

    @Mock
    lateinit var movieConverter: MovieConverter

    @Mock
    lateinit var threadConverter: ThreadConverter

    @Mock
    lateinit var scopeProvider: ScopeProvider

    private val board = "testBoard"

    private val numOne = "One"
    private val numTwo = "Two"
    private val model = GetThreadsFromNeoChanUseCaseModel(listOf(Pair(1, numOne), Pair(2, numTwo)))

    private val testException = TestException()

    private val movieEntityOne = NullMovie("one")
    private val movieEntityTwo = NullMovie("two")

    private val resultHappyModel = DvachUseCaseModel(listOf(movieEntityOne, movieEntityTwo), listOf())
    private val resultPartOfModel = DvachUseCaseModel(listOf(movieEntityOne), listOf())

    private val threadModel = GetThreadsFromNeoChanUseCase.Params(board)

    private var happyExecutorResult = object : ExecutorResult {
        override suspend fun onSuccess(useCaseModel: UseCaseModel) {
            when (useCaseModel) {
                is DvachAmountRequestsUseCaseModel ->
                    Assert.assertEquals(2, useCaseModel.max)
                is DvachUseCaseModel -> Assert.assertEquals(resultHappyModel, useCaseModel)
            }
        }

        override suspend fun onFailure(t: Throwable) {
            Assert.assertEquals(testException, t)
        }
    }

    private var partOfSuccessfulyExecutorResult = object : ExecutorResult {
        override suspend fun onSuccess(useCaseModel: UseCaseModel) {
            when (useCaseModel) {
                is DvachAmountRequestsUseCaseModel ->
                    Assert.assertEquals(2, useCaseModel.max)
                is DvachUseCaseModel -> Assert.assertEquals(resultPartOfModel, useCaseModel)
            }
        }

        override suspend fun onFailure(t: Throwable) {
            Assert.assertEquals(testException, t)
        }
    }

    private var zeroExecutorResult = object : ExecutorResult {
        override suspend fun onSuccess(useCaseModel: UseCaseModel) {
            when (useCaseModel) {
                is DvachAmountRequestsUseCaseModel ->
                    Assert.assertEquals(2, useCaseModel.max)
                is DvachUseCaseModel -> Assert.assertEquals(null, useCaseModel)
            }
        }

        override suspend fun onFailure(t: Throwable) {
            Assert.assertEquals("This is a private board or internet problem", t.message)
        }
    }

    @Before
    fun `Set up`() {
        given(scopeProvider.ioScope).willReturn(CoroutineScope(Dispatchers.Unconfined))
    }

    @Test
    fun `Happy pass`() {
        runBlocking {
            given(getThreadsFromNeoChanUseCase.executeAsync(threadModel)).willReturn(model)

            val dvachInputModel = DvachUseCase.Params(board, happyExecutorResult)
            neoChanUseCase.executeAsync(dvachInputModel)
        }
    }

    @Test
    fun `Error emits error in the GetThreadUseCase`() {
        runBlocking {
            given(getThreadsFromNeoChanUseCase.executeAsync(threadModel)).willThrow(testException)

            val dvachInputModel = DvachUseCase.Params(board, happyExecutorResult)
            neoChanUseCase.executeAsync(dvachInputModel)
        }
    }

    @Test
    fun `Error emits error but return a part of successful result`() {
        runBlocking {
            given(getThreadsFromNeoChanUseCase.executeAsync(threadModel)).willReturn(model)

            val dvachInputModel = DvachUseCase.Params(board, partOfSuccessfulyExecutorResult)
            neoChanUseCase.executeAsync(dvachInputModel)
        }
    }

    @Test
    fun `Return zero movies`() {
        runBlocking {
            given(getThreadsFromNeoChanUseCase.executeAsync(threadModel)).willReturn(model)

            val dvachInputModel = DvachUseCase.Params(board, zeroExecutorResult)
            neoChanUseCase.executeAsync(dvachInputModel)
        }
    }
}