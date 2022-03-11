package com.example.main

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.main.databinding.ActivityMainBinding
import com.example.main.model.MainViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            //showDatePicker()
            getTitleDialog()
            viewModel.addTermin("Hallo")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun showDatePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTitleText(R.string.date_picker_title)
                .build()
        datePicker.addOnPositiveButtonClickListener {
            Log.i(TAG, "From DatePicker ${datePicker.selection.toString()}")
            showTimePicker()
        }
        // bringe den TimePicker zur Anzeige
        datePicker.show(supportFragmentManager, TAG)
    }

    fun showTimePicker() {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText(R.string.time_picker_title)
                .build()
        timePicker.addOnPositiveButtonClickListener {
            Log.i(TAG, "From TimePicker ${timePicker.hour}:${timePicker.minute}")

        }
        // bringe den TimePicker zur Anzeige
        timePicker.show(supportFragmentManager, TAG)
    }

    fun getTitleDialog() {

        //val textInputLayout = TextInputLayout(applicationContext)
        //val editTextTitle = TextInputEditText(textInputLayout.context)
        val editTextTitle = EditText(applicationContext)
        editTextTitle.setPadding(20,0,20,0)


        val builder: AlertDialog.Builder? = let {
            AlertDialog.Builder(it)
        }
        builder?.setMessage(R.string.app_name)
            ?.setTitle(R.string.time_picker_title)
            ?.setView(editTextTitle)
        val dialog: AlertDialog? = builder?.create()
        dialog?.show()
    }

}