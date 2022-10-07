package com.example.timeplateactivity.ui.BottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.timeplateactivity.R
import com.example.timeplateactivity.databinding.BottomsheetFragmentBinding
import com.example.timeplateactivity.databinding.FragmentHomeBinding
import com.example.timeplateactivity.ui.home.HomeFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ActionBottomDialogFragment(private var mListener: ItemClickListener) :
    BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: BottomsheetFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**тут будем обращаться к элементам во фрагменте*/
//        binding.profileName.setText()
    }


    override fun onClick(v: View?) {
        val tvSelected = v as TextView
        mListener.onItemClick(tvSelected.text.toString())
        dismiss()
    }
}