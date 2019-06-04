package dvachmovie.pipe.settingsstorage

import dvachmovie.architecture.PipeAsync
import dvachmovie.usecase.settingsstorage.PutIsReportBtnVisibleUseCase
import javax.inject.Inject

class PutIsReportBtnVisiblePipe @Inject constructor(
        private val useCase: PutIsReportBtnVisibleUseCase) : PipeAsync<Boolean>() {

    override suspend fun execute(input: Boolean) {
        useCase.executeAsync(input)
    }
}