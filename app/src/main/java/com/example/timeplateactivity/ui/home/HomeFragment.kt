package com.example.timeplateactivity.ui.home

import android.annotation.SuppressLint
import android.graphics.Color.rgb
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.boxingtimer.model.profile
import com.example.timeplateactivity.R
import com.example.timeplateactivity.data.repository.AppDatabase
import com.example.timeplateactivity.data.repository.Profile
import com.example.timeplateactivity.databinding.FragmentHomeBinding
import com.google.android.material.color.MaterialColors.getColor
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
   var beforeTime: Long = 5

   var roundTimeString: String? = null
   var restTimeString: String? = null
   var roundAmountString: String? = null
   var profileName: String? = null

   var currentRoundTime: Long? = null
   var currentRestTime: Long? = null
   var whatRound: Int? = null

   var currentProfile: Profile? = null

   var isDeleatableNow: Boolean = false



    fun playSound() {
        var  mediaPlayer = MediaPlayer.create(this.requireContext(),R.raw.gongsound)
        mediaPlayer.start()
    }

    fun resumeTimer(){
        roundTimer(currentRoundTime!!, tick, whatRound!!)
    }

    fun resumeRestTimer(){
        restTimer(currentRestTime!!, tick)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")

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

        val spannable = SpannableStringBuilder("seconds remaining: ")
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this.requireContext(), R.color.yellow)),
            0,
            7,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        spannable.setSpan(RelativeSizeSpan(2f), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

//        binding.span.setText("hey" + spannable)

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
        beforeTimer(beforeTime, tick)                                                               //HERE NEW
        roundTimer(roundTime, tick, setRoundsAmount1)
        timerRunning = true
        binding.btnPause.isVisible = true
            playSound()

        if (timerRunning) {
            binding.btnStart.setOnClickListener{
                beforeTimer(beforeTime, tick)                                                       //HERE NEW
                cancelTimer()
                roundTimer(roundTime,tick, 1)
                binding.btnPause.isVisible = true
                binding.btnResume.isVisible = false
                playSound()
            }
        } else {

        }

            val amountOfRounds: TextView = binding.amountOfRounds
            amountOfRounds.text = "Round  " + setRoundsAmount1
                }
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

        var spannable = SpannableStringBuilder("seconds remaining: ")
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this.requireContext(), R.color.yellow)),
            0,
            18,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.toString()


        timer =  object : CountDownTimer(roundTime, tick) {

            @SuppressLint("ResourceAsColor")
            override fun onTick(roundTime: Long) {
                val round = Date(roundTime)
                var formatter = SimpleDateFormat("mm:ss")
                val a = formatter.format(round)

                currentRoundTime = roundTime
                whatRound = currentRound



                binding.textHome.setText("seconds remaining: " + a)

   //             binding.textHome.setTextColor(rgb(255, 255, 0))
            }

            override fun onFinish() {
                binding.amountOfRounds.text = "Round  " + currentRound

                if (currentRound < makeRounds) {
                    setRoundsAmount1 ++
                    restTimer (restTime, tick)

                } else {
                    binding.textHome.setText("done!")
                    binding.root.setBackgroundResource(R.drawable.green_gradient)

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

    fun beforeTimer (beforeTime: Long, tick: Long){

        var currentRound = setRoundsAmount1
        binding.amountOfRounds.text = "First round"

        timer =  object : CountDownTimer(beforeTime, tick) {

            override fun onTick(beforeTime: Long) {
                val before = Date(beforeTime)
                var formatter = SimpleDateFormat("mm:ss")
                val c = formatter.format(before)
                binding.textHome.setText("get ready: " + c)
//                currentRestTime = beforeTime
//                timerOnRest = true
            }

            override fun onFinish() {
//                timerOnRest = false
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



//mvvm архитектура


/*
1) сделать кнопку пауза  24.06
2) кнопку продолжить/стоп 01.06 *так же продолжать с отдыха
3) создать возможность настраивать раунды   08.07     читать: sqlite, room
4) подвязать раунды к режимам 12.07
5) изменить цвет цифр в зависимости от времени, изменить цвет режима 15.07
6) добавить звук начала/конца раунда  17.07
7) добавить 5 сек перед началом старта таймера 24.07
8) добавить счётчик калорий  05.08

9) раскидать всё по разным директориям (clean) 05.08
10) тестировка, отлов ошибок 16.08
11) выложить в гугл плей 29.08

не использовать "!!", вместо него проверку через "?", через "let" или элвис-оператор ":?"
val и var
в лэйаутах - в ресурсы переделать
 */