package com.example.timeplateactivity.ui.calc

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timeplateactivity.R
import com.example.timeplateactivity.databinding.FragmentCalcBinding

class CalcFragment : Fragment() {


    private var _binding: FragmentCalcBinding? = null

    private val binding get() = _binding!!

    private var calcViewModel: CalcViewModel? = null

    val result = calcViewModel?.calc().toString()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val calcViewModel =
            ViewModelProvider(this).get(CalcViewModel::class.java)

        _binding = FragmentCalcBinding.inflate(inflater, container, false)
        with(binding) {

            sexBtnMale.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    sexBtnMale.setBackgroundResource(@drawable / btn_sex_male_on)
                } else {
                    sexBtnMale.setBackgroundResource(@drawable / btn_sex_male)
                }

                //               sexBtnMale.setBackgroundResource(@color/primary50Transparent)
            }


//            sexBtnMale.setOnClickListener() {
//
//                calcViewModel.setMale()
//            }
            sexBtnFemale.setOnClickListener() {
                calcViewModel.setFemale()
            }

            heightSB.min = 140
            heightSB.max = 220

            heightSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    calcViewModel.height = progress

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })

            ageSB.min = 14
            ageSB.max = 60

            ageSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    calcViewModel.age = progress

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })

            weightSB.min = 45
            weightSB.max = 120

            weightSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    calcViewModel.weight = progress

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })

            btnCalculate.setOnClickListener() {

                test.setText(result)
            }


        }


        return binding.root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}