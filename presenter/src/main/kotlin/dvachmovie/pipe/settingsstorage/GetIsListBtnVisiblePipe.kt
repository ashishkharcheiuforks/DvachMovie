package dvachmovie.pipe.settingsstorage

import dvachmovie.architecture.PipeSync
import dvachmovie.usecase.settingsstorage.GetIsListBtnVisibleUseCase
import javax.inject.Inject

class GetIsListBtnVisiblePipe @Inject constructor(
        private val useCase: GetIsListBtnVisibleUseCase) : PipeSync<Unit, Boolean>() {

    override fun execute(input: Unit): Boolean = useCase.execute(input)

}
