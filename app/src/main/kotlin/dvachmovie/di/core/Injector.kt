package dvachmovie.di.core

import dvachmovie.di.RoomModule

object Injector {

    private lateinit var component: ApplicationComponent

    fun prepare(application: MainApplication) {
        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(application))
                .roomModule(RoomModule(application))
                .build()
    }

    fun applicationComponent() = component
    fun navigationComponent() = component.navigationComponent()
    fun viewComponent() = component.viewComponent()

}