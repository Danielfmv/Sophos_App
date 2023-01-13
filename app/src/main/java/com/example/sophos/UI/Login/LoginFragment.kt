package com.example.sophos.UI.Login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.sophos.Data.Model.LoginResponse
import com.example.sophos.R
import com.example.sophos.ViewModels.LoginViewModel
import com.example.sophos.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val loginViewModel : LoginViewModel by activityViewModels()

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var email : String
    private lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginbttn.setOnClickListener {
            email = binding.Emailinput.text.toString().trim()
            password = binding.Passwordinput.text.toString().trim()

            loginViewModel.login(email, password)

            loginViewModel.apiLoginResponse.observe(viewLifecycleOwner) { response ->
                run {
                    if (response.body() ==null){
                        Toast.makeText(requireContext(), "Ingresa tu correo y tu contraseña", Toast.LENGTH_SHORT).show()

                    } else {
                        if (response.body()!!.uaccess) {
                            Toast.makeText(requireContext(), "Bienvenido ${response.body()!!.uname}", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_loginFragment_to_menu_Screen)
                        } else {
                            Toast.makeText(requireContext(), "Los datos ingresados no son válidos, intenta de nuevo", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}