package com.example.timeplateactivity.ui

import com.example.timeplateactivity.ActionBottomDialogFragment
import com.example.timeplateactivity.ui.home.HomeFragment

class ActionBottom {

    companion object {
        const val TAG = "ActionBottomDialog"
        fun newInstance(mListener: HomeFragment): ActionBottomDialogFragment {

            return ActionBottomDialogFragment(mListener)
        }
    }
}