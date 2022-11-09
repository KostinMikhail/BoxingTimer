package com.example.timeplateactivity.ui.home

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.timeplateactivity.R


class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "05:00"
    }
    val text: LiveData<String> = _text



    @SuppressLint("ResourceAsColor")
    fun timerSpan() {
        val timerSpan =
            SpannableStringBuilder(null)
        val orangeColor = ForegroundColorSpan(R.color.primary)
        timerSpan.setSpan(orangeColor, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


    }


}