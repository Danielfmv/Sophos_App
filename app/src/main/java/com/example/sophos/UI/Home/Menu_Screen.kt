package com.example.sophos.UI.Home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sophos.R
import com.example.sophos.ViewModels.LoginViewModel
import com.example.sophos.databinding.FragmentMenuScreenBinding

class Menu_Screen : Fragment() {

    private val loginViewModel : LoginViewModel by activityViewModels()

    private var _binding : FragmentMenuScreenBinding? = null
    private val binding get() = _binding!!

    //val toolbar: Toolbar = binding.topMenuBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentMenuScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    // INFLATES THE ONTOPMENU TO CHANGE ITS COLOR
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.ontop_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val uName = loginViewModel.userName
        binding.backBttn.text = uName


        binding.buttonOffices.setOnClickListener {
            findNavController().navigate(R.id.action_menu_Screen_to_officesFragment)
        }
        binding.buttonSendDocs.setOnClickListener {
            findNavController().navigate(R.id.action_menu_Screen_to_sendDocumentsFragment)
        }
        binding.buttonViewDocs.setOnClickListener {
            findNavController().navigate(R.id.action_menu_Screen_to_getDocumentsFragment)
        }

        binding.backBttn.setOnClickListener {
            findNavController().navigate(R.id.action_menu_Screen_to_loginFragment)
        }

        binding.burguerMenuHome.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), binding.burguerMenuHome)
            popupMenu.menuInflater.inflate(R.menu.ontop_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.topMenuSendDocs ->
                        findNavController().navigate(R.id.action_menu_Screen_to_sendDocumentsFragment)
                    R.id.topMenuViewDocs ->
                        findNavController().navigate(R.id.action_menu_Screen_to_getDocumentsFragment)
                    R.id.topMenuOffices ->
                        findNavController().navigate(R.id.action_menu_Screen_to_officesFragment)
                    R.id.topMenuLogout ->
                        findNavController().navigate(R.id.loginFragment)
                }
                true
            }
            popupMenu.show()
        }

    }
}