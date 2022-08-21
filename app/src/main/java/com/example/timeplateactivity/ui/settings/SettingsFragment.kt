package com.example.timeplateactivity.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
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

    var roundTime: Long = 0
    var restTime: Long = 0
    var makeRounds: Int = 0

    var roundTimeString: String? = null
    var restTimeString: String? = null
    var roundAmountString: String? = null
    var profileName: String? = null

    var currentProfile: Profile? = null

    var isDeleatableNow: Boolean = false

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        root.setBackgroundResource(R.drawable.blue_gradient)

        val db = Room.databaseBuilder(
            this.requireContext(),
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries()
            .build()
        val userDao = db.profileDao()

        fun spinnerRefresh(){
            var profiles: List<Profile> = userDao.getAll()
            var profilesTitles: ArrayList<String?> = arrayListOf()
            for (list in profiles) {
                profilesTitles.add(list.profileName)
            }



            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    currentProfile = profiles.get(position)
                    roundTime = currentProfile!!.roundTime!!
                    restTime = currentProfile!!.restTime!!
                    makeRounds = currentProfile!!.roundAmount!!
                    isDeleatableNow = currentProfile!!.isDeletable
                }

            }

            var customSpinnerAdapter = ArrayAdapter (this.requireContext(),
                android.R.layout.simple_spinner_item,
                profilesTitles). also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinner.adapter = adapter
            }
        }
        spinnerRefresh()

        binding.nmbr.setOnClickListener{
            roundTimeString = binding.nmbr.text.toString()
            var roundTimeString1: Long = roundTimeString!!.toLong()
            roundTime = roundTimeString1 * 1000
        }

        binding.nmbr2.setOnClickListener{
            restTimeString = binding.nmbr2.text.toString()
            var restTimeString1: Long = restTimeString!!.toLong()
            restTime = restTimeString1 * 1000

        }
        binding.nmbr3.setOnClickListener{
            roundAmountString = binding.nmbr3.text.toString()
            val roundAmountString1: Int = roundAmountString!!.toInt()
            makeRounds = roundAmountString1
        }

        binding.nmbr4.setOnClickListener{
            profileName = binding.nmbr4.text.toString()
        }


        binding.create.setOnClickListener{
            if (profileName == null){
                Toast.makeText(this.requireContext(),getString(R.string.noName), Toast.LENGTH_LONG).show()
            } else if(roundAmountString == null){
                Toast.makeText(this.requireContext(),getString(R.string.noRoundAmount), Toast.LENGTH_LONG).show()
            } else if(restTimeString == null){
                Toast.makeText(this.requireContext(),getString(R.string.noRestTime), Toast.LENGTH_LONG).show()
            } else if(roundTimeString == null){
                Toast.makeText(this.requireContext(),getString(R.string.noRoundTime), Toast.LENGTH_LONG).show()
            }

            else {
                userDao.insertAll(Profile(0, profileName, roundTime, restTime, makeRounds, true))
                Toast.makeText(this.requireContext(),getString(R.string.saved), Toast.LENGTH_LONG).show()
                spinnerRefresh()
            }

        }

        binding.delete.setOnClickListener{
            if (isDeleatableNow){
                userDao.delete(currentProfile!!)
                Toast.makeText(this.requireContext(),getString(R.string.deleted), Toast.LENGTH_LONG).show()
                spinnerRefresh()
            } else {
                Toast.makeText(this.requireContext(),getString(R.string.youCantDeleteThis), Toast.LENGTH_LONG).show()
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}