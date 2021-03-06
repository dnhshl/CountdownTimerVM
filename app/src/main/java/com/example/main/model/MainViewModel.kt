package com.example.main.model

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel



class MainViewModel: ViewModel() {

    private val TAG = "MainViewModel"

    private val _terminList = MutableLiveData<MutableList<String>>()
    val terminList: LiveData<MutableList<String>>
        get() = _terminList

    private val _terminSelected = MutableLiveData<String>()
    val terminSelected: LiveData<String>
        get() = _terminSelected

    init {
        _terminList.value = mutableListOf()
        _terminSelected.value = ""
    }

    fun addTermin(termin: String) {
        if (!(_terminList.value?.contains(termin) ?: true)) {
            _terminList.value?.add(termin)
            _terminList.notifyObserver()
            Log.i(TAG, _terminList.value.toString())
        }
    }

    fun removeTermin(termin: String) {
        _terminList.value?.removeAll {it.equals(termin)}
        _terminList.notifyObserver()
        if (_terminSelected.value.equals(termin))
            _terminSelected.value = ""
        Log.i(TAG, _terminList.value.toString())
    }

    fun setTerminSelected(termin: String) {
        _terminSelected.value = termin
        Log.i(TAG, _terminSelected.value.toString())
    }

    fun getTerminList(): List<String>? {
        return _terminList.value
    }
    fun getTerminSelected(): String {
        return _terminSelected.value.toString()
    }

    // Extension Function, um Änderung in den Einträgen von Listen
    // dem Observer anzeigen zu können
    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}