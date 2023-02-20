package com.hardik.zujudigital.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hardik.zujudigital.R
import com.hardik.zujudigital.adapters.TeamsAdapter
import com.hardik.zujudigital.ui.MainActivity
import com.hardik.zujudigital.ui.viewmodels.MainViewModel
import com.hardik.zujudigital.util.Resource
import kotlinx.android.synthetic.main.fragment_team_list.*

class TeamListFragment : Fragment(R.layout.fragment_team_list) {

    private lateinit var teamsAdapter: TeamsAdapter
    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setupAdapter()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = (activity as MainActivity).viewModel

        viewModel.teams.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        teamsAdapter.differ.submitList(it.teams)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        txt_empty_list_teams.text = it
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        progress_bar_teams.visibility = View.GONE
    }

    private fun showProgressBar() {
        progress_bar_teams.visibility = View.VISIBLE
    }

    private fun setupAdapter() {
        teamsAdapter = TeamsAdapter()

        rv_teams.apply {
            adapter = teamsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        teamsAdapter.setOnItemClicklistener {
            var bundle = Bundle().apply {
                putSerializable("team", it)
            }
            findNavController().navigate(
                R.id.action_teamListFragment_to_teamDetailsFragment,
                bundle
            )
        }
    }

    private fun initView() {
        //Use this method for initializing views.
    }
}