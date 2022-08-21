package com.example.timeplateactivity.ui.settings

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


}