package ru.santaev.techtask.feature.gallery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.santaev.techtask.R
import ru.santaev.techtask.databinding.ListItemPhotoBinding
import ru.santaev.techtask.feature.gallery.ui.entity.PhotoUiEntity
import ru.santaev.techtask.utils.ListAdapter

class PhotoAdapter(
    private val photoClickListener: (PhotoUiEntity) -> Unit
) : PagingDataAdapter<PhotoUiEntity, PhotoAdapter.PhotoViewHolder>(PhotoListDiffUtil()) {

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            ListItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    inner class PhotoViewHolder(
        private val binding: ListItemPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PhotoUiEntity) {
            Glide.with(binding.photo.context)
                .load(item.url)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(binding.photo)
            binding.root.setOnLongClickListener {
                photoClickListener.invoke(item)
                true
            }
        }
    }
}

private class PhotoListDiffUtil : DiffUtil.ItemCallback<PhotoUiEntity>() {
    override fun areItemsTheSame(oldItem: PhotoUiEntity, newItem: PhotoUiEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PhotoUiEntity, newItem: PhotoUiEntity): Boolean {
        return oldItem == newItem
    }
}
