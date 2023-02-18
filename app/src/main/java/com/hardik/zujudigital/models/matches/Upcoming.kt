package com.hardik.zujudigital.models.matches

data class Upcoming(
    val away: String,
    var awayIcon: String?,
    val date: String,
    val description: String,
    val home: String,
    var homeIcon: String?,
) : java.io.Serializable