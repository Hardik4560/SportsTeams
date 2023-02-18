package com.hardik.zujudigital.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hardik.zujudigital.R
import com.hardik.zujudigital.adapters.PreviousMatchesAdapter
import com.hardik.zujudigital.adapters.UpcomingMatchesAdapter
import com.hardik.zujudigital.models.Team
import com.hardik.zujudigital.models.matches.Previous
import com.hardik.zujudigital.ui.MainActivity
import com.hardik.zujudigital.ui.viewmodels.MainViewModel
import com.hardik.zujudigital.util.Resource

class TeamDetailsFragment : Fragment(R.layout.fragment_matches) {

    private val TAG = TeamDetailsFragment::class.java.simpleName

    private lateinit var viewModel: MainViewModel
    private lateinit var upcomingMatchesAdapter: UpcomingMatchesAdapter
    private lateinit var previousMatchesAdapter: PreviousMatchesAdapter

    private lateinit var rvUpcoming: RecyclerView
    private lateinit var rvPrevious: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView
    private lateinit var txtHeader: TextView
    private lateinit var txtPreviousHeader: TextView
    private lateinit var txtUpcomingHeader: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        initView(view)

        //TODO: Try using navArgs for getting the values.
        var team = arguments?.getSerializable("team") as Team
        var id = team.id

        txtHeader.text = team.name

        setupAdapter()

        viewModel.teamWiseMatch.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let { matchesResponse ->
                        txtPreviousHeader.visibility = View.VISIBLE
                        txtUpcomingHeader.visibility = View.VISIBLE

                        upcomingMatchesAdapter.differ.submitList(matchesResponse.matches.upcoming)
                        previousMatchesAdapter.differ.submitList(matchesResponse.matches.previous)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        textView.text = message
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

    private fun initView(view: View) {
        progressBar = view.findViewById(R.id.progress_bar)
        textView = view.findViewById(R.id.txt_empty_list)
        rvUpcoming = view.findViewById(R.id.rv_upcoming_matches)
        rvPrevious = view.findViewById(R.id.rv_previous_matches)
        txtHeader = view.findViewById(R.id.txt_header)

        txtPreviousHeader = view.findViewById<TextView?>(R.id.txt_header_previous).also {
            it.visibility = View.GONE
        }
        txtUpcomingHeader = view.findViewById<TextView?>(R.id.txt_header_upcoming)
            .also { it.visibility = View.GONE }

    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun setupAdapter() {
        //Upcoming
        upcomingMatchesAdapter = UpcomingMatchesAdapter()
        rvUpcoming.apply {
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
                R.id.action_matchesFragment_to_videoPlayerFragment,
                bundle
            )
        }
        rvPrevious.apply {
            adapter = previousMatchesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}