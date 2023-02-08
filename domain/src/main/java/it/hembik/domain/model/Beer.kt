package it.hembik.domain.model

data class Beer (
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val tagLine: String,
)