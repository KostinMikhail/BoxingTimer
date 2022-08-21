package com.example.timeplateactivity.ui.home

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
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


    private fun playSound() {
        val mediaPlayer = MediaPlayer.create(this.requireContext(), R.raw.gongsound)
        mediaPlayer.start()
    }

    fun resumeTimer() {
        roundTimer(currentRoundTime!!, tick, whatRound!!)
    }

    fun resumeRestTimer() {
        restTimer(currentRestTime!!, tick)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor", "SetTextI18n")

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

        val spannable = SpannableStringBuilder(getString(R.string.secondsRemaining))
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this.requireContext(), R.color.yellow)),
            0,
            7,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
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

        fun spinnerRefresh() {
            val profiles: List<Profile> = userDao.getAll()
            val profilesTitles: ArrayList<String?> = arrayListOf()
            for (list in profiles) {
                profilesTitles.add(list.profileName)
            }



            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    currentProfile = profiles.get(position)
                    roundTime = currentProfile!!.roundTime!!
                    restTime = currentProfile!!.restTime!!
                    makeRounds = currentProfile!!.roundAmount!!
                    isDeleatableNow = currentProfile!!.isDeletable
                }

            }

            var customSpinnerAdapter = ArrayAdapter(
                this.requireContext(),
                android.R.layout.simple_spinner_item,
                profilesTitles
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinner.adapter = adapter
            }
        }
        spinnerRefresh()

        binding.nmbr.setOnClickListener {
            roundTimeString = binding.nmbr.text.toString()
            val roundTimeString1: Long = roundTimeString!!.toLong()
            roundTime = roundTimeString1 * 1000
        }

        binding.nmbr2.setOnClickListener {
            restTimeString = binding.nmbr2.text.toString()
            val restTimeString1: Long = restTimeString!!.toLong()
            restTime = restTimeString1 * 1000

        }
        binding.nmbr3.setOnClickListener {
            roundAmountString = binding.nmbr3.text.toString()
            val roundAmountString1: Int = roundAmountString!!.toInt()
            makeRounds = roundAmountString1
        }

        binding.nmbr4.setOnClickListener {
            profileName = binding.nmbr4.text.toString()
        }

        binding.create.setOnClickListener {
            userDao.insertAll(Profile(0, profileName, roundTime, restTime, makeRounds, true))
            Toast.makeText(this.requireContext(), "saved", LENGTH_LONG).show()
            spinnerRefresh()
        }
        binding.delete.setOnClickListener {
            if (isDeleatableNow) {
                userDao.delete(currentProfile!!)
                Toast.makeText(this.requireContext(), getString(R.string.deleted), LENGTH_LONG)
                    .show()
                spinnerRefresh()
            } else {
                Toast.makeText(
                    this.requireContext(),
                    getString(R.string.youCantDeleteThis),
                    LENGTH_LONG
                ).show()
            }
        }

        fun cancelTimer() {

            timer?.cancel()
        }

        binding.btnStart.setOnClickListener {
            //  beforeTimer(beforeTime, tick)                                                               //HERE NEW
            cancelTimer()
            roundTimer(roundTime, tick, setRoundsAmount1)
            timerRunning = true
            binding.btnPause.isVisible = true
            playSound()

            if (timerRunning) {
                binding.btnStart.setOnClickListener {
                    //    beforeTimer(beforeTime, tick)                                                       //HERE NEW
                    cancelTimer()
                    roundTimer(roundTime, tick, 1)
                    binding.btnPause.isVisible = true
                    binding.btnResume.isVisible = false
                    playSound()
                }

            } else {

            }

            val amountOfRounds: TextView = binding.amountOfRounds
            amountOfRounds.text = getString(R.string.showRound) + setRoundsAmount1
        }
        binding.btnPause.setOnClickListener {
            cancelTimer()
            binding.btnStart.setText(getString(R.string.start))
            binding.btnResume.isVisible = true
        }

        binding.btnResume.setOnClickListener {
            if (timerOnRest) {
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

    fun roundTimer(roundTime: Long, tick: Long, setRoundsAmount: Int) {

        val currentRound = setRoundsAmount
        binding.amountOfRounds.text = getString(R.string.showRound) + currentRound

        val spannable = SpannableStringBuilder(getString(R.string.secondsRemaining))
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this.requireContext(), R.color.yellow)),
            0,
            18,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.toString()


        timer = object : CountDownTimer(roundTime, tick) {

            @SuppressLint("ResourceAsColor")
            override fun onTick(roundTime: Long) {
                val round = Date(roundTime)
                val formatter = SimpleDateFormat("mm:ss")
                val a = formatter.format(round)

                currentRoundTime = roundTime
                whatRound = currentRound



                binding.textHome.setText(getString(R.string.secondRemaining) + a)

                //             binding.textHome.setTextColor(rgb(255, 255, 0))
            }

            override fun onFinish() {
                binding.amountOfRounds.text = " "

                if (currentRound < makeRounds) {
                    setRoundsAmount1++
                    restTimer(restTime, tick)

                } else {
                    binding.textHome.setText(getString(R.string.done))
                    binding.root.setBackgroundResource(R.drawable.green_gradient)

                    setRoundsAmount1 = 1
//                    binding.amountOfRounds.setText(getString(R.string.startAgain))
                    binding.btnPause.isVisible = false
                    binding.btnResume.isVisible = false
                }
            }
        }.start()
    }

    fun restTimer(restTime: Long, tick: Long) {

        var currentRound = setRoundsAmount1
        binding.amountOfRounds.text = getString(R.string.rest)

        timer = object : CountDownTimer(restTime, tick) {

            override fun onTick(restTime: Long) {
                val rest = Date(restTime)
                var formatter = SimpleDateFormat("mm:ss")
                val b = formatter.format(rest)
                binding.textHome.setText(getString(R.string.secondRemaining) + b)
                currentRestTime = restTime
                timerOnRest = true
            }

            override fun onFinish() {
                timerOnRest = false
                roundTimer(roundTime, tick, currentRound)
            }
        }.start()

    }

    fun beforeTimer(beforeTime: Long, tick: Long) {

        var currentRound = setRoundsAmount1
        binding.amountOfRounds.text = getString(R.string.firstRound)

        timer = object : CountDownTimer(beforeTime, tick) {

            override fun onTick(beforeTime: Long) {
                val before = Date(beforeTime)
                var formatter = SimpleDateFormat("mm:ss")
                val c = formatter.format(before)
                binding.textHome.setText(getString(R.string.getReady) + c)
//                currentRestTime = beforeTime
//                timerOnRest = true
            }

            override fun onFinish() {
//                timerOnRest = false
                roundTimer(roundTime, tick, currentRound)
            }
        }.start()

    }
}

/*
1) не использовать "!!", вместо него проверку через "?", через "let" или элвис-оператор ":?"
2) val и var
3) перенести в хоум вью модель
4)Private val
5) стринги - в ресурсы переделать
6) расставить скобки (ctrl+alt+l)
7) reformat code (ctrl+k для гита)
8) homefragment 93 строчка
удалить texthome: texthome, оставить только биндинг
9) создать фрагмент launch
10) ViewPager2 - онбординг (индикатор? нижняя фигня с переходом) sharePreferences start Android
11) написать калькулятор калорий (кастомный seekBar)

***************************
tips:
ctrl+shift+k = push
observe = подписка
использовать with (binding){

}
***************************
вопросы: приложение крашится, когда запускаю таймер и перехожу на новый фрагмент

***************************
ППР
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
 */