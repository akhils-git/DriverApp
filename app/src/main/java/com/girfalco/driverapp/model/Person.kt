package com.girfalco.driverapp.model

data class Person(
    val id: Int? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val mobile: String? = null,
    val image: String? = null
)

object PersonStore {
    var current: Person? = null
}
