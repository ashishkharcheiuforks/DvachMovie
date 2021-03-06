package dvachmovie.usecase.settingsstorage

import dvachmovie.storage.SettingsStorage
import dvachmovie.usecase.base.UseCase
import javax.inject.Inject

open class GetIsAllowGestureUseCase @Inject constructor(
        private val settingsStorage: SettingsStorage) : UseCase<Unit, Boolean>() {

    override suspend fun executeAsync(input: Unit): Boolean =
            settingsStorage.isAllowGestureAsync().await()

    override fun execute(input: Unit): Boolean =
            settingsStorage.isAllowGesture()
}
