package com.example.sophos.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.sophos.Data.Model.getDocsItems
import com.example.sophos.R
import com.example.sophos.UI.GetDocuments.Detail_fragment
import com.example.sophos.ViewModels.LoginViewModel

class MainActivity : AppCompatActivity() {

    private val MenuViewModel : LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Retrieve NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        // Set up the action bar for use with the NavController
        //setupActionBarWithNavController(navController)
    }


}