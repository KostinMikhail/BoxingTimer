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
import kotlinx.coroutines.NonCancellable.start
import java.lang.String.format
import java.text.MessageFormat.format
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment(R.layout.fragment_home) {

   private var _binding: FragmentHomeBinding? = null
   private val binding get() = _binding!!
   var setRoundsAmount: Int = 1
   var timer: CountDownTimer? = null
    var isDoingTimer: Boolean = false

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
        root.setBackgroundResource(R.drawable.blue_gradient)

        binding.btnStart.setOnClickListener{
        if (isDoingTimer){

        } else {
            binding.btnStart.isEnabled = false
            isDoingTimer = true
        }

            val amountOfRounds: TextView = binding.amountOfRounds
            amountOfRounds.text = "Round  " + setRoundsAmount

            timer =  object : CountDownTimer(3000, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    val date = Date(millisUntilFinished)
                    var formatter = SimpleDateFormat("mm:ss")
                    val a = formatter.format(date)
                    textView.setText("seconds remaining: " + a)
                }

                override fun onFinish() {

                    setRoundsAmount ++
                    amountOfRounds.text = "Round  " + setRoundsAmount

                    if (setRoundsAmount < 4) {
                        timer!!.start()
                    } else {
                        textView.setText("done!")
                        root.setBackgroundResource(R.drawable.blue_gradient)
                        isDoingTimer = false
                       binding.btnStart.isEnabled = true
                       setRoundsAmount = 1
                       amountOfRounds.text = "done"
                   }

                }
            }.start()
                root.setBackgroundResource(R.drawable.green_gradient)

                }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}