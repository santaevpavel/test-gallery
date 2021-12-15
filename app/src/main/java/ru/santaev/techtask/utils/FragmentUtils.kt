package ru.santaev.techtask.utils

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.santaev.techtask.TechTaskApplication
import ru.santaev.techtask.di.AppComponent

val Activity.appComponent: AppComponent
    get() = (application as TechTaskApplication).component

val Fragment.appComponent: AppComponent
    get() = (requireActivity().application as TechTaskApplication).component

fun Fragment.setupToolbar(toolbar: Toolbar) {
    (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
}

fun Fragment.observeToast(viewModel: BaseViewModel) {
    viewModel.toast.observeEvent(viewLifecycleOwner) { toast ->
        val length = when (toast.isLong) {
            true -> Toast.LENGTH_LONG
            else -> Toast.LENGTH_SHORT
        }
        Toast.makeText(requireContext(), toast.message, length).show()
    }
}
