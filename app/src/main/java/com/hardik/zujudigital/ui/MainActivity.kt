package com.hardik.zujudigital.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hardik.zujudigital.R
import com.hardik.zujudigital.repository.DataRepository
import com.hardik.zujudigital.ui.viewmodels.MainViewModel
import com.hardik.zujudigital.ui.viewmodels.MainViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val teamVMProviderFactory = MainViewModelProviderFactory(DataRepository())
        viewModel =
            ViewModelProvider(this, teamVMProviderFactory)[MainViewModel::class.java]

        setContentView(R.layout.activity_main)

        var bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        var mainNavHostFragment = findNavController(R.id.mainNavHostFragment)
        bottomNavView.setupWithNavController(mainNavHostFragment)

        mainNavHostFragment.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.videoPlayerFragment || destination.id == R.id.teamDetailsFragment) {
                bottomNavView.visibility = View.GONE
            } else {
                bottomNavView.visibility = View.VISIBLE
            }
        }
    }
}