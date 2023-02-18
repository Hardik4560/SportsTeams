package com.hardik.zujudigital.repository

import com.hardik.zujudigital.api.RetrofitInstance

class DataRepository {

    suspend fun getTeams() = RetrofitInstance.api.getTeams()

    suspend fun getMatches() = RetrofitInstance.api.getMatches()

    suspend fun getMatches(id: String) = RetrofitInstance.api.getMatchesByTeamId(id)
}