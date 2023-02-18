package com.hardik.zujudigital.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hardik.zujudigital.models.Team
import com.hardik.zujudigital.models.TeamsResponse
import com.hardik.zujudigital.models.matches.MatchesResponse
import com.hardik.zujudigital.repository.DataRepository
import com.hardik.zujudigital.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(
    private val repository: DataRepository

) : ViewModel() {
    private val TAG = MainViewModel::class.java.simpleName

    val teams: MutableLiveData<Resource<TeamsResponse>> = MutableLiveData()
    val matches: MutableLiveData<Resource<MatchesResponse>> = MutableLiveData()
    val teamWiseMatch: MutableLiveData<Resource<MatchesResponse>> = MutableLiveData()

    init {
        getTeams()
    }

    private fun getTeams() {
        viewModelScope.launch {
            teams.postValue(Resource.Loading())
            val response = repository.getTeams()
            teams.postValue(handleTeamsResponse(response))

            getMatches()
        }
    }

    fun getMatches(teamId: String) {
        viewModelScope.launch {
            teamWiseMatch.postValue(Resource.Loading())
            val response = repository.getMatches(teamId)
            teamWiseMatch.postValue(handleMatchesResponse(response))
        }
    }

    private fun getMatches() {
        viewModelScope.launch {
            matches.postValue(Resource.Loading())
            val response = repository.getMatches()
            matches.postValue(handleMatchesResponse(response))
        }
    }

    private fun handleMatchesResponse(response: Response<MatchesResponse>): Resource<MatchesResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                //check if teams is loaded.
                var list = teams.value?.data?.teams
                for (i in it.matches.upcoming) {
                    i.awayIcon = list?.single { team ->
                        team.name == i.away
                    }?.logo ?: null

                    var x = list?.filter { teamX ->
                        teamX.name == i.home
                    }

                    //TODO: Check why this is crashing on single.
                    if (x?.isNotEmpty() == true) {
                        i.homeIcon = x?.get(0)?.logo
                    }
                }

                for (i in it.matches.previous) {
                    i.awayIcon = list?.single { team ->
                        team.name == i.away
                    }?.logo ?: null

                    i.homeIcon = list?.single { team ->
                        team.name == i.home
                    }?.logo ?: null
                }

                return Resource.Success(it)
            }
            return Resource.Error("Empty response from server")
        }
        return Resource.Error(response.message())
    }

    private fun handleTeamsResponse(response: Response<TeamsResponse>): Resource<TeamsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
            return Resource.Error("Empty response from server")
        } else {
            Log.e(TAG, response.message())
            return Resource.Error(response.message())
        }
    }
}