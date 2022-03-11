package com.example.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.main.databinding.FragmentSettimerBinding
import com.example.main.model.MainViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SettimerFragment : Fragment() {

    private val TAG = "SettimerFragment"
    private var _binding: FragmentSettimerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var time: LocalTime
    private lateinit var date: LocalDate
    private var timeSet = false
    private var dateSet = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettimerBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOK.setOnClickListener {
            val titlestring = binding.editTitle.text.toString()
            if (!titlestring.equals("") && dateSet && timeSet) {
                val format = getString(R.string.date_time_format_string)
                val datetime =  date.atTime(time)
                val datetimestring = datetime.format(DateTimeFormatter.ofPattern(format))
                val terminstring = getString(R.string.list_entry).format(titlestring, datetimestring)
                viewModel.addTermin(terminstring)
                findNavController().navigate(R.id.action_settimerFragment_to_mainFragment)
            } else {
                Toast.makeText(context,R.string.error_edit_termin, Toast.LENGTH_LONG).show()
            }
        }

        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_settimerFragment_to_mainFragment)
        }


        binding.btnEditTime.setOnClickListener {
            showTimePicker()
        }


        binding.btnEditDate.setOnClickListener {
            showDatePicker()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showTimePicker () {
        // Konfiguration des TimePickers
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText(R.string.time_picker_title)
                .build()

        timePicker.addOnPositiveButtonClickListener {
            time = LocalTime.of(timePicker.hour, timePicker.minute, 0)
            val format = getString(R.string.time_format_string)
            val timestring = time.format(DateTimeFormatter.ofPattern(format))
            binding.tvTime.text =  getString(R.string.time_display).format(timestring)
            timeSet = true
        }
        timePicker.show(parentFragmentManager, TAG)
    }

    private fun showDatePicker () {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.date_picker_title)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener {
            Log.i(TAG, "DatePicker ${datePicker.selection.toString()}")
            date = Instant.ofEpochMilli(datePicker.selection!!)
                .atZone(ZoneId.systemDefault()).toLocalDate();
            val format = getString(R.string.date_format_string)
            val datestring = date.format(DateTimeFormatter.ofPattern(format))
            binding.tvDate.text = datestring
            dateSet = true
        }
        datePicker.show(parentFragmentManager, TAG)
    }


}