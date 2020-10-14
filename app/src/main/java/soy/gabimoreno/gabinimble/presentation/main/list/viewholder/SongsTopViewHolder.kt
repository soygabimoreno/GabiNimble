package soy.gabimoreno.gabinimble.presentation.main.list.viewholder

import android.annotation.SuppressLint
import android.view.View
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_song_top.view.*
import soy.gabimoreno.gabinimble.coredomain.Song
import soy.gabimoreno.gabinimble.libbase.recyclerview.ListAdapterBuilder
import soy.gabimoreno.gabinimble.libimageloader.load

class SongsTopViewHolder(
    itemView: View,
    private val onItemClick: (Song) -> Unit
) : ListAdapterBuilder.ViewHolder<Song>(itemView), LayoutContainer {

    override val containerView: View = itemView

    @SuppressLint("SetTextI18n")
    override fun bind(item: Song) = with(itemView) {
        with(item) {
            ivThumbnail.load(thumbnailUrl)

            setOnClickListener { onItemClick(this) }
        }
    }
}
