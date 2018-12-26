package dvachmovie.di.core

import dagger.Subcomponent
import dvachmovie.di.base.FragmentScope
import dvachmovie.fragment.back.BackFragment
import dvachmovie.fragment.movie.MovieFragment
import dvachmovie.fragment.preview.PreviewFragment
import dvachmovie.fragment.settings.SettingsFragment
import dvachmovie.fragment.start.StartFragment

@FragmentScope
@Subcomponent()
interface FragmentComponent {
    fun inject(movieFragment: MovieFragment)
    fun inject(previewFragment: PreviewFragment)
    fun inject(backFragment: BackFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(startFragment: StartFragment)
}