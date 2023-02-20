package com.hardik.zujudigital.ui.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import com.hardik.zujudigital.broadcastreceiver.AlarmReceiver
import com.hardik.zujudigital.models.matches.Upcoming
import com.hardik.zujudigital.ui.MainActivity
import com.hardik.zujudigital.ui.viewmodels.MainViewModel
import com.hardik.zujudigital.util.Resource
import kotlinx.android.synthetic.main.fragment_matches.*
import java.util.*

class MatchesFragment : Fragment(R.layout.fragment_matches) {

    private val TAG = MatchesFragment::class.java.simpleName

    private lateinit var viewModel: MainViewModel
    private lateinit var upcomingMatchesAdapter: UpcomingMatchesAdapter
    private lateinit var previousMatchesAdapter: PreviousMatchesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        initView()

        setupAdapter()

        viewModel.matches.observe(viewLifecycleOwner, Observer { response ->
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
                R.id.action_matchesFragment_to_videoPlayerFragment,
                bundle
            )
        }
        rv_previous_matches.apply {
            adapter = previousMatchesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}