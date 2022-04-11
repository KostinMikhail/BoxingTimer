package com.example.timeplateactivity.ui.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.timeplateactivity.R

import com.example.timeplateactivity.databinding.FragmentHomeBinding
import java.lang.String.format
import java.text.MessageFormat.format
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment(R.layout.fragment_home) {

   private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )  : View {



        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it

        }
        root.setBackgroundColor(Color.GREEN)

        binding.btnStart.setOnClickListener{

            object : CountDownTimer(3000, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    val date = Date(millisUntilFinished)
                    var formatter = SimpleDateFormat("mm:ss")
                    val a = formatter.format(date)

                    textView.setText("seconds remaining: " + a)

                }

                override fun onFinish() {
// метод ChangeColour2 - изменить цвет, когда начинается онтик, когда он заканчивается, вернуть вьюбэкграунд колор = грин
                    textView.setText("done!")
                    root.setBackgroundColor(Color.GREEN)
                }
            }.start()
                root.setBackgroundColor(Color.RED)


                }





        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}