package com.example.timeplateactivity.ui.calc

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.*
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timeplateactivity.R
import com.example.timeplateactivity.databinding.FragmentCalcBinding

class CalcFragment : Fragment() {


    private var _binding: FragmentCalcBinding? = null

    private val binding get() = _binding!!

    private var calcViewModel: CalcViewModel? = null

    //var result = calcViewModel?.calc().toString()

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


            binding.btnCalculate.setOnClickListener {
                var result = calcViewModel.calc().toString()


                binding.test.setText(result)
            }


            physicalActivityTV.setOnClickListener() {
                spinner.performClick()
            }



            physicalActivityTV.setText(R.string.physicalActivity)


            fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
                super.onCreateOptionsMenu(menu, inflater)

            }


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

            val trainingActivity =
                arrayOf(
                    resources.getString(R.string.lightActivity),
                    resources.getString(R.string.middleActivity),
                    resources.getString(R.string.heavyActivity)
                )

            spinner.adapter = ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_view,
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


        }


        return binding.root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
