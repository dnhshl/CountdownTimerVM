package com.example.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.main.databinding.FragmentMainBinding
import com.example.main.model.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private val TAG = "MainFragment"

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter für den ListView
        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_list_item_1,   // Layout zur Darstellung der ListItems
            viewModel.getTerminList()!!)           // Liste, die Dargestellt werden soll

        // Adapter an den ListView koppeln
        binding.lvTermine.adapter = adapter

        // Mittels Observer den Adapter über Änderungen in der Liste informieren
        viewModel.terminList.observe(viewLifecycleOwner) { adapter.notifyDataSetChanged() }

        viewModel.terminSelected.observe(viewLifecycleOwner) { termin ->
                if (termin.equals(""))
                    binding.tvTermin.text = getString(R.string.nothing_selected)
                else
                    binding.tvTermin.text = getString(R.string.termin_selected).format(termin)
        }




        binding.btnSetTimer.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_settimerFragment)
        }
        binding.btnShowTimer.setOnClickListener {
            if (!viewModel.terminSelected.value.equals(""))
                findNavController().navigate(R.id.action_mainFragment_to_showTimerFragment)
            else
                Toast.makeText(context, R.string.nothing_selected, Toast.LENGTH_LONG).show()
        }

        binding.lvTermine.setOnItemClickListener { adapterView, view, i, l ->
            // i ist der Index des geklickten Eintrags
            showDialog(binding.lvTermine.getItemAtPosition(i).toString())
        }
    }

    private fun showDialog(selectedItem: String) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(R.string.dialog_title)
                .setMessage(selectedItem)
                .setNeutralButton(R.string.dialog_cancel) { dialog, which ->
                }
                .setNegativeButton(R.string.dialog_delete) { dialog, which ->
                    viewModel.removeTermin(selectedItem)
                }
                .setPositiveButton(R.string.dialog_select) { dialog, which ->
                    viewModel.setTerminSelected(selectedItem)
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}