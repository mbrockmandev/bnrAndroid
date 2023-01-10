//package com.mbdev.criminalintent
//
//import android.graphics.Bitmap
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.navArgs
//import com.mbdev.criminalintent.databinding.FragmentDialogBinding
//
//class DialogFragment : Fragment() {
//    private var _binding: FragmentDialogBinding? = null
//    private val binding
//        get() = checkNotNull(_binding) {
//            "Cannot access binding because it is null. Is the view visible?"
//        }
//
//    private val args: DialogFragmentArgs by navArgs()
//    private var _photo: Bitmap? = null
//    private val photo
//        get() = checkNotNull(_photo) {
//            "This is NULL!"
//        }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentDialogBinding.inflate(layoutInflater, container, false)
//        _photo = args.crimePhoto
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.apply {
//
//            ivCrimePhotoZoomed.setImageBitmap(photo)
//
//            ivCrimePhotoZoomed.setOnClickListener {
//
//            }
//        }
//    }
//}