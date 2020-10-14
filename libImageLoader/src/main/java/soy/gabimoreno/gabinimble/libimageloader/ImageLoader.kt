package soy.gabimoreno.gabinimble.libimageloader

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.load(thumbnailUrl: String) {
    Glide
        .with(context)
        .load(thumbnailUrl)
        .into(this)
}
