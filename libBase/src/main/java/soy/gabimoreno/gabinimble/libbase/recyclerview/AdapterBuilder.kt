package soy.gabimoreno.gabinimble.libbase.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

fun <BINDING : ViewBinding, T, VH : BindingListAdapterBuilder.ViewHolder<BINDING, T>> createBindingListAdapter(
    init: BindingListAdapterBuilder<BINDING, T, VH>.() -> Unit
): ListAdapter<T, VH> = BindingListAdapterBuilder(init).build()

class BindingListAdapterBuilder<BINDING : ViewBinding, T, VH : BindingListAdapterBuilder.ViewHolder<BINDING, T>>() {
    constructor(init: BindingListAdapterBuilder<BINDING, T, VH>.() -> Unit) : this() {
        init()
    }

    private var areItemsEqualByReference: ((T, T) -> Boolean)? = null
    private var areItemsEqualByContent: ((T, T) -> Boolean)? = null
    private var viewBinding: ((LayoutInflater, ViewGroup?) -> BINDING)? = null
    private var viewHolderCreation: ((BINDING, Int) -> VH)? = null
    private var itemViewType: (Int, Int) -> Int = { _, _ -> layoutRes ?: 1 }

    fun holderViewBinding(init: (LayoutInflater, ViewGroup?) -> BINDING) {
        viewBinding = init
    }

    fun compareItemsByReference(f: (T, T) -> Boolean) {
        areItemsEqualByReference = f
    }

    fun compareItemsByContent(f: (T, T) -> Boolean) {
        areItemsEqualByContent = f
    }

    fun viewHolderCreation(f: (BINDING, Int) -> VH) {
        viewHolderCreation = f
    }

    fun viewTypeByPosition(f: (Int, Int) -> Int) {
        itemViewType = f
    }

    fun build(): ListAdapter<T, VH> {
        val byReference = requireNotNull(areItemsEqualByReference)
        val byContent = requireNotNull(areItemsEqualByContent)
        val createViewHolder = requireNotNull(viewHolderCreation)

        val diffCalculator = object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
                byReference(oldItem, newItem)

            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
                byContent(oldItem, newItem)
        }

        return object : ListAdapter<T, VH>(diffCalculator) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
                viewBinding!!(
                    LayoutInflater.from(parent.context),
                    parent
                )
                    .run { createViewHolder(this, viewType) }

            override fun onBindViewHolder(
                holder: VH,
                position: Int
            ) {
                holder.bind(
                    viewBinding,
                    getItem(position)
                )
            }

            override fun getItemViewType(position: Int): Int {
                return itemViewType(
                    position,
                    itemCount
                )
            }
        }
    }

    abstract class ViewHolder<BINDING, T>(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(
            binding: BINDING,
            item: T
        )
    }
}
