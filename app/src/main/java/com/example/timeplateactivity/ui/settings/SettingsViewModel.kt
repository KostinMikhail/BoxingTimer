package com.example.timeplateactivity.ui.settings

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.timeplateactivity.data.repository.Profile

class SettingsViewModel : ViewModel() {

    var roundTime: Long = 0
    var restTime: Long = 0
    var makeRounds: Int = 0

    var roundTimeString: String? = null
    var restTimeString: String? = null
    var roundAmountString: String? = null
    var profileName: String? = null

    var currentProfile: Profile? = null

    var isDeleatableNow: Boolean = false


    fun setRoundTime() {
        val roundTimeString1: Long = roundTimeString!!.toLong()
        roundTime = roundTimeString1 * 1000

    }

    fun setRestTime(){
        val restTimeString1: Long = restTimeString!!.toLong()
        restTime = restTimeString1 * 1000
    }

    fun setRoundAmount(){
        val roundAmountString1: Int = roundAmountString!!.toInt()
        makeRounds = roundAmountString1
    }
    fun setProfileName(){
        var profileName1: String = profileName?: "new Profile"
    }

    fun onItemSelectedMethod(){
        currentProfile = profiles.get(position)
        roundTime = currentProfile?.roundTime ?: 0
        restTime = currentProfile?.restTime ?: 0
        makeRounds = currentProfile?.roundAmount ?: 0
        isDeleatableNow = currentProfile?.isDeletable == true
    }

}
