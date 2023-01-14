package com.example.sophos.UI.Login

import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.sophos.Data.Model.LoginResponse
import com.example.sophos.R
import com.example.sophos.ViewModels.LoginViewModel
import com.example.sophos.databinding.FragmentLoginBinding
import java.util.concurrent.Executor

class LoginFragment : Fragment() {

    private val loginViewModel : LoginViewModel by activityViewModels()

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var email : String
    private lateinit var password : String

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

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

        // ----------------------- USER DATA VALIDATION ----------------------------- //

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
                            val bundle = Bundle()
                            bundle.putString("userName", response.body()!!.uname)

                            findNavController().navigate(R.id.action_loginFragment_to_menu_Screen, bundle)
                        } else {
                            Toast.makeText(requireContext(), "Los datos ingresados no son válidos, intenta de nuevo", Toast.LENGTH_SHORT).show()
//                            findNavController().navigate(R.id.loginFragment)
                        }
                    }
                }
            }
        }

        // ----------------------- BIOMETRIC VALIDATION ----------------------------- //

        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> Log.d(TAG, "BIOMETRIC_SUCCESS")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> Log.e(
                TAG,
                "BIOMETRIC_ERROR_HW_UNAVAILABLE"
            )
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> Log.e(
                TAG,
                "BIOMETRIC_ERROR_NO_HARDWARE"
            )
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                TODO()
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                TODO()
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                TODO()
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                TODO()
            }
        }

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Ingresa con tu huella")
            .setSubtitle("Puedes ingresar usando validación biométrica")
            .setNegativeButtonText("Cancelar")
            .build()

        executor = ContextCompat.getMainExecutor(requireContext())

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(requireContext(), errString.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(requireContext(), "Bienvenido", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(requireContext(), "No se reconoció tu huella", Toast.LENGTH_SHORT).show()
                }
            })

        binding.fprintbttn.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
            findNavController().navigate(R.id.action_loginFragment_to_menu_Screen)
        }
    }
}