package ru.santaev.techtask.feature.gallery.ui

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.santaev.techtask.R
import ru.santaev.techtask.databinding.FragmentPhotosBinding
import ru.santaev.techtask.feature.gallery.ui.adapter.PhotoAdapter
import ru.santaev.techtask.utils.ViewModelFactory
import ru.santaev.techtask.utils.appComponent
import ru.santaev.techtask.utils.observe
import ru.santaev.techtask.utils.observeToast
import javax.inject.Inject

class GalleryFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: GalleryViewModel by viewModels { viewModelFactory }
    private lateinit var binding: FragmentPhotosBinding
    private val adapter = PhotoAdapter(
        photoClickListener = { }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotosBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeToast(viewModel)
    }

    private fun initViews() {
        binding.photos.adapter = adapter
        binding.photos.layoutManager = GridLayoutManager(requireContext(), COLUMNS)
        adapter.addOnPagesUpdatedListener {
            binding.skeleton.root.isGone = true
        }
        viewModel.photos.observe(viewLifecycleOwner) { photos ->
            viewLifecycleOwner.lifecycle.coroutineScope.launch {
                if (photos != null) {
                    adapter.submitData(photos)

                }
            }
        }
        binding.photos.addItemDecoration(PhotosSpacingItemDecorator(requireContext(), COLUMNS))
    }

    companion object {
        private const val COLUMNS = 3
    }
}

private class PhotosSpacingItemDecorator(
    private val context: Context,
    private val columns: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val padding = context.resources.getDimensionPixelSize(R.dimen.photo_padding)
        val position = parent.getChildAdapterPosition(view)
        if (position >= 0) {
            val isStart = position % columns == 0
            val isEnd = position % columns == columns - 1
            outRect.set(
                padding,
                padding,
                padding,
                padding
            )
        }
    }
}