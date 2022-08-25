package com.example.timeplateactivity.ui.settings

import android.content.Context
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.boxingtimer.model.profile
import com.example.timeplateactivity.R
import com.example.timeplateactivity.data.repository.AppDatabase
import com.example.timeplateactivity.data.repository.Profile
import com.example.timeplateactivity.data.repository.ProfileDao

class SettingsViewModel(private val context: Context, private val db: AppDatabase) : ViewModel() {

    var roundTime: Long = 0
    var restTime: Long = 0
    var makeRounds: Int = 0

    var roundTimeString: String? = null
    var restTimeString: String? = null
    var roundAmountString: String? = null
    var profileName: String? = null

    var currentProfile: Profile? = null

    var isDeleatableNow: Boolean = false

    private val _errorData = MutableLiveData<String>()
    val errorData: LiveData<String> = _errorData

    private var profileDao: ProfileDao? = null

    private val _timerSuccsessCreated = MutableLiveData<String>()
    val timerSuccsessCreated: LiveData<String> = _timerSuccsessCreated

    init {

        profileDao = db?.profileDao()
    }

    fun createNewTimerError() {
        if (profileName == null) {
            _errorData.value = context?.getString(R.string.noName)
            return
        }
        if (roundAmountString == null) {
            _errorData.value = context?.getString(R.string.noRoundAmount)
            return
        }
        if (restTimeString == null) {
            _errorData.value = context?.getString(R.string.noRestTime)
            return
        }
        if (roundTimeString == null) {
            _errorData.value = context?.getString(R.string.noRoundTime)
            return
        }

        profileDao?.insertAll(
            Profile(
                uid = 0,
                profileName = profileName,
                roundTime = roundTime,
                restTime = restTime,
                roundAmount = makeRounds,
                isDeletable = false
            )
        )
        _timerSuccsessCreated.value = context?.getString(R.string.saved)

    }

    fun spinnerRefresh(): MutableList<String?> {
        val profiles: List<Profile> = profileDao?.let { profile ->
            profileDao?.getAll()
        } ?: emptyList()

        val profilesTitles: MutableList<String?> = arrayListOf()

        for (list in profiles) {
            profilesTitles.add(list.profileName)
        }
        return profilesTitles
    }

    fun deleteTimer() {
        if (isDeleatableNow == true) {
            _errorData.value = context?.getString(R.string.youCantDeleteThis)
            return
        }
        currentProfile?.let { profile ->
            profileDao?.delete(profile)
        }
        _timerSuccsessCreated.value = context?.getString(R.string.deleted)

    }

    fun setRoundTime(roundTimeString: String) {
        if (roundTimeString.isEmpty()) return

        val roundTimeString1: Long = roundTimeString.toLong()
        roundTime = roundTimeString1 * 1000
        this.roundTimeString = roundTimeString
    }

    fun setRestTime(restTimeString: String) {
        val restTimeString1: Long = restTimeString.toLong()
        restTime = restTimeString1 * 1000
        this.restTimeString = restTimeString
    }

    fun setRoundAmount(roundAmountString: String) {
        val roundAmountString1: Int = roundAmountString.toInt()
        makeRounds = roundAmountString1
        this.roundAmountString = roundAmountString
    }


    fun setNewProfileName(profileName2: String) {
        profileName = profileName2
    }

    fun onItemSelectedMethod(profilePosition: Int) {
        currentProfile = profileDao?.getAll()?.get(profilePosition)
        roundTime = currentProfile?.roundTime ?: 0
        restTime = currentProfile?.restTime ?: 0
        makeRounds = currentProfile?.roundAmount ?: 0
        isDeleatableNow = currentProfile?.isDeletable == true
    }


}






