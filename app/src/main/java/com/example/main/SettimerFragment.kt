package com.example.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.main.databinding.FragmentSettimerBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SettimerFragment : Fragment() {

    private val TAG = "SettimerFragment"
    private var _binding: FragmentSettimerBinding? = null
    private val binding get() = _binding!!

    private lateinit var time: LocalTime
    private var timestring = "00:00"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettimerBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnShowtimer.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_showTimerFragment)
        }

        binding.btnEditTime.setOnClickListener {
            // Konfiguration des TimePickers
            val timePicker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText(R.string.time_picker_title)
                    .build()
            // Was soll passieren, wenn der TimePicker über den OK Button verlassen wird?
            // onClickListener für den OK Button des TimePickers
            timePicker.addOnPositiveButtonClickListener {
                // Setze die Zeitvariable (vom Typ LocalTime) mit den
                // im TimePicker gewählten Werten
                time = LocalTime.of(timePicker.hour, timePicker.minute, 0)
                // Konvertiere die Zeitvariable in eine String
                // Das Format kann über einen Formatstring definiert werden
                // Hier: <string name="time_format_string">HH:mm</string>
                val format = getString(R.string.time_format_string)
                timestring = time.format(DateTimeFormatter.ofPattern(format))

                // Dieser String wird zur Anzeige gebracht.
                // Es soll aber noch "Uhr" ergänzt werden.
                // Stringressourcen können auch Parameter enthalten
                // hier: <string name="time_display">%s Uhr</string>
                // %s wird durch den timestring ersetzt
                binding.tvTime.text =  getString(R.string.time_display).format(timestring)
            }
            // bringe den TimePicker zur Anzeige
            timePicker.show(parentFragmentManager, TAG)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}