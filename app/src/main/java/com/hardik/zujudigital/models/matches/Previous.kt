package com.hardik.zujudigital.models.matches

data class Previous(
    val away: String,
    var awayIcon: String?,
    val date: String,
    val description: String,
    val highlights: String,
    val home: String,
    var homeIcon: String?,
    val winner: String
) : java.io.Serializable