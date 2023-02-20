package com.hardik.zujudigital.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hardik.zujudigital.R
import com.hardik.zujudigital.repository.DataRepository
import com.hardik.zujudigital.ui.viewmodels.MainViewModel
import com.hardik.zujudigital.ui.viewmodels.MainViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val teamVMProviderFactory = MainViewModelProviderFactory(DataRepository())
        viewModel =
            ViewModelProvider(this, teamVMProviderFactory)[MainViewModel::class.java]

        setContentView(R.layout.activity_main)

        var mainNavHostFragment = findNavController(R.id.mainNavHostFragment)
        bottomNavigationView.setupWithNavController(mainNavHostFragment)

        mainNavHostFragment.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.videoPlayerFragment || destination.id == R.id.teamDetailsFragment) {
                bottomNavigationView.visibility = View.GONE
            } else {
                bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }
}