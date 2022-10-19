package com.example.timeplateactivity.ui.BottomSheet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.timeplateactivity.databinding.BottomsheetFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomsheetFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dialog = dialog
        if (dialog != null) {

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomsheetFragmentBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  arguments?.getParcelableArrayList("some") as ArrayList<String>

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /*   companion object{
           fun newInstance(profilesTitles: ArrayList<String>): BottomSheetFragment{
               val bundle = Bundle().apply {
               putParcelableArrayList("some", profilesTitles)
               //разобраться, привести к одному типу, либо создать модель, узнать парцелабл
               }
               val fragment = BottomSheetFragment()
              fragment.arguments = bundle

               return fragment
           }
       }*/


}