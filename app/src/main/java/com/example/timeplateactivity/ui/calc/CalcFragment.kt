package com.example.timeplateactivity.ui.calc

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

            sexBtnMale.setBackgroundResource(R.drawable.btn_sex_male_on)

            sexBtnMale.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    sexBtnMale.setBackgroundResource(R.drawable.btn_sex_male_on)
                    sexBtnMale.isChecked = false
                    sexBtnFemale.setBackgroundResource(R.drawable.btn_sex_female)
                    sexBtnFemale.isChecked
                    calcViewModel.setFemale()
                } else {
                    sexBtnMale.setBackgroundResource(R.drawable.btn_sex_male)
                    sexBtnMale.isChecked
                    sexBtnFemale.setBackgroundResource(R.drawable.btn_sex_female_on)
                    sexBtnFemale.isChecked = false
                    calcViewModel.setMale()
                }


            }
            sexBtnFemale.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    sexBtnFemale.setBackgroundResource(R.drawable.btn_sex_female_on)
                    sexBtnFemale.isChecked = false
                    sexBtnMale.setBackgroundResource(R.drawable.btn_sex_male)
                    sexBtnMale.isChecked
                    calcViewModel.setMale()
                } else {
                    sexBtnFemale.setBackgroundResource(R.drawable.btn_sex_female)
                    sexBtnFemale.isChecked
                    sexBtnMale.setBackgroundResource(R.drawable.btn_sex_male_on)
                    sexBtnMale.isChecked = false
                    calcViewModel.setFemale()
                }
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

            val trainingActivity = arrayOf("lightActivity", "middleActivity", "heavyActivity")

            spinner.adapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                trainingActivity
            )

            spinner.onItemSelectedListener = object : AdapterView.OnItemClickListener,
                AdapterView.OnItemSelectedListener {

                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    test.text = trainingActivity.get(position)
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }


//            btnCalculate.setOnClickListener() {
//                test.text =  calcViewModel.weight!!.toString()
//            }


        }


        return binding.root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
