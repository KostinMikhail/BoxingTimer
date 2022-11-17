package com.kostlin.timeplateactivity.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.kostlin.timeplateactivity.data.repository.AppDatabase

class SettingsViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(context, createDb()) as T

    }

    fun createDb(): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries()
            .build()
    }
}