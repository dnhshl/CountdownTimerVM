package com.example.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.main.databinding.FragmentMainBinding
import com.example.main.model.MainViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private val TAG = "MainFragment"

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var list: MutableList<String>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.terminList.observe(viewLifecycleOwner,
            { terminList -> adapter.notifyDataSetChanged()
            })

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,viewModel.terminList.value!!)
        binding.lvTermine.adapter = adapter

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_settimerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}