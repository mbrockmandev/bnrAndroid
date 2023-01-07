package com.mbdev.criminalintent

import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mbdev.criminalintent.databinding.FragmentCrimeListBinding

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {
    private var _binding: FragmentCrimeListBinding? = null
    private val binding: FragmentCrimeListBinding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val crimeListViewModel: CrimeListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeListBinding.inflate(layoutInflater, container, false)

        binding.rvCrimeList.layoutManager = LinearLayoutManager(context)

        val crimes = crimeListViewModel.crimes
//        val adapter = CrimeListAdapter(crimes)
        val adapter = MultipleCrimeListTypeAdapter(crimes)
        binding.rvCrimeList.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
