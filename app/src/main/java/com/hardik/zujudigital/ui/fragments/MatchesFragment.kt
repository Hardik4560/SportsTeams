package com.hardik.zujudigital.ui.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hardik.zujudigital.R
import com.hardik.zujudigital.adapters.PreviousMatchesAdapter
import com.hardik.zujudigital.adapters.UpcomingMatchesAdapter
import com.hardik.zujudigital.broadcastreceiver.AlarmReceiver
import com.hardik.zujudigital.models.Team
import com.hardik.zujudigital.models.matches.Upcoming
import com.hardik.zujudigital.ui.MainActivity
import com.hardik.zujudigital.ui.viewmodels.MainViewModel
import com.hardik.zujudigital.util.DateUtils
import com.hardik.zujudigital.util.Resource
import java.util.Calendar

class MatchesFragment : Fragment(R.layout.fragment_matches) {

    private val TAG = MatchesFragment::class.java.simpleName

    private lateinit var viewModel: MainViewModel
    private lateinit var upcomingMatchesAdapter: UpcomingMatchesAdapter
    private lateinit var previousMatchesAdapter: PreviousMatchesAdapter

    private lateinit var rvUpcoming: RecyclerView
    private lateinit var rvPrevious: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView
    private lateinit var txtPreviousHeader: TextView
    private lateinit var txtUpcomingHeader: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        initView(view)

        setupAdapter()

        viewModel.matches.observe(viewLifecycleOwner, Observer { response ->
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
    }

    private fun initView(view: View) {
        progressBar = view.findViewById(R.id.progress_bar)
        textView = view.findViewById(R.id.txt_empty_list)
        rvUpcoming = view.findViewById(R.id.rv_upcoming_matches)
        rvPrevious = view.findViewById(R.id.rv_previous_matches)

        txtPreviousHeader = view.findViewById<TextView?>(R.id.txt_header_previous).also {
            it.visibility = View.GONE
        }
        txtUpcomingHeader = view.findViewById<TextView?>(R.id.txt_header_upcoming).also {
            it.visibility = View.GONE
        }

    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun startAlarm(match: Upcoming) {

        //TODO: remove it later for actual match time.
        var cal = Calendar.getInstance() //DateUtils.dateToCalendar(match.date)
        cal.add(Calendar.SECOND, 10)

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("description", match.description)
        intent.putExtra("date", match.date)

        val pendingIntent =
            PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_IMMUTABLE)


        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)

        Log.d(TAG, "Alarm set for " + cal.time)
    }

    private fun setupAdapter() {
        //Upcoming
        upcomingMatchesAdapter = UpcomingMatchesAdapter()
        upcomingMatchesAdapter.setOnRemindMeClickListener {
            startAlarm(it)
        }
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