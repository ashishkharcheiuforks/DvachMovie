package dvachmovie.di.core

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dvachmovie.di.ViewModelFactory
import dvachmovie.di.ViewModelKey
import dvachmovie.fragment.movie.MovieViewModel

@Module
abstract class ViewModuleFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MovieViewModel::class)
    internal abstract fun postListViewModel(viewModel: MovieViewModel): ViewModel

}