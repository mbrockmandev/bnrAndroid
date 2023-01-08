package com.mbdev.criminalintent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.mbdev.criminalintent.MultipleCrimeListTypeAdapter.Companion.POLICE_VIEW
import com.mbdev.criminalintent.databinding.ListItemCrimeBinding
import com.mbdev.criminalintent.databinding.ListItemCrimePoliceBinding
import kotlinx.coroutines.NonDisposableHandle.parent
import java.util.*
import kotlin.text.Typography.copyright

class CrimeHolder(val binding: ListItemCrimeBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(crime: Crime, onCrimeClicked: (crimeId: UUID) -> Unit) {
        binding.tvCrimeTitle.text = crime.title
        binding.tvCrimeDate.text = crime.date.toString()

        binding.root.setOnClickListener {
            onCrimeClicked(crime.id)
        }

        binding.ivSolved.visibility = if (crime.isSolved) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}

class CrimeListAdapter(
    private val crimes: List<Crime>,
    private val onCrimeClicked: (crimeId: UUID) -> Unit
) : RecyclerView.Adapter<CrimeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
        return CrimeHolder(binding)
    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        holder.apply {
            binding.tvCrimeTitle.text = crime.title
            binding.tvCrimeDate.text = crime.date.toString()
        }
        holder.bind(crime, onCrimeClicked)
    }

    override fun getItemCount() = crimes.size
}


class MultipleCrimeListTypeAdapter(
    private val crimes: List<Crime>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val POLICE_VIEW = 1
        const val NO_POLICE_VIEW = 2
    }

    private inner class PoliceView(val binding: ListItemCrimePoliceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(crime: Crime) {
            binding.tvPoliceCrimeTitle.text = crime.title
            binding.tvPoliceCrimeDate.text = crime.date.toString()
            binding.btnContactPolice.setOnClickListener {
                Toast.makeText(
                    binding.root.context,
                    "Police were sent for ${crime.title}!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            binding.ivSolved.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private inner class NoPoliceView(val binding: ListItemCrimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(crime: Crime) {
            binding.tvCrimeTitle.text = crime.title
            binding.tvCrimeDate.text = crime.date.toString()

            binding.root.setOnClickListener {
                Toast.makeText(
                    binding.root.context,
                    "${crime.title} clicked!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            binding.ivSolved.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == POLICE_VIEW) {
            val binding = ListItemCrimePoliceBinding.inflate(inflater, parent, false)
            return PoliceView(binding)
        } else {
            val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
            return NoPoliceView(binding)
        }
    }

    override fun getItemCount() = crimes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val crime = crimes[position]
//        (holder as).bind(crime)
//        if (crime.requiresPolice) {
//            (holder as PoliceView).bind(crime)
//        } else {
//            (holder as NoPoliceView).bind(crime)
//        }
        (holder as NoPoliceView).bind(crime)
    }

//    override fun getItemViewType(position: Int): Int {
//        if (crimes[position].requiresPolice) {
//            return POLICE_VIEW
//        } else {
//            return NO_POLICE_VIEW
//        }
//    }

}
