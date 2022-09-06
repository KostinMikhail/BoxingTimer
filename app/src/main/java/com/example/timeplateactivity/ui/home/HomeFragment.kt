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

    var currentRoundTime: Long? = null
    var currentRestTime: Long? = null
    var whatRound: Int? = null

    var currentProfile: Profile? = null

    var isDeleatableNow: Boolean = false
    var pauseBtnPushed: Boolean = true


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
        val textView = binding.timeTV



        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it

        }

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

        fun cancelTimer() {

            timer?.cancel()
        }

        binding.btnStart.setOnClickListener {

            //cancelTimer()
            roundTimer(roundTime, tick, setRoundsAmount1)
            timerRunning = true
            binding.btnStart.isVisible = false
            binding.btnPause.isVisible = true
            binding.btnStop.isVisible = true
            playSound()

            if (timerRunning) {
                binding.btnStart.setOnClickListener {

                    cancelTimer()
                    roundTimer(roundTime, tick, 1)
                    binding.btnStart.isVisible = false
                    binding.btnPause.isVisible = true
                    binding.btnStop.isVisible = true
                    playSound()
                }

            } else {

            }

            val amountOfRounds: TextView = binding.roundTV
            amountOfRounds.text =
                getString(R.string.showRound) + setRoundsAmount1 + "/" + makeRounds
            val roundString = amountOfRounds.toString()


        }

//        val spannable = SpannableStringBuilder(getString(roundString))
//        spannable.setSpan(
//            ForegroundColorSpan(ContextCompat.getColor(this.requireContext(), R.color.yellow)),
//            0,
//            7,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )

        binding.btnPause.setOnClickListener {

            if (pauseBtnPushed) {
                cancelTimer()
                pauseBtnPushed = false
                binding.btnPause.setText(R.string.resume)
            } else {
                if (timerOnRest) {
                    resumeRestTimer()
                    pauseBtnPushed = true
                    binding.btnPause.setText(R.string.pause)
                } else {
                    cancelTimer()
                    resumeTimer()
                    pauseBtnPushed = true
                    binding.btnPause.setText(R.string.pause)
                }
            }
        }
        binding.btnStop.setOnClickListener {
            cancelTimer()
            binding.btnStart.isVisible = true
            binding.btnStop.isVisible = false
            binding.btnPause.isVisible = false
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun roundTimer(roundTime: Long, tick: Long, setRoundsAmount: Int) {

        val currentRound = setRoundsAmount
        binding.roundTV.text = getString(R.string.showRound) + currentRound




        timer = object : CountDownTimer(roundTime, tick) {

            @SuppressLint("ResourceAsColor")
            override fun onTick(roundTime: Long) {
                val round = Date(roundTime)
                val formatter = SimpleDateFormat("mm:ss")
                val a = formatter.format(round)

                currentRoundTime = roundTime
                whatRound = currentRound



                binding.timeTV.setText(a)


            }

            override fun onFinish() {
                binding.roundTV.text = " "

                if (currentRound < makeRounds) {
                    setRoundsAmount1++
                    restTimer(restTime, tick)

                } else {
                    binding.timeTV.setText(" ")
                    setRoundsAmount1 = 1
                    binding.btnStart.isVisible = true
                    binding.btnPause.isVisible = false
                    binding.btnStop.isVisible = false

                }
            }
        }.start()
    }

    fun restTimer(restTime: Long, tick: Long) {

        var currentRound = setRoundsAmount1
        binding.roundTV.text = getString(R.string.rest)

        timer = object : CountDownTimer(restTime, tick) {

            override fun onTick(restTime: Long) {
                val rest = Date(restTime)
                var formatter = SimpleDateFormat("mm:ss")
                val b = formatter.format(rest)
                binding.timeTV.setText(b)
                currentRestTime = restTime
                timerOnRest = true
            }

            override fun onFinish() {
                timerOnRest = false
                roundTimer(roundTime, tick, currentRound)
            }
        }.start()

    }


}

/*

1) не использовать "!!", вместо него проверку через "?", через "let" или элвис-оператор ":?"
2)+ val и var
3) перенести в хоум вью модель
4)+ Private val
5)+ стринги - в ресурсы переделать
6)+ расставить скобки (ctrl+alt+l)
7)+ reformat code (ctrl+k для гита)
8)+ homefragment 85 строчка удалить texthome: texthome, оставить только биндинг
9) создать фрагмент launch
10) ViewPager2 - онбординг (индикатор? нижняя фигня с переходом) sharePreferences start Android
11) написать калькулятор калорий (кастомный seekBar)
12)+ вместо setOnClickListner заменить на setOnKeyListner
13) with (binding)
14) создать livedata sucsessCreateNewTimer, по аналогии с еррором (settingsViewModl)
15) создать LiveData во вьюМодели и перенести туда спиннерРефреш

calc
1) action bar - надпись "калькулятор калорий"
2)
3) progressChangeListner - с сикбара
//todo сделать калькулятор
***************************
tips:
ctrl+k = commit
ctrl+shift+k = push
observe = подписка    settingsViewModel.error.Observe(viewLifecuycleOwner){
toast.maketext
binding.textHome.setText
ctrl+alt+l = расставить скобки
}
использовать with (binding){} (если страдает requareContext = убрать THIS)
всё, что мы возвращаем из вьюмодели во фрагмент - возвращаем через лайвдата
shift+F6 = переименование сразу во всём проекте
ctrl+p = справка, что запихнуть в скобки
}
***************************

вопросы:
1) заменить на кнопке старт надпись на иконку
отрисовал для неё новый drawable. с иконкой плей посередине? как саму иконку добавить в дровабл? (в ресурсах она есть уже у меня)
2) в хоум фрагменте сделать цифры по всей ширине экрана, но что бы они помещались и не съедались
3) там же сделать "раунд" поближе
4) уменьшить расстояние между кнопками
5) как реализовать вместо спиннера менюшку снизу, которая выскакивает с выбором режимов? "меню режимов"
6) span для цифр в textView


14) приложение крашится, когда запускаю таймер и перехожу на новый фрагмент
***************************
ППР
1) 15.09 приложение
2) 20.09 собесы
 */