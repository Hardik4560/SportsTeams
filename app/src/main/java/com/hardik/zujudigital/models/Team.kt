package com.hardik.zujudigital.models

data class Team(
    val id: String,
    val logo: String,
    val name: String
) : java.io.Serializable {

    override fun toString(): String {
        return name
    }
}