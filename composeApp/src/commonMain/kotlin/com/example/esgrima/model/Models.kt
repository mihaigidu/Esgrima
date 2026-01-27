package com.example.esgrima.model

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val streetAddress: String,
    val city: String,
    val state: String,
    val postalCode: String
)

@Serializable
data class PhoneNumber(
    val type: String,
    val number: String
)

enum class Fase { POULES, ELIMINATORIAS_32, ELIMINATORIAS_16, CUARTOS, SEMIFINAL, FINAL }

@Serializable
data class Tirador(
    val firstName: String,
    val lastName: String,
    val gender: String,
    val age: Int,
    val federateNumber: Int,
    val club: String,
    val country: String,
    val modality: String,
    val address: Address? = null,
    val phoneNumbers: List<PhoneNumber> = emptyList(),

    val id: String = federateNumber.toString(),
    val rankingInicial: Int = 0
) {
    val nombreCompleto: String get() = "$firstName $lastName"
}

@Serializable
data class Arbitro(
    val firstName: String,
    val lastName: String,
    val gender: String,
    val age: Int,
    val federateNumber: Int,
    val club: String,
    val country: String,
    val modality: List<String>,
    val address: Address? = null,
    val phoneNumbers: List<PhoneNumber> = emptyList(),

    val id: String = federateNumber.toString()
) {
    val nombreCompleto: String get() = "$firstName $lastName"
}

@Serializable
data class Asalto(
    val id: String,
    val tirador1: Tirador,
    val tirador2: Tirador?, 
    var tocados1: Int = 0,
    var tocados2: Int = 0,
    var finalizado: Boolean = false,
    val esPoule: Boolean = true,
    val rondaEliminatoria: Fase? = null
) {
    fun getGanador(): Tirador? {
        if (!finalizado) return null
        if (tirador2 == null) return tirador1
        return if (tocados1 > tocados2) tirador1 else tirador2
    }
}

@Serializable
data class Poule(
    val numero: Int,
    val pista: String,
    val arbitro: Arbitro?,
    val tiradores: List<Tirador>,
    val asaltos: MutableList<Asalto> = mutableListOf()
)

@Serializable
data class CompeticionData(
    var nombre: String = "Torneo Esgrima 2026",
    val tiradores: MutableList<Tirador> = mutableListOf(),
    val arbitros: MutableList<Arbitro> = mutableListOf(),
    val poules: MutableList<Poule> = mutableListOf(),
    val cuadroEliminatorio: MutableList<Asalto> = mutableListOf()
)