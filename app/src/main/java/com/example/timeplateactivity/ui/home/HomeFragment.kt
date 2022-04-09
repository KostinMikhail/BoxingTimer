package com.example.timeplateactivity.ui.home

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.example.timeplateactivity.databinding.FragmentHomeBinding
import java.lang.String.format
import java.text.MessageFormat.format
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        object : CountDownTimer(300000, 1000) {
// метод ChangeColour1 - изменить цвет, когда начинается онтик, когда он заканчивается, вернуть вьюбэкграунд колор = грин
            override fun onTick(millisUntilFinished: Long) {
            val date = Date(millisUntilFinished)
                var formatter = SimpleDateFormat("mm:ss")
               val a = formatter.format(date)

                textView.setText("seconds remaining: " + a)

            }

            override fun onFinish() {
// метод ChangeColour2 - изменить цвет, когда начинается онтик, когда он заканчивается, вернуть вьюбэкграунд колор = грин
                textView.setText("done!")
            }
        }.start()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}