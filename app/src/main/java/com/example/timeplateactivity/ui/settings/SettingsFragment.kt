package com.example.timeplateactivity.ui.settings

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timeplateactivity.R
import com.example.timeplateactivity.databinding.FragmentSettingsBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null


    private val binding get() = _binding!!
    private var settingsViewModel: SettingsViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsViewModel =
            ViewModelProvider(this, SettingsViewModelFactory(requireContext())).get(
                SettingsViewModel::class.java
            )

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        settingsViewModel?.timerSuccsessCreated?.observe(viewLifecycleOwner) {
            spinnerRefresh()
            Toast.makeText(this@SettingsFragment.requireContext(), it, Toast.LENGTH_LONG)
                .show()
        }

        with(binding) {

            settingsViewModel?.errorData?.observe(viewLifecycleOwner) {
                Toast.makeText(this@SettingsFragment.requireContext(), it, Toast.LENGTH_LONG)
                    .show()
            }
        }


        spinnerRefresh()

//        binding.btnRoundTime.setOnKeyListener { view, keyCode, event ->
//            if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                settingsViewModel?.setRoundTime(binding.btnRoundTime.text.toString())
//                binding.btnRoundTime.text.toString()
//                keyBoardCloser(view)
//                true
//            }
//            false
//
//        }
        binding.btnRoundTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener =
                TimePickerDialog.OnTimeSetListener { view: TimePicker?, hour: Int, minute: Int ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    //     settingsViewModel?.setRoundTime(binding.btnRoundTime.text = SimpleDateFormat("mm:ss").format(cal.time)

                }
            TimePickerDialog(
                this.requireContext(),
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        binding.btnRestTime.setOnKeyListener { view, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                settingsViewModel?.setRestTime(binding.btnRestTime.text.toString())
                binding.btnRestTime.text.toString()
                keyBoardCloser(view)
                true
            }
            false

        }
        binding.btnRoundAmount.setOnKeyListener { view, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                settingsViewModel?.setRoundAmount(binding.btnRoundAmount.text.toString())
                binding.btnRoundAmount.text.toString()
                keyBoardCloser(view)
                true
            }
            false

        }

        binding.btnProfileName.setOnKeyListener { view, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                settingsViewModel?.setNewProfileName(binding.btnProfileName.text.toString())

                keyBoardCloser(view)
                true
            }
            false

        }

        binding.create.setOnClickListener {
            settingsViewModel?.createNewTimerError()

        }

        binding.delete.setOnClickListener {
            settingsViewModel?.deleteTimer()

        }

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun keyBoardCloser(view: View) {
        val keyBoardCloser =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        keyBoardCloser?.hideSoftInputFromWindow(view.windowToken, 0)

    }

    private fun spinnerRefresh() {

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                settingsViewModel?.onItemSelectedMethod(position)
            }

        }

        var customSpinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_view,
            settingsViewModel?.spinnerRefresh() ?: emptyList()
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_view)
            binding.spinner.adapter = adapter
        }


    }


}