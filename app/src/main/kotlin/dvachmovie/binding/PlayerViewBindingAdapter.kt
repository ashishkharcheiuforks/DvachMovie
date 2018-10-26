package dvachmovie.binding

import android.databinding.BindingAdapter
import android.databinding.BindingMethod
import android.databinding.BindingMethods
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

@BindingMethods(
        BindingMethod(
                type = PlayerView::class,
                attribute = "movie",
                method = "bindCurrentMovie"
        )
)
class PlayerViewBindingAdapter {
    companion object {
        @BindingAdapter("movie")
        @JvmStatic
        fun bindCurrentMovie(playerView: PlayerView, value: String) {
            val urlVideo: Uri = Uri.parse(value)
            val player: SimpleExoPlayer =
                    ExoPlayerFactory.newSimpleInstance(playerView.context)
            playerView.player = player
            val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(playerView.context,
                    Util.getUserAgent(playerView.context, "AppName"))
            val videoSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(urlVideo)

            player.prepare(videoSource)
        }
    }

}