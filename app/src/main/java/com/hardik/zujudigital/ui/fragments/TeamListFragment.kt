package com.hardik.zujudigital.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hardik.zujudigital.R
import com.hardik.zujudigital.adapters.TeamsAdapter
import com.hardik.zujudigital.ui.MainActivity
import com.hardik.zujudigital.ui.viewmodels.MainViewModel
import com.hardik.zujudigital.util.Resource

class TeamListFragment : Fragment(R.layout.fragment_team_list) {

    lateinit var progressBar: ProgressBar
    lateinit var textView: TextView
    lateinit var recyclerView: RecyclerView

    private lateinit var teamsAdapter: TeamsAdapter
    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
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
                        textView.text = it
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun setupAdapter() {
        teamsAdapter = TeamsAdapter()

        recyclerView.apply {
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

    private fun initView(view: View) {
        progressBar = view.findViewById(R.id.progress_bar)
        textView = view.findViewById(R.id.txt_empty_list)
        recyclerView = view.findViewById(R.id.rv_teams)

    }
}