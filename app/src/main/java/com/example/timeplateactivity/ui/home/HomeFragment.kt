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
   var timerRunning: Boolean = false
   var timePlus: Long = 1000


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
        newTimer(3000 + timePlus, 1000, 1000, 1)
        if (timerRunning) {

        }   else {
            timerRunning = false
            binding.btnStart.isEnabled = false
        }

            val amountOfRounds: TextView = binding.amountOfRounds
            amountOfRounds.text = "Round  " + setRoundsAmount

                }
        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
    fun newTimer (roundTime: Long, tick: Long, timePlus: Long, setRoundsAmount: Int){

        var currentRound = setRoundsAmount
        binding.amountOfRounds.text = "Round  " + currentRound

        timer =  object : CountDownTimer(roundTime, tick) {

            override fun onTick(roundTime: Long) {
                val date = Date(roundTime)
                var formatter = SimpleDateFormat("mm:ss")
                val a = formatter.format(date)
                binding.textHome.setText("seconds remaining: " + a)

            }

            override fun onFinish() {
                binding.amountOfRounds.text = "Round  " + currentRound

                if (currentRound < 4) {
                    currentRound ++
                    newTimer(roundTime, 1000, 1000,currentRound )

                } else {

                    binding.textView2.setText("done!")
                    binding.root.setBackgroundResource(R.drawable.green_gradient)
                    binding.btnStart.isEnabled = true
                    currentRound = 1
                    binding.amountOfRounds.setText("start again")

                }
                /*
                                   if (setRoundsAmount < 4) {
                                       timer!!.start()

                                   } else {
                                       textView.setText("done!")
                                       root.setBackgroundResource(R.drawable.blue_gradient)
                                       binding.btnStart.isEnabled = true
                                       setRoundsAmount = 1
                                       binding.amountOfRounds.setText ("start again")

                                  }
*/
            }
        }.start()

    }
}
