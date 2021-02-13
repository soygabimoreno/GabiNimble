package soy.gabimoreno.gabinimble.presentation.main.list.viewholder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import soy.gabimoreno.gabinimble.R
import soy.gabimoreno.gabinimble.coredomain.Song
import soy.gabimoreno.gabinimble.databinding.ItemSongBottomBinding
import soy.gabimoreno.gabinimble.libbase.recyclerview.ListAdapterBuilder
import soy.gabimoreno.gabinimble.libimageloader.load

class SongsBottomViewHolder(
    layoutInflater: LayoutInflater,
    itemView: View,
    private val onItemClick: (Song) -> Unit
) : ListAdapterBuilder.ViewHolder<Song>(itemView) {

    private val binding = ItemSongBottomBinding.inflate(layoutInflater)

    @SuppressLint("SetTextI18n")
    override fun bind(item: Song) = with(itemView) {
        with(item) {
            with(binding) {
                ivThumbnail.load(thumbnailUrl)
                tvName.text = name
                tvArtist.text = "${context.getString(R.string.by_)}  $artist"
            }

            setOnClickListener { onItemClick(this) }
        }
    }
}
