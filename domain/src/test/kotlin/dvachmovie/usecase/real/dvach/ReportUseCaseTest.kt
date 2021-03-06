package dvachmovie.usecase.real.dvach

import dvachmovie.TestException
import dvachmovie.repository.DvachRepository
import dvachmovie.usecase.base.ExecutorResult
import dvachmovie.usecase.base.UseCaseModel
import dvachmovie.usecase.real.DvachReportUseCaseModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class ReportUseCaseTest {

    @InjectMocks
    private lateinit var useCase: ReportUseCase

    @Mock
    private lateinit var dvachRepository: DvachRepository

    private val testException = TestException()

    private val executorResult = object : ExecutorResult {
        override suspend fun onSuccess(useCaseModel: UseCaseModel) {
            Assert.assertEquals("Test", (useCaseModel as DvachReportUseCaseModel).message)
        }

        override suspend fun onFailure(t: Throwable) {
            Assert.assertEquals(testException, t)
        }
    }

    private val model = ReportUseCase.Params("test", 0, 0, executorResult)

    @Test
    fun `Happy pass`() {
        runBlocking {
            given(dvachRepository
                    .reportPost("test", 0, 0, "Adult content"))
                    .willReturn("Test")
            useCase.executeAsync(model)
        }
    }

    @Test
    fun `Error send to callback`() {
        runBlocking {
            given(dvachRepository
                    .reportPost("test", 0, 0, "Adult content"))
                    .willThrow(testException)
            useCase.executeAsync(model)
        }
    }
}
