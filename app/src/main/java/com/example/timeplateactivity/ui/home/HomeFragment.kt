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
import androidx.core.view.isVisible
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
import kotlin.properties.Delegates

class HomeFragment : Fragment(R.layout.fragment_home) {

   private var _binding: FragmentHomeBinding? = null
   private val binding get() = _binding!!

   var timer: CountDownTimer? = null
   var timerRunning: Boolean = false
   var timerOnRest: Boolean = false

   var roundTime: Long = 4000
   var restTime: Long = 8000
   val tick: Long = 1000
   var setRoundsAmount1: Int = 1

   var currentRoundTime: Long? = null
   var currentRestTime: Long? = null
   var whatRound: Int? = null

//   var timerStatus: Int = 0 //0 is for not in work, 1 is for roundTime, 2 is for restTime


    fun resumeTimer(){
//        timerStatus = 1
        roundTimer(currentRoundTime!!, tick, whatRound!!)

    }

    fun resumeRestTimer(){
        restTimer(currentRestTime!!, tick)
    }

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

        fun cancelTimer() {
//            timerStatus = 0
            timer?.cancel()
        }



        binding.btnStart.setOnClickListener{
        roundTimer(roundTime, tick, setRoundsAmount1)
        timerRunning = true
        binding.btnPause.isVisible = true
//        timerStatus = 1

        if (timerRunning) {
            binding.btnStart.setOnClickListener{
                cancelTimer()
                roundTimer(roundTime,tick, 1)
                binding.btnPause.isVisible = true
                binding.btnResume.isVisible = false
//                timerStatus = 1

            }


        }   else {

        }

 /*       if (timerOnStop){

        }   else {

        }*/

            val amountOfRounds: TextView = binding.amountOfRounds
            amountOfRounds.text = "Round  " + setRoundsAmount1

                }
        binding.btnPause.setOnClickListener{
            cancelTimer()
//            timerStatus = 0
            binding.btnStart.setText("restart")
            binding.btnResume.isVisible = true

        }



        binding.btnResume.setOnClickListener{
            if (timerOnRest){
                resumeRestTimer()
            } else {
            cancelTimer()
            resumeTimer()
            }
        }



        return root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


    fun roundTimer (roundTime: Long, tick: Long,  setRoundsAmount: Int ){

        var currentRound = setRoundsAmount
        binding.amountOfRounds.text = "Round  " + currentRound

        timer =  object : CountDownTimer(roundTime, tick) {

            override fun onTick(roundTime: Long) {
                val round = Date(roundTime)
                var formatter = SimpleDateFormat("mm:ss")
                val a = formatter.format(round)
                currentRoundTime = roundTime
                whatRound = currentRound


                binding.textHome.setText("seconds remaining: " + a)


            }

            override fun onFinish() {
                binding.amountOfRounds.text = "Round  " + currentRound

                if (currentRound < 3) {
                    setRoundsAmount1 ++
                    restTimer (restTime, tick)

                } else {

                    binding.textHome.setText("done!")
                    binding.root.setBackgroundResource(R.drawable.green_gradient)
//                    binding.btnStart.isEnabled = true
                    setRoundsAmount1 = 1
                    binding.amountOfRounds.setText("start again")
                    binding.btnPause.isVisible = false
                    binding.btnResume.isVisible = false
                }

            }
        }.start()

    }
    fun restTimer (restTime: Long, tick: Long){

        var currentRound = setRoundsAmount1
        binding.amountOfRounds.text = "Rest"

        timer =  object : CountDownTimer(restTime, tick) {

            override fun onTick(restTime: Long) {
                val rest = Date(restTime)
                var formatter = SimpleDateFormat("mm:ss")
                val b = formatter.format(rest)
                binding.textHome.setText("resting: " + b)
                currentRestTime = restTime
                timerOnRest = true


            }

            override fun onFinish() {
                timerOnRest = false
                roundTimer(roundTime, tick, currentRound )

            }
        }.start()

    }

}
/*
1) сделать кнопку пауза  24.04
2) кнопку продолжить/стоп 01.05 *так же продолжать с отдыха
3) создать возможность настраивать раунды   08.05     читать: sqlite, room
4) подвязать раунды к режимам 12.05
5) изменить цвет цифр в зависимости от времени, изменить цвет режима 15.05
6) добавить звук начала/конца раунда  17.05
7) добавить 5 сек перед началом старта таймера 19.05
8) добавить счётчик калорий  22.05

9) раскидать всё по разным директориям (clean)
 */