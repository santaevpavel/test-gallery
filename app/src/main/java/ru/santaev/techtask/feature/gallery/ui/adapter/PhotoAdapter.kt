package ru.santaev.techtask.feature.gallery.ui.adapter

import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import ru.santaev.techtask.databinding.ListItemPhotoBinding
import ru.santaev.techtask.feature.gallery.ui.entity.PhotoUiEntity
import ru.santaev.techtask.utils.ListAdapter

class PhotoAdapter(
    private val photoClickListener: (String) -> Unit
) : ListAdapter<PhotoUiEntity>(PhotoListDiffUtil()) {

    init {
        registerView(
            matcher = { true },
            viewHolderCreator = { parent ->
                UserViewHolder(
                    ListItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        )
    }

    private inner class UserViewHolder(
        private val binding: ListItemPhotoBinding
    ) : ViewHolder<PhotoUiEntity>(binding.root) {

        override fun bind(item: PhotoUiEntity) {
            Glide.with(binding.photo.context)
                .load(item.url)
                .centerCrop()
                .into(binding.photo)
            binding.root.setOnLongClickListener {
                photoClickListener.invoke(item.id)
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
