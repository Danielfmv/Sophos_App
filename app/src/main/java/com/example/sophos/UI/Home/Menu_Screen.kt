package com.example.sophos.UI.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sophos.R
import com.example.sophos.ViewModels.LoginViewModel
import com.example.sophos.databinding.FragmentMenuScreenBinding
import com.example.sophos.databinding.FragmentOfficesBinding

class Menu_Screen : Fragment() {

    private val MenuViewModel : LoginViewModel by viewModels()

    private var _binding : FragmentMenuScreenBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMenuScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonOffices.setOnClickListener {
            findNavController().navigate(R.id.action_menu_Screen_to_officesFragment)
        }
        binding.buttonSendDocs.setOnClickListener {
            findNavController().navigate(R.id.action_menu_Screen_to_sendDocumentsFragment)
        }
    }

}