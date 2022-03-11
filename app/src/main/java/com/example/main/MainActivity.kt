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


const val LISTSIZE = "listsize"
const val LISTITEM = "item_"
const val SELECTEDITEM ="selected"

class MainActivity : AppCompatActivity() {

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


        //binding.fab.setOnClickListener { view ->
        //    navController.navigate(R.id.settimerFragment)
        //}
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.menu_main, menu)
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