package com.mbdev.photogallery

import android.content.Context
import android.os.Bundle
import android.text.InputType.TYPE_NULL
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.*
import com.mbdev.photogallery.databinding.FragmentPhotoGalleryBinding
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private const val TAG = "PhotoGalleryFragment"
private const val POLL_WORK = "POLL_WORK"

class PhotoGalleryFragment : Fragment(), MenuProvider {
    private var searchView: SearchView? = null
    private var pollingMenuItem: MenuItem? = null

    private var _binding: FragmentPhotoGalleryBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val photoGalleryViewModel: PhotoGalleryViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoGalleryBinding.inflate(layoutInflater, container, false)
        binding.rvPhotoGrid.layoutManager = GridLayoutManager(context, 3)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.addMenuProvider(this)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoGalleryViewModel.uiState.collect { state ->
                    binding.rvPhotoGrid.adapter = PhotoListAdapter(state.images)
                    searchView?.setQuery(state.query, false)
                    updatePollingState(state.isPolling)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.removeMenuProvider(this)
        searchView = null
        pollingMenuItem = null
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.fragment_photo_gallery, menu)
        val searchItem: MenuItem = menu.findItem(R.id.miSearch)
        searchView = searchItem.actionView as? SearchView
        pollingMenuItem = menu.findItem(R.id.miTogglePolling)

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                setLoadingState()
                Log.d(TAG, "QueryTextSubmit: $query")
                photoGalleryViewModel.setQuery(query ?: "")
                setLoadedState()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "QueryTextChange: $newText")
                return false
            }
        })
    }


    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.miSearch -> {
                true
            }
            R.id.miClear -> {
                photoGalleryViewModel.setQuery("")

                val inputManager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputManager?.hideSoftInputFromWindow(view?.windowToken, 0)

                true
            }
            R.id.miTogglePolling -> {
                photoGalleryViewModel.toggleIsPolling()
                true
            }
            else -> false
        }
    }

    private fun setLoadingState() {
        binding.pbLoading.visibility = View.VISIBLE
        binding.rvPhotoGrid.visibility = View.GONE
        val inputManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputManager?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun setLoadedState() {
        binding.pbLoading.visibility = View.GONE
        binding.rvPhotoGrid.visibility = View.VISIBLE
    }

    private fun updatePollingState(isPolling: Boolean) {
        val toggleItemTitle = if (isPolling) {
            R.string.stop_polling
        } else {
            R.string.start_polling
        }
        pollingMenuItem?.setTitle(toggleItemTitle)

        if (isPolling) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()
            val periodicRequest = PeriodicWorkRequestBuilder<PollWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                POLL_WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest
            )
        } else {
            WorkManager.getInstance(requireContext()).cancelUniqueWork(POLL_WORK)
        }
    }

}

fun SearchView.setReadOnly(value: Boolean, inputType: Int = TYPE_NULL) {
    isFocusable = !value
    isFocusableInTouchMode = !value
    this.inputType = inputType
}










