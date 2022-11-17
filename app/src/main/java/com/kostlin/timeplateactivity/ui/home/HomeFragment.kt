package com.kostlin.timeplateactivity.ui.home

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
import com.kostlin.timeplateactivity.R
import com.kostlin.timeplateactivity.data.repository.AppDatabase
import com.kostlin.timeplateactivity.data.repository.Profile
import com.kostlin.timeplateactivity.databinding.FragmentHomeBinding
import com.kostlin.timeplateactivity.ui.BottomSheet.ActionBottom
import com.kostlin.timeplateactivity.ui.BottomSheet.BottomSheetFragment
import com.kostlin.timeplateactivity.ui.BottomSheet.ItemClickListener
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
        playSound()
    }

    fun resumeRestTimer() {
        restTimer(currentRestTime!!, tick)
        playSound()
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
                    binding.roundTV.setText("Раунд " + "1/" + makeRounds)
                }
            }

            var customSpinnerAdapter = ArrayAdapter(
                this.requireContext(),
                R.layout.spinner_view_home,
                profilesTitles
            ).also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_view_home)
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
            val startTime = Date(roundTime)
            var formatter = SimpleDateFormat("mm:ss")
            val c = formatter.format(startTime)
            binding.timeTV.setText(spannable(c))
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        binding.roundTV.text = getString(R.string.showRound) + setRoundsAmount1 + "/" + makeRounds

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
                    currentRoundTime!! <= 10000 ->
                        binding.greenBG.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.red_bg,
                                null
                            )
                        )
                    currentRoundTime!! <= 60000 ->
                        binding.greenBG.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.yellow_bg,
                                null
                            )
                        )
                    currentRoundTime!! >= 60000 ->
                        binding.greenBG.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.green_bg,
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
                    playSound()

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
        binding.roundTV.text = getString(R.string.rest) + " " + setRoundsAmount1 + "/" + makeRounds

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
                playSound()
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
