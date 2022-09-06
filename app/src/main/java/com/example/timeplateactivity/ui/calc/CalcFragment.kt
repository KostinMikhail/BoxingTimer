package com.example.timeplateactivity.ui.calc

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timeplateactivity.R
import com.example.timeplateactivity.databinding.FragmentCalcBinding

class CalcFragment : Fragment() {

    var isMale: Boolean = true
    var height: Int? = null
    var age: Int? = null
    var weight: Int? = null
    var activity: Int? = null


    private var _binding: FragmentCalcBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(CalcViewModel::class.java)

        _binding = FragmentCalcBinding.inflate(inflater, container, false)
        with(binding) {
            sexBtnMale.setOnClickListener {
                isMale = true
            }
            sexBtnFemale.setOnClickListener {
                isMale = false
            }
//            heightSB.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
//                override fun onProgressChanged(heightSB: SeekBar?, )
//            })

        }


        return binding.root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}