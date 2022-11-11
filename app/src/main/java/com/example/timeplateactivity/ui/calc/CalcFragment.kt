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
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timeplateactivity.R
import com.example.timeplateactivity.databinding.FragmentCalcBinding

class CalcFragment : Fragment() {

    private var _binding: FragmentCalcBinding? = null
    private val binding get() = _binding!!

    private var calcViewModel: CalcViewModel? = null
    var isMale: Boolean = true
    var height: Int? = null
    var age: Int? = null
    var weight: Int? = null
    var physicalActivity: Double = 1.2
    var result: Int = 2000

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

            fun calc() {
                if (isMale) {
                    result =
                        ((weight!! * 10 + 6.25 * height!! - 5 * age!! + 5) * physicalActivity).toInt()
                } else {
                    result =
                        ((weight!! * 10 + 6.25 * height!! - 5 * age!! - 161) * physicalActivity).toInt()
                }

            }

            fun isBtnRdy() {
                if (height != null) {
                    if (age != null) {
                        if (weight != null) {
                            binding.btnCalculate.setBackgroundResource(R.drawable.btn_calculate_on)
                        }
                    }
                }
            }
            binding.btnCalculate.setOnClickListener {

                if (height == null) {
                    Toast.makeText(requireContext(), R.string.noHeight, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener

                }

                if (age == null) {
                    Toast.makeText(requireContext(), R.string.noAge, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener

                }
                if (weight == null) {
                    Toast.makeText(requireContext(), R.string.noWeight, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener

                } else {
                    calc()
                    binding.btnCalculate.setBackgroundResource(R.drawable.btn_calculate_on)
                    binding.resultTV.setText(result.toString() + "\n" + "калорий в день")
                }
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
                    isMale = true
                } else {
                    sexBtnMale.setBackgroundResource(R.drawable.btn_sex_male)
                    sexBtnMale.isChecked
                    sexBtnFemale.setBackgroundResource(R.drawable.btn_sex_female_on)
                    sexBtnFemale.isChecked = false
                    calcViewModel.setMale()
                    isMale = false
                }
            }
            sexBtnFemale.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    sexBtnFemale.setBackgroundResource(R.drawable.btn_sex_female_on)
                    sexBtnFemale.isChecked = false
                    sexBtnMale.setBackgroundResource(R.drawable.btn_sex_male)
                    sexBtnMale.isChecked
                    calcViewModel.setMale()
                    isMale = false

                } else {
                    sexBtnFemale.setBackgroundResource(R.drawable.btn_sex_female)
                    sexBtnFemale.isChecked
                    sexBtnMale.setBackgroundResource(R.drawable.btn_sex_male_on)
                    sexBtnMale.isChecked = false
                    calcViewModel.setFemale()
                    isMale = true

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
                    height = progress
                    binding.heightTV.setText("Рост: " + height)

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    isBtnRdy()
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
                    age = progress
                    binding.ageTV.setText("Возраст: " + age)

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    isBtnRdy()
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
                    weight = progress
                    binding.weightTV.setText("Вес: " + weight)

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    isBtnRdy()
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
                    when (position) {
                        0 -> physicalActivity = 1.2
                        1 -> physicalActivity = 1.375
                        2 -> physicalActivity = 1.6375
                    }

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
