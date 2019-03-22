package dvachmovie.architecture.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dvachmovie.api.CookieManager
import dvachmovie.db.data.MovieEntity

@BindingAdapter("imageFromUrl", "cookie")
fun ImageView.bindImageFromUrl(movieEntity: MovieEntity, cookie: CookieManager.Cookie) {
    if (!movieEntity.previewUrl.isEmpty()) {
        val builder = LazyHeaders.Builder().addHeader("Cookie", cookie.toString())

        val glideUrl = GlideUrl(movieEntity.previewUrl, builder.build())

        Glide.with(this.context)
                .load(glideUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
    }
}

@BindingAdapter("imageFromDrawable")
fun bindImageFromDrawable(view: ImageView, image: Drawable?) {
    if (image != null) {
        Glide.with(view.context)
                .load(image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view)
    }
}

@BindingAdapter("imageFromGif")
fun bindImageFromGif(view: ImageView, image: Drawable?) {
    if (image != null) {
        Glide.with(view.context)
                .load(image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view)
    }
}
