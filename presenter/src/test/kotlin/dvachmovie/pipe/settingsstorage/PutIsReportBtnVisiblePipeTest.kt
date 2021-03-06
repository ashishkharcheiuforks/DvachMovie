package dvachmovie.pipe.settingsstorage

import dvachmovie.usecase.settingsstorage.PutIsReportBtnVisibleUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PutIsReportBtnVisiblePipeTest {


    @InjectMocks
    lateinit var pipe: PutIsReportBtnVisiblePipe

    @Mock
    lateinit var useCase: PutIsReportBtnVisibleUseCase

    @Test
    fun `Happy pass`() {
        runBlocking {
            pipe.execute(false)
        }
    }
}
