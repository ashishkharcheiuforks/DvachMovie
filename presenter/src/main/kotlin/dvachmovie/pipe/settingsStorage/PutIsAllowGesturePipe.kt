package dvachmovie.pipe.settingsStorage

import dvachmovie.architecture.ScopeProvider
import dvachmovie.pipe.Pipe
import dvachmovie.usecase.settingsStorage.PutIsAllowGestureUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class PutIsAllowGesturePipe @Inject constructor(
        private val useCase: PutIsAllowGestureUseCase,
        private val scopeProvider: ScopeProvider) : Pipe<Boolean>() {

    override fun execute(input: Boolean) {
        scopeProvider.ioScope.launch {
            useCase.execute(input)
        }
    }
}
