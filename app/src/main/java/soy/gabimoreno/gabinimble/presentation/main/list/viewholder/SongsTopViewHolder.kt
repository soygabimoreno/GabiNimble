package soy.gabimoreno.gabinimble.presentation.main.list.viewholder

import android.annotation.SuppressLint
import androidx.viewbinding.ViewBinding
import soy.gabimoreno.gabinimble.coredomain.Song
import soy.gabimoreno.gabinimble.databinding.ItemSongTopBinding
import soy.gabimoreno.gabinimble.libbase.recyclerview.BindingListAdapterBuilder
import soy.gabimoreno.gabinimble.libimageloader.load

class SongsTopViewHolder(
    binding: ViewBinding,
    private val onItemClick: (Song) -> Unit
) : BindingListAdapterBuilder.ViewHolder<ItemSongTopBinding, Song>(binding) {

    @SuppressLint("SetTextI18n")
    override fun bind(
        binding: ItemSongTopBinding,
        item: Song
    ) {
        with(item) {
            binding.ivThumbnail.load(thumbnailUrl)
            binding.root.setOnClickListener { onItemClick(this) }
        }
    }
}
