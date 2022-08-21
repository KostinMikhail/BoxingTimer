package com.example.timeplateactivity.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.timeplateactivity.R
import com.example.timeplateactivity.data.repository.AppDatabase
import com.example.timeplateactivity.data.repository.Profile
import com.example.timeplateactivity.databinding.FragmentSettingsBinding


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
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        root.setBackgroundResource(R.drawable.blue_gradient)

        with(binding) {
        }


        val db = Room.databaseBuilder(
            this.requireContext(),
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries()
            .build()
        val userDao = db.profileDao()

        fun spinnerRefresh() {
            val profiles: List<Profile> = userDao.getAll()
            val profilesTitles: ArrayList<String?> = arrayListOf()
            for (list in profiles) {
                profilesTitles.add(list.profileName)
            }



            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {


                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    settingsViewModel?.onItemSelectedMethod()
                }

            }

            var customSpinnerAdapter = ArrayAdapter(
                this.requireContext(),
                android.R.layout.simple_spinner_item,
                profilesTitles
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinner.adapter = adapter
            }
        }
        spinnerRefresh()

        binding.btnRoundTime.setOnKeyListener { view, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                settingsViewModel?.setRoundTime()
                binding.btnRoundTime.text.toString()
                val keyBoardCloser =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                keyBoardCloser?.hideSoftInputFromWindow(view.windowToken, 0)
                true
            }
            false

        }
        //roundTimeString = binding.nmbr.text.toString()
        //
        //            settingsViewModel?.roundTimeString1()
        binding.btnRestTime.setOnKeyListener { view, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                settingsViewModel?.setRestTime()
                binding.btnRestTime.text.toString()
                val keyBoardCloser =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                keyBoardCloser?.hideSoftInputFromWindow(view.windowToken, 0)
                true
            }
            false

        }
        binding.btnRoundAmount.setOnKeyListener { view, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                settingsViewModel?.setRoundAmount()
                binding.btnRoundAmount.text.toString()
                val keyBoardCloser =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                keyBoardCloser?.hideSoftInputFromWindow(view.windowToken, 0)
                true
            }
            false

        }

        binding.btnProfileName.setOnKeyListener { view, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                settingsViewModel?.setProfileName() = binding.btnProfileName.text.toString()

                val keyBoardCloser =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                keyBoardCloser?.hideSoftInputFromWindow(view.windowToken, 0)
                true
            }
            false

        }
        /*{
            //было так       profileName = binding.btnProfileName.text.toString()
            settingsViewModel?.profileName = binding.btnProfileName.text.toString()
        }*/


        binding.create.setOnClickListener {
            if (settingsViewModel?.profileName == null) {
                Toast.makeText(this.requireContext(), getString(R.string.noName), Toast.LENGTH_LONG)
                    .show()
            } else if (settingsViewModel?.roundAmountString == null) {
                Toast.makeText(
                    this.requireContext(),
                    getString(R.string.noRoundAmount),
                    Toast.LENGTH_LONG
                ).show()
            } else if (settingsViewModel?.restTimeString == null) {
                Toast.makeText(
                    this.requireContext(),
                    getString(R.string.noRestTime),
                    Toast.LENGTH_LONG
                ).show()
            } else if (settingsViewModel?.roundTimeString == null) {
                Toast.makeText(
                    this.requireContext(),
                    getString(R.string.noRoundTime),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                userDao.insertAll(
                    Profile(
                        0, settingsViewModel?.profileName,
                        settingsViewModel?.roundTime,
                        settingsViewModel?.restTime,
                        settingsViewModel?.makeRounds,
                        true
                    )
                )
                Toast.makeText(this.requireContext(), getString(R.string.saved), Toast.LENGTH_LONG)
                    .show()
                spinnerRefresh()
            }

        }

        binding.delete.setOnClickListener {
            if (settingsViewModel?.isDeleatableNow == false) {
                userDao.delete(settingsViewModel?.currentProfile!!)
                Toast.makeText(
                    this.requireContext(),
                    getString(R.string.deleted),
                    Toast.LENGTH_LONG
                ).show()
                spinnerRefresh()
            } else {
                Toast.makeText(
                    this.requireContext(),
                    getString(R.string.youCantDeleteThis),
                    Toast.LENGTH_LONG


                ).show()
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}