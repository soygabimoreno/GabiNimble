package soy.gabimoreno.gabinimble.presentation.main.list.viewholder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import soy.gabimoreno.gabinimble.coredomain.Song
import soy.gabimoreno.gabinimble.databinding.ItemSongTopBinding
import soy.gabimoreno.gabinimble.libbase.recyclerview.ListAdapterBuilder
import soy.gabimoreno.gabinimble.libimageloader.load

class SongsTopViewHolder(
    layoutInflater: LayoutInflater,
    itemView: View,
    private val onItemClick: (Song) -> Unit
) : ListAdapterBuilder.ViewHolder<Song>(itemView) {

    private val binding = ItemSongTopBinding.inflate(layoutInflater)

    @SuppressLint("SetTextI18n")
    override fun bind(item: Song) = with(itemView) {
        with(item) {
            binding.ivThumbnail.load(thumbnailUrl)

            setOnClickListener { onItemClick(this) }
        }
    }
}
