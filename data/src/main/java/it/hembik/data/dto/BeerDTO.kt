package it.hembik.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import it.hembik.domain.model.Beer

@JsonClass(generateAdapter = true)
data class BeerDTO (
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "image_url")
    val imageUrl: String?,
    @Json(name = "tagline")
    val tagLine: String,
)

fun BeerDTO.toBeer(): Beer {
    return Beer(
        id,
        name,
        description,
        imageUrl,
        tagLine
    )
}