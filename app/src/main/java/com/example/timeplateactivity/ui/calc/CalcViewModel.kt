package com.example.timeplateactivity.ui.calc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalcViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "nutriton calculator here"
    }
    val text: LiveData<String> = _text
}