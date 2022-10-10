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
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.timeplateactivity.R
import com.example.timeplateactivity.data.repository.AppDatabase
import com.example.timeplateactivity.data.repository.Profile
import com.example.timeplateactivity.databinding.FragmentHomeBinding
import com.example.timeplateactivity.ui.BottomSheet.ActionBottom
import com.example.timeplateactivity.ui.BottomSheet.BottomSheetFragment
import com.example.timeplateactivity.ui.BottomSheet.ItemClickListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment(R.layout.fragment_home), ItemClickListener {

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

    fun spannable(text: String): Spannable {
        val spannable = SpannableStringBuilder(text)
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this.requireContext(), R.color.primary)),
            0,
            2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
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


        val db = Room.databaseBuilder(
            this.requireContext(),
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries()
            .build()

        val userDao = db.profileDao()

        val bottomSheetFragment = BottomSheetFragment()


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

                    val startTime = Date(roundTime)
                    var formatter = SimpleDateFormat("mm:ss")
                    val c = formatter.format(startTime)


                    binding.timeTV.setText(spannable(c))


                }

            }


            var customSpinnerAdapter = ArrayAdapter(
                this.requireContext(),
                R.layout.spinner_view,
                profilesTitles
            ).also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_view)


                binding.spinner.adapter = adapter

            }
        }
        spinnerRefresh()

        fun cancelTimer() {

            timer?.cancel()
        }

        binding.btnStart.setOnClickListener {


            cancelTimer()
            roundTimer(roundTime, tick, setRoundsAmount1)
            timerRunning = true
            binding.groupStart.isGone = true
            binding.groupPause.isGone = false
            binding.groupStop.isGone = false
            playSound()
            binding.greenBG.isGone = false

//            binding.greenBG.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.yellow_bg, null))


            if (timerRunning) {
                binding.btnStart.setOnClickListener {

                    cancelTimer()
                    roundTimer(roundTime, tick, 1)
                    binding.groupStart.isGone = true
                    binding.groupPause.isGone = false
                    binding.groupStop.isGone = false
                    playSound()
                    binding.greenBG.isGone = false
                }

            } else {

            }

            val amountOfRounds: TextView = binding.roundTV
            amountOfRounds.text =
                getString(R.string.showRound) + setRoundsAmount1 + "/" + makeRounds
            val roundString = amountOfRounds.toString()
            //сделать, что бы раунды показывались всегда через слэш, даже во время отдыха

        }


        binding.btnPause.setOnClickListener {

            if (pauseBtnPushed) {
                cancelTimer()
                pauseBtnPushed = false
                binding.imgPause.isGone = true
                binding.imgPlayPause.isGone = false
            } else {
                if (timerOnRest) {
                    resumeRestTimer()
                    pauseBtnPushed = true
                    binding.imgPause.isGone = false
                    binding.imgPlayPause.isGone = true
                } else {
                    cancelTimer()
                    resumeTimer()
                    pauseBtnPushed = true
                    binding.imgPause.isGone = false
                    binding.imgPlayPause.isGone = true

                }
            }
        }
        binding.btnStop.setOnClickListener {
            cancelTimer()
            binding.groupStart.isGone = false
            binding.groupStop.isGone = true
            binding.groupPause.isGone = true
            binding.imgPlayPause.isGone = true
            binding.groupBG.isGone = true
            setRoundsAmount1 = 1
        }







        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profile.setOnClickListener { openBottomSheet() }
    }

    fun openBottomSheet() {
        val addPhotoBottomDialogFragment = ActionBottom.newInstance(this)
        addPhotoBottomDialogFragment.show(
            childFragmentManager, ActionBottom.TAG
        )
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
                binding.timeTV.setText(spannable(a))
                when {
                    currentRoundTime!! <= 3 ->
                        binding.greenBG.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.yellow_bg,
                                null
                            )
                        )
                }

            }

            override fun onFinish() {
                binding.roundTV.text = " "

                if (currentRound < makeRounds) {
                    setRoundsAmount1++
                    restTimer(restTime, tick)

                } else {
                    binding.timeTV.setText(" ")
                    setRoundsAmount1 = 1
                    binding.groupStart.isGone = false
                    binding.groupPause.isGone = true
                    binding.groupStop.isGone = true
                    binding.groupBG.isGone = true
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
                val spannable = SpannableStringBuilder(b)
                spannable.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.primary)),
                    0,
                    2,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.timeTV.setText(spannable)
                currentRestTime = restTime
                timerOnRest = true
            }


            override fun onFinish() {
                timerOnRest = false
                roundTimer(roundTime, tick, currentRound)
//TODO кнопки должны меняться на старт
            }
        }.start()


    }

    override fun onStop() {
        super.onStop()
    }

    fun test(value: Int, name: String): Unit {
        return
    }

    override fun onItemClick(item: String?) {

    }

}

/*
вопросы:
1) в bottomsheet_fragment как сделать фон диалога прозрачным?
2) как в bottomsheet закинуть мой список режимов?


1) span для треугльничка в текствью (CalcFragment) как сделать его меньще?
ответ: лучше сделать новый текствью с нужным размером

2) к вопросу № 3!  почему мой BottomSheetFragment не видит элементы, которые есть у меня в bottomsheet_fragment?
не могу к ним обращаться в классе
Ответ: был указан не тот класс, теперь можно обращатсья к этим элементам.
Подсказка: как вариант передавать информацию между активити через Instanse
Подсказка: как передать информацию из фрагмента в диалог

3) к вопросу № 4! у меня есть nav_header_main, нужно сделать его биндинг в мэйнактивити и оттуда уже управлять
его элементами. как это сделать?
ответ: попробовать https://stackoverflow.com/questions/42220041/how-to-intent-image-button-in-navigation-drawer-header
Подсказка: попробовать гуглить "управление Header" или Image Button In Navigation Drawer Header

4) прописываю условия смены цветов BackGround у таймера. Блоки if else, как ему передать условия
 текущего значения currentRoundTime? типо если оно меньше 3 - меняешь цвет, если меньше 1 - ещё раз меняешь
 Ответ: сделать 1 ImgView и просто менять у него ресурс BG, если он не нужен - прописывать ему isGone
 блок When 267 строчка, разобраться с вьюхами и мб заработает


5) к вопросу № 7! supportActionBar?.hide() самое нормальное, что нашёл, но он применяется только
в мэйн активити и ничего не делает
Ответ: гуглить

6) почему калькулятор не считает, а выдаёт null?
как в спиннер в калькуляторе передать string значения, а не перечислять? использовать не arrayAdapter?


Задачи:

1)подогнать размеры под дизайн, сделать что бы раунд отображался всегда через / с количеством оставшихся раундов

3) как реализовать вместо спиннера менюшку снизу, которая выскакивает с выбором режимов? "меню режимов"
Это в таймере в хоум фрагменте
3.1) по нажатию на spinner (он должен быть TextView, с выводимым режимом, а не спиннер) должна открываться
менюшка выбора режимов
Ответ: https://developer.android.com/reference/com/google/android/material/bottomsheet/BottomSheetDialogFragment
сделать кастомный диалог, сверстать для него xml


4) когда открываю боковую меню - добавил туда крестик. как сделать, что бы он цеплялся к концу этого меню
как повесить на него "закрытие" этой вьюхи и переход к уже выбранному фрагменту. и где вообще
редактируется то, что там находится в коде? мне нужно добавить туда textView с названием режима
что бы он показывал текущий режим
ответ: гуглить drawer android
либо создавать новый фрагмент


7) как убрать actionBar из фрагмента HomeFragment
ответ: загуглить, как его убрать с определённой вьюхи

8) как перенести настройки в боковое меню
ответ: гуглить drawer android


14) приложение крашится, когда запускаю таймер и перехожу на новый фрагмент
***************************
Задачи от Димы:

1) не использовать "!!", вместо него проверку через "?", через "let" или элвис-оператор ":?"
2) перенести в хоум вью модель
3) создать фрагмент launch
4) ViewPager2 - онбординг (индикатор? нижняя фигня с переходом) sharePreferences start Android
5) написать калькулятор калорий (кастомный seekBar)
6) with (binding)
7) создать livedata sucsessCreateNewTimer, по аналогии с еррором (settingsViewModl)
8) создать LiveData во вьюМодели и перенести туда спиннерРефреш
***************************
tips:

ctrl+k = commit
ctrl+shift+k = push
observe = подписка    settingsViewModel.error.Observe(viewLifecuycleOwner){
toast.maketext
binding.textHome.setText
ctrl+alt+l = расставить скобки
использовать with (binding){} (если страдает requareContext = убрать THIS)
всё, что мы возвращаем из вьюмодели во фрагмент - возвращаем через лайвдата
shift+F6 = переименование сразу во всём проекте
ctrl+p = справка, что запихнуть в скобки
заменять на appCompat, если что-то не работает
***************************
ППР:

1) 15.09 приложение
2) 20.09 собесы
 */