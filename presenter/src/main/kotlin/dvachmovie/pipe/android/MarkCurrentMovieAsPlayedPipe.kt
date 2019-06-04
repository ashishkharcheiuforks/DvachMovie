package dvachmovie.pipe.android

import dvachmovie.architecture.PipeSync
import dvachmovie.usecase.MarkCurrentMovieAsPlayedUseCase
import javax.inject.Inject

class MarkCurrentMovieAsPlayedPipe @Inject constructor(
        private val useCase: MarkCurrentMovieAsPlayedUseCase
) : PipeSync<Int, Unit>() {

    override fun execute(input: Int): Unit =
            useCase.execute(input)

}
