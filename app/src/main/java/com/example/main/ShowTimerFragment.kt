package com.example.main

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.main.databinding.FragmentShowtimerBinding
import com.example.main.model.MainViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ShowTimerFragment : Fragment() {

    private val TAG = "ShowTimerFragment"

    private var _binding: FragmentShowtimerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var countDownTimer: CountDownTimer
    private var countDownTimerStarted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowtimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parts: List<String> = viewModel.getTerminSelected().split("\n")
        val terminTitel = parts[0]
        val terminDateTimeString = parts[1]
        Log.i(TAG, "Termin: ${parts.toString()}")

        binding.tvTitle.text = terminTitel
        val timediff = calcTimeDiffInSeconds(terminDateTimeString)

        if (timediff < 0) {
            binding.tvCountdown.text = getString(R.string.countdown_display_over)
        } else {
            // Countdowntimer zählt die timediff im Sekundentakt runter
            countDownTimer = object : CountDownTimer(timediff * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.tvCountdown.text = timediffToComponents(millisUntilFinished)
                }
                override fun onFinish() {
                    binding.tvCountdown.text = getString(R.string.countdown_display_over)
                    MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI).start()
                }
            }
            countDownTimer.start()
            countDownTimerStarted = true
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (countDownTimerStarted) countDownTimer.cancel()
        _binding = null
    }

    // Berechne die Zeit von jetzt bis zum Termin in Sekunden
    fun calcTimeDiffInSeconds(timestring: String): Long {
        val pattern = DateTimeFormatter.ofPattern(getString(R.string.date_time_format_string))
        val time = LocalDateTime.parse(timestring, pattern)
        val timediff = Duration.between(LocalDateTime.now(), time).seconds
        return timediff
    }

    // Berechne aus der Zeitdifferenz in Millisekunden den Anteil
    // in Stunden, Minuten und Sekunden
    // Formatiere dies als String zur Ausgabe
    fun timediffToComponents(millisUntilFinished: Long): String {
        val days = millisUntilFinished / (24*60*60*1000)
        var remainder = (millisUntilFinished - 24*60*60*1000*days)
        val hours = remainder / (60*60*1000)
        remainder -= 60*60*1000*hours
        val minutes = remainder / (60*1000)
        remainder -= 60*1000*minutes
        val seconds = remainder / 1000
        return getString(R.string.countdown_format_string).format(days,hours,minutes,seconds)
    }
}