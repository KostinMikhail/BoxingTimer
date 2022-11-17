package com.kostlin.timeplateactivity.domain.usecase

import android.R
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.kostlin.timeplateactivity.data.repository.Profile

class SpinnerRefresh {
 /*   fun spinnerRefresh(){
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
            R.layout.simple_spinner_item,
            profilesTitles). also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }
    }
    spinnerRefresh()*/
}