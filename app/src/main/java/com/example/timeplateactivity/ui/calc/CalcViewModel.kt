package com.example.timeplateactivity.ui.calc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.timeplateactivity.databinding.FragmentCalcBinding


class CalcViewModel : ViewModel() {

    var isMale: Boolean = true
    var height: Int? = 177
    var age: Int? = 28
    var weight: Int? = 77
    var activity: Int? = 2
    var result: String? = null

    fun setMale() {
        isMale = true
    }

    fun setFemale() {
        isMale = false
    }

    fun calc() {
        if (isMale) {

            result = (weight!! * 10 + 6.25 * height!! - 5 * age!! + 5 * activity!!).toString()
        } else {
            result = (weight!! * 10 + 6.25 * height!! - 5 * age!! + 5 * activity!!).toString()
        }

    }


}