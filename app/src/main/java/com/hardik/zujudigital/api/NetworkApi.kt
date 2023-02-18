package com.hardik.zujudigital.api

import com.hardik.zujudigital.models.TeamsResponse
import com.hardik.zujudigital.models.matches.MatchesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkApi {

    @GET("teams")
    suspend fun getTeams(): Response<TeamsResponse>

    @GET("teams/matches")
    suspend fun getMatches(): Response<MatchesResponse>

    @GET("teams/{id}/matches")
    suspend fun getMatchesByTeamId(@Path("id") id: String): Response<MatchesResponse>
}