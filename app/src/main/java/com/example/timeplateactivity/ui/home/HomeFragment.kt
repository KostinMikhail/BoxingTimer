package com.example.timeplateactivity.ui.home

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.boxingtimer.model.profile
import com.example.timeplateactivity.R
import com.example.timeplateactivity.data.repository.AppDatabase
import com.example.timeplateactivity.data.repository.Profile
import com.example.timeplateactivity.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment(R.layout.fragment_home) {

   private var _binding: FragmentHomeBinding? = null
   private val binding get() = _binding!!

   var timer: CountDownTimer? = null
   var timerRunning: Boolean = false
   var timerOnRest: Boolean = false

   var roundTime: Long = 0
   var restTime: Long = 0
   val tick: Long = 1000
   var setRoundsAmount1: Int = 1
   var makeRounds: Int = 0

   var roundTimeString: String? = null
   var restTimeString: String? = null
   var roundAmountString: String? = null
   var profileName: String? = null

   var currentRoundTime: Long? = null
   var currentRestTime: Long? = null
   var whatRound: Int? = null

   var currentProfile: Profile? = null

   var isDeleatableNow: Boolean = false

   var mMediaPlayer: MediaPlayer? = null

 /*   fun playSound(){
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this.requireContext(), R.raw.gong)
             mMediaPlayer!!.start()
        } else {
            mMediaPlayer!!.start()
        }
    }*/


    fun resumeTimer(){
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

        val db = Room.databaseBuilder(
            this.requireContext(),
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries()
            .build()

        val userDao = db.profileDao()

        fun spinnerRefresh(){
        var profiles: List<Profile> = userDao.getAll()
        var profilesTitles: ArrayList<String?> = arrayListOf()
        for (list in profiles) {
            profilesTitles.add(list.profileName)
            }

//        binding.spinner.onItemSelectedListener

            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                 currentProfile = profiles.get(position)
                 roundTime = currentProfile!!.roundTime!!
                 restTime = currentProfile!!.restTime!!
                 makeRounds = currentProfile!!.roundAmount!!
                 isDeleatableNow = currentProfile!!.isDeletable
                }

            }

        var customSpinnerAdapter = ArrayAdapter (this.requireContext(),
            android.R.layout.simple_spinner_item,
            profilesTitles). also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
            }
        }
        spinnerRefresh()

        binding.nmbr.setOnClickListener{
            roundTimeString = binding.nmbr.text.toString()
            var roundTimeString1: Long = roundTimeString!!.toLong()
            roundTime = roundTimeString1 * 1000
        }

        binding.nmbr2.setOnClickListener{
            restTimeString = binding.nmbr2.text.toString()
            var restTimeString1: Long = restTimeString!!.toLong()
            restTime = restTimeString1 * 1000

        }
        binding.nmbr3.setOnClickListener{
            roundAmountString = binding.nmbr3.text.toString()
            var roundAmountString1: Int = roundAmountString!!.toInt()
            makeRounds = roundAmountString1
        }

        binding.nmbr4.setOnClickListener{
            profileName = binding.nmbr4.text.toString()
        }

        binding.create.setOnClickListener{
            userDao.insertAll(Profile(0, profileName, roundTime, restTime, makeRounds, true))
            Toast.makeText(this.requireContext(),"saved", LENGTH_LONG).show()
            spinnerRefresh()
        }
        binding.delete.setOnClickListener{
            if (isDeleatableNow){
            userDao.delete(currentProfile!!)
            Toast.makeText(this.requireContext(),"deleted", LENGTH_LONG).show()
            spinnerRefresh()
            } else {
            Toast.makeText(this.requireContext(),"you can't delete this", LENGTH_LONG).show()
            }
        }

        fun cancelTimer() {

            timer?.cancel()
        }

        binding.btnStart.setOnClickListener{
        roundTimer(roundTime, tick, setRoundsAmount1)
        timerRunning = true
        binding.btnPause.isVisible = true

        if (timerRunning) {
            binding.btnStart.setOnClickListener{
                cancelTimer()
                roundTimer(roundTime,tick, 1)
                binding.btnPause.isVisible = true
                binding.btnResume.isVisible = false
   //             playSound()
            }
        } else {

        }

            val amountOfRounds: TextView = binding.amountOfRounds
            amountOfRounds.text = "Round  " + setRoundsAmount1
                }  //amountofrounds.color
        binding.btnPause.setOnClickListener{
            cancelTimer()
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

                if (currentRound < makeRounds) {
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




//Threading
//Callbacks
//Futures, promises, and others
//Reactive extensions
//Coroutines


//звук сделать
//*цвет цифр в зависимости от времени


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
10) тестировка, отлов ошибок
 */