package com.example.timeplateactivity.ui.calc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.timeplateactivity.databinding.FragmentCalcBinding


class CalcViewModel : ViewModel() {

    var isMale: Boolean = true
    var height: Int? = null
    var age: Int? = null
    var weight: Int? = null
    var activity: Int? = null
    var result: Double? = null

    fun setMale() {
        isMale = true
    }

    fun setFemale() {
        isMale = false
    }

    fun calc() {
        if (isMale) {
            result = weight!! * 10 + 6.25 * height!! - 5 * age!! + 5 * activity!!

        } else {
            result = weight!! * 10 + 6.25 * height!! - 5 * age!! - 161 * activity!!
        }

    }


//    private val _text = MutableLiveData<String>().apply {
//        value = "nutriton calculator here"
//    }
//
//
//    val text: LiveData<String> = _text


}