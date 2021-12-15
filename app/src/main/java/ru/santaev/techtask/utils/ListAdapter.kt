package ru.santaev.techtask.utils

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

open class ListAdapter<T>(
    diffUtil: DiffUtil.ItemCallback<T>,
) : ListAdapter<T, ru.santaev.techtask.utils.ListAdapter.ViewHolder<T>>(diffUtil) {

    private val viewMatchers = mutableListOf<ViewMatcher<T, *, *>>()

    fun <P : T> registerView(
        matcher: (T) -> Boolean,
        viewHolderCreator: (parent: ViewGroup) -> ViewHolder<P>
    ) {
        val element: ViewMatcher<T, P, ViewHolder<P>> = ViewMatcher(matcher, viewHolderCreator)
        viewMatchers.add(element)
    }

    override fun getItemViewType(position: Int): Int {
        val viewType = viewMatchers.indexOfFirst { it.matcher.invoke(getItem(position)) }
        if (viewType < 0) {
            throw IllegalStateException("Unknown view type of item ${getItem(position)} ")
        }
        return viewType
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val viewHolderCreator = viewMatchers.getOrNull(viewType)?.viewHolderCreator
        return viewHolderCreator?.invoke(parent) as ViewHolder<T>
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }

    abstract class ViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(item: T) {
        }
    }

    private class ViewMatcher<T, P : T, VH : ViewHolder<P>>(
        val matcher: (T) -> Boolean,
        val viewHolderCreator: (parent: ViewGroup) -> VH
    )
}
