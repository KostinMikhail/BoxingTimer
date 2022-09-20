package com.example.timeplateactivity.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import com.example.timeplateactivity.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.timeplateactivity.data.repository.AppDatabase
import com.example.timeplateactivity.data.repository.Profile
import com.example.timeplateactivity.databinding.FragmentHomeBinding
import com.example.timeplateactivity.ui.BottomSheetFragment
import java.text.SimpleDateFormat
import java.util.*
import android.os.Build
import androidx.compose.material.icons.materialIcon
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import androidx.core.graphics.drawable.DrawableCompat.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timeplateactivity.databinding.ActivityMainBinding.inflate
import com.example.timeplateactivity.databinding.AppBarMainBinding.inflate
import com.example.timeplateactivity.databinding.BottomsheetFragmentBinding
import com.example.timeplateactivity.databinding.BottomsheetFragmentBinding.inflate
import com.example.timeplateactivity.databinding.ContentMainBinding.inflate
import com.example.timeplateactivity.databinding.FragmentCalcBinding
import com.example.timeplateactivity.databinding.FragmentCalcBinding.inflate
import com.example.timeplateactivity.databinding.FragmentClockBinding.inflate
import com.example.timeplateactivity.databinding.FragmentHomeBinding.inflate
import com.example.timeplateactivity.databinding.FragmentSettingsBinding.inflate
import com.example.timeplateactivity.databinding.LaunchBinding.inflate
import com.example.timeplateactivity.databinding.NavHeaderMainBinding.inflate
import com.example.timeplateactivity.databinding.SpinnerViewBinding.inflate
import com.example.timeplateactivity.ui.home.HomeViewModel


class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomsheetFragmentBinding? = null

    private val binding get() = _binding!!


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
        binding.logo

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}