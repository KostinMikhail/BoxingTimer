package com.example.timeplateactivity.ui.BottomSheet

import com.example.timeplateactivity.ui.home.HomeFragment

class ActionBottom {

    companion object {
        const val TAG = "ActionBottomDialog"
        fun newInstance(mListener: ItemClickListener): ActionBottomDialogFragment {

            return ActionBottomDialogFragment(mListener)
        }
    }
}