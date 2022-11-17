package com.kostlin.timeplateactivity.ui.BottomSheet

import com.kostlin.timeplateactivity.ui.home.HomeFragment

class ActionBottom {

    companion object {
        const val TAG = "ActionBottomDialog"
        fun newInstance(mListener: ItemClickListener): ActionBottomDialogFragment {

            return ActionBottomDialogFragment(mListener)
        }
    }
}