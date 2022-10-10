package com.example.timeplateactivity.ui.BottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.timeplateactivity.R
import com.example.timeplateactivity.data.BottomSheetAdapter
import com.example.timeplateactivity.data.TestList
import com.example.timeplateactivity.databinding.BottomsheetFragmentBinding
import com.example.timeplateactivity.databinding.FragmentHomeBinding
import com.example.timeplateactivity.ui.home.HomeFragment
import com.example.timeplateactivity.ui.settings.SettingsViewModel
import com.example.timeplateactivity.ui.settings.SettingsViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ActionBottomDialogFragment(private var mListener: ItemClickListener) :
    BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: BottomsheetFragmentBinding? = null
    private val binding get() = _binding!!
    private var homeFragment: HomeFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        homeFragment =
//            ViewModelProvider(this, HomeFragmentViewModelFactory(requireContext())).get(
//                HomeFragmentViewModel::class.java
//            )
        //тут я пытался прокинуть homeFragment в этот класс, что бы использовать его БД, но мне
        //кажется, что нужно переносить бд сюда

        return inflater.inflate(R.layout.bottomsheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**тут будем обращаться к элементам во фрагменте*/

        var arrProfile: ArrayList<TestList> = ArrayList()
        arrProfile.add(TestList("profile1"))
        arrProfile.add(TestList("profile2"))
        arrProfile.add(TestList("profile3"))

        //    binding.profilesList.adapter = BottomSheetAdapter(requireContext(), arrProfile)
        //почему с этой залупой всё крашится?!
    }


    override fun onClick(v: View?) {
        val tvSelected = v as TextView
        mListener.onItemClick(tvSelected.text.toString())
        dismiss()
    }
}