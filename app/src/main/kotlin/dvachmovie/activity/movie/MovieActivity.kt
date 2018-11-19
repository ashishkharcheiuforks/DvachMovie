package dvachmovie.activity.movie

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dvachmovie.R
import dvachmovie.databinding.ActivityMovieBinding
import dvachmovie.di.core.Injector
import dvachmovie.fragment.movie.MovieFragment

class MovieActivity : AppCompatActivity() {
    private lateinit var viewModel: MovieActivityViewModel
    private lateinit var binding: ActivityMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDI()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie)
        viewModel = ViewModelProviders.of(this).get(MovieActivityViewModel::class.java)

        binding.viewmodel = viewModel

        initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
                .replace(binding.container.id, MovieFragment())
                .commit()
    }

    private fun initDI() {
        Injector.navigationComponent().inject(this)
    }
}