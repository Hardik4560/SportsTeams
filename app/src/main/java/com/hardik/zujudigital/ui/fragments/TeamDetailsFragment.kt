package com.hardik.zujudigital.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hardik.zujudigital.R
import com.hardik.zujudigital.adapters.PreviousMatchesAdapter
import com.hardik.zujudigital.adapters.UpcomingMatchesAdapter
import com.hardik.zujudigital.models.Team
import com.hardik.zujudigital.ui.MainActivity
import com.hardik.zujudigital.ui.viewmodels.MainViewModel
import com.hardik.zujudigital.util.Resource
import kotlinx.android.synthetic.main.fragment_matches.*

class TeamDetailsFragment : Fragment(R.layout.fragment_matches) {

    private val TAG = TeamDetailsFragment::class.java.simpleName

    private lateinit var viewModel: MainViewModel
    private lateinit var upcomingMatchesAdapter: UpcomingMatchesAdapter
    private lateinit var previousMatchesAdapter: PreviousMatchesAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        initView()

        //TODO: Try using navArgs for getting the values.
        var team = arguments?.getSerializable("team") as Team
        var id = team.id

        txt_header.text = team.name

        setupAdapter()

        viewModel.teamWiseMatch.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let { matchesResponse ->
                        txt_header_previous.visibility = View.VISIBLE
                        txt_header_upcoming.visibility = View.VISIBLE

                        upcomingMatchesAdapter.differ.submitList(matchesResponse.matches.upcoming)
                        previousMatchesAdapter.differ.submitList(matchesResponse.matches.previous)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        txt_empty_list.text = message
                        Log.e(TAG, message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
        viewModel.getMatches(id)
    }

    private fun initView() {
        txt_header_previous.visibility = View.GONE
        txt_header_upcoming.visibility = View.GONE
    }

    private fun hideProgressBar() {
        progress_bar.visibility = View.GONE
    }

    private fun showProgressBar() {
        progress_bar.visibility = View.VISIBLE
    }

    private fun setupAdapter() {
        //Upcoming
        upcomingMatchesAdapter = UpcomingMatchesAdapter()
        rv_upcoming_matches.apply {
            adapter = upcomingMatchesAdapter
            layoutManager = LinearLayoutManager(activity).also {
                it.orientation = LinearLayoutManager.HORIZONTAL
            }
        }

        //Previous
        previousMatchesAdapter = PreviousMatchesAdapter()
        previousMatchesAdapter.setOnItemClickListener {
            var bundle = Bundle().apply {
                putSerializable("match", it)
            }
            findNavController().navigate(
                R.id.action_teamDetailsFragment_to_videoPlayerFragment,
                bundle
            )
        }
        rv_previous_matches.apply {
            adapter = previousMatchesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}