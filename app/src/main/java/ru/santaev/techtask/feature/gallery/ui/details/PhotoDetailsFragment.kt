package ru.santaev.techtask.feature.gallery.ui.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.santaev.techtask.databinding.FragmentPhotoDetailsBinding
import ru.santaev.techtask.feature.gallery.ui.details.factory.PhotoDetailsViewModelFactoryProvider
import ru.santaev.techtask.utils.appComponent
import ru.santaev.techtask.utils.setupToolbar
import javax.inject.Inject

class PhotoDetailsFragment : Fragment() {

    @Inject
    lateinit var factoryProvider: PhotoDetailsViewModelFactoryProvider
    private val viewModel: PhotoDetailsViewModel by viewModels {
        factoryProvider.create(args.photoId)
    }
    private lateinit var binding: FragmentPhotoDetailsBinding
    private val args by navArgs<PhotoDetailsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoDetailsBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        // TODO handle insets using Insetter library
        setupToolbar(binding.toolbar)
        NavigationUI.setupWithNavController(binding.toolbar, findNavController())
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewModel.photo.collect { photo ->
                if (photo != null) {
                    Glide.with(this@PhotoDetailsFragment)
                        .load(photo)
                        .addListener(getPhotoRequestListener())
                        .into(binding.image)
                }
            }
        }
    }

    private fun getPhotoRequestListener() = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            // TODO implement it
            Toast.makeText(requireContext(), "Unable to load photo", Toast.LENGTH_LONG).show()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            binding.progress.isGone = true
            return false
        }
    }
}
