package com.example.timeplateactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.timeplateactivity.ui.ItemClickListener
import com.example.timeplateactivity.ui.home.HomeFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ActionBottomDialogFragment(private var mListener: HomeFragment) :
    BottomSheetDialogFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**тут будем обращаться к элементам во фрагменте*/

    }


    override fun onClick(v: View?) {
        val tvSelected = v as TextView
        //  mListener.onItemClick(tvSelected.text.toString())
        dismiss()
    }
}