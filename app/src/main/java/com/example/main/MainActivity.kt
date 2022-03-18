package com.example.main

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
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

import androidx.activity.viewModels

import com.example.main.databinding.ActivityMainBinding

import com.example.main.model.MainViewModel




class MainActivity : AppCompatActivity() {

    private val LISTSIZE = "listsize"
    private val LISTITEM = "item_"
    private val SELECTEDITEM ="selected"

    private val TAG = "MainActivity"

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        readSharedPreferences()

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
        writeSharedPreferences()
    }

    private fun writeSharedPreferences() {
        Log.i(TAG, "writeSharedPreferences")
        // speicher die Terminliste
        val sp = getPreferences(Context.MODE_PRIVATE)
        val edit = sp.edit()
        val list = viewModel.getTerminList()!!
        edit.putInt(LISTSIZE, list.size)
        for(i in 0 until list.size){
            edit.putString("$LISTITEM$i", list.get(i))
        }
        edit.putString(SELECTEDITEM, viewModel.getTerminSelected())
        edit.commit()
    }

    private fun readSharedPreferences() {
        Log.i(TAG, "readSharedPreferences")
        // Termine wieder einlesen
        val sp = getPreferences(Context.MODE_PRIVATE)
        val anzahl = sp.getInt(LISTSIZE, 0)
        for(i in 0 until anzahl){
            viewModel.addTermin(sp.getString("$LISTITEM$i", "").toString())
        }
        viewModel.setTerminSelected(sp.getString(SELECTEDITEM, "").toString())
    }

}